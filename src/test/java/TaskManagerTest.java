import jdk.jfr.Description;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.Test;
import service.InMemoryTaskManager;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.assertEquals;

public class TaskManagerTest extends BasePreferences {

    @Test
    public void addAndGetTasksTest() {
        Task task = new Task("title", "description", Status.NEW, Duration.ofMinutes(10), LocalDateTime.now());
        manager.addTask(task);
        assertEquals(task, manager.getTaskById(1));
    }

    @Test
    public void addAndGetSubTasksTest() {
        Epic epic = new Epic("title", "description");
        Subtask subtask = new Subtask("title", "description", Status.NEW, Duration.ofMinutes(10), LocalDateTime.now(), 1);
        manager.addEpic(epic);
        manager.addSubTask(subtask);
        assertEquals(subtask, manager.getSubTaskById(2));
    }

    @Test
    public void addAndGetEpicsTest() {
        Epic epic = new Epic("title", "description");
        manager.addEpic(epic);
        Subtask subtask = new Subtask("title", "description", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2025, Month.MAY, 15, 12, 15, 0), 1);
        manager.addSubTask(subtask);
        assertEquals(epic, manager.getEpicTaskById(1));
    }

    @Test
    @Description("Проверка перехода эпика в статус IN_PROGRESS, при переходе его подзадачи в аналогичный статус")
    public void EpicChangeStatusToInProgress() {
        Epic epic1 = new Epic("title", "description");
        manager.addEpic(epic1);
        Subtask subtask = new Subtask("title", "description", Status.IN_PROGRESS, Duration.ofMinutes(10),
                LocalDateTime.of(2025, Month.MAY, 15, 12, 15, 0), 1);
        manager.addSubTask(subtask);
        assertEquals(Status.IN_PROGRESS, manager.getEpicTaskById(1).getStatus());
    }

    @Test
    @Description("Проверка перехода эпика в статус DONE, при переходе его подзадачи в аналогичный статус")
    public void EpicChangeStatusToDone() {
        Epic epic1 = new Epic("title", "description");
        manager.addEpic(epic1);
        Subtask subtask = new Subtask("title", "description", Status.DONE, Duration.ofMinutes(10),
                LocalDateTime.of(2025, Month.MAY, 15, 12, 15, 0), 1);
        manager.addSubTask(subtask);
        assertEquals(Status.DONE, manager.getEpicTaskById(1).getStatus());
    }

    @Test
    public void inMemoryTaskManagerTest() {
        TaskManager manager = InMemoryTaskManager.getInstance();
        Task task = new Task("Тайтл", "Описание", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        Epic epic = new Epic("Тайтл", "Описание");
        manager.addTask(task);
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Тайтл", "Описание", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.now().plusMinutes(10), 2);
        manager.addSubTask(subtask);
        assertEquals(task, manager.getTaskById(task.getId()));
        assertEquals(epic, manager.getEpicTaskById(epic.getId()));
        assertEquals(subtask, manager.getSubTaskById(subtask.getId()));
    }
}
