
import jdk.jfr.Description;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static webserver.RequestSteps.*;

public class HttpTaskServerTest extends BasePreferencesServer {

    @Test
    @Description("Проверка наличия задач на сервере и корректности статус-кода")
    public void GETTasksTest() {
        Task task1 = new Task("Http-task1", "Http-description1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2025, Month.MAY, 4, 12, 0, 0));
        Task task2 = new Task("Http-task2", "Http-description1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.now());
        manager.addTask(task1);
        manager.addTask(task2);
        Assert.assertEquals(getAllTasksStep().then().statusCode(200).extract().body().jsonPath().
                getString("[0].description"), manager.getTaskById(1).getDescription());
        Assert.assertEquals(getAllTasksStep().then().statusCode(200).extract().body().jsonPath().
                getString("[1].title"), manager.getTaskById(2).getTitle());
    }

    @Test
    @Description("Проверка наличия эпика на сервере и корректности статус-кода")
    public void GETEpicTest() {
        Epic epic1 = new Epic("Epic1", "Http-EpicDescription1");
        Subtask subtask1 = new Subtask("Subtask1", "SubtaskDescription1", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(30), 1);
        manager.addEpic(epic1);
        manager.addSubTask(subtask1);
        Assert.assertEquals(getAllEpicsStep().then().statusCode(200).extract().body().jsonPath().
                getString("[0].description"), manager.getEpicTaskById(1).getDescription());
    }

    @Test
    @Description("Проверка наличия сабтаска на сервере и корректности статус-кода")
    public void GETSubTasksTest() {
        Epic epic1 = new Epic("Epic1", "Http-EpicDescription1");
        Subtask subtask1 = new Subtask("Subtask1", "SubtaskDescription1", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(30), 1);
        manager.addEpic(epic1);
        manager.addSubTask(subtask1);
        Assert.assertEquals(getAllSubTasksStep().then().statusCode(200).extract().body().jsonPath().
                getString("[0].description"), manager.getSubTaskById(2).getDescription());
    }

    @Test
    @Description("Проверка поиска задачи по ID и корректности статус-кода")
    public void GETTaskByIdTest() {
        Task task1 = new Task("Http-task1", "Http-description1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2025, Month.MAY, 4, 12, 0, 0));
        Task task2 = new Task("Http-task2", "Http-description1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.now());
        manager.addTask(task1);
        manager.addTask(task2);
        Assert.assertEquals(getTaskByIdStep(2).then().statusCode(200).extract().body().jsonPath().
                getString("title"), manager.getTaskById(2).getTitle());
    }

    @Test
    @Description("Проверка поиска эпика по ID и корректности статус-кода")
    public void GETEpicByIdTest() {
        Epic epic1 = new Epic("Http-Epic1", "Http-EpicDescription1");
        Subtask subtask1 = new Subtask("Subtask1", "SubtaskDescription1", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(30), 1);
        manager.addEpic(epic1);
        manager.addSubTask(subtask1);
        Assert.assertEquals(getEpicByIdStep(1).then().statusCode(200).extract().body().jsonPath().
                getString("title"), manager.getEpicTaskById(1).getTitle());
    }

    @Test
    @Description("Проверка поиска эпика по ID и корректности статус-кода")
    public void GETSubtaskByIdTest() {
        Epic epic1 = new Epic("Http-Epic1", "Http-EpicDescription1");
        Subtask subtask1 = new Subtask("Subtask1", "SubtaskDescription1", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(30), 1);
        manager.addEpic(epic1);
        manager.addSubTask(subtask1);
        Assert.assertEquals(getSubTaskByIdStep(2).then().statusCode(200).extract().body().jsonPath().
                getString("description"), manager.getSubTaskById(2).getDescription());
    }

    @Test
    @Description("Проверка корректности создания задачи на сервере и корректности статус-кода")
    public void POSTTaskTest() {
        createTaskStep().then().statusCode(201);
        Assert.assertEquals(getTaskByIdStep(1).then().extract().body().jsonPath().
                getString("description"), manager.getTaskById(1).getDescription());
    }

    @Test
    @Description("Проверка корректности создания эпика на сервере и корректности статус-кода")
    public void POSTEpicTest() {
        createEpicStep().then().statusCode(201);
        createSubTaskStep().then().statusCode(201);
        Assert.assertEquals(getEpicByIdStep(1).then().extract().body().jsonPath().
                getString("description"), manager.getEpicTaskById(1).getDescription());
    }

    @Test
    @Description("Проверка корректности создания подзадачи на сервере и корректности статус-кода")
    public void POSTSubTaskTest() {
        createEpicStep().then().statusCode(201);
        createSubTaskStep().then().statusCode(201);
        Assert.assertEquals(getSubTaskByIdStep(2).then().extract().body().jsonPath().
                getString("description"), manager.getSubTaskById(2).getDescription());
    }

    @Test
    @Description("Проверка удаления задачи на сервере и кода 404 при запросе несуществующего ресурса")
    public void DELETETaskTest() {
        Task task1 = new Task("Http-task1", "Http-description1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2025, Month.MAY, 4, 12, 0, 0));
        Task task2 = new Task("Http-task2", "Http-description1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.now());
        manager.addTask(task1);
        manager.addTask(task2);
        deleteTaskByIdStep(2).then().statusCode(204);
        getTaskByIdStep(2).then().statusCode(404);
    }

    @Test
    @Description("Проверка удаления эпика на сервере, автовайпа его подзадач и кода 404 при запросе несуществующего ресурса")
    public void DELETEEpicTest() {
        Epic epic1 = new Epic("Epic1", "Http-EpicDescription1");
        Subtask subtask1 = new Subtask("Subtask1", "SubtaskDescription1", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(30), 1);
        manager.addEpic(epic1);
        manager.addSubTask(subtask1);
        deleteEpicByIdStep(1).then().statusCode(204);
        getEpicByIdStep(1).then().statusCode(404);
        getSubTaskByIdStep(2).then().statusCode(404);
    }

    @Test
    @Description("Проверка удаления подзадаи на сервере и кода 404 при запросе несуществующего ресурса")
    public void DELETESubTaskTest() {
        Epic epic1 = new Epic("Epic1", "Http-EpicDescription1");
        Subtask subtask1 = new Subtask("Subtask1", "SubtaskDescription1", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(30), 1);
        manager.addEpic(epic1);
        manager.addSubTask(subtask1);
        deleteSubTaskByIdStep(2).then().statusCode(204);
        getEpicByIdStep(1).then().statusCode(200);
        getSubTaskByIdStep(2).then().statusCode(404);
    }

    @Test
    @Description("Проверка наличия задач на сервере и корректности статус-кода")
    public void GETHistoryTest() {
        Task task1 = new Task("Http-task1", "Http-description1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2025, Month.MAY, 4, 12, 0, 0));
        Task task2 = new Task("Http-task2", "Http-description1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.now());
        manager.addTask(task1);
        manager.addTask(task2);
        manager.getTaskById(2);
        Assert.assertEquals(getHistory().then().statusCode(200).extract().body().jsonPath().
                getString("[0].title"), manager.getTaskById(2).getTitle());
    }

    @Test
    @Description("Проверка вызова задачи по приоритету")
    public void GETPrioritizedTest() {
        Task task1 = new Task("task1", "description1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2025, Month.MAY, 4, 12, 0, 0));
        Task task2 = new Task("title2", "description2", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.now());
        Epic epic1 = new Epic("Epic1", "EpicDescription1");
        Subtask subtask1 = new Subtask("Subtask1", "SubtaskDescription1", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(30), 3);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addSubTask(subtask1);
        Assert.assertEquals(getPrioritized().then().statusCode(200).extract().body().jsonPath().
                getString("[1].description"), manager.getTaskById(2).getDescription());
    }
}
