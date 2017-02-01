package Domain;

import java.util.List;

/**
 * Created by Polle on 1-2-2017.
 */
public class RuleFactory {

    public Rule createRule(int type, String name, Database db, List<Attribute> attrs, List<Value> vals, String operator){
        if(type == 1){
            return new AttributeCompareRule(operator,db, attrs.get(0), vals.get(0), name);
        }
        else if(type == 2){
            return new AttributeRangeRule(operator, db, attrs.get(0), vals.get(0), vals.get(1), name);
        }
        else if(type == 3){
            return new InterEntityCompareRule(operator,db,attrs.get(0), attrs.get(1), name);
        }
        else if(type == 4){
            return new TupelCompareRule(operator,db,attrs.get(0), attrs.get(1), name);
        }
        else{
            Controller.printToConsole("RuleType " + type + " does not exist!");
            return null;
        }
    }

}
