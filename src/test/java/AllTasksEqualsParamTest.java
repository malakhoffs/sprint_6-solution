import static org.junit.Assert.assertEquals;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class AllTasksEqualsParamTest extends BasePreferences {

    private final String tittle;
    private final String description;
    private final Status status;

    @Parameterized.Parameters(name = "{index} Value = {0}")
    public static Object[][] getData() {
        return new Object[][]{{"Тайтл", "Описание", Status.NEW},};
    }

    public AllTasksEqualsParamTest(String tittle, String price, Status status) {
        this.tittle = tittle;
        this.description = price;
        this.status = status;
    }

    @Test
    public void tasksEqualsTest() {
        Task task = new Task("Тайтл", "Описание", Status.NEW);
        assertEquals(0, task.getId());
        assertEquals(tittle, task.getTitle());
        assertEquals(description, task.getDescription());
        assertEquals(status, task.getStatus());
    }

    @Test
    public void subTasksEqualsTest() {
        Subtask subtask = new Subtask("Тайтл", "Описание", Status.NEW, 1);
        assertEquals(0, subtask.getId());
        assertEquals(tittle, subtask.getTitle());
        assertEquals(description, subtask.getDescription());
        assertEquals(status, subtask.getStatus());
    }

    @Test
    public void epicTasksEqualsTest() {
        Epic epic = new Epic("Тайтл", "Описание");
        assertEquals(0, epic.getId());
        assertEquals(tittle, epic.getTitle());
        assertEquals(description, epic.getDescription());
        assertEquals(status, epic.getStatus());
    }

    @Test
    public void managerTasksEqualsTest() {
        Task task = new Task("Тайтл", "Описание", Status.NEW);
        manager.addTask(task);
        assertEquals(1, manager.getTaskById(1).getId());
        assertEquals(tittle, manager.getTaskById(1).getTitle());
        assertEquals(description, manager.getTaskById(1).getDescription());
        assertEquals(status, manager.getTaskById(1).getStatus());
    }

    @Test
    public void managerSubTasksEqualsTest() {
        Epic epic = new Epic("Тайтл", "Описание");
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Тайтл", "Описание", Status.NEW, 1);
        manager.addSubTask(subtask);
        assertEquals(2, manager.getSubTaskById(2).getId());
        assertEquals(tittle, manager.getSubTaskById(2).getTitle());
        assertEquals(description, manager.getSubTaskById(2).getDescription());
        assertEquals(status, manager.getSubTaskById(2).getStatus());
    }

    @Test
    public void managerEpicTasksEqualsTest() {
        Epic epic = new Epic("Тайтл", "Описание");
        manager.addEpic(epic);
        assertEquals(1, manager.getEpicTaskById(1).getId());
        assertEquals(tittle, manager.getEpicTaskById(1).getTitle());
        assertEquals(description, manager.getEpicTaskById(1).getDescription());
        assertEquals(status, manager.getEpicTaskById(1).getStatus());
    }
}
