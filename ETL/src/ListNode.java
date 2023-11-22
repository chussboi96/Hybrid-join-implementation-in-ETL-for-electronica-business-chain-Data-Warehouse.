class ListNode<T>  {
    int joinAttributeValue; // the join attribute productID is an integer
    T data;
    ListNode<T> prev;
    ListNode<T> next;

    public ListNode(int joinAttributeValue, T data) {
        this.joinAttributeValue = joinAttributeValue;
        this.data = data;
        this.prev = null;
        this.next = null;
    }

}