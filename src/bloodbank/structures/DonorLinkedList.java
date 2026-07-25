package bloodbank.structures;

import bloodbank.model.Donor;
import java.util.ArrayList;
import java.util.List;

public class DonorLinkedList {
    private Node<Donor> head;
    private int size;

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

    public Donor search(String id) {
        Node<Donor> current = head;
        while (current != null) {
            if (current.data.getId().equals(id)) return current.data;
            current = current.next;
        }
        return null;
    }

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