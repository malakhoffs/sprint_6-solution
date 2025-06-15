package webserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import handler.*;
import service.InMemoryTaskManager;
import adapter.DurationAdapter;
import adapter.LocalDateTimeAdapter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

import static webserver.PathConstants.*;

public class LocalHostServer {

    private final InMemoryTaskManager manager;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    private final HttpServer httpServer;

    public LocalHostServer() {
        try {
            httpServer = HttpServer.create();
            manager = InMemoryTaskManager.getInstance();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка сервера: " + e.getMessage(), e);
        }
    }

    public void startServ() throws IOException {
        int port = PORT;
        InetSocketAddress socketAddress = new InetSocketAddress(HOST, port);

        httpServer.bind(socketAddress, 0);
        httpServer.createContext(TASKS, new TaskHandler(manager, gson));
        httpServer.createContext(SUBTASKS, new SubTaskHandler(manager, gson));
        httpServer.createContext(EPICS, new EpicHandler(manager, gson));
        httpServer.createContext(PRIORITIZED, new PrioritizedHandler(manager, gson));
        httpServer.createContext(HISTORY, new HistoryHandler(manager, gson));
        httpServer.start();
        System.out.println("HTTP-сервер запущен на: " + HOST + ":" + PORT);
    }

    public void stopServ() {
        httpServer.stop(0);
        System.out.println("сервер остановлен");
    }
}