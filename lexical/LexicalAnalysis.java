package miniruby.lexical;

import java.io.FileInputStream;
import java.io.PushbackInputStream;

public class LexicalAnalysis {

    private int line;
    private SymbolTable st;
    private PushbackInputStream input;

    public LexicalAnalysis (String filename)
    {
        try {

			input = new PushbackInputStream(new FileInputStream(filename));

		} catch (Exception e) {

			throw new LexicalException("Unable to open file: " + filename);

		}
    }

    private int getc() {
        try {
            return input.read();
        } catch (Exception e) {
            throw new LexicalException("Unable to read file");
        }
    }

    private void ungetc(int c) {
        if (c != -1) {
            try {
                input.unread(c);
            } catch (Exception e) {
                throw new LexicalException("Unable to ungetc");
            }
        }
    }

    public void close() {
        try {
            input.close();
        } catch (Exception e) {
            throw new LexicalException("Unable to close file");
        }
    }

    public int getLine() {
        return this.line;
    }

    public Lexeme nextToken()
    {
        Lexeme lex = new Lexeme();

        int state = 1;

        while (state != 12 && state != 13) {

            int c = getc();
            
            switch (state) {
                case 1:
                    if ( c == '\t' || c == '\r' || c == '\s') {
                        state = 1;
                    }
                    else if (c == '\n') {
                        line++;
                        state = 1;
                    }
                    else if (c == '#') {
                        state = 2;
                    }
                    else if (c == '.') {
                        state = 3;
                    }
                    else if (c == '=') {
                        state = 5;
                    }                        
                    else if (c == '<' || c == '>') {
                        state = 6;
                    }
                    break;
                case 2:
                    // TODO: Implement me!
                    break;
                case 3:
                    // TODO: Implement me!
                    break;
                case 4:
                    // TODO: Implement me!
                    break;
                case 5:
                    // TODO: Implement me!
                    break;
                case 6:
                    // TODO: Implement me!
                    break;
                case 7:
                    // TODO: Implement me!
                    break;
                case 8:
                    // TODO: Implement me!
                    break;
                case 9:
                    // TODO: Implement me!
                    break;
                case 10:
                    // TODO: Implement me!
                    break;
                case 11:
                    // TODO: Implement me!
                    break;
                default:
                    throw new LexicalException("Unreachable");
            }
        }

        if (state == 12)
            lex.type = st.find(lex.token);

        return lex;

    }

}