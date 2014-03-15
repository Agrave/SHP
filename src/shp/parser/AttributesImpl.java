package shp.parser;

import java.util.ArrayList;
import java.util.List;

public class AttributesImpl implements Attributes{
    private List<Attribute> attsList;

    public AttributesImpl() {
        this.attsList = new ArrayList(); 
    }
    
    @Override
    public int getLength() {
        return attsList.size();
    }

    @Override
    public int getIndex(String name) {
        for (int i=0;i<=attsList.size();i++)
            if (name.equalsIgnoreCase(attsList.get(i).getName())) return i;
        return -1;
    }

    @Override
    public String getName(int index) throws IndexOutOfBoundsException{
        if (index <0 || attsList.isEmpty() || index>= attsList.size()) 
            throw new IndexOutOfBoundsException();
        else return attsList.get(index).getName();
    }    

    @Override
    public String getValue(int index) {
        if (index <0 || attsList.isEmpty() || index>= attsList.size()) 
            throw new IndexOutOfBoundsException();
        else return attsList.get(index).getValue();
    }

    @Override
    public String getValue(String name) {
        for (int i=0;i<attsList.size();i++)
            if (name.equalsIgnoreCase(attsList.get(i).getName())) return attsList.get(i).getValue();
        return "";
    }
    
    @Override
    public void addAttribute(String name, String value){
        Attribute att=new Attribute(name,value);
        this.attsList.add(att);
    }
//    public void addAttribute(Attribute att){
//        this.attsList.add(att);
//    }
    
    public void clear(){
        this.attsList.clear();
    }
    

private class Attribute {
     private String name;
     private String value;

        public Attribute(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setValue(String value) {
            this.value = value;
        }
     
}
    
}    


