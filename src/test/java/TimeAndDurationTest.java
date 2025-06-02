import jdk.jfr.Description;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TimeAndDurationTest extends BasePreferences {


    @Test
    @Description("Проверка перекрываемости задач")
    public void isAddedTaskOverlap() {
        LocalDateTime startTime = LocalDateTime.now();
        Task task1 = new Task("Таск1", "Описание1", Status.NEW, Duration.ofMinutes(15), startTime);
        Task task2 = new Task("Таск2", "Описание2", Status.NEW, Duration.ofMinutes(100), startTime.minusMinutes(90));
        Task task3 = new Task("Таск3", "Описание3", Status.NEW, Duration.ofMinutes(100), startTime.minusMinutes(200));
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        assertEquals(manager.getPrioritizedTasks().size(), 2);
    }

    @Test
    @Description("Проверка правильности очереди в приоритетности задач")
    public void isPrioritizedTasksInCorrectQueue() {
        LocalDateTime startTime = LocalDateTime.now();
        Task task1 = new Task("Таск1", "Описание1", Status.NEW, Duration.ofMinutes(10), startTime);
        Task task2 = new Task("Таск2", "Описание2", Status.NEW, Duration.ofMinutes(10), startTime.minusMinutes(20));
        Task task3 = new Task("Таск3", "Описание3", Status.NEW, Duration.ofMinutes(10), startTime.minusMinutes(40));
        Task task4 = new Task("Таск4", "Описание4", Status.NEW, Duration.ofMinutes(10), startTime.minusMinutes(60));
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.addTask(task4);
        List<Task> queue = new ArrayList<>(List.of(task4, task3, task2, task1));
        assertEquals(manager.getPrioritizedTasks(), queue);
    }

    @Test
    @Description("Проверка равенства продолжительности эпика сумме продолжительностей его подзадач")
    public void isEpicDurationEqualsItsSubtask() {
        LocalDateTime startTime = LocalDateTime.now();
        Epic epic1 = new Epic("Эпик1", "Описание1");
        Subtask subtask1 = new Subtask("Тайтл2", "Описание2", Status.NEW, Duration.ofMinutes(10), startTime, 1);
        Subtask subtask2 = new Subtask("Тайтл2", "Описание2", Status.NEW, Duration.ofMinutes(10), startTime.plusMinutes(20), 1);
        manager.addEpic(epic1);
        manager.addSubTask(subtask1);
        manager.addSubTask(subtask2);
        System.out.println(manager.getEpicTaskById(1));
        assertEquals(Duration.ofMinutes(20), manager.getEpicTaskById(1).getDuration());
    }

    @Test
    @Description("Проверка равенства окончания времени эпика окончанию времени самой продолжительной подзадачи")
    public void isEpicEndTimeEqualsItsSubtask() {
        LocalDateTime startTime = LocalDateTime.now();
        Epic epic1 = new Epic("Эпик1", "Описание1");
        Subtask subtask1 = new Subtask("Тайтл2", "Описание2", Status.NEW, Duration.ofMinutes(10), startTime.plusMinutes(20), 1);
        Subtask subtask2 = new Subtask("Тайтл2", "Описание2", Status.NEW, Duration.ofMinutes(10), startTime.plusMinutes(80), 1);
        manager.addEpic(epic1);
        manager.addSubTask(subtask1);
        manager.addSubTask(subtask2);
        System.out.println(manager.getEpicTaskById(1).getEndTime());
        assertEquals(startTime.plusMinutes(90), manager.getEpicTaskById(1).getEndTime());
    }
}
