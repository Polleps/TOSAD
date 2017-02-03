package Domain;

/**
 * Created by Polle on 1-2-2017.
 */
public class AttributeRangeRule implements Rule {
    private String operator;
    private Database db;
    private Attribute attr;
    private Value valA;
    private Value valB;
    private String name;

    public AttributeRangeRule(String operator, Database db, Attribute attr, Value valA, Value valB, String name) {
        this.operator = operator;
        this.db = db;
        this.attr = attr;
        this.valA = valA;
        this.valB = valB;
        this.name = name;
    }

    @Override
    public String generateConstraint() {
        String constraint = "";
        if(db instanceof SQLDatabase || db instanceof OracleDatabase || db == null){
            constraint = "ALTER TABLE " + attr.getTable() + " ADD (CONSTRAINT " + name + " CHECK (" + attr.getName() + " " + Controller.translateOperator(operator, "sql") + " " + valA.getSqlName() + " AND " + valB.getSqlName() +" ))";
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
