package Domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Polle on 31-1-2017.
 */
public class Controller {
    public static String out = "<strong>Output:</strong><br>";
    private static List<Rule> rules = new ArrayList<Rule>();
    private static RuleFactory ruleFactory = new RuleFactory();
    public static String getOut(){
        return out;
    }
    public static void printToConsole(String err){
        out += err + "<br>";
    }

    public static void addRule(String type, String name, Database db, List<Attribute> attrs, List<Value> vals, String operator){
        rules.add(ruleFactory.createRule(type, name, db, attrs, vals, operator));
        printToConsole("<span style='color:green;'> Added rule: " + rules.get(rules.size() - 1).getName() + "</span>");
    }

    public static ArrayList<String> generateRules(){
        ArrayList<String> constraints = new ArrayList<String>();
        for(Rule r : rules){
            constraints.add(r.generateConstraint());
        }
        rules.clear();
        return constraints;
    }

    public static String translateOperator(String op, String lang){
        ArrayList<String> opWords = new ArrayList<String>();
        Collections.addAll(opWords,"Equals", "NotEquals", "LessThan", "GreaterThan", "LessOrEqualTo", "GreaterOrEqualTo", "Between", "NotBetween");
        ArrayList<String> sql = new ArrayList<String>();
        Collections.addAll(sql,"=", "!=", "<", ">", "<=", ">=", "BETWEEN", "NOT BETWEEN");
        ArrayList<String> oracle = new ArrayList<String>();
        Collections.addAll(oracle,"=", "!=", "<", ">", "<=", ">=", "BETWEEN", "NOT BETWEEN");
        if(lang.equals("sql")){
            return sql.get(opWords.indexOf(op));
        }
        else if(lang.equals("oracle")){
            return oracle.get(opWords.indexOf(op));
        }
        return null;

    }





}
