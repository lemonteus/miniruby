package interpreter.expr;

import interpreter.value.*;

import interpreter.util.Memory;

public class Variable extends SetExpr {

    private String name;

    public Variable (int line, String name)
    {
        super(line);
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public Value<?> expr()
    {
        return Memory.read(this.name);
    }

    public void setValue(Value<?> value)
    {
        Memory.write(this.name, value);
    }
    
}
