package Domain;

import java.util.ArrayList;

/**
 * Created by Polle on 2-2-2017.
 */
public class Table {
    private String name;
    private ArrayList<Column> columns = new ArrayList<Column>();

    public Table(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void addAttribute(Column attr){
        columns.add(attr);
    }
    public ArrayList<Column> getAttributes(){return columns;}

    public String getJSON(){
        String out = "{table:'" + getName() + "', columns:[";
        if(columns != null) {
            for (Column s : columns) {
                out += "'" + s.getName() + "',";
            }
        }
        out += "]}";
        return out;
    }
}
