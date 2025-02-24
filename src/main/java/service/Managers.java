package service;

public class Managers {
    public static final HistoryManager historyManager = new InMemoryHistoryManager();

    public static HistoryManager getHistoryManager() {
        return historyManager;
    }
}
