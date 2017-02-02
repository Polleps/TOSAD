package Domain;

/**
 * Created by Polle on 1-2-2017.
 */
public class TupelCompareRule implements Rule {
    private String operator;
    private Database db;
    private Attribute attrA;
    private Attribute attrB;
    private String name;

    public TupelCompareRule(String operator, Database db, Attribute attrA, Attribute attrB, String name) {
        this.operator = operator;
        this.db = db;
        this.attrA = attrA;
        this.attrB = attrB;
        this.name = name;
    }

    @Override
    public String generateConstraint() {
        String constraint = "";
        if(db instanceof SQLDatabase || db instanceof OracleDatabase){
            constraint = "ALTER TABLE " + attrA.getTable() + "ADD CONSTRAINT " + name + " CHECK (" + attrA + " " + Controller.translateOperator(operator, "sql") + " " + attrB + ");";
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

    @Override
    public String getName() {
        return name;
    }
}
