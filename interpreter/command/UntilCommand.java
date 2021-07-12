package interpreter.command;

import interpreter.expr.*;

public class UntilCommand extends Command {
    
    private BoolExpr cond;
    private Command cmds;

    public UntilCommand(int line, BoolExpr cond, Command cmds)
    {
        super(line);
        this.cond = cond;
        this.cmds = cmds;
    }

    public void execute()
    {
        do { cmds.execute(); } while (!cond.expr());

    }

}
