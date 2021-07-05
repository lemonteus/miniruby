package miniruby;

import miniruby.lexical.LexicalAnalysis;

public class mrbi {
    public static void main(String args[]) {
        if (args.length != 1)
        {
            System.out.println("Uso: java mrbi [arquivo fonte]");
            return;
        }

        LexicalAnalysis l;

        try {
           l = new LexicalAnalysis (args[0]);
            System.out.println(l.nextToken().str());
        }
        catch (Exception e)
        {
            System.err.println("Internal error: " + e.getMessage());
        }

    }
}