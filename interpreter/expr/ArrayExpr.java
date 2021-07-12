package interpreter.expr;

import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Iterator;

import interpreter.value.*;

public class ArrayExpr extends Expr {
    
    private List<Expr> exprs;

    public ArrayExpr (int line, List<Expr> exprs)
    {
        super(line);
        this.exprs = new ArrayList<Expr>();
        this.exprs.addAll(exprs);
    }

    public Value<?> expr()
    {
        Vector<Value<?>> auxVector = new Vector<Value<?>>();

        Iterator<Expr> it = this.exprs.iterator();

        while(it.hasNext())
            auxVector.add((Value<?>) it.next().expr());

        return new ArrayValue(auxVector);
    }

}
