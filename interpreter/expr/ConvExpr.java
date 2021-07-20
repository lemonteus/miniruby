package interpreter.expr;

import interpreter.value.*;

public class ConvExpr extends Expr {
    
    private Expr expr;
    private ConvOp op;

    public ConvExpr(int line, ConvOp op, Expr expr)
    {
        super(line);
        this.expr = expr;
        this.op = op;
    }

    public Value<?> expr()
    {
        Value<?> auxV = this.expr.expr();

        if (auxV instanceof IntegerValue)
        {
            switch (this.op)
            {
                case PlusOp:
                    break;
                
                case MinusOp:
                    IntegerValue auxIV = (IntegerValue) auxV;
                    auxV = new IntegerValue(-auxIV.value());
                    break;
            }
        }

        return auxV;
    }

}
