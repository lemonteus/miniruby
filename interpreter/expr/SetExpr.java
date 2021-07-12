package interpreter.expr;

import interpreter.value.Value;

public abstract class SetExpr extends Expr {
    
    protected SetExpr(int line)
    {
        super(line);
    }

    public abstract void setValue (Value<?> value);

    public abstract Value<?> expr();

}
