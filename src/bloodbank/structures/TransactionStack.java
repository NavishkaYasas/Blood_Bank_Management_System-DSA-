package bloodbank.structures;

import bloodbank.model.Transaction;
import java.util.ArrayList;
import java.util.List;

/**
 * Member 2's component: Task 1 - Stack.
 * Standard LIFO push/pop, with a peek() to look at the top without removing it.
 * Used for admin transaction history / undo feature.
 * - Implemented with a linked list.
 * - Push, pop, peek all O(1).
 */
public class TransactionStack {
    private Node<Transaction> top; // reference to the top of the stack
    private int size;              // number of transactions in stack

    /** Push: add a new transaction on top of the stack. */
    public void push(Transaction t) {
        Node<Transaction> newNode = new Node<>(t);
        newNode.next = top; // link new node to current top
        top = newNode;      // update top pointer
        size++;
    }

    /** Pop: remove and return the most recent transaction.(Undo most recent action ) */
    public Transaction pop() {
        if (top == null) return null; // empty stack
        Transaction data = top.data;  // get top transaction
        top = top.next;               // move top pointer down
        size--;
        return data;
    }

    /** Peek: look at the most recent transaction without removing it. */
    public Transaction peek() {
        return top == null ? null : top.data;
    }

    public boolean isEmpty() { return top == null; }
    public int size() { return size; }

    /**
     * Convert stack into a list (most-recent → oldest).
     * Does not modify the stack itself.
     */
    public List<Transaction> toList() {
        List<Transaction> result = new ArrayList<>();
        Node<Transaction> current = top;
        while (current != null) {
            result.add(current.data);
            current = current.next;
        }
        return result;
    }
}
