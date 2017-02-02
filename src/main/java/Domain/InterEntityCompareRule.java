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
        return null;
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
