package interpreter.command;

import interpreter.expr.*;

public class IfCommand extends Command {
    
    private BoolExpr cond;
    private Command thenCmds;
    private Command elseCmds;
    
    public IfCommand (int line, BoolExpr cond, Command thenCmds)
    {
        super(line);
        this.cond = cond;
        this.thenCmds = thenCmds;
    }

    public IfCommand (int line, BoolExpr cond, Command thenCmds, Command elseCmds)
    {
        super(line);
        this.cond = cond;
        this.thenCmds = thenCmds;
        this.elseCmds = elseCmds;
    }

    public void setElseCommands (Command elseCmds)
    {
        this.elseCmds = elseCmds;
    }

    public void execute()
    {
        if (cond.expr())
        {
            thenCmds.execute();
        }
        else if (elseCmds != null)
        {
            elseCmds.execute();
        }
    }
    

}
