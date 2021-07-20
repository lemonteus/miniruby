import lexical.LexicalAnalysis;
import syntatic.SyntaticAnalysis;
import interpreter.command.*;
import interpreter.util.*;

//Interpretador miniruby - Feito em Java
//Aluno: Mateus Lemos de Freitas Barbosa
//Disciplina de Linguagens de Programação, ministrada pelo professor Andrei,
//no semestre 2021.1 do curso de Engenharia de Computação.

public class mrbi {

    /*private static boolean checkType(TokenType type) {
        return !(type == TokenType.END_OF_FILE ||
                 type == TokenType.INVALID_TOKEN ||
                 type == TokenType.UNEXPECTED_EOF);
    }*/

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java mrbi [miniRuby file]");
            return;
        }

        try (LexicalAnalysis l = new LexicalAnalysis(args[0])) {
            
            // O código a seguir é dado para testar o interpretador.
            SyntaticAnalysis s = new SyntaticAnalysis(l);
            Command c = s.start();
            c.execute();
            
            // O código a seguir é usado apenas para testar o analisador léxico.
            /*Lexeme lex = l.nextToken();
            while (checkType(lex.type)) {
                System.out.printf("(\"%s\", %s)\n", lex.token, lex.type);
                lex = l.nextToken();
            }

            switch (lex.type) {
                case INVALID_TOKEN:
                    System.out.printf("%02d: Lexema inválido [%s]\n", l.getLine(), lex.token);
                    break;
                case UNEXPECTED_EOF:
                    System.out.printf("%02d: Fim de arquivo inesperado\n", l.getLine());
                    break;
                default:
                    System.out.printf("(\"%s\", %s)\n", lex.token, lex.type);
                    break;
            }*/
            
        } catch (Exception e) {
            Execution.stop(0);
        }
    }
}

