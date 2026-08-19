package bloodbank.modules;

import bloodbank.io.RequestFileManager;
import bloodbank.io.TransactionFileManager;
import bloodbank.model.BloodRequest;
import bloodbank.model.Transaction;
import bloodbank.structures.RequestQueue;
import bloodbank.structures.TransactionStack;

import java.util.List;

/**
 * MEMBER 2 MODULE
 * - Owns: Stack (admin transaction history / undo)
 * - Owns: Queue (pending blood requests)
 * - Bubble Sort lives separately in bloodbank.sort.BubbleSorter
 */
public class TransactionQueueModule {

    // Stack to store admin transaction history (supports undo)
    private final TransactionStack historyStack = new TransactionStack();
    // Queue to store pending blood requests
    private final RequestQueue pendingQueue = new RequestQueue();
    // File managers for persistence
    private final TransactionFileManager transactionFileManager = new TransactionFileManager();
    private final RequestFileManager requestFileManager = new RequestFileManager();
    // Paths to log and request files
    private final String logFilePath;
    private final String requestsFilePath;

    // Constructor: initialize with file paths
    public TransactionQueueModule(String logFilePath, String requestsFilePath) {
        this.logFilePath = logFilePath;
        this.requestsFilePath = requestsFilePath;
    }

    // ---- Load existing transactions into the stack ----
    public void loadTransactions() {
        List<Transaction> loaded = transactionFileManager.loadFromFile(logFilePath);
        for (Transaction t : loaded) {
            historyStack.push(t); // push each transaction into stack
        }
    }

    // ---- Stack (transaction history) ----

    // Log a new transaction: create, push to stack, and append to file
    public void logTransaction(String action, String details) {
        Transaction t = new Transaction(action, details);
        historyStack.push(t); // add to stack
        transactionFileManager.appendTransaction(logFilePath, t); // save to file
    }

    // Get a snapshot of history (most recent → oldest)
    public List<Transaction> historySnapshot() {
        return historyStack.toList();
    }

    // Check if history stack is empty
    public boolean historyIsEmpty() {
        return historyStack.isEmpty();
    }

    // Undo: pop the last transaction from stack
    public Transaction popLastTransaction() {
        return historyStack.pop();
    }

    // ---- Queue (pending blood requests) ----

    // Add a normal request to the queue (rear)
    public void enqueue(BloodRequest request) {
        pendingQueue.enqueue(request);
    }

    // Add an urgent request to the front of the queue
    public void enqueueUrgent(BloodRequest request) {
        pendingQueue.enqueueUrgent(request);
    }

    // Remove and return the next request (front of queue)
    public BloodRequest dequeue() {
        return pendingQueue.dequeue();
    }

    // Peek: look at the next request without removing it
    public BloodRequest peekQueue() {
        return pendingQueue.peek();
    }

    // Check if queue is empty
    public boolean queueIsEmpty() {
        return pendingQueue.isEmpty();
    }

    // Get number of requests in queue
    public int queueSize() {
        return pendingQueue.size();
    }

    // Save all requests to file
    public void saveRequests(List<BloodRequest> allRequests) {
        requestFileManager.saveToFile(requestsFilePath, allRequests);
    }

    // Load requests from file into memory
    public List<BloodRequest> loadRequests() {
        return requestFileManager.loadFromFile(requestsFilePath);
    }
}
