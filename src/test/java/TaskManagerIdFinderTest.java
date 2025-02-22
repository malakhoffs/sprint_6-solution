import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.Test;
import service.InMemoryTaskManager;
import service.TaskManager;

import static org.junit.Assert.assertEquals;

public class TaskManagerIdFinderTest extends BasePreferences {

    @Test
    public void addAndGetTasksTest() {
        Task task = new Task("title", "description", Status.NEW);
        manager.addTask(task);
        assertEquals(task, manager.getTaskById(1));
    }

    @Test
    public void addAndGetSubTasksTest() {
        Subtask subtask = new Subtask("title", "description", Status.NEW, 1);
        manager.addSubTask(subtask);
        assertEquals(subtask, manager.getSubTaskById(1));
    }

    @Test
    public void addAndGetEpicsTest() {
        Epic epic = new Epic("title", "description");
        manager.addEpic(epic);
        assertEquals(epic, manager.getEpicTaskById(1));
    }

    @Test
    public void inMemoryTaskManagerTest() {
        TaskManager manager = InMemoryTaskManager.getInstance();
        Task task = new Task("Тайтл", "Описание", Status.NEW);
        Epic epic = new Epic("Тайтл", "Описание");
        manager.addTask(task);
        manager.addEpic(epic);
        Subtask subtask = new Subtask("Тайтл", "Описание", Status.NEW, 2);
        manager.addSubTask(subtask);
        assertEquals(task, manager.getTaskById(task.getId()));
        assertEquals(epic, manager.getEpicTaskById(epic.getId()));
        assertEquals(subtask, manager.getSubTaskById(subtask.getId()));
    }

}
