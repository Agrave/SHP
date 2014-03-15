/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shp;

import shp.parser.Attributes;
import shp.parser.DefaultHandler;

import java.util.HashSet;


public class HandlerImpl extends DefaultHandler{
    private String[] wList = {"dl", "dt","a","li"};
    private String[] bList={"font","gr0","i","b","small","div","img"};
    private HashSet whiteList= new HashSet()    ;
    private HashSet blackList=new HashSet();
    private HashSet<String> allTags=new HashSet();
    private boolean bodyFlag=false;
    private SkipNextEndTag skipNextEndTag=new SkipNextEndTag();
    private int tagPos=0;

    public  HandlerImpl() {
        for (int i = 0; i < wList.length; i++) {
            whiteList.add(wList[i]);
        }
        for (String s:bList){
            blackList.add(s);
        }
    }


    @Override
    public void startElement(String name, Attributes attr) {
        allTags.add(bodyFlag ?name:null);
        if (bodyFlag && isPrint(name,attr)) {
            for (int i=0;i<tagPos;i++){
                System.out.print(" ");
            }
            System.out.print("<" + name);
            for (int i = 0; i < attr.getLength(); i++) {
                System.out.print(" " + attr.getName(i) + "=" + attr.getValue(i));
            }
            System.out.println(">");
            if (name.indexOf("br")==-1){
                tagPos++;
            }
        }
    }

    @Override
    public void endElement(String name) {
        if (bodyFlag && isPrint(name)) {
            tagPos--;
            for (int i=0;i<tagPos;i++){
                System.out.print(" ");
            }
            System.out.println("</" + name + ">");
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (bodyFlag) {
            System.out.print("??");
            System.out.print(ch);
            System.out.println("??");
        }
    }

    public void characters(String str) {
        if (bodyFlag) {
            for (int i=0;i<tagPos+1;i++){
                System.out.print(" ");
            }
            System.out.println(str);
        }
    }

    @Override
    public void comment(char[] ch, int start, int length) {
        String str=new String(ch);
        if (str.indexOf("<!-------- вместо <body> вставятся ссылки на произведения! ------>")!=-1) {
            bodyFlag=true;
        }
        if (str.indexOf("<!--------- Подножие ------------------------------->")!=-1){
            bodyFlag=false;
        }
    }

    @Override
    public void endDocument() {
    }

    private Boolean isPrint(String name)  {
        if (blackList.contains(name))   {
            return false;
        } else if (skipNextEndTag.isSkip(name)){
            skipNextEndTag.skiped();
            return false;
        }else {return true;}
    }
    private Boolean isPrint(String name, Attributes att)  {
        if (blackList.contains(name))   {
            return false;
        } else if(name.equals("a") && att.getValue("name").indexOf("gr")!=-1){
            skipNextEndTag.skipNext(name);
//            System.out.println("skip next");
            return false;
        }else {
            return true;
        }
    }

    private class SkipNextEndTag{
        private String name;
        private boolean skip;

        boolean isSkip(String skipName) {
            return  (skipName.equals(name)&&skip==true)? true : false;
        }

        void skipNext(String name){
            this.name=name;
            skip=true;
        }

        String getName() {
            return name;
        }
        void skiped(){
            skip=false;
        }

    }
}
