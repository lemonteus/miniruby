package interpreter.expr;

import interpreter.value.*;

import java.util.Vector;

public class SingleBoolExpr extends BoolExpr {
    
    private Expr left;
    private Expr right;
    private RelOp op;

    public SingleBoolExpr(int line, Expr left, RelOp op, Expr right)
    {
        super(line);
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public boolean expr()
    {
        Value<?> expr1 = left.expr();
        Value<?> expr2 = right.expr();

        //TODO:consertar tratamento de erros, para todos os casos
        //TODO: trocar returns por atribuição ao exprValidation

        boolean exprValidation = false;

        switch (op) {
            case EqualsOp:
                if (expr1 instanceof IntegerValue && expr1 instanceof IntegerValue)
                    return expr1.value() == expr2.value();
                else if (expr1 instanceof StringValue && expr2 instanceof StringValue)
                    return expr1.value().equals(expr2.value());
                else {
                    
                    System.out.println("ERRO: Operacao invalida");
                    System.exit(1);
                }
                    break;
            case NotEqualsOp:
                if (expr1 instanceof IntegerValue && expr1 instanceof IntegerValue)
                    return expr1.value() != expr2.value();
                else if (expr1 instanceof StringValue && expr2 instanceof StringValue)
                    return (!expr1.value().equals(expr2.value()));
                    break;

            case LowerThanOp:
                if (expr1 instanceof IntegerValue && expr1 instanceof IntegerValue)
                    return (((int) expr1.value()) < ((int) expr2.value()));
                    break;
                
            case LowerEqualOp:
                if (expr1 instanceof IntegerValue && expr1 instanceof IntegerValue)
                    return (((int) expr1.value()) <= ((int) expr2.value()));
            case GreaterThanOp:
                if (expr1 instanceof IntegerValue && expr1 instanceof IntegerValue)
                    return (((int) expr1.value()) > ((int) expr2.value()));
                    break;
            case GreaterEqualOp:
                if (expr1 instanceof IntegerValue && expr1 instanceof IntegerValue)
                    return (((int) expr1.value()) >= ((int) expr2.value()));
                    break;
            case ContainsOp:
            default:
                if (expr2 instanceof ArrayValue)
                {   
                    ArrayValue arrayRight = (ArrayValue) right.expr();
                    Vector<Value<?>> auxV = arrayRight.value();
                    int rightExprLength = auxV.size();
                    boolean expr1Found = false;

                    if (expr1 instanceof IntegerValue)
                    {
                        for (int i = 0; i < rightExprLength; i++)
                        {
                            if (auxV.get(i) == expr1.value())
                                expr1Found = true;
                        }

                        return expr1Found;

                    } else if (expr2 instanceof StringValue)
                    {
                        for (int i = 0; i < rightExprLength; i++)
                        {
                            if (auxV.get(i).equals(expr1.value()))
                                expr1Found = true;
                        }

                        return expr1Found;
                    }

                }


                break;
        }

        return exprValidation;

    }

    
}
