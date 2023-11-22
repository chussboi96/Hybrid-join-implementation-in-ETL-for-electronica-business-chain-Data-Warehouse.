import java.util.NoSuchElementException;

class customQueue<T> {
    private DoublyLinkedList<T> list = new DoublyLinkedList<>(); // Make sure to initialize the list

    public void enqueue(int joinAttributeValue, T data) {
        list.add(joinAttributeValue, data);
    }

    public void removeByProductID(int productID) {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        ListNode<T> current = list.head;
        while (current != null) {
            if (current.joinAttributeValue == productID) {
                // Use the existing remove method in DoublyLinkedList
                list.remove(current);
                return;
            }
            current = current.next;
        }
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public int size() {
        return list.size();
    }
}