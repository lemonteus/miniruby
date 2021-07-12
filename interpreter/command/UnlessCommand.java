package interpreter.command;

import interpreter.expr.*;

public class UnlessCommand extends Command {
    
    private BoolExpr cond;
    private Command thenCmds;
    private Command elseCmds;

    public UnlessCommand(int line, BoolExpr cond, Command thenCmds)
    {
        super(line);
        this.cond = cond;
        this.thenCmds = thenCmds;
        this.elseCmds = null;
    }

    public UnlessCommand(int line, BoolExpr cond, Command thenCmds, Command elseCmds)
    {
        super(line);
        this.cond = cond;
        this.thenCmds = thenCmds;
        this.elseCmds = elseCmds;
    }

    public void execute()
    {
        if (!cond.expr())
            thenCmds.execute();
        else if (elseCmds != null)
            elseCmds.execute();
    }
}
