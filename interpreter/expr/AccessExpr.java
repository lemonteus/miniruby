package interpreter.expr;

import java.util.Vector;

import interpreter.value.*;

import interpreter.util.*;

public class AccessExpr extends SetExpr {
    
    private Expr base;
    private Expr index;

    public AccessExpr(int line, Expr base, Expr index)
    {
        super(line);
        this.base = base;
        this.index = index;
    }

    public Value<?> expr()
    {
        Value<?> baseValue = this.base.expr();
        
        if (index == null)
        {
            return baseValue;

        } else {

            if (baseValue instanceof ArrayValue) {
                
                ArrayValue auxAV = (ArrayValue) baseValue;
                Value<?> indexValue = index.expr();

                if (indexValue instanceof IntegerValue)
                {
                    IntegerValue integerValue  = (IntegerValue) indexValue;
                    Vector<Value<?>> arrayValue = auxAV.value();

                    int index = integerValue.value();

                    Value<?> returnValue = null;

                    if (index >= 0 && index < arrayValue.size())
                    {
                        returnValue = arrayValue.elementAt(index);
                    }

                    if (returnValue == null)
                    {
                        returnValue = new StringValue("");
                    }

                    return returnValue;
                }

            }

            Execution.stop(this.getLine());
            return null;

        }
    }

    public void setValue (Value<?> value)
    {
        if (index == null)
        {            
            if (base instanceof SetExpr)
            {
                SetExpr auxSE = (SetExpr) base;
                auxSE.setValue(value);
                return;
            }

        } else {
            
            Value<?> baseValue = base.expr();
        
            if (baseValue instanceof ArrayValue)
            {
                ArrayValue auxArray = (ArrayValue) baseValue;
                Value<?> indexValue = index.expr();

                if (indexValue instanceof IntegerValue)
                {
                    IntegerValue auxI = (IntegerValue) indexValue;
                    Vector<Value<?>> auxV = auxArray.value();

                    int index = auxI.value();

                    if (index > -1)
                    {
                        if (index >= auxV.size())
                        {
                            auxV.setSize(index + 1);
                        }

                        auxV.setElementAt(value, index);
                        return;
                    }
                }
             }
       }
   }
}   
