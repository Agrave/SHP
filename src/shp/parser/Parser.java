/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shp.parser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import org.xml.sax.InputSource;

/**
 *
 * @author Agrave
 */
public class Parser {
    private Handler handler;
    private InputSource sourse;
    
    public void parse(File file, Handler handler) throws IOException {
          if (file==null)
            throw new IOException("File can't be null");
        
        if (!file.exists())
             throw new IOException("File don't exists");
        else{
            InputStream is = new FileInputStream(file);
            this.parse(is, handler);
        }
     
    }
    
    
    public void parse(InputStream stream,Handler handler)throws IOException{
        this.setHandler(handler);
        if (stream == null) throw new IOException("InputStream can't be nul");
        BufferedInputStream in = new BufferedInputStream(stream);
        String strRead;
//        while (strRead = in.)
        
        handler.startDocument();
        byte[] cbuf = new byte[2048];
        boolean inElement=false;
        StringBuilder element = new StringBuilder();
        AbstractElement elementParser;
        while ((in.read(cbuf)) != -1 ){
            String s = new String(cbuf,"Cp1251");
            
            int sLen=s.length();
            for (int i=0;i<sLen;i++){
                if (inElement)/*внутри тега <...>*/ {
                    if (s.charAt(i)=='>'){   /*вся логика наворочена что-бы отловить  конструкции коментария
                    типа <--blabla <blabla> blabla--> */
                        if (element.indexOf("<!--")!=-1)  {
                        /*потенциально закрывает коментарий*/
                            element.append(s.charAt(i));
                            if (element.indexOf("-->")!=-1){/*обрабатываем закрытие коментария*/
                                inElement=false;
                                elementParser=new AbstractElement(element);
                                doHandler(elementParser);
                                element = new StringBuilder();
                            }
                        }else if (element.indexOf("<!--")==-1) {
                            /*закрывает обычный тег(не коментарий)*/
                            element.append(s.charAt(i));
                            inElement=false;
                            elementParser=new AbstractElement(element);
                            doHandler(elementParser);
                            element=new StringBuilder();
                        }
                    } else if (s.charAt(i)=='<'){
                        if (element.indexOf("<!--")!=-1){/*внутри коментария обработали как обычный символ*/
                            element.append(s.charAt(i));
                        }else /*в любом другом месте признак того что закончилась символьная строка и начинается новый тег*/{
                            elementParser=new AbstractElement(element);
                            doHandler(elementParser);
                            element = new StringBuilder();//начали новый тег
                            element.append(s.charAt(i));
                        }
                    }else /*любой другой символ*/{
                        element.append(s.charAt(i));
                    }
                }else /*снаружи тега >...<*/{
                    inElement=true;
                    element.append(s.charAt(i));


                }
             }
        }
        handler.endDocument();
        
    }
    protected void doHandler(AbstractElement elementParser){
        if (ElementType.STARTTAG.equals(elementParser.getType())) {
            handler.startElement(elementParser.getName(), elementParser.getAtts());
        } else if (ElementType.ENDTAG.equals(elementParser.getType())) {
            handler.endElement(elementParser.getName());
        } else if (ElementType.COMENTS.equals(elementParser.getType())) {
            handler.comment(elementParser.getCharArray(), 0, elementParser.getCharArray().length);
        } else if (ElementType.CHARACTERS.equals(elementParser.getType())) {
            handler.characters(elementParser.getName());
        }
        if (ElementType.UNKNOWN.equals(elementParser.getType())){
            handler.characters(elementParser.getCharArray(), 0, elementParser.getCharArray().length);
        }
    }
    protected void setHandler(Handler handler){
        if (handler==null)
            this.handler=new DefaultHandler();
        else 
            this.handler=handler;
    }
    private void checkSourse(File file) throws IOException {
        if (file==null)
            throw new IOException("File can't be null");
        
    }
}
