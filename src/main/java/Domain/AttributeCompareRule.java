package Domain;

/**
 * Created by Polle on 1-2-2017.
 */
public class AttributeCompareRule implements Rule {
    private String operator;
    private Database db;
    private Attribute attr;
    private Value val;
    private String name;

    public AttributeCompareRule(String operator, Database db, Attribute attr, Value val, String name) {
        this.operator = operator;
        this.db = db;
        this.attr = attr;
        this.val = val;
        this.name = name;
    }

    @Override
    public String generateConstraint() {
        String constraint = "";
        if(db instanceof SQLDatabase || db instanceof OracleDatabase){
            constraint = "ALTER TABLE " + attr.getTable() + "ADD CONSTRAINT " + name + " CHECK (" + attr + " " + operator + " " + val + ");";
        }
        else{
            Controller.printToConsole("ERROR: AttributeCompareRule is not supported for this database!");
            return null;
        }
        return constraint;
    }

    @Override
    public Database getDataBase() {
        return null;
    }
}
