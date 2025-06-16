import org.junit.After;
import org.junit.Before;
import service.InMemoryTaskManager;
import webserver.LocalHostServer;

import java.io.IOException;


public class BasePreferencesServer {
    InMemoryTaskManager manager = InMemoryTaskManager.getInstance();
    LocalHostServer server = new LocalHostServer(manager);

    @Before
    public void startServer() throws IOException {
        server.startServ();
    }

    @Before
    public void managerClear() {
        manager.deleteAllTasks();
        manager.deleteAllEpicTasks();
    }

    @After
    public void setNextID() {
        manager.setNextId(1);
    }

    @After
    public void stopServer() {
        server.stopServ();
    }
}
