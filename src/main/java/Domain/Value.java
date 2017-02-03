package Domain;

/**
 * Created by Polle on 1-2-2017.
 */
public class Value {
    private String val;
    private int valPos;
    private String varType;

    public Value(String val, int valPos, String varType) {
        this.val = val;
        this.valPos = valPos;
        this.varType = varType;
        Controller.printToConsole(varType);
    }

    public String getVal() {
        return val;
    }

    public int getValPos() {
        return valPos;
    }

    public String getSqlName(){
        if(varType.equals("Text") || varType.equals("Char") ||varType.equals("Date")){
            return "'" + val + "'";
        }
        else{
            return val;
        }
    }
}
