package interpreter.expr;

import interpreter.value.*;

public class ConstExpr extends Expr {
    
    private Value<?> value;

    public ConstExpr(int line, Value<?> value)
    {
        super(line);
        this.value = value;
    }

    public Value<?> expr()
    {
        return value;
    }

}
