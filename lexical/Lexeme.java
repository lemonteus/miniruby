package miniruby.lexical;

public class Lexeme {

    TokenType type;
    String token;

    public Lexeme (String token, TokenType type)
    {
        this.type = type;
        this.token = token;
    }

    public Lexeme ()
    {
        this.token = "";
        this.type = TokenType.END_OF_FILE;
    }

    public String str() 
    {
        return ("(\"" + token + "\", " + type + ")");
    }
    
}
