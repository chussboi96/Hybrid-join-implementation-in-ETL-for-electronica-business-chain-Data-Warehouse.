import java.util.NoSuchElementException;

class DoublyLinkedList<T> {
    ListNode<T> head;
    ListNode<T> tail;
    public int size;

    public DoublyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public void add(int joinAttributeValue, T data) {
        ListNode<T> newNode = new ListNode<>(joinAttributeValue, data);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.prev = tail;
            if (tail != null) { // Null check for robustness
                tail.next = newNode;
            }
            tail = newNode;
        }
        size++;
    }

    public void remove(ListNode<T> node) {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }

        if (node == head) {
            head = node.next;
            if (head != null) {
                head.prev = null;
            }
        } else if (node == tail) {
            tail = node.prev;
            if (tail != null) {
                tail.next = null;
            }
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        size--;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}