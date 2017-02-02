package Domain;

/**
 * Created by Polle on 1-2-2017.
 */
public interface Rule {
    public String generateConstraint();
    public Database getDataBase();
    public String getName();
}
