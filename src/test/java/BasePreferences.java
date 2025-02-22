import org.junit.After;
import service.InMemoryTaskManager;

public class BasePreferences {
    InMemoryTaskManager manager = InMemoryTaskManager.getInstance();

    @After
    public void setNextID() {
        manager.setNextId(1);
    }
}
