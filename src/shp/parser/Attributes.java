/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shp.parser;

/**
 *
 * @author Agrave
 */
public interface Attributes {
    public int getLength();
    public int getIndex(String name);
    public String getName(int index);
    public String getValue(int index);
    public String getValue(String name);
    public void addAttribute(String name,String value);
}
