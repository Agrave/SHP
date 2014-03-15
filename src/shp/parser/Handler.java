/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shp.parser;

/**
 *
 * @author Agrave
 */
public interface Handler {
    public void startElement(String name,Attributes attr);
    public void endElement(String name);
    public void startDocument();
    public void endDocument();
    public void comment(char[] ch, int start, int length);
    public void characters(char[] ch, int start, int length);
    public void characters(String str);
    
}
