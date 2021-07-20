package interpreter.util;

public class Execution {

    public static void stop (int line)
    {
        System.out.printf("%02d: Operacao invalida\n", line);
        System.exit(1);
    }
    
}
