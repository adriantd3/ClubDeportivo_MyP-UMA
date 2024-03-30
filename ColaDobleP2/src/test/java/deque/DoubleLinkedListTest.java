// Authors: Adrián Torremocha Doblas
//          Ezequiel Sánchez García
package deque;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("A DoubleLinkedList")
public class DoubleLinkedListTest {

    @Nested
    @DisplayName("when new")
    class Constructor {
        @Test
        @DisplayName("is empty with size 0")
        public void DoubleLinkedList_SizeIsZero_CreatesList() {
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            int expected_size = 0;

            int res_size = list.size();

            assertEquals(expected_size, res_size);
        }

        @Test
        @DisplayName("first node is null")
        public void DoubleLinkedList_FirstNodeIsNull_CreatesList() {
            DoubleLinkedList<String> list = new DoubleLinkedList<>();

            assertThrows(DoubleLinkedQueueException.class, () -> {
                list.first();
            });
        }

        @Test
        @DisplayName("last node is null")
        public void DoubleLinkedList_LastNodeIsNull_CreatesList() {
            DoubleLinkedList<String> list = new DoubleLinkedList<>();

            assertThrows(DoubleLinkedQueueException.class, () -> {
                list.last();
            });
        }
    }

    @Nested
    @DisplayName("prepend")
    class Prepend {
        @Test
        @DisplayName("throws exception on null node")
        public void Prepend_NullNode_ThrowsDoubleLinkedQueueException() {
            DoubleLinkedList<String> list = new DoubleLinkedList<>();

            assertThrows(DoubleLinkedQueueException.class, () -> {
                list.prepend(null);
            });
        }

        @Test
        @DisplayName("on empty list, provokes first and last to be the same")
        public void Prepend_EmptyList_FirstAndLastAreTheSame() {
            DoubleLinkedList<String> list = new DoubleLinkedList<>();

            list.prepend("First");

            assertEquals(list.first(), list.last());
        }

        @Test
        @DisplayName("on non-empty list, updates first node reference")
        public void Prepend_NonEmptyList_UpdatesFirstNode() {
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.prepend("One");

            String first = list.first();
            list.prepend("Two");

            assertNotEquals(first, list.first());
        }

        @Test
        @DisplayName("increases by one the list size")
        public void Prepend_InRageParameters_IncreasesListSize() {
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            int expected_size = list.size() + 1;

            list.prepend("Elem");
            int res_size = list.size();

            assertEquals(expected_size, res_size);
        }
    }

    @Nested
    @DisplayName("append")
    class Append {
        @Test
        @DisplayName("throws exception on null node")
        public void Append_NullNode_ThrowsDoubleLinkedQueueException() {
            DoubleLinkedList<String> list = new DoubleLinkedList<>();

            assertThrows(DoubleLinkedQueueException.class, () -> {
                list.append(null);
            });
        }

        @Test
        @DisplayName("on empty list, provokes first and last to be the same")
        public void Append_EmptyList_FirstAndLastAreTheSame() {
            DoubleLinkedList<String> list = new DoubleLinkedList<>();

            list.append("First");

            assertEquals(list.first(), list.last());
        }

        @Test
        @DisplayName("on non-empty list, updates last node reference")
        public void Append_NonEmptyList_UpdatesLastNode() {
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.append("One");

            String last = list.last();
            list.append("Two");

            assertNotEquals(last, list.last());
        }

        @Test
        @DisplayName("increases by one the list size")
        public void Append_UpdatesListSize() {
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            int expected_size = list.size() + 1;

            list.append("Elem");
            int res_size = list.size();

            assertEquals(expected_size, res_size);
        }

    }

    @Nested
    @DisplayName("deleteFirst")
    class DeleteFirst {
        @Test
        @DisplayName("throws exception on empty list")
        public void DeleteFirst_EmptyList_ThrowsDoubleLinkedQueueException() {
            DoubleLinkedList<String> list = new DoubleLinkedList<>();

            assertThrows(DoubleLinkedQueueException.class, () -> {
                list.deleteFirst();
            });
        }

        @Test
        @DisplayName("on a single element list, decreases list size to 0")
        public void DeleteFirst_ListHasOneElement_ListSizeIsZero() {
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.append("First");

            list.deleteFirst();

            assertEquals(0, list.size());

        }

        @Test
        @DisplayName("updates first node reference")
        public void DeleteFirst_ListHasMoreThanOneElement_UpdatesFirstNodeReference() {
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.prepend("First");
            String second = "second";
            list.append(second);

            list.deleteFirst();

            assertEquals(second, list.first());
        }

