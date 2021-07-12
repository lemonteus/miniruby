package syntatic;

import java.util.List;
import java.util.ArrayList;

import lexical.Lexeme;
import lexical.LexicalAnalysis;
import lexical.TokenType;
import interpreter.command.*;
import interpreter.expr.*;
import interpreter.value.*;


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

    Command auxCommand = null;

    switch (current.type)
    {
        case IF:
            procIf();
            break;
        
        case UNLESS:
            procUnless();
            break;
        
        case WHILE:
            procWhile();
            break;
        
        case UNTIL:
            procUntil();
            break;
        
        case FOR:
            procFor();
            break;

        case PUTS:
        case PRINT:
            Command c = (Command) procOutput();
            return c;
        
        case ID:
            procAssign();
            break;

        default:
            showError();
    }

    return auxCommand;
}

private void procIf() { 

    eat(TokenType.IF);
    procBoolExpr();

    if(current.type == TokenType.THEN)
        eat(TokenType.THEN);

    procCode();

    if (current.type == TokenType.ELSIF)
    {
        eat(TokenType.ELSIF);
        procBoolExpr();

        if(current.type == TokenType.THEN)
            eat(TokenType.THEN);

        procCode();
    }

    if (current.type == TokenType.ELSE)
    {   advance();
        procCode();
    }

    eat(TokenType.END);

 }

private void procUnless() { 

    eat(TokenType.UNLESS);

    procBoolExpr();

    if (current.type == TokenType.THEN)
        eat(TokenType.THEN);

    procCode();

    if (current.type == TokenType.ELSE)
    {
        eat(TokenType.ELSE);
        procCode();
    }

    eat(TokenType.END);
 }

private void procWhile() { 

    eat(TokenType.WHILE);
    int line = lex.getLine();
    
    eat(TokenType.DO);    
    eat(TokenType.END);

}

//private UntilCommand procUntil() {

private void procUntil() { 

    eat(TokenType.UNTIL);
    procBoolExpr();
    eat(TokenType.DO);
    //BoolExpr cond = procBoolExpr();
    procCode();
    //Command cmds = procCode();
    eat(TokenType.END);

    //return new UntilCommand(line, cond, cmds);
}

private void procFor() { 

    eat(TokenType.FOR);
    
    eat(TokenType.ID);

    eat(TokenType.IN);
    
    procExpr();

    if (current.type == TokenType.DO)
        eat(TokenType.DO);
    
    procCode();

    eat(TokenType.END);
 }

private OutputCommand procOutput() { 

    OutputOp auxOp = null;
    int line;
    OutputCommand auxCommand = null;

    if (current.type == TokenType.PUTS)
    {
        advance();
        auxOp = OutputOp.PutsOp;
        line = lex.getLine();


    } else if (current.type == TokenType.PRINT)
    {
        line = lex.getLine();
        auxOp = OutputOp.PrintOp;
        advance();

    } else {

        line = lex.getLine();
        showError();
    }
    
    if (current.type == TokenType.IF || current.type == TokenType.UNLESS)
    {   
        procPost();
        return new OutputCommand(line, auxOp);
    }
    else if (current.type != TokenType.SEMI_COLON)
    {
        Expr auxExpr = procExpr();
       
        if (current.type == TokenType.IF || current.type == TokenType.UNLESS)
        {   
            procPost();
            return new OutputCommand(line, auxOp, auxExpr);
        }
    }

    eat(TokenType.SEMI_COLON);

    return auxCommand;

}

private void procAssign() { 

    procAccess();

    int accessCount = 0;

    while (current.type == TokenType.COMMA)
    {
        eat(TokenType.COMMA);
        procAccess();
        accessCount++;
    }

    eat(TokenType.ASSIGN);

    procExpr();

    while (accessCount != 0)
    {   
        if (current.type == TokenType.COMMA)
        {
            eat(TokenType.COMMA);
            procExpr();
            accessCount--;

        } else {
            showError();
        }
    }

    if (current.type == TokenType.IF ||
        current.type == TokenType.UNLESS)
    {
        procPost();
    }

    eat(TokenType.SEMI_COLON);

 }

private void procPost() { 

    if (current.type == TokenType.IF)
    {
        eat(TokenType.IF);
        procBoolExpr();
    } else if (current.type == TokenType.UNLESS)
    {
        eat(TokenType.UNLESS);
        procBoolExpr();
    } else {
        showError();
    }
}

private void procBoolExpr() { 

    if (current.type == TokenType.NOT)
    {
        eat(TokenType.NOT);
    }

    procCmpExpr();

    if (current.type == TokenType.AND)
    {
        eat(TokenType.AND);
        procBoolExpr();

    } else if (current.type == TokenType.OR)
    {
        eat(TokenType.OR);
        procBoolExpr();
    }



 }

private void procCmpExpr() { 

    procExpr();

    switch (current.type) {
        
        case EQUALS:
            eat(TokenType.EQUALS);
            break;

        case NOT_EQUALS:
            eat(TokenType.NOT_EQUALS);
            break;
            
        case LESS_THAN:
            eat(TokenType.LESS_THAN);
            break;
        
        case GREATER_THAN:
            eat(TokenType.GREATER_THAN);
            break;
        
        case GREATER_EQ:
            eat(TokenType.GREATER_EQ);
            break;

        case LESS_EQ:
            eat(TokenType.LESS_EQ);
            break;
        
        case CONTAINS:
            eat(TokenType.CONTAINS);
            break;
    }

    procExpr();
}

private Expr procExpr() { 

    //TODO: alterar para BinaryExpr
    Expr left = procArith();

    if (current.type == TokenType.RANGE_WITH || 
        current.type == TokenType.RANGE_WITHOUT)
    {
        if (current.type == TokenType.RANGE_WITH)
        {
            eat(TokenType.RANGE_WITH); //TODO: ao implementar logica nao sera mais necessario implementar o eat
        }

        if (current.type == TokenType.RANGE_WITHOUT)
        {
            eat(TokenType.RANGE_WITHOUT); //TODO: ao implementar logica nao sera mais necessario implementar o eat
        }

        Expr right = procArith();
    }

    //TODO: resolver isso, só ta retornando o termo esquerdo
    return left;

 }

    private Expr procArith() { 

        //TODO: alterar para BinaryExpr
        Expr expr = procTerm();
        
        while (current.type == TokenType.ADD || current.type == TokenType.SUB)
        {
            advance();
            Expr expr2 = procTerm();
        }

        //TODO: resolver isso, só ta retornando o termo esquerdo
        return expr;

    }

private Expr procTerm() { 

    Expr expr = procPower();

    while (current.type == TokenType.MUL || current.type == TokenType.DIV || current.type == TokenType.MOD) 
    {
        advance();
        Expr expr2 = procPower();
    }

    //TODO: resolver isso, só ta retornando o termo esquerdo
    return expr;
}

private Expr procPower() { 

    Expr expr = procFactor();

    while (current.type == TokenType.EXP)
    {
        advance();
        Expr expr2 = procFactor();
    }
    
    //TODO: resolver isso, só ta retornando o termo esquerdo
    return expr;
}

private Expr procFactor() { 

    boolean neg = false;
    Expr expr = null;

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
            //TODO: implement factor
            return expr;
        
        case GETS:
        case RAND:
            procInput();
            break;

        case ID:
        case OPEN_PAR:
            procAccess();
            break;
    }

    //must verify if next token isn't interval operand
    if (current.type == TokenType.RANGE_WITH || current.type == TokenType.RANGE_WITHOUT)
    {
        
    }
    else if (current.type == TokenType.DOT)
    {
        procFunction();
    }

    return (Expr) expr;

}

private ConstExpr procConst() { 

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
            eat(TokenType.STRING);
            break;

        case OPEN_BRA:
            procArray();
            break;
        
        default:
            showError();
            break;
    }

    return cExpr;
}

private void procInput() { 

    switch (current.type) {

        case GETS:
            eat(TokenType.GETS);
            break;

        case RAND:
            eat(TokenType.RAND);
            break;
    }
}  

private void procArray() { 
    
    eat(TokenType.OPEN_BRA);
    
    if(current.type != TokenType.CLOSE_BRA) 
    {
        procExpr();

        while (current.type == TokenType.COMMA)
        {
            advance();
            procExpr();
        }
    }
    eat(TokenType.CLOSE_BRA);
}

private void procAccess() { 

    switch (current.type) {

        case ID:
            eat(TokenType.ID);
            break;

        case OPEN_PAR:
            advance();
            procExpr();
            eat(TokenType.CLOSE_PAR);
            break;
    }

    if (current.type == TokenType.OPEN_BRA)
    {
        advance();
        procExpr();
        eat(TokenType.CLOSE_BRA);
    }
}

private void procFunction() { 

    eat(TokenType.DOT);

    switch (current.type) {

        case LENGTH:
            advance();
            break;
        
        case TO_INT:
            advance();
            break;

        case TO_STR:
            advance();
            break;

        default:
            showError();
            break;
    }
}

}
