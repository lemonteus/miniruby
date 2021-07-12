package interpreter.command;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

public class BlocksCommand extends Command {
    
    private List<Command> cmds;

    public BlocksCommand(int line, List<Command> cmds)
    {
        super(line);
        this.cmds = new ArrayList<Command>();
        this.cmds.addAll(cmds);
    }

    public void execute() {

        Iterator<Command> it = this.cmds.listIterator();

        while(it.hasNext())
        {
            it.next().execute();
        }

    }

}
