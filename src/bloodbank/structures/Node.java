package bloodbank.structures;

/**
 * Generic Node class used across multiple linear data structures.
 * - Stores data of type T.
 * - Points to the next node in the chain.
 * - Reused in DonorLinkedList, TransactionStack, RequestQueue, HashTable, Set.
 */
public class Node<T> {
    public T data;       // The element stored in this node
    public Node<T> next; // Reference to the next node

    // Constructor: create a new node with given data
    public Node(T data) {
        this.data = data;
        this.next = null; // initially no next node
    }
}
