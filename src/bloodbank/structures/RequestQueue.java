package bloodbank.structures;

import bloodbank.model.BloodRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Member 2's component: Task 1 - Queue, used for incoming blood requests.
 * Standard FIFO enqueue/dequeue, with an enqueueUrgent() that inserts at the
 * front so Urgent requests are served before Normal ones already waiting.
 * - Standard FIFO enqueue/dequeue.
 * - Special enqueueUrgent() inserts at the front so urgent requests are served first.
 */
public class RequestQueue {
    private Node<BloodRequest> front; // points to first request
    private Node<BloodRequest> rear;  // points to last request
    private int size;                 // number of requests in queue

    /** Normal FIFO enqueue - added at the rear. O(1). */
    public void enqueue(BloodRequest request) {
        Node<BloodRequest> newNode = new Node<>(request);
        if (rear == null) {
            front = rear = newNode; // first element
        } else {
            rear.next = newNode;    // link new node at end
            rear = newNode;         // update rear pointer
        }
        size++;
    }

    /** Urgent enqueue - inserted at the front so it is served next. O(1). */
    public void enqueueUrgent(BloodRequest request) {
        Node<BloodRequest> newNode = new Node<>(request);  // Create new node for urgent request
        newNode.next = front; // link new node before current front
        front = newNode;      // update front pointer
        if (rear == null) rear = newNode; // if queue was empty
        size++;
    }

    /** Removes and returns the request at the front of the queue. O(1). */
    public BloodRequest dequeue() {
        if (front == null) return null;
        BloodRequest data = front.data;
        front = front.next;   // move front pointer forward
        if (front == null) rear = null; // queue became empty
        size--;
        return data;
    }

    /** Peek at the front request without removing it. */
    public BloodRequest peek() {
        return front == null ? null : front.data;
    }

    public boolean isEmpty() { return front == null; }
    public int size() { return size; }

    /** Convert queue contents to a List for display/reporting. */
    public List<BloodRequest> toList() {
        List<BloodRequest> result = new ArrayList<>();
        Node<BloodRequest> current = front;
        while (current != null) {
            result.add(current.data);
            current = current.next;
        }
        return result;
    }
}
