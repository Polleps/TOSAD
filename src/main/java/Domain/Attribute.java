package Domain;

/**
 * Created by Polle on 1-2-2017.
 */
public class Attribute {
    private String table;
    private String name;
    private int pos;
    private String entityType;



    public Attribute(String table, String name, int pos, String entityType) {
        this.table = table;

        this.name = name;
        this.pos = pos;
        this.entityType = entityType;
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
    public  String getEntityType() { return entityType;}
}