        @Test
        @DisplayName("decreases list size by one")
        public void DeleteFirst_NonEmptyList_DecreasesListSize() {
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.prepend("First");
            int expected_size = list.size() - 1;

            list.deleteFirst();
            int res_size = list.size();

            assertEquals(expected_size, res_size);
        }

    }

    @Nested
    @DisplayName("deleteLast")
    class DeleteLast {
        @Test
        @DisplayName("throws exception on empty list")
        public void DeleteLast_EmptyList_ThrowsDoubleLinkedQueueException() {
            DoubleLinkedList<String> list = new DoubleLinkedList<>();

            assertThrows(DoubleLinkedQueueException.class, () -> {
                list.deleteLast();
            });
        }

        @Test
        @DisplayName("on a single element list, decreases size to 0")
        public void DeleteLast_ListHasOneNode_ListSizeIsZero() {
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.append("First");
            list.deleteLast();

            assertEquals(0, list.size());
        }

        @Test
        @DisplayName("updates last node reference")
        public void DeleteLast_PreviousToLastNodeIsNewLastNode() {
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            String previous = "Previous";
            list.prepend(previous);
            list.append("Last");

            list.deleteLast();

            assertEquals(previous, list.last());
        }

        @Test
        @DisplayName("decreases list size by one")
        public void DeleteLast_NonEmptyList_UpdatesListSize() {
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.prepend("First");
            int expected_size = list.size() - 1;

            list.deleteLast();
            int res_size = list.size();

            assertEquals(expected_size, res_size);
        }
    }

    @Nested
    @DisplayName("first")
    class First {
        @Test
        @DisplayName("throws exception on empty list")
        public void First_EmptyList_ThrowsDoubleLinkedQueueException() {
            DoubleLinkedList<String> list = new DoubleLinkedList<>();

            assertThrows(DoubleLinkedQueueException.class, () -> {
                list.first();
            });
        }

        @Test
        @DisplayName("returns first node item")
        public void First_NonEmptyList_ReturnsFirstNodeItem() {
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            String expected_first = "First";
            list.append(expected_first);
            list.append("Second");

            String res_first = list.first();

            assertEquals(expected_first, res_first);
        }

    }

    @Nested
    @DisplayName("last")
    class Last {
        @Test
        @DisplayName("throws exception on empty list")
        public void Last_EmptyList_ThrowsDoubleLinkedQueueException() {
            DoubleLinkedList<String> list = new DoubleLinkedList<>();

            assertThrows(DoubleLinkedQueueException.class, () -> {
                list.last();
            });
        }

        @Test
        @DisplayName("returns last node item")
        public void Last_NonEmptyList_ReturnsLastNodeItem() {
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            String expected_last = "Last";
            list.prepend(expected_last);
            list.prepend("First");

            String res_last = list.last();

            assertEquals(expected_last, res_last);
        }
    }

    @Nested
    @DisplayName("size")
    class Size {
        @Test
        @DisplayName("is 0 on empty list")
        public void Size_EmptyList_ReturnsZero() {
            DoubleLinkedList<String> list = new DoubleLinkedList<>();

            assertEquals(0, list.size());
        }

        @Test
        @DisplayName("is correct on non-empty list")
        public void Size_NonEmptyList_ReturnsCorrectSize() {
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.append("One");
            list.append("Two");
            list.append("Three");
            int expected_size = 3;

            int res_size = list.size();

            assertEquals(expected_size, res_size);
        }


    }
    @Nested
    @DisplayName("get")
    class Get {
        @ParameterizedTest
        @DisplayName("index out of bounds")
        @CsvSource({
                "-1",
                "3",
        })
        public void Get_IndexOutOfBounds_ReturnsIndexOutOfBoundsException(int index){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.append("One");
            list.append("Two");
            list.append("Three");

            assertThrows(IndexOutOfBoundsException.class, ()->{list.get(index);});
        }

        @Test
        @DisplayName("empty list")
        public void Get_EmptyList_ReturnsIndexOutOfBoundsException(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            assertThrows(IndexOutOfBoundsException.class, ()->{list.get(0);});
        }

