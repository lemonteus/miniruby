package interpreter.command;

import interpreter.expr.*;
import interpreter.value.*;
import interpreter.util.*;

import java.util.Vector;

public class ForCommand extends Command {
    
    private Variable var;
    private Expr expr;
    private Command cmds;

    public ForCommand(int line, Variable var, Expr expr, Command cmds)
    {
        super(line);
        this.var = var;
        this.expr = expr;
        this.cmds = cmds;
    }

    public void execute()
    {
        Value<?> auxExpr = (Value<?>) this.expr.expr();

        if (auxExpr instanceof IntegerValue)
        {
            int i = 0;
            var.setValue(new IntegerValue(i));
            
            int upperBound = ((IntegerValue)auxExpr).value();

            for (; i < upperBound; i++)
            {
                cmds.execute();
                var.setValue(new IntegerValue(i));
            }
        } else if (auxExpr instanceof ArrayValue)
        {
            ArrayValue auxAV = (ArrayValue) auxExpr;
            Vector<Value<?>> auxV = auxAV.value();

            int i = (int) auxV.firstElement().value();

            int upperBound = (int) auxV.lastElement().value();

            for (; i < upperBound; i++)
            {
                var.setValue(new IntegerValue(i));
                cmds.execute();
            }

        }
        else {
            Execution.stop(this.getLine());
        }
    }


}
