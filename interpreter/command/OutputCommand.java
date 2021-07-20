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
                if(expr != null)
                    System.out.println((expr).expr().toString());
                else
                    System.out.println();
                break;
            
            case PrintOp:
                if(expr != null)
                    System.out.print(expr.expr().toString());
                else
                    System.out.println();
                break;
        }
       
    }

        

}
