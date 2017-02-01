package Domain;

/**
 * Created by Polle on 1-2-2017.
 */
public class Attribute {
    private String table;
    private String name;
    private int pos;

    public Attribute(String table, String name, int pos) {
        this.table = table;
        this.name = name;
        this.pos = pos;
    }

    public String getTable() {
        return table;
    }

    public String getName() {
        return name;
    }

    public int getPos() {
        return pos;
    }
}
