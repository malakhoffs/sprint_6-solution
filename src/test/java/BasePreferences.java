import org.junit.After;
import org.junit.Before;
import service.InMemoryTaskManager;


import java.io.File;

public class BasePreferences {
    String path = new File("src/main/resources/task_info.csv").getAbsolutePath();
    InMemoryTaskManager manager = InMemoryTaskManager.getInstance();

    @Before
    public void managerClear() {
        manager.deleteAllTasks();
        manager.deleteAllEpicTasks();
    }

    @After
    public void setNextID() {
        manager.setNextId(1);
    }
}
