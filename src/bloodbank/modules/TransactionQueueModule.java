package bloodbank.modules;

import bloodbank.io.RequestFileManager;
import bloodbank.io.TransactionFileManager;
import bloodbank.model.BloodRequest;
import bloodbank.model.Transaction;
import bloodbank.structures.RequestQueue;
import bloodbank.structures.TransactionStack;

import java.util.List;

/**
 * MEMBER 2 MODULE - owns: Stack (admin transaction history / undo),
 * Queue (pending blood requests). Bubble Sort lives in bloodbank.sort.BubbleSorter.
 */
public class TransactionQueueModule {

    private final TransactionStack historyStack = new TransactionStack();
    private final RequestQueue pendingQueue = new RequestQueue();
    private final TransactionFileManager transactionFileManager = new TransactionFileManager();
    private final RequestFileManager requestFileManager = new RequestFileManager();
    private final String logFilePath;
    private final String requestsFilePath;

    public TransactionQueueModule(String logFilePath, String requestsFilePath) {
        this.logFilePath = logFilePath;
        this.requestsFilePath = requestsFilePath;
    }

    public void loadTransactions() {
        List<Transaction> loaded = transactionFileManager.loadFromFile(logFilePath);
        for (Transaction t : loaded) {
            historyStack.push(t);
        }
    }

    // ---- Stack (transaction history) ----

    public void logTransaction(String action, String details) {
        Transaction t = new Transaction(action, details);
        historyStack.push(t);
        transactionFileManager.appendTransaction(logFilePath, t);
    }

    public List<Transaction> historySnapshot() {
        return historyStack.toList();
    }

    public boolean historyIsEmpty() {
        return historyStack.isEmpty();
    }

    public Transaction popLastTransaction() {
        return historyStack.pop();
    }

    // ---- Queue (pending requests) ----

    public void enqueue(BloodRequest request) {
        pendingQueue.enqueue(request);
    }

    public void enqueueUrgent(BloodRequest request) {
        pendingQueue.enqueueUrgent(request);
    }

    public BloodRequest dequeue() {
        return pendingQueue.dequeue();
    }

    public BloodRequest peekQueue() {
        return pendingQueue.peek();
    }

    public boolean queueIsEmpty() {
        return pendingQueue.isEmpty();
    }

    public int queueSize() {
        return pendingQueue.size();
    }

    public void saveRequests(List<BloodRequest> allRequests) {
        requestFileManager.saveToFile(requestsFilePath, allRequests);
    }

    public List<BloodRequest> loadRequests() {
        return requestFileManager.loadFromFile(requestsFilePath);
    }
}
