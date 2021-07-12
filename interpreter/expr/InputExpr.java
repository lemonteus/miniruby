package interpreter.expr;

import java.util.Scanner;
import java.util.Random;

import interpreter.value.*;

public class InputExpr extends Expr {

    private InputOp op;

    private static Scanner s;
    private static Random r;

    static {
        s = new Scanner (System.in);
        r = new Random();
    }

    public InputExpr(int line, InputOp op)
    {
        super(line);
        this.op = op;
    }

    public Value<?> expr()
    {      
        switch (this.op)
        {
            case GetsOp:             
                try {

                    String auxString = String.valueOf(s.nextLine());
                    StringValue returnSV = new StringValue(auxString);
                    s.close();
                    return (returnSV);

                } catch (Exception e)
                {
                    System.out.println(e.getMessage());
                    System.exit(0);
                    //TODO: Make this better
                }
                
                break;

            case RandOp:
                int auxInt = (int) r.nextInt((Integer.MAX_VALUE));
                IntegerValue returnIV = new IntegerValue(auxInt);
                return (returnIV);

            default:
                break;
        }
        

        //TODO: Erro aconteceu aqui, fazer print do log e tals
        return null;
    }
    
}
