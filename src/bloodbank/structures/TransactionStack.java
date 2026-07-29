package bloodbank.structures;

import bloodbank.model.Transaction;
import java.util.ArrayList;
import java.util.List;

/**
 * Member 2's component: Task 1 - Stack, used for admin transaction history / undo.
 * Implemented with a linked list (push/pop/peek all O(1)).
 */
public class TransactionStack {
    private Node<Transaction> top;
    private int size;

    public void push(Transaction t) {
        Node<Transaction> newNode = new Node<>(t);
        newNode.next = top;
        top = newNode;
        size++;
    }

    public Transaction pop() {
        if (top == null) return null;
        Transaction data = top.data;
        top = top.next;
        size--;
        return data;
    }

    public Transaction peek() {
        return top == null ? null : top.data;
    }

    public boolean isEmpty() { return top == null; }
    public int size() { return size; }

    /** Returns the history from most-recent to oldest, without modifying the stack. */
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
