class MyList<T> : Listlike<T> {
    private var head: Node<T>? = null
    private var size = 0

    // O(1)
    override fun addFirst(data: T) {
        if (isEmpty()) {
            this.head = Node(data)
        }
        else {
            val newNode = Node(data, next = this.head)
            this.head!!.previous = newNode
            this.head = newNode
        }
        size++
    }

    // O(1)
    override fun getFirst(): T {
        if (isEmpty()) {
            throw NoSuchElementException("List is empty")
        }

        return head!!.data
    }

    // O(n)
    private fun getLastNode(node: Node<T>): Node<T> {
        if (node.next == null) {
            return node
        }

        return getLastNode(node.next!!)
    }

    // getLastNode => O(n)
    override fun addLast(data: T) {
        if (isEmpty()) {
            head = Node(data)
        }
        else {
            val lastNode = getLastNode(head!!)
            lastNode.next = Node(data, previous = lastNode)
        }
        size++
    }

    // O(1)
    override fun removeFirst(): T {
        val data = getFirst()
        this.head = head?.next

        return data
    }

    // O(1)
    override fun size(): Int {
       return size
    }

    // O(n)
    // Finds first(!) occurrence or nothing
    private fun search(data: T, node: Node<T>?): Node<T>? {
        if (node?.data == data) {
            return node
        }

        if (node?.next != null) {
            return search(data, node.next)
        }

        return null
    }

    // search => O(n)
    override fun contains(data: T): Boolean {
        return search(data, head) != null
    }

    // O(1)
    override fun isEmpty(): Boolean {
        return head == null
    }

    // O(n)
    private fun getNode(index: Int, node: Node<T>): Node<T> {
        if (index == 0) {
            return node
        }
        if (node.next == null) {
            throw IndexOutOfBoundsException("No node found")
        }

        return getNode(index - 1, node.next!!)
    }

    // getNode => O(n)
    override fun get(index: Int): T {
        if (isEmpty()) {
            throw IndexOutOfBoundsException("List is empty")
        }

        return getNode(index, this.head!!).data
    }

    // O(n)
    override fun removeAtIndex(index: Int): T {
        if (isEmpty()) {
            throw NoSuchElementException("List is empty")
        }

        val nodeToRemove = getNode(index, this.head!!)
        if (nodeToRemove.previous == null) {
            head = nodeToRemove.next
        }
        else {
            nodeToRemove.previous!!.next = nodeToRemove.next
        }
        size--

        return nodeToRemove.data
    }
}