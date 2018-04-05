import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        String inputFile = null;
        String logFile = ".\\src\\main\\resources\\log.txt";

        // params
        // inputFile = "D:\\Education\\Java\\framework2\\src\\main\\resources\\input.txt";
        // args = new String[]{inputFile, logFile};

        if (args.length == 2) {
            inputFile = args[0];
            logFile = args[1];
        } else if (args.length == 1) {
            inputFile = args[0];
        } else {
            System.out.println("Ошибка");
        }

        // Устанавливается путь к лог-файлу
        System.setProperty("logFileName", logFile);

        // читаем входной файл с "инструкциями"
        InstructionFile instructionFile = new InstructionFile();
        instructionFile.read(inputFile);
        ArrayList<String> instructions = instructionFile.getInstructions();

        Framework framework = new Framework(instructions);
        framework.run();
    }
}
