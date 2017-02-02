package Domain;

import java.util.ArrayList;

/**
 * Created by Polle on 2-2-2017.
 */
public class Table {
    private String name;
    private ArrayList<String> columns;

    public Table(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void addAttribute(String attr){
        columns.add(attr);
    }

    public String getJSON(){
        String out = "{table:'" + getName() + "', columns:[";
        for(String s : columns){
            out += "'" + s + "',";
        }
        out += "]}";
        return out;
    }
}
