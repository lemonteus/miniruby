package syntatic;

import interpreter.command.Command;
import lexical.Lexeme;
import lexical.LexicalAnalysis;
import lexical.TokenType;

public class SyntaticAnalysis {

    private LexicalAnalysis lex;
    private Lexeme current;

    public SyntaticAnalysis(LexicalAnalysis lex) {
        this.lex = lex;
        this.current = lex.nextToken();
    }

    public Command start() {
        Command cmd = procCode();
        eat(TokenType.END_OF_FILE);

        return cmd;
    }

    private void advance() {
        System.out.println("Advanced (\"" + current.token + "\", " +
            current.type + ")");
        current = lex.nextToken();
    }

    private void eat(TokenType type) {
        System.out.println("Expected (..., " + type + "), found (\"" + 
             current.token + "\", " + current.type + ")");
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

    private void procCode() { 

        

    }

private void procCmd() { 

    

}

private void procIf() {  }

private void procUnless() {  }

private void procWhile() { 

    eat(TokenType.WHILE);
    procBoolExpr();
    eat(TokenType.DO);
    procCode();
    eat(TokenType.END);

}

private void procUntil() { 

    eat(TokenType.UNTIL);
    procBoolExpr();
    eat(TokenType.DO);
    procCode();
    eat(TokenType.END);
}

private void procFor() {  }

private void procOutput() {  }

private void procAssign() {  }

private void procPost() { 


}

private void procBoolExpr() {  }

private void procCmpExpr() {  }

private void procExpr() {  }

private void procArith() {  }

private void procTerm() {  }

private void procPower() {  }

private void procFactor() { 

    procConst();

}

private void procConst() { 



}

private void procInput() {  }

private void procArray() {  }

private void procAccess() {  }

private void procFunction() {  }







}
