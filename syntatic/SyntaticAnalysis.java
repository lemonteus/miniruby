package syntatic;

import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

import lexical.Lexeme;
import lexical.LexicalAnalysis;
import lexical.TokenType;
import interpreter.command.*;
import interpreter.expr.*;
import interpreter.value.*;
import interpreter.util.*;


public class SyntaticAnalysis {

    private LexicalAnalysis lex;
    private Lexeme current;

    public SyntaticAnalysis(LexicalAnalysis lex) {
        this.lex = lex;
        this.current = lex.nextToken();
    }

    public Command start() {
        Command cmd = (Command) procCode();
        eat(TokenType.END_OF_FILE);

        return cmd;
    }

    private void advance() {
        //System.out.println("Advanced (\"" + current.token + "\", " +
        //    current.type + ")");
        current = lex.nextToken();
    }

    private void eat(TokenType type) {
        //System.out.println("Expected (..., " + type + "), found (\"" + 
        //     current.token + "\", " + current.type + ")");
        if (type == current.type) {
            current = lex.nextToken();
        } else {
            showError();
        }
    }

    private void showError() {
        System.out.printf("%02d: ", lex.getLine());

        switch (current.type) {
            case INVALID_TOKEN:
                System.out.printf("Lexema inválido [%s]\n", current.token);
                break;
            case UNEXPECTED_EOF:
            case END_OF_FILE:
                System.out.printf("Fim de arquivo inesperado\n");
                break;
            default:
                System.out.printf("Lexema não esperado [%s]\n", current.token);
                break;
        }

        System.exit(1);
    }

    private BlocksCommand procCode() { 

        int line = lex.getLine();

        List<Command> auxList = new ArrayList<Command>();
        auxList.add(procCmd());

        while (current.type == TokenType.ID || 
               current.type == TokenType.PUTS || 
               current.type == TokenType.PRINT ||
               current.type == TokenType.RAND ||  
               current.type == TokenType.GETS || 
               current.type == TokenType.IF ||
               current.type == TokenType.UNLESS ||
               current.type == TokenType.WHILE || 
               current.type == TokenType.UNTIL ||   
               current.type == TokenType.FOR) {

            auxList.add(procCmd());
        } 

        return new BlocksCommand(line, auxList);
    }

private Command procCmd() { 

    Command c = null;

    switch (current.type)
    {
        case IF:
            c = (Command) procIf();
            break;
        
        case UNLESS:
            c = (Command) procUnless();
            break;
        
        case WHILE:
            c = (Command) procWhile();
            break;
        
        case UNTIL:
            c = (Command) procUntil();
            break;
        
        case FOR:
            c = (Command) procFor();
            break;

        case PUTS:
        case PRINT:
            c = (Command) procOutput();
            return c;
        
        case ID:
            c = (Command) procAssign();
            return c;

        default:
            showError();
            break;
    }

    return c;
}

private IfCommand procIf() { 

    int line = lex.getLine();

    eat(TokenType.IF);
    BoolExpr cond = procBoolExpr();

    if(current.type == TokenType.THEN)
        advance();

    Command thenCmds = procCode();

    IfCommand ifCmd = new IfCommand(line, cond, thenCmds);

    Vector<IfCommand> nestedIfs = new Vector<IfCommand>();

    int i = 0;

    while (current.type == TokenType.ELSIF)
    {
        int elsifLine = lex.getLine();

        advance();
        BoolExpr elsifCond = procBoolExpr();

        if(current.type == TokenType.THEN)
            advance();

        Command elsifThenCmds = procCode();

        nestedIfs.add(new IfCommand (elsifLine, elsifCond, elsifThenCmds));
        if (nestedIfs.size() > 1)
            nestedIfs.get(i-1).setElseCommands(nestedIfs.lastElement());
        
        i++;
    }

    Command elseCmds = null;

    if (current.type == TokenType.ELSE)
    {   advance();
        elseCmds = procCode();
    }

    if(!(nestedIfs.isEmpty()))
    {
        nestedIfs.lastElement().setElseCommands(elseCmds);
        ifCmd.setElseCommands(nestedIfs.firstElement());
    } else {
        if (elseCmds != null)
            ifCmd.setElseCommands(elseCmds);
    }

    eat(TokenType.END);

    return ifCmd;

 }

private UnlessCommand procUnless() { 
    
    int line = lex.getLine();

    eat(TokenType.UNLESS);

    BoolExpr cond = procBoolExpr();

    if (current.type == TokenType.THEN)
        advance();

    Command thenCmds = procCode();
    Command elseCmds = null;

    if (current.type == TokenType.ELSE)
    {
        advance();
        elseCmds = procCode();
    }

    eat(TokenType.END);

    return new UnlessCommand(line, cond, thenCmds, elseCmds);
 }

private WhileCommand procWhile() { 

    int line = lex.getLine();
    eat(TokenType.WHILE);
    
    BoolExpr cond = procBoolExpr();
    
    if (current.type == TokenType.DO)
        advance();
    
    Command cmds = procCode();

    eat(TokenType.END);

    return new WhileCommand(line, cond, cmds);

}

private UntilCommand procUntil() {

    int line = lex.getLine();

    eat(TokenType.UNTIL);
    BoolExpr cond = procBoolExpr();
    
    if (current.type == TokenType.DO)
        advance();

    Command cmds = procCode();

    eat(TokenType.END);

    return new UntilCommand(line, cond, cmds);
}

private ForCommand procFor() { 

    int line = lex.getLine();

    eat(TokenType.FOR);
    
    String var_id = current.token;
    eat(TokenType.ID);
    Variable var = new Variable(line, var_id);

    eat(TokenType.IN);
    
    Expr expr = procExpr();

    if (current.type == TokenType.DO)
        advance();
    
    Command blkcmd = procCode();

    eat(TokenType.END);

    return new ForCommand(line, var, expr, blkcmd);

 }

private Command procOutput() { 

    OutputOp op = null;
    Command c = null;
    int line = lex.getLine();

    if (current.type == TokenType.PUTS)
    {
        advance();
        op = OutputOp.PutsOp;

    } else if (current.type == TokenType.PRINT) 
    {
        op = OutputOp.PrintOp;
        advance();

    } else {
        showError();
    }

    if (current.type == TokenType.ID ||
        current.type == TokenType.OPEN_BRA ||
        current.type == TokenType.STRING ||
        current.type == TokenType.INTEGER ||
        current.type == TokenType.OPEN_PAR ||
        current.type == TokenType.GETS ||
        current.type == TokenType.RAND ||
        current.type == TokenType.ADD ||
        current.type == TokenType.SUB )
    {
        Expr auxExpr = procExpr();

        c = new OutputCommand(line, op, auxExpr);        
       
        if (current.type == TokenType.IF || current.type == TokenType.UNLESS)
            c = procPost(c);
    }

    eat(TokenType.SEMI_COLON);

    return c;

}

private Command procAssign() { 

    ArrayList<Expr> deList = new ArrayList<Expr>();
    ArrayList<Expr> seList = new ArrayList<Expr>();
    int line = lex.getLine();

    Expr destExpr = procAccess();
    deList.add(destExpr);

    int accessCount = 0;
    
    while (current.type == TokenType.COMMA)
    {
        advance();
        deList.add(procAccess());
        accessCount++;
    }

    eat(TokenType.ASSIGN);

    Expr sourceExpr = procExpr();
    seList.add(sourceExpr);

    while (accessCount > 0)
    {   
        if (current.type == TokenType.COMMA)
        {
            eat(TokenType.COMMA);
            seList.add(procExpr());
            accessCount--;

        } else {
            showError();
        }
    }

    Command c = null;

    if (current.type == TokenType.IF || current.type == TokenType.UNLESS)
        c = procPost(new AssignCommand(line, deList, seList));
    else
        c = new AssignCommand(line, deList, seList);

    eat(TokenType.SEMI_COLON);

    return c;

 }

private Command procPost(Command cmd) { 

    int line = lex.getLine();

    if (current.type == TokenType.IF)
    {
        advance();
        BoolExpr bxpr = procBoolExpr();
        return new IfCommand(line, bxpr, cmd);

    } else if (current.type == TokenType.UNLESS)
    {
        advance();
        BoolExpr bxpr = procBoolExpr();
        return new UnlessCommand(line, bxpr, cmd);

    } else {
        showError();
    }

    return null;
}

private BoolExpr procBoolExpr() { 

    int line = lex.getLine();
    boolean neg = false;

    if (current.type == TokenType.NOT)
    {
        neg = true;
        advance();
    }

    BoolOp op = null;
    BoolExpr cmpExpr = procCmpExpr();
    BoolExpr boolExpr = null;

    if (current.type == TokenType.AND || current.type == TokenType.OR)
    {
        if (current.type == TokenType.AND)
        {
            op = BoolOp.And;
            advance();
    
        } else if (current.type == TokenType.OR)
        {
            op = BoolOp.Or;
            advance();

        } else {
            showError();
        }

        boolExpr = procBoolExpr();
    }

    if (neg)
        return new NotBoolExpr(line, new CompositeBoolExpr(line, cmpExpr, op, boolExpr));
    else if (op != null)
        return new CompositeBoolExpr(line, cmpExpr, op, boolExpr);
    else 
        return cmpExpr;
    
 }

private SingleBoolExpr procCmpExpr() { 

    int line = lex.getLine();
    Expr expr = procExpr();
    RelOp op = null;

    switch (current.type) {
        
        case EQUALS:
            op = RelOp.EqualsOp;
            break;

        case NOT_EQUALS:
            op = RelOp.NotEqualsOp;
            break;
            
        case LESS_THAN:
            op = RelOp.LowerThanOp;
            break;
        
        case GREATER_THAN:
            op = RelOp.GreaterThanOp;
            break;
        
        case GREATER_EQ:
            op = RelOp.GreaterEqualOp;
            break;

        case LESS_EQ:
            op = RelOp.LowerEqualOp;
            break;
        
        case CONTAINS:
            op = RelOp.ContainsOp;
            break;
        
        default:   
            break;
    }

    advance();

    Expr expr2 = procExpr();

    return new SingleBoolExpr(line, expr, op, expr2);
}

private Expr procExpr() { 

    Expr expr = procArith();
    int line = lex.getLine();
    BinaryOp op = null;

    if (current.type == TokenType.RANGE_WITH || 
        current.type == TokenType.RANGE_WITHOUT)
    {
        if (current.type == TokenType.RANGE_WITH)
            op = BinaryOp.RangeWithOp;

        if (current.type == TokenType.RANGE_WITHOUT)
            op = BinaryOp.RangeWithoutOp;

        advance();
        Expr expr2 = procArith();
        Expr auxExpr = new BinaryExpr(line, expr, op, expr2);
        expr = auxExpr;
    }

    return expr;
 }

