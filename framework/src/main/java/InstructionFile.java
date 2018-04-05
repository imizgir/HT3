
import java.io.*;
import java.util.ArrayList;

public class InstructionFile {

    // хранятся строки из входного файла
    private ArrayList<String> instructions = new ArrayList<String>();

    public ArrayList<String> getInstructions() {
        return instructions;
    }

    // читаем файл
    public void read(String filePath) {

        String str;

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(filePath)));
            // проходим по файлу и добавляем строки в лист instructions
            while ((str = reader.readLine()) != null) {
                instructions.add(str);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден " + filePath);
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }
}
