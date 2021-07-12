package interpreter.expr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Math;

import interpreter.value.*;

public class InputExpr extends Expr {

    private InputOp op;

    public InputExpr(int line, InputOp op)
    {
        super(line);
        this.op = op;
    }

    public Value<?> expr()
    {      
        switch (op)
        {
            case GetsOp:
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                
                try {

                    String auxString = reader.readLine();
                    StringValue returnSV = new StringValue(auxString);
                    return ((Value<?>) returnSV);

                } catch (IOException e)
                {
                    System.out.println(e.getMessage());
                    System.exit(0);
                    //TODO: Make this better
                }

                break;

            case RandOp:
                int auxInt = (int) Math.random() * (Integer.MAX_VALUE - Integer.MIN_VALUE + 1) + Integer.MIN_VALUE;
                IntegerValue returnIV = new IntegerValue(auxInt);
                return ((Value<?>) returnIV);

            default:
                break;
        }
    
        //TODO: Erro aconteceu aqui, fazer print do log e tals
        return null;
    }
    
}
