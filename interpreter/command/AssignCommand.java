package interpreter.command;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import interpreter.expr.*;

public class AssignCommand extends Command{

    private List<Expr> left;
    private List<Expr> right;

    public AssignCommand(int line, List<Expr> left, List<Expr> right)
    {
        super(line);

        this.left = new ArrayList<Expr>();
        this.left.addAll(left);

        this.right = new ArrayList<Expr>();
        this.right.addAll(right);

    }

    public void execute()
    {
        Iterator<Expr> lit = this.left.iterator();
        Iterator<Expr> rit = this.right.iterator();

        while (lit.hasNext() && rit.hasNext())
        {
            
            AccessExpr auxAE = (AccessExpr) lit.next();
            Expr auxE = rit.next();

            auxAE.setValue(auxE.expr());
        }
    }


}
