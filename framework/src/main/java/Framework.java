
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class Framework {

    private static final Logger log = LogManager.getLogger(Framework.class.getName());

    // хранятся строки (инструкции) из входного файла
    private ArrayList<String> instructions = new ArrayList<>();

    public Framework(ArrayList<String> instructions) {
        this.instructions = instructions;
    }

    public void setInstructions(ArrayList<String> instructions) {
        this.instructions = instructions;
    }

    // получаем инструкцию, выполняем, записываем в log-файл
    public void run() {
        Instruction instruction = new Instruction();
        for (String s : instructions) {
            // отсекаем команду от параметров
            String[] words = s.split(" ");
            String command = words[0];
            instruction.setInstruction(s);

            // определяем комманду и обращаемся к соответствующему методу
            switch (command) {
                case "open":
                    instruction.open();
                    break;
                case "checkLinkPresentByHref":
                    instruction.checkLinkPresentByHref();
                    break;
                case "checkLinkPresentByName":
                    instruction.checkLinkPresentByName();
                    break;
                case "checkPageTitle":
                    instruction.checkPageTitle();
                    break;
                case "checkPageContains":
                    instruction.checkPageContains();
                    break;
                default:
                    instruction.invalidCommand();
            }
            // записываем лог работы
            log.info(instruction.getTestResult() + " [" + instruction.getInstruction() + "] " + Instruction.testTime);
        }

        // выводим итог (статистику) тестов
        log.info("Total tests: " + (Instruction.failedTestsCounter + Instruction.passedTestsCounter));
        log.info("Passed/failed: " + Instruction.passedTestsCounter + "/" + Instruction.failedTestsCounter);
        log.info("Total time: " + String.format("%.3f", Instruction.totalTime));
        log.info("Average time: " + String.format("%.3f", Instruction.totalTime / (Instruction.failedTestsCounter + Instruction.passedTestsCounter)));
    }


}
