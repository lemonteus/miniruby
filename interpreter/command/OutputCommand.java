package interpreter.command;

import interpreter.expr.*;

public class OutputCommand extends Command {

    private OutputOp op;
    private Expr expr;

    public OutputCommand(int line, OutputOp op)
    {
        super(line);
        this.op = op;
    }

    public OutputCommand(int line, OutputOp op, Expr expr)
    {
        super(line);
        this.op = op;
        this.expr = expr;
    }

    public void execute()
    {
        switch (this.op) {

            case PutsOp:
                if (expr instanceof ConstExpr)
                System.out.println(expr.toString());
                break;
            
            case PrintOp:
                System.out.print(expr);
                break;
        }
       
    }

        

}
