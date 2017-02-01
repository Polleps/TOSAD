package Domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Polle on 31-1-2017.
 */
public class Controller {
    public static String out = "Output:<br>";
    private static List<Rule> rules = new ArrayList<Rule>();
    private static RuleFactory ruleFactory = new RuleFactory();
    public static String getOut(){
        return out;
    }
    public static void printToConsole(String err){
        out += err + "<br>";
    }

    public static void addRule(int type, String name, Database db, List<Attribute> attrs, List<Value> vals, String operator){
        rules.add(ruleFactory.createRule(type, name, db, attrs, vals, operator));
    }

    public ArrayList<String> generateRules(){
        ArrayList<String> constraints = new ArrayList<String>();
        for(Rule r : rules){
            constraints.add(r.generateConstraint());
        }
        return constraints;
    }




}
