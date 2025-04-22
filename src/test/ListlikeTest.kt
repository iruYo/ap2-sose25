import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ListlikeTest {
    private lateinit var list: Listlike<String>

    @BeforeEach
    fun setUp() {
        list = MyList()
    }

    @Test
    @DisplayName("addFirst should add elements to the beginning")
    fun addFirstShouldAddElementsToBeginning() {
        list.addFirst("A")
        assertEquals("A", list.getFirst())
    }

    @Test
    @DisplayName("getFirst should throw NoSuchElementException if the list is empty")
    fun getFirstThrowsIfListIsEmpty() {
        assertThrows(NoSuchElementException::class.java, list::getFirst)
    }

    @Test
    @DisplayName("addFirst should keep adding in front of the list")
    fun addFirstShouldKeepAddingInFrontOfTheList() {
        list.addFirst("A")
        list.addFirst("B")
        list.addFirst("C")

        assertEquals("C", list.get(0))
        assertEquals("B", list.get(1))
        assertEquals("A", list.get(2))
    }

    @Test
    @DisplayName("new list should be empty")
    fun newListShouldBeEmpty() {
        assertTrue(list.isEmpty())
    }

    @Test
    @DisplayName("size of new list should be 0")
    fun sizeOfNewListShouldBeZero() {
        assertEquals(0, list.size())
    }

    @Test
    @DisplayName("size returns number of elements")
    fun sizeReturnsNumberOfElements() {
        for (i in 0..99) {
            list.addFirst(i.toString())
        }
        assertEquals(100, list.size())
    }

    @Test
    @DisplayName("removeFirst should remove and return the first element")
    fun removeFirstShouldRemoveAndReturnFirstElement() {
        list.addFirst("A")
        list.addFirst("B")

        assertEquals("B", list.removeFirst())
        assertEquals("A", list.removeFirst())
    }

    @Test
    @DisplayName("removeFirst should throw NoSuchElementException if the list is empty")
    fun removeFirstThrowsIfListIsEmpty() {
        assertThrows(NoSuchElementException::class.java, list::removeFirst)
    }

    @Test
    @DisplayName("addLast should add elements to the end")
    fun addLastShouldAddElementsToEnd() {
        list.addLast("A")
        list.addLast("B")
        list.addLast("C")

        assertEquals("A", list.get(0))
        assertEquals("B", list.get(1))
        assertEquals("C", list.get(2))
    }

    @Test
    @DisplayName("get should throw IndexOutOfBoundsException for invalid indices")
    fun getShouldThrowExceptionForInvalidIndices() {
        list.addFirst("A")
        assertThrows(java.lang.IndexOutOfBoundsException::class.java) { list.get(-1) }
        assertThrows(java.lang.IndexOutOfBoundsException::class.java) { list.get(1) }
    }

    @Test
    @DisplayName("contains should correctly identify present and absent elements")
    fun containsShouldIdentifyElements() {
        list.addFirst("A")
        list.addFirst("B")
        list.addFirst("C")

        assertTrue(list.contains("A"))
        assertTrue(list.contains("B"))
        assertTrue(list.contains("C"))
        assertFalse(list.contains("D"))
    }

    @Test
    @DisplayName("removeAtIndex should remove and return element at given position")
    fun removeAtIndexBasicOperation() {
        list.addFirst("A")
        list.addFirst("B")
        list.addFirst("C")
        list.addFirst("?")

        assertEquals("?", list.removeAtIndex(0))
        assertEquals(3, list.size())
        assertEquals("B", list.removeAtIndex(1))
        assertEquals(2, list.size())
        assertEquals("C", list.get(0))
        assertEquals("A", list.get(1))

        assertEquals("A", list.removeAtIndex(1))
        assertEquals(1, list.size())
        assertEquals("C", list.getFirst())
    }

    @Test
    @DisplayName("removeAtIndex should throw IndexOutOfBoundsException for invalid indices")
    fun removeAtIndexInvalidIndices() {
        list.addFirst("A")

        assertThrows(java.lang.IndexOutOfBoundsException::class.java) { list.removeAtIndex(-1) }
        assertThrows(java.lang.IndexOutOfBoundsException::class.java) { list.removeAtIndex(1) }

        assertEquals(1, list.size())
    }

    @Test
    @DisplayName("getIndexOf should correctly return index of present elements")
    fun getIndexOfShouldFindElements() {
        list.addFirst("A")
        list.addFirst("B")
        list.addFirst("C")

        assertEquals(2, list.getIndexOf("A"))
        assertEquals(1, list.getIndexOf("B"))
        assertEquals(0, list.getIndexOf("C"))
    }

    @Test
    @DisplayName("getIndexOf should throw NoSuchElementException for absent elements")
    fun getIndexOfAbsentElements() {
        list.addFirst("A")
        list.addFirst("B")

        assertThrows(NoSuchElementException::class.java) { list.getIndexOf("D") }
        assertThrows(NoSuchElementException::class.java) { list.getIndexOf("4") }
    }

    @Test
    @DisplayName("getLast should return the last element")
    fun getLastShouldReturnLastElement() {
        list.addLast("A")
        list.addLast("B")

        assertEquals("B", list.getLast())
    }

    @Test
    @DisplayName("getLast should throw NoSuchElementException if the list is empty")
    fun getLastThrowsIfListIsEmpty() {
        assertThrows(NoSuchElementException::class.java, list::getLast)
    }

    @Test
    @DisplayName("addSorted should add elements at correct indices")
    fun addSortedAddInCorrectOrder() {
        list.addLast("A")
        list.addLast("C")
        list.addLast("X")

        list.addSorted("B", compareBy { it })
        assertEquals(1, list.getIndexOf("B"))

        assertEquals(3, list.getIndexOf("X"))

        list.addSorted("D", compareBy { it })
        assertEquals(3, list.getIndexOf("D"))
        assertEquals(4, list.getIndexOf("X"))

        list.addSorted("Z", compareBy { it })
        assertEquals(5, list.getIndexOf("Z"))
    }

    @Test
    @DisplayName("sort returns sorted list")
    fun sortInCorrectOrder() {
        list.addLast("Z")
        list.addLast("B")
        list.addLast("A")
        list.addLast("K")
        list.addLast("U")
        list.addLast("L")
        list.addLast("T")
        list.addLast("E")

        val sortedList = list.sort(compareBy { it })
        assertEquals(0, sortedList.getIndexOf("A"))
        assertEquals(1, sortedList.getIndexOf("B"))
        assertEquals(2, sortedList.getIndexOf("E"))
        assertEquals(3, sortedList.getIndexOf("K"))
        assertEquals(4, sortedList.getIndexOf("L"))
        assertEquals(5, sortedList.getIndexOf("T"))
        assertEquals(6, sortedList.getIndexOf("U"))
        assertEquals(7, sortedList.getIndexOf("Z"))
    }
}
