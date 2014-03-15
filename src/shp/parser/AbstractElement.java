/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shp.parser;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Agrave
 */
public class AbstractElement {
    private String element=null;
    private Attributes atts=new AttributesImpl();
    private ElementType type= ElementType.UNKNOWN;
    private String name=null;
    private char[] charArray=null;


    public AbstractElement(StringBuilder str) {

        element = new String(str);

        element = element.replaceAll("&nbsp;"," ").replaceAll("\\s+", " ");//чистим управляющие символы и двойные пробелы
        //TODO реализовать очистку спецсимволов html
        element = element.trim();
        charArray = element.toCharArray();
        if (element.length() != 0) {
            if (element.indexOf("</") == 0) {
                type = ElementType.ENDTAG;
                name = element.substring(2, str.length() - 1);
            } else if (element.indexOf("<!--") != -1) {
                type = ElementType.COMENTS;
                name = element;
            } else if (element.indexOf("<?") == 0) {
                type = ElementType.UNKNOWN;
            } else if (element.indexOf("<!") == 0) {
                type = ElementType.UNKNOWN;
            } else if (element.lastIndexOf("/>") != -1) {
                type = ElementType.SINGLTAG;
                parseTag(element);
            } else if (element.indexOf("<") == 0) {
                type = ElementType.STARTTAG;
                parseTag(element);
            } else {
                type = ElementType.CHARACTERS;
                name = element;
            }
        } else {
            type = ElementType.EMPTY;
        }
    }
    private void parseTag(String tag){      //парсим тег на имя и атрибуты
        if (tag.indexOf(" ")!=-1){          //имя отделяется пробелом или тег состоит только из имени
            int pos = tag.indexOf(" ");
            this.name = tag.substring(1,pos );
            int posLast = pos+1;
            while ((pos=tag.indexOf("=",posLast))!=-1){             //атрибут начинается с пробела и
                pos = tag.indexOf("\"", pos+2);                     //заканчивается 2-й кавычкой 
                String att = tag.substring(posLast, pos+1);         //!Внутри атрибута могут быть пробелы
                posLast=pos+2;
                String attName=att.substring(0, att.indexOf("="));
                String attValue=att.substring(att.indexOf("\"")+1, att.lastIndexOf("\""));
                atts.addAttribute(attName, attValue);
            }
            
        } else 
            this.name = tag.substring(1, tag.length()-1);

    }
    public ElementType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Attributes getAtts() {
        return atts;
    }
    public char[] getCharArray(){
        
        return charArray;
    }
        
    }
    
    
    

