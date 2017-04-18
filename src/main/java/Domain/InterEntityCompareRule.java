package Domain;

/**
 * Created by Polle on 1-2-2017.
 */
public class InterEntityCompareRule  implements Rule{
    private String operator;
    private Database db;
    private Attribute attrA;
    private Attribute attrB;
    private String name;

    public InterEntityCompareRule(String operator, Database db, Attribute attrA, Attribute attrB, String name) {
        this.operator = operator;
        this.db = db;
        this.attrA = attrA;
        this.attrB = attrB;
        this.name = name;
    }

    @Override
    public String generateConstraint() {
        String constraint = "";
        String trigger = "";
        if(db instanceof SQLDatabase || db instanceof OracleDatabase || db == null){
            //constraint = "ALTER TABLE " + attrA.getTable() + " ADD (CONSTRAINT " + name + " CHECK (" + attrA.getTable() + "." + attrA.getName() + " " + Controller.translateOperator(operator, "sql") + " " + attrB.getTable() + "." + attrB.getName() + "))";
            trigger = "CREATE TRIGGER " + "name" + " BEFORE INSERT ON " + attrA.getTable() +
                    "DECLARE " +
                    "cursor curs is select " + attrB.getTable() + "." + attrB.getName() + " from " + attrB.getTable();
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
