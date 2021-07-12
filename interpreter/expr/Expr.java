package interpreter.expr;

import interpreter.value.*;

public abstract class Expr {
    
    private int line;

    protected Expr(int line)
    {
        this.line = line;
    }

    public int getLine()
    {
        return this.line;
    }

    public abstract Value<?> expr();

}
