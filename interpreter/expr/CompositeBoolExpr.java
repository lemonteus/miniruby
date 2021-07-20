package interpreter.expr;

public class CompositeBoolExpr extends BoolExpr {
    
    private BoolExpr left;
    private BoolExpr right;
    private BoolOp op;

    public CompositeBoolExpr(int line, BoolExpr left, BoolOp op, BoolExpr right)
    {
        super(line);
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public boolean expr()
    {
        boolean exprValidation = false;

        switch (op) {

            case And:
                if (right != null)
                    exprValidation = (left.expr() && right.expr());
                else
                    exprValidation = left.expr();
                break;

            case Or:
                if (right != null)
                    exprValidation = (left.expr() || right.expr());
                else
                    exprValidation = left.expr();
                break;
            
            default:
                exprValidation = left.expr();
                break;
                    
        }
        
        return exprValidation;
    }
    
}