    private Expr procArith() { 

        Expr expr = procTerm();
        int line = lex.getLine();
        BinaryOp op = null;
    
        while (current.type == TokenType.ADD || current.type == TokenType.SUB) 
        {
            switch (current.type) {
    
                    case ADD:
                        op = BinaryOp.AddOp;
                        break;
    
                    case SUB:
                        op = BinaryOp.SubOp;
                        break;
    
                    default:
                        Execution.stop(line);
                        break;
            }
    
            advance();
            Expr expr2 = procTerm();
            Expr auxExpr = new BinaryExpr(line, expr, op, expr2);
            expr = auxExpr;
        }
            
        return expr;

    }

private Expr procTerm() { 

    Expr expr = procPower();
    int line = lex.getLine();
    BinaryOp op = null;

    while (current.type == TokenType.MUL || current.type == TokenType.DIV || current.type == TokenType.MOD) 
    {
        switch (current.type) {

                case MUL:
                    op = BinaryOp.MulOp;
                    break;

                case DIV:
                    op = BinaryOp.DivOp;
                    break;

                case MOD:
                    op = BinaryOp.ModOp;
                    break;

                default:
                    Execution.stop(lex.getLine());
                    break;
        }

        advance();
        Expr expr2 = procPower();
        Expr auxExpr = new BinaryExpr(line, expr, op, expr2);
        expr = auxExpr;
    }
        
    return expr;
}

private Expr procPower() { 

    int line = lex.getLine();
    Expr expr = procFactor();

    while (current.type == TokenType.EXP)
    {
        advance();
        Expr expr2 = procFactor();
        Expr auxExpr = new BinaryExpr(line, expr, BinaryOp.ExpOp, expr2);
        expr = auxExpr;
    }
    
    return expr;
}

private Expr procFactor() { 

    Expr expr = null;
    Boolean neg = false;
    int line = lex.getLine();

    if (current.type == TokenType.ADD)
    {
        advance();
    }
    else if (current.type == TokenType.SUB)
    {
        neg = true;
        advance();
    }

    switch (current.type) {
        
        case INTEGER:
        case STRING:
        case OPEN_BRA:
            expr = (Expr) procConst();
            break;
        
        case GETS:
        case RAND:
            expr = (Expr) procInput();
            break;

        case ID:
        case OPEN_PAR:
            expr = (Expr) procAccess();
            break;
        
        default:
            showError();
            break;
    }

    if (neg)
    {
        expr = new ConvExpr(line, ConvOp.MinusOp, expr);
        System.out.println("Expressão negativa:" + expr.expr().value());
    }


    //must verify if next token isn't interval operand
    if (current.type == TokenType.RANGE_WITH || current.type == TokenType.RANGE_WITHOUT)
    {
        
    }
    else if (current.type == TokenType.DOT)
    {
        expr = (Expr) procFunction(expr);
    }

    return expr;

}

private Expr procConst() { 

    ConstExpr cExpr = null;
    String auxS = null;
    int line = lex.getLine();

    switch (current.type) {
    
        case INTEGER:
            auxS = current.token;
    
            eat(TokenType.INTEGER);
            
            IntegerValue number;

            try {
                number = new IntegerValue(Integer.parseInt(auxS));
            } catch (Exception e)
            {
                number = new IntegerValue(0);
            }

            return new ConstExpr(line, number);
            
        case STRING:
            auxS = current.token;
            eat(TokenType.STRING);

            StringValue string = new StringValue(auxS);

            return new ConstExpr(line, string);

        case OPEN_BRA:
            ArrayExpr array = procArray();
            return array;
            
        default:
            showError();
            break;
    }

    return cExpr;
}

private InputExpr procInput() { 

    InputExpr auxIE = null;
    int line = lex.getLine();

    switch (current.type) {

        case GETS:
            eat(TokenType.GETS);
            return new InputExpr(line, InputOp.GetsOp);

        case RAND:
            eat(TokenType.RAND);
            line = lex.getLine();
            return new InputExpr(line, InputOp.RandOp);
        
        default:
            line = -120;
            break;
    }

    return auxIE;
    
}  

private ArrayExpr procArray() { 

    ArrayList<Expr> auxVector = new ArrayList<Expr>();
    int line = lex.getLine();
    
    eat(TokenType.OPEN_BRA);
    
    if (current.type == TokenType.ID ||
    current.type == TokenType.OPEN_BRA ||
    current.type == TokenType.STRING ||
    current.type == TokenType.INTEGER ||
    current.type == TokenType.OPEN_PAR ||
    current.type == TokenType.GETS ||
    current.type == TokenType.RAND ||
    current.type == TokenType.ADD ||
    current.type == TokenType.SUB )
    {
        auxVector.add(procExpr());

        while (current.type == TokenType.COMMA)
        {
            advance();
            auxVector.add(procExpr());
        }
    }

    eat(TokenType.CLOSE_BRA);
    return new ArrayExpr(line, auxVector);

}

private AccessExpr procAccess() {
    
    int line = lex.getLine();
    Expr baseExpr = null;
    Expr indexExpr = null;


    switch (current.type) {

        case ID:
            String varName = current.token;
            baseExpr = new Variable(line, varName);
            eat(TokenType.ID);
            break;

        case OPEN_PAR: 
            advance();
            baseExpr = procExpr();
            eat(TokenType.CLOSE_PAR);
            break;
        
        default:
            Execution.stop(lex.getLine());
            break;

    }

    if (current.type == TokenType.OPEN_BRA)
    {
        advance();
        indexExpr = procExpr();
        eat(TokenType.CLOSE_BRA);
        return new AccessExpr(line, baseExpr, indexExpr);

    } else {
        return new AccessExpr(line, baseExpr, null);
    }
}

private FunctionExpr procFunction(Expr sourceExpr) { 

    eat(TokenType.DOT);
    int line = lex.getLine();

    switch (current.type) {

        case LENGTH:
            advance();
            return new FunctionExpr(line, sourceExpr, FunctionOp.LengthOp);
        
        case TO_INT:
            advance();
            return new FunctionExpr(line, sourceExpr, FunctionOp.ToIntOp);

        case TO_STR:
            advance();
            return new FunctionExpr(line, sourceExpr, FunctionOp.ToStringOp);

        default:
            showError();
            break;
    }

    return null;
}

}
