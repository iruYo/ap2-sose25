class MyList<T> : Listlike<T> {
    private var head: Node<T>? = null
    private var tail: Node<T>? = null
    private var size = 0

    // O(1)
    override fun addFirst(data: T) {
        val newNode = Node(data, next = this.head)

        if (isEmpty()) {
            this.tail = newNode
        }
        else {
            this.head!!.previous = newNode
        }
        this.head = newNode

        size++
    }

    // O(1)
    override fun addLast(data: T) {
        val newNode = Node(data, previous = this.tail)

        if (isEmpty()) {
            this.head = newNode
        }
        else {
            this.tail!!.next = newNode
        }
        this.tail = newNode

        size++
    }

    // O(1)
    override fun getFirst(): T {
        if (isEmpty()) {
            throw NoSuchElementException("List is empty")
        }

        return head!!.data
    }

    // O(1)
    override fun getLast(): T {
        if (isEmpty()) {
            throw NoSuchElementException("List is empty")
        }

        return tail!!.data
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
    private fun searchWithIndex(data: T, node: Node<T>?, index: Int): Pair<Int, Node<T>?> {
        if (node?.data == data) {
            return Pair(index, node)
        }

        if (node?.next != null) {
            return searchWithIndex(data, node.next, index + 1)
        }

        return Pair(-1, null)
    }

    // searchWithIndex => O(n)
    override fun contains(data: T): Boolean {
        val (_, node) = searchWithIndex(data, this.head, 0)

        return node != null
    }

    // searchWithIndex => O(n)
    override fun getIndexOf(data: T): Int {
        val (index, _) = searchWithIndex(data, this.head, 0)

        if (index == -1) {
            throw NoSuchElementException("Element is not in list!")
        }

        return index
    }

    // O(1)
    override fun isEmpty(): Boolean {
        return this.head == null
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
        when (nodeToRemove) {
            this.head -> {
                head = nodeToRemove.next
            }
            this.tail -> {
                tail = nodeToRemove.previous
            }
            else -> {
                nodeToRemove.previous!!.next = nodeToRemove.next
            }
        }
        size--

        return nodeToRemove.data
    }

    // O(n)
    private fun addAscending(data: T, node: Node<T>, comparator: Comparator<T>) {
        if (comparator.compare(data, node.data) <= 0) {
            val newNode = Node(data, previous = node.previous, next = node)
            node.previous!!.next = newNode
            node.previous = newNode
        }
        else {
            if (node.next != null) {
                addAscending(data, node.next!!, comparator)
            }
            else {
                addLast(data)
            }
        }
    }

    // compare => (n)
    override fun addSorted(data: T, comparator: Comparator<T>) {
        if (isEmpty()) {
            val newNode = Node(data)
            this.head = newNode
            this.tail = newNode
        }
        else {
            addAscending(data, head!!, comparator)
        }

        size++
    }

    private fun swap(node1: Node<T>, node2: Node<T>) {
        val tmp = node2.data
        node2.data = node1.data
        node1.data = tmp
    }

    private fun innerInsertSort(node: Node<T>, comparator: Comparator<T>) {
        val currentData = node.data
        val previousData = node.previous!!.data

        if (comparator.compare(currentData, previousData) <= 0) {
            swap(node, node.previous!!)
        }

        innerInsertSort(node.previous!!, comparator)
    }

    // Insert Sort
    // O(n^2(?))
    private fun insertSort(node: Node<T>, comparator: Comparator<T>) {
        if (node.previous != null) {
            innerInsertSort(node, comparator)
        }

        if (node.next != null) {
            insertSort(node.next!!, comparator)
        }
    }

    override fun sort(comparator: Comparator<T>): MyList<T> {
        val sortedList = this

        insertSort(sortedList.head!!, comparator)

        return sortedList
    }

    override fun quicksort(comparator: Comparator<T>): MyList<T> {
        TODO()
    }
}