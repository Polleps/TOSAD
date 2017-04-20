package Domain;

/**
 * Created by Polle on 1-2-2017.
 */
public class InterEntityCompareRule  implements Rule{
    private String operator;
    private Database db;
    private Attribute attrA;
    private Attribute attrB;
    private Value primaryKey;
    private Value foreignKey;
    private String name;

    public InterEntityCompareRule(String operator, Database db, Attribute attrA, Attribute attrB, Value primaryKey, Value foreignKey , String name) {
        this.operator = operator;
        this.db = db;
        this.attrA = attrA;
        this.attrB = attrB;
        this.name = name;
        this.primaryKey = primaryKey;
        this.foreignKey = foreignKey;
    }

    @Override
    public String generateConstraint() {
        String trigger = "";
        if(db instanceof SQLDatabase || db instanceof OracleDatabase || db == null){
            //constraint = "ALTER TABLE " + attrA.getTable() + " ADD (CONSTRAINT " + name + " CHECK (" + attrA.getTable() + "." + attrA.getName() + " " + Controller.translateOperator(operator, "sql") + " " + attrB.getTable() + "." + attrB.getName() + "))";
            trigger = "create or replace trigger \""+ name +"\" " +
                "BEFORE " +
                "insert or update on \""+ attrA.getTable() +"\" " +
                "for each row " +
                "declare " +
                "cursor cur is SELECT attrb."+ attrB.getName() +" FROM " + attrB.getTable() + " attrb WHERE attrb." + foreignKey.getRawVal() + " = :new." + primaryKey.getRawVal() + "; " +
                "attrb_value " + attrB.getTable() + "." + attrB.getName() + "%type; " +
                "BEGIN " +
                "open cur; " +
                "fetch cur into attrb_value; " +
                "close cur; " +
                "if attrb_value " + Controller.translateOperator(operator, "sql") + " :new." + attrA.getName() + " " +
                "then " +
                "raise_application_error(-20000,'Constraind: " + name + " voilated'); " +
                "end if; " +
                "end; ";
        }
        else{
            Controller.printToConsole("ERROR: InterEntityCompareRule is not supported for this database!");
            return null;
        }
        return trigger;
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
