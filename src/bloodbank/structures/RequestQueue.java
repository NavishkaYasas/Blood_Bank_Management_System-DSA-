package bloodbank.structures;

import bloodbank.model.BloodRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Member 2's component: Task 1 - Queue, used for incoming blood requests.
 * Standard FIFO enqueue/dequeue, with an enqueueUrgent() that inserts at the
 * front so Urgent requests are served before Normal ones already waiting.
 */
public class RequestQueue {
    private Node<BloodRequest> front;
    private Node<BloodRequest> rear;
    private int size;

    /** Normal FIFO enqueue - added at the rear. O(1). */
    public void enqueue(BloodRequest request) {
        Node<BloodRequest> newNode = new Node<>(request);
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    /** Urgent enqueue - inserted at the front so it is served next. O(1). */
    public void enqueueUrgent(BloodRequest request) {
        Node<BloodRequest> newNode = new Node<>(request);
        newNode.next = front;
        front = newNode;
        if (rear == null) rear = newNode;
        size++;
    }

    /** Removes and returns the request at the front of the queue. O(1). */
    public BloodRequest dequeue() {
        if (front == null) return null;
        BloodRequest data = front.data;
        front = front.next;
        if (front == null) rear = null;
        size--;
        return data;
    }

    public BloodRequest peek() {
        return front == null ? null : front.data;
    }

    public boolean isEmpty() { return front == null; }
    public int size() { return size; }

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
