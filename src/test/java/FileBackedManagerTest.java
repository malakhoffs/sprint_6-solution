import jdk.jfr.Description;
import model.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import service.*;

import static org.junit.Assert.assertEquals;

public class FileBackedManagerTest {
    @Before
    @Description("Очистка тестового файла перед тестами")
    public void clearFile() {
        File file = new File("src/resources/task_info.csv");
        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write("");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    @Description("Проверка что файл пустой")
    public void isFileBlankTest() {
        File file = new File("src/resources/task_info.csv");
        assertEquals(file.length(), 0);
    }

    @Test
    @Description("Проверка на записываемость в файл")
    public void isFileWritable() throws IOException {

        String path = new File("src/resources/task_info.csv").getAbsolutePath();
        TaskManager fileBackedManager = Managers.getPath(path);

        Task task1 = new Task("Таск1", "ТаскДескрипшн1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2025, Month.MAY, 4, 12, 0, 0));
        fileBackedManager.addTask(task1);
        Epic epic1 = new Epic("Эпик1", "ЭпикДескрипшн1");
        fileBackedManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Субтаск1", "СубтаскДескрипшн1", Status.NEW,
                Duration.ofMinutes(10),
                LocalDateTime.of(2025, Month.MAY, 5, 12, 0, 0), 2);
        fileBackedManager.addSubTask(subtask1);

        List<String> actual = Files.readAllLines(Paths.get("src/resources/task_info.csv"), Charset.defaultCharset());
        System.out.println(actual);
        List<String> expected = new ArrayList<>();
        expected.add("id,type,title,description,status,duration,startTime,epicId, 1,TASK,Таск1,ТаскДескрипшн1,NEW,PT10M," +
                "2025-05-04T12:00,, 2,EPIC,Эпик1,ЭпикДескрипшн1,NEW,PT10M,2025-05-05T12:00,, 3," +
                "SUBTASK,Субтаск1,СубтаскДескрипшн1,NEW,PT10M,2025-05-05T12:00,2");

        assertEquals(expected.toString(), actual.toString());
    }


    @Test
    @Description("Проверка на читаемость файла")
    public void isFileReadableTest() {
        File file = new File("src/resources/task_file_load_test.csv");
        FileBackedTaskManager parseFromFile = FileBackedTaskManager.loadFromFile(file);

        Task task1 = new Task("Таск1", "ТаскДескрипшн1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2025, Month.MAY, 4, 12, 0, 0));
        task1.setId(1);
        Epic epic1 = new Epic("Эпик1", "ЭпикДескрипшн1");
        epic1.setId(2);
        epic1.setDuration(Duration.ofMinutes(10));
        epic1.setStartTime(LocalDateTime.of(2025, Month.MAY, 4, 12, 0, 0));

        Subtask subtask1 = new Subtask("Субтаск1", "СубтаскДескрипшн1", Status.NEW,
                Duration.ofMinutes(10),
                LocalDateTime.of(2025, Month.MAY, 4, 12, 0, 0), 2);
        subtask1.setId(3);

        List<String> expected = new ArrayList<>();
        expected.add(task1.toString());
        expected.add(epic1.toString());
        expected.add(subtask1.toString());

        assertEquals(expected.get(0), parseFromFile.getTaskById(1).toString());
        assertEquals(expected.get(1), parseFromFile.getEpicTaskById(2).toString());
        assertEquals(expected.get(2), parseFromFile.getSubTaskById(3).toString());
    }

    @Test
    @Description("Проверка на добавление Subtask в Epic просле загрузки из файла")
    public void isSubtaskAddedToEpic() {
        File file = new File("src/resources/task_file_load_test.csv");
        FileBackedTaskManager parseFromFile = FileBackedTaskManager.loadFromFile(file);


        Epic epic1 = new Epic("Эпик1", "ЭпикДескрипшн1");
        epic1.setId(2);
        Subtask subtask1 = new Subtask("Субтаск1", "СубтаскДескрипшн1", Status.NEW,
                Duration.ofMinutes(10),
                LocalDateTime.of(2025, Month.MAY, 4, 12, 0, 0), 2);
        subtask1.setId(3);

        List<Subtask> expected = new ArrayList<>();
        expected.add(subtask1);

        List<Subtask> actual = parseFromFile.getEpicTaskById(2).getSubtasks();
        assertEquals(expected, actual);
    }
}