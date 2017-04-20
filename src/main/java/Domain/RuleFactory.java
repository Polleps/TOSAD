package Domain;

import java.util.List;

/**
 * Created by Polle on 1-2-2017.
 */
public class RuleFactory {

    public Rule createRule(String type, String name, Database db, List<Attribute> attrs, List<Value> vals, String operator){
        if(type.equals("AttributeCompareRule")){
            return new AttributeCompareRule(operator,db, attrs.get(0), vals.get(0), name);
        }
        else if(type.equals("AttributeRangeRule")){
            return new AttributeRangeRule(operator, db, attrs.get(0), vals.get(0), vals.get(1), name);
        }
        else if(type.equals("Inter-EntityCompareRule")){
            return new InterEntityCompareRule(operator,db,attrs.get(0), attrs.get(1), vals.get(0), vals.get(1), name);
        }
        else if(type.equals("TupelCompareRule")){
            return new TupelCompareRule(operator,db,attrs.get(0), attrs.get(1), name);
        }
        else{
            Controller.printToConsole("RuleType " + type + " does not exist!");
            return null;
        }
    }

}
