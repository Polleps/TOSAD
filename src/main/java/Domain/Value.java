package Domain;

import java.util.Scanner;

/**
 * Created by Polle on 1-2-2017.
 */
public class Value {
    private String val;
    private int valPos;

    public Value(String val, int valPos) {
        this.val = val;
        this.valPos = valPos;

    }

    public String getVal() {
        return "'" + val + "'";
    }
    public String getRawVal(){
        return val;
    }

    public int getValPos() {
        return valPos;
    }
}
