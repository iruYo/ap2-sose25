internal interface Listlike<T> {
    /**
     * Fügt das angegebene Element am Anfang der Liste hinzu.
     *
     * @param data das hinzuzufügende Element
     */
    fun addFirst(data: T)

    /**
     * Gibt das erste Element aus der Liste zurück.
     *
     * @return das erste Element aus der Liste
     * @throws NoSuchElementException wenn die Liste leer ist
     */
    fun getFirst(): T

    /**
     * Fügt das angegebene Element am Ende der Liste hinzu.
     *
     * @param data das hinzuzufügende Element
     */
    fun addLast(data: T)

    /**
     * Entfernt das erste Element aus der Liste und gibt dieses zurück.
     *
     * @return das erste Element aus der Liste
     * @throws NoSuchElementException wenn die Liste leer ist
     */
    fun removeFirst(): T

    /**
     * Gibt die Anzahl der Elemente in der Liste zurück.
     *
     * @return die Anzahl der Elemente in der Liste
     */
    fun size(): Int

    /**
     * Prüft, ob die Liste das angegebene Element enthält.
     *
     * @param data Element, dessen Vorhandensein in der Liste geprüft werden soll
     * @return true, wenn die Liste das angegebene Element enthält
     */
    fun contains(data: T): Boolean

    /**
     * Prüft, ob die Liste keine Elemente enthält.
     *
     * @return true, wenn die Liste keine Elemente enthält
     */
    fun isEmpty(): Boolean

    /**
     * Gibt das Element an der angegebenen Position in der Liste zurück. Der Index fängt bei 0 an.
     *
     * @param index Index des zurückzugebenden Elements.
     * @return das Element an der angegebenen Position in der Liste
     * @throws IndexOutOfBoundsException wenn der Index außerhalb des gültigen Bereichs liegt (index < 0 || index >= size())
     */
    fun get(index: Int): T

    /**
     * Entfernt das Element an der angegebenen Position und gibt dieses zurück.
     *
     * @param index Index des zu entfernenden Elements
     * @return das entfernte Element
     * @throws IndexOutOfBoundsException wenn der Index außerhalb des gültigen Bereichs liegt (index < 0 || index >= size())
     */
    fun removeAtIndex(index: Int): T

    fun getIndexOf(data: T): Int

    fun getLast(): T

    fun addSorted(data: T, comparator: Comparator<T>)

    fun sort(comparator: Comparator<T>): MyList<T>

    fun quicksort(comparator: Comparator<T>): MyList<T>
}