        @Test
        @DisplayName("non-empty list")
        public void Get_NonEmptyList_ReturnsCorrectNodeItem(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.append("One");
            list.append("Two");
            list.append("Three");
            String expected = "Two";

            String res = list.get(1);

            assertEquals(expected, res);
        }
    }
    @Nested
    @DisplayName("contains")
    class Contains {
        @Test
        @DisplayName("null value")
        public void Contains_NullValue_ReturnsDoubleLinkedQueueException(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            String value = null;
            assertThrows(DoubleLinkedQueueException.class,()->{list.contains(value);});
        }

        @Test
        @DisplayName("empty list")
        public void Contains_EmptyList_ReturnsFalse(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            String value = "One";
            Boolean res = list.contains(value);

            assertFalse(res);

        }

        @ParameterizedTest
        @DisplayName("existing values")
        @CsvSource({
                "One",
                "Two"
        })
        public void Contains_ExistingValues_ReturnsTrue(String value){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.append("One");
            list.append("Two");
            Boolean res = list.contains(value);
            assertTrue(res);
        }

        @ParameterizedTest
        @DisplayName("non-existing values")
        @CsvSource({
                "Three",
                "Four"
        })
        public void Contains_NonExistingValues_ReturnsFalse(String value){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.append("One");
            list.append("Two");
            Boolean res = list.contains(value);
            assertFalse(res);
        }
    }

    @Nested
    @DisplayName("remove")
    class Remove {
        @Test
        @DisplayName("null value")
        public void Remove_NullValue_ReturnsDoubleLinkedQueueException(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            String value = null;
            assertThrows(DoubleLinkedQueueException.class, ()->{list.remove(value);});
        }

        @Test
        @DisplayName("empty list")
        public void Remove_EmptyList_SizeRemainsAtZero(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            int expected = 0;
            list.remove("One");
            int res = list.size();

            assertEquals(expected, res);
        }

        @ParameterizedTest
        @DisplayName("existing values")
        @CsvSource({
                "One",
                "Two"
        })
        public void Remove_ExistingValues_DecreasesListSizeByOne(String value){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.append("One");
            list.append("Two");
            int expected_size = list.size() - 1;
            list.remove(value);
            int res = list.size();

            assertEquals(expected_size, res);
        }

        @ParameterizedTest
        @DisplayName("non-existing values")
        @CsvSource({
                "Three",
                "Four"
        })
        public void Remove_NonExistingValues_ListSizeRemainsTheSame(String value){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.append("One");
            list.append("Two");
            int expected_size = list.size();
            list.remove(value);
            int res = list.size();

            assertEquals(expected_size, res);
        }

        @Test
        @DisplayName("non-terminal node")
        public void Remove_NonTerminalNode_UpdatesPreviousAndNextNode(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.append("One");
            list.append("Two");
            list.append("Three");
            String second_item = list.get(1);
            list.remove("Two");
            String new_second_item = list.get(1);
            assertNotEquals(second_item, new_second_item);
        }

        @Test
        @DisplayName("list with a single item")
        public void Remove_ListWithASingleItem_BecomesAnEmptyList(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.append("One");
            list.remove("One");
            assertThrows(DoubleLinkedQueueException.class, ()->{list.first();});
        }
    }

    @Nested
    @DisplayName("sort")
    class Sort {

        @Test
        @DisplayName("list size is smaller than two")
        public void Sort_ListSmallerThanTwo_DoesNothing(){
            DoubleLinkedList<Integer> list = new DoubleLinkedList<>();
            list.append(1);
            Integer expected = list.get(0);
            Comparator<Integer> comparator = Comparator.naturalOrder();
            list.sort(comparator);
            Integer res = list.get(0);

            assertEquals(expected, res);

        }
        @Test
        @DisplayName("sorts in natural order")
        public void Sort_NaturalOrder_SortsTheList(){
            DoubleLinkedList<Integer> list = new DoubleLinkedList<>();
            list.append(3);
            list.append(4);
            list.append(1);
            list.append(2);
            Comparator<Integer> comparator = Comparator.naturalOrder();
            list.sort(comparator);
            for (int i=0; i< list.size(); i++){
                assertEquals(i+1,list.get(i));
            }

        }

        @Test
        @DisplayName("sorts in reverse order")
        public void Sort_ReverseOrder_SortsTheList(){
            DoubleLinkedList<Integer> list = new DoubleLinkedList<>();
            list.append(3);
            list.append(4);
            list.append(1);
            list.append(2);
            Comparator<Integer> comparator = Comparator.reverseOrder();
            list.sort(comparator);
            for (int i=0; i< list.size(); i++){
                assertEquals(list.size()- i,list.get(i));
            }

        }
    }
}
