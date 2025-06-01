import org.junit.After;
import service.InMemoryTaskManager;


import java.io.File;

public class BasePreferences {
    String path = new File("src/main/resources/task_info.csv").getAbsolutePath();
    InMemoryTaskManager manager = InMemoryTaskManager.getInstance();

    @After
    public void setNextID() {
        manager.setNextId(1);
    }
}
