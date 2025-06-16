package webserver;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static webserver.PathConstants.*;

public class RequestSteps {

    private static final String HOST = "http://localhost:8080";

    static String taskJsonExample = "{\"id\":1,\"title\":\"title99\",\"description\":\"TaskDescription99\"," +
            "\"status\":\"NEW\",\"duration\":10,\"startTime\":\"2000-04-15T10:45:20\"}";
    static String EpicJsonExample = "{\"subtasks\":[],\"endTime\":\"null\",\"id\":1,\"title\":\"Epic1\"," +
            "\"description\":\"EpicDescription1\",\"status\":\"NEW\",\"duration\":0,\"startTime\":\"null\"}";
    static String subTaskJsonExample = "{\"epicId\":1,\"id\":2,\"title\":\"Subtask1\"," +
            "\"description\":\"SubtaskDescription1\",\"status\":\"NEW\",\"duration\":10,\"startTime\":\"2001-04-15T10:45:20\"}";


    @Step("Получить все задачи")
    public static Response getAllTasksStep() {
        return given()
                .baseUri(HOST)
                .header("Content-Type", "application/json")
                .when()
                .get(TASKS);
    }

    @Step("Получить все эпики")
    public static Response getAllEpicsStep() {
        return given()
                .baseUri(HOST)
                .header("Content-Type", "application/json")
                .when()
                .get(EPICS);
    }

    @Step("Получить все подзадачи")
    public static Response getAllSubTasksStep() {
        return given()
                .baseUri(HOST)
                .header("Content-Type", "application/json")
                .when()
                .get(SUBTASKS);
    }

    @Step("Получить задачу по Id")
    public static Response getTaskByIdStep(int id) {
        return given()
                .baseUri(HOST)
                .header("Content-Type", "application/json")
                .pathParam("id", id)
                .when()
                .get(TASKS + "/{id}");
    }

    @Step("Получить эпик по Id")
    public static Response getEpicByIdStep(int id) {
        return given()
                .baseUri(HOST)
                .header("Content-Type", "application/json")
                .pathParam("id", id)
                .when()
                .get(EPICS + "/{id}");
    }

    @Step("Получить подзадачу по Id")
    public static Response getSubTaskByIdStep(int id) {
        return given()
                .baseUri(HOST)
                .header("Content-Type", "application/json")
                .pathParam("id", id)
                .when()
                .get(SUBTASKS + "/{id}");
    }

    @Step("Создать задачу")
    public static Response createTaskStep() {
        return given()
                .baseUri(HOST)
                .header("Content-Type", "application/json")
                .body(taskJsonExample)
                .when()
                .post(TASKS);
    }


    @Step("Создать эпик")
    public static Response createEpicStep() {
        return given()
                .baseUri(HOST)
                .header("Content-Type", "application/json")
                .body(EpicJsonExample)
                .when()
                .post(EPICS);
    }

    @Step("Создать подзадачу")
    public static Response createSubTaskStep() {
        return given()
                .baseUri(HOST)
                .header("Content-Type", "application/json")
                .body(subTaskJsonExample)
                .when()
                .post(SUBTASKS);
    }

    @Step("Удалить задачу по ID")
    public static Response deleteTaskByIdStep(int id) {
        return given()
                .baseUri(HOST)
                .header("Content-Type", "application/json")
                .pathParam("id", id)
                .when()
                .delete(TASKS + "/{id}");
    }

    @Step("Удалить эпик по ID")
    public static Response deleteEpicByIdStep(int id) {
        return given()
                .baseUri(HOST)
                .header("Content-Type", "application/json")
                .pathParam("id", id)
                .when()
                .delete(EPICS + "/{id}");
    }

    @Step("Удалить подзадачу по ID")
    public static Response deleteSubTaskByIdStep(int id) {
        return given()
                .baseUri(HOST)
                .header("Content-Type", "application/json")
                .pathParam("id", id)
                .when()
                .delete(SUBTASKS + "/{id}");
    }

    @Step("Получить историю")
    public static Response getHistory() {
        return given()
                .baseUri(HOST)
                .header("Content-Type", "application/json")
                .when()
                .get(HISTORY);
    }

    @Step("Получить список задач по приоритету")
    public static Response getPrioritized() {
        return given()
                .baseUri(HOST)
                .header("Content-Type", "application/json")
                .when()
                .get(PRIORITIZED);
    }
}
