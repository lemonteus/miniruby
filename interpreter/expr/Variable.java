package interpreter.expr;

import interpreter.value.*;

public class Variable extends SetExpr {

    //TODO

    private String name;

    public Variable (int line, String name)
    {
        super(line);
        this.name = name;
    }

    public Value<?> expr()
    {
        return null;
    }

    public void setValue(Value<?> value)
    {
        
    }
    
}
