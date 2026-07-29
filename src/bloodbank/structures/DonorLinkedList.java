package bloodbank.structures;

import bloodbank.model.Donor;
import java.util.ArrayList;
import java.util.List;

/**
 * Member 1's component: Task 1 - Linear Data Structures.
 * Custom singly linked list (does not use java.util.LinkedList) storing Donor records.
 */
public class DonorLinkedList {
    private Node<Donor> head;
    private int size;

    public DonorLinkedList() {
        head = null;
        size = 0;
    }

    /** Insert at the end of the list. O(n) since we walk to the tail (keeps insertion order). */
    public void insert(Donor donor) {
        Node<Donor> newNode = new Node<>(donor);
        if (head == null) {
            head = newNode;
        } else {
            Node<Donor> current = head;
            while (current.next != null) current = current.next;
            current.next = newNode;
        }
        size++;
    }

    /** Delete a donor by ID. Returns true if a record was removed. O(n). */
    public boolean delete(String id) {
        if (head == null) return false;
        if (head.data.getId().equals(id)) {
            head = head.next;
            size--;
            return true;
        }
        Node<Donor> current = head;
        while (current.next != null) {
            if (current.next.data.getId().equals(id)) {
                current.next = current.next.next;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /** Search a donor by ID. O(n). */
    public Donor search(String id) {
        Node<Donor> current = head;
        while (current != null) {
            if (current.data.getId().equals(id)) return current.data;
            current = current.next;
        }
        return null;
    }

    /** Returns all donors as a List, in list order. O(n). */
    public List<Donor> traverse() {
        List<Donor> result = new ArrayList<>();
        Node<Donor> current = head;
        while (current != null) {
            result.add(current.data);
            current = current.next;
        }
        return result;
    }

    public int size() { return size; }
    public boolean isEmpty() { return head == null; }
}
