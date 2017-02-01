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
        return null;
    }

    @Override
    public Database getDataBase() {
        return null;
    }
}
