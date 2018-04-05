
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Instruction {

    private static Document page = null; // принимает содержимое веб-ресурса
    private static long startTime = 0;          // время начала теста
    static double testTime = 0;                 // время, затраченное на тест
    static double totalTime = 0;                // время, затраченное на все тесты
    static int passedTestsCounter = 0;          // счетчик успешных тестов
    static int failedTestsCounter = 0;          // счетчик заваленных тестов
    private String instruction;
    private String testResult;

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    // открываем url с заданным таймаутом
    public void open() {
        startTimer();
        String url;
        double timeout;

        // получаем url и таймаут
        if (getCommandParams(instruction) != null) {
            url = getCommandParams(instruction)[0];
            timeout = Double.parseDouble(getCommandParams(instruction)[1]) * 1000;
        } else {
            failTest();
            return;
        }

        // соединяемся с веб-ресурсом
        try {
            Connection connection = Jsoup.connect(url);
            Document doc = connection.get();
            page = doc;
        } catch (IOException e) {
            failTest();
            return;
        }
        stopTimer();

        // если время теста не привысило таймаут -- тест пройден
        if (testTime > timeout) {
            failedTestsCounter++;
            setTestResult("!");
        } else {
            passedTestsCounter++;
            setTestResult("+");
        }
    }


    // проверяем значение ссылки в теге href
    public void checkLinkPresentByHref() {
        startTimer();
        String param;

        // получаем параметр команды
        if (getCommandParams(instruction) != null) {
            param = getCommandParams(instruction)[0];
        } else {
            failTest();
            return;
        }

        //ищем a[href]
        if (page != null) {
            Elements links = page.select("a[href]");
            // сравниваем href с параметром команды
            for (Element link : links) {
                String href = link.attr("href");
                // если совпадает -- тест пройден
                if (href.equals(param)) {
                    passTest();
                    return;
                }
            }
        }
        failTest();
    }

    // проверяем имя ссылки
    public void checkLinkPresentByName() {
        startTimer();
        String param;

        // получаем параметр команды
        if (getCommandParams(instruction) != null) {
            param = getCommandParams(instruction)[0];
        } else {
            failTest();
            return;
        }

        //ищем a[href]
        if (page != null) {
            Elements links = page.select("a[href]");
            for (Element link : links) {
                String linkText = link.text();
                if (linkText.equals(param)) {
                    passTest();
                    return;
                }
            }
        }
        failTest();
    }

    // проверяем название страницы
    public void checkPageTitle() {
        startTimer();
        String param;

        // получаем параметр команды
        if (getCommandParams(instruction) != null) {
            param = getCommandParams(instruction)[0];
        } else {
            failTest();
            return;
        }

        if (page != null) {
            String title = page.title();
            if (title.equals(param)) {
                passTest();
                return;
            }
        }
        failTest();
    }

    // проверяем содержится ли указанный текст на странице
    public void checkPageContains() {
        startTimer();
        String param;

        // получаем параметр команды
        if (getCommandParams(instruction) != null) {
            param = getCommandParams(instruction)[0];
        } else {
            failTest();
            return;
        }

        if (page != null) {
            String phrase = param;
            if (page.toString().contains(phrase)) {
                passTest();
                return;
            }
        }
        failTest();
    }

    // неверная инструкция
    public void invalidCommand() {
        startTimer();
        setTestResult("Неверная иснтрукция. Тест проигнорирован");
        stopTimer();
    }

    // получаем параметры команды
    private String[] getCommandParams(String instruction) {
        String[] params = null;
        // отделяем команду от параметров
        String[] commands = instruction.split(" ", 2);

        // команда open имеет два параметра
        if (commands[0].equals("open")) {
            if (commands[1].matches("\".+\" \"\\d+\\.?\\d*\"")) {
                params = new String[2];
                // разделяем параметры и убираем кавычки
                String[] str = commands[1].split(" ");
                params[0] = str[0].substring(1, str[0].length() - 1);
                params[1] = str[1].substring(1, str[1].length() - 1);
            }
        } else {
            // команды с одним парметром
            if (commands[1].matches("\".+\"")) {
                params = new String[1];
                // убираем кавчки
                params[0] = commands[1].substring(1, commands[1].length() - 1);
            }
        }
        return params;
    }

    private static void startTimer() {
        startTime = System.currentTimeMillis();
    }

    private static void stopTimer() {
        long endTime = System.currentTimeMillis() - startTime;
        testTime = ((double) endTime) / 1000;
        totalTime = totalTime + testTime;
    }

    private void failTest() {
        stopTimer();
        failedTestsCounter++;
        setTestResult("!");
    }

    private void passTest() {
        stopTimer();
        passedTestsCounter++;
        setTestResult("+");
    }
}
