import jdk.jfr.Description;
import model.Status;
import model.Task;
import org.junit.Test;
import service.HistoryManager;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;
import service.Managers;

import java.util.List;

import static org.junit.Assert.*;

public class HistoryManagerTest extends BasePreferences {
    @Test
    public void nodeNotNullTest() {
        HistoryManager historyManager = Managers.getHistoryManager();
        assertNotNull(historyManager);
    }

    @Test
    public void managersClassTest() {
        HistoryManager historyManager = Managers.getHistoryManager();
        assertEquals(manager.getClass(), InMemoryTaskManager.class);
        assertEquals(historyManager.getClass(), InMemoryHistoryManager.class);
        Task task = new Task("Тайтл", "Описание", Status.NEW);
        manager.addTask(task);
        manager.getTaskById(task.getId());
        assertFalse(manager.getHistory().isEmpty());
    }

    @Test
    public void historyManagerAddingNotNullTest() {
        Task task = new Task("Тайтл", "Описание", Status.NEW);
        Task task2 = new Task("Тайтл", "Описание", Status.NEW);
        Managers.historyManager.add(task);
        Managers.historyManager.add(task2);

        List<Task> history = Managers.historyManager.getHistory();
        System.out.println(Managers.historyManager.getHistory());
        assertNotNull(history);
    }

    @Test
    @Description("Тест на проверку соответствия таска по индексу")
    public void historyManagerIndexTest() {
        Task task = new Task("Тайтл1", "desc", Status.NEW);
        Task task2 = new Task("Тайтл2", "desc", Status.NEW);
        Task task3 = new Task("Тайтл3", "desc", Status.NEW);
        Task task4 = new Task("Тайтл4", "desc", Status.NEW);
        manager.addTask(task);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.addTask(task4);

        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getTaskById(3);
        manager.getTaskById(4);

        List<Task> history = Managers.historyManager.getHistory();
        assertEquals(task3, history.get(3));
    }
}
