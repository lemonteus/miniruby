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

        //TODO:consertar tratamento de erros, para todos os casos

        switch (op) {

            case And:
                exprValidation = (left.expr() && right.expr());
                break;

            case Or:
                exprValidation = (left.expr() || right.expr());
                break;
            
            default:
                break;
                    
        }
        
        return exprValidation;
    }
    
}
