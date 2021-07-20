package interpreter.expr;

import java.util.Vector;

import interpreter.value.*;
import interpreter.util.*;

public class BinaryExpr extends Expr {

    private Expr left;
    private BinaryOp op;
    private Expr right;

    public BinaryExpr (int line, Expr left, BinaryOp op, Expr right)
    {
        super(line);
        this.left = left;
        this.op = op;
        this.right = right;
    }

    public Value<?> expr()
    {
        Value<?> leftValue = (Value<?>) this.left.expr();
        Value<?> rightValue = (Value<?>) this.right.expr();

        /*if (!(leftValue.getClass().equals(rightValue.getClass())))
        {
            System.out.println("ATENCAO: TIPOS DIFERENTES");
        }*/

        switch (op)
        {
            case RangeWithOp:

                if (!(leftValue instanceof IntegerValue) || !(rightValue instanceof IntegerValue))
                {
                    Execution.stop(this.getLine());
                    return null;
                }
                else
                {
                    IntegerValue auxLV = (IntegerValue) leftValue;
                    IntegerValue auxRV = (IntegerValue) rightValue;

                    Vector<Value<?>> vector = new Vector<Value<?>>();

                    if (auxLV.value() < auxRV.value())
                    {
                        for (int i = auxLV.value(); i <= auxRV.value(); i++)
                            vector.add(new IntegerValue(i));

                    } else if (auxLV.value() > auxRV.value()) 
                    {
                        for (int i = auxLV.value(); i >= auxRV.value(); i--)
                            vector.add(new IntegerValue(i));
                    } else
                    {
                        vector.add(auxLV);
                    }

                    return new ArrayValue(vector);
                }

            case RangeWithoutOp:

                if (!(leftValue instanceof IntegerValue) || !(rightValue instanceof IntegerValue))
                    {
                        Execution.stop(this.getLine());
                        return null;
                    }
                    else
                    {
                        IntegerValue auxLV = (IntegerValue) leftValue;
                        IntegerValue auxRV = (IntegerValue) rightValue;
    
                        Vector<Value<?>> vector = new Vector<Value<?>>();
    
                        if (auxLV.value() < auxRV.value())
                        {
                            for (int i = auxLV.value(); i < auxRV.value(); i++)
                                vector.add(new IntegerValue(i));
    
                        } else if (auxLV.value() > auxRV.value()) 
                        {
                            for (int i = auxLV.value(); i > auxRV.value(); i--)
                            vector.add(new IntegerValue(i));
                        } 

                        return new ArrayValue(vector);
                    }

            case AddOp:
                                    
                if (leftValue instanceof IntegerValue && rightValue instanceof IntegerValue)
                    return new IntegerValue(((Integer) leftValue.value()) + ((Integer) rightValue.value()));
                else if (leftValue instanceof StringValue)
                {
                    if (rightValue instanceof StringValue)
                        return new StringValue(((String) leftValue.value()) + ((String) rightValue.value()));
                    else if (rightValue instanceof IntegerValue)
                        return new StringValue(((String) leftValue.value()) + (String.valueOf((Integer)rightValue.value())));
                    else if (rightValue instanceof ArrayValue)
                        return new StringValue(((String) leftValue.value()) + (rightValue.value().toString()));
                }
                else if (leftValue instanceof ArrayValue && rightValue instanceof ArrayValue)
                {
                   
                    Vector<Value<?>> vector = new Vector<Value<?>>();
                    vector.addAll(((ArrayValue)leftValue).value());
                    vector.addAll(((ArrayValue)rightValue).value());
                    return new ArrayValue(vector);
                }
                else {
                    Execution.stop(this.getLine());
                    return null;
                }
            
            case SubOp: 

                if (leftValue instanceof IntegerValue && rightValue instanceof IntegerValue)
                    return new IntegerValue(((Integer) leftValue.value()) - ((Integer) rightValue.value()));
                else if (leftValue instanceof StringValue && rightValue instanceof StringValue)
                {
                    Execution.stop(this.getLine());
                    return null;
                }
                else if (leftValue instanceof ArrayValue && rightValue instanceof ArrayValue)
                {
                    Vector<Value<?>> vector = new Vector<Value<?>>();
                    vector.addAll(((ArrayValue)leftValue).value());
                    vector.removeAll(((ArrayValue)rightValue).value());
                    return new ArrayValue(vector);
                }
                else {
                    Execution.stop(this.getLine());
                    return null;
                }
            
            case MulOp:

                if (leftValue instanceof IntegerValue && rightValue instanceof IntegerValue)
                    return new IntegerValue(((Integer) leftValue.value()) * ((Integer) rightValue.value()));
                else {
                    Execution.stop(this.getLine());
                    return null;
                }
            
            case DivOp:

                if (leftValue instanceof IntegerValue && rightValue instanceof IntegerValue)
                    return new IntegerValue(((Integer) leftValue.value()) / ((Integer) rightValue.value()));
                else {
                    Execution.stop(this.getLine());
                    return null;
                }
            
            case ModOp:
                if (leftValue instanceof IntegerValue && rightValue instanceof IntegerValue)
                    return new IntegerValue(((Integer) leftValue.value()) % ((Integer) rightValue.value()));
                else {
                    Execution.stop(this.getLine());
                    return null;
                }
            
            case ExpOp:
                if (leftValue instanceof IntegerValue && rightValue instanceof IntegerValue) {
                    Integer base = ((Integer) leftValue.value());
                    Integer exponent = ((Integer) rightValue.value());
                    return new IntegerValue((int) Math.pow(base, exponent));
                }
                else {
                    Execution.stop(this.getLine());
                    return null;
                }
            
            default:
            Execution.stop(this.getLine());
            return null;
        }
    }
    
}
