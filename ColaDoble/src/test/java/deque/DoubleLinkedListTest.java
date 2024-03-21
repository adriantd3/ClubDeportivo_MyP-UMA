package deque;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DoubleLinkedListTest {

    @Nested
    @DisplayName("Constructor")
    class Constructor{
        @Test
        public void DoubleLinkedList_SizeIsZero(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            assertEquals(0,list.size());
        }
    }

    @Nested
    @DisplayName("Prepend")
    class Prepend{
        @Test
        public void Prepend_NullValue_ReturnsDoubleLinkedQueueException(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            assertThrows(DoubleLinkedQueueException.class, ()->{list.prepend(null);});
        }

        @Test
        public void Prepend_EmptyList_FirstAndLastAreTheSame(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.prepend("First");

            assertEquals(list.first(), list.last());
        }

        @Test
        public void Prepend_NonEmptyList_UpdatesFirstNode(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.prepend("One");
            String first = list.first();
            list.prepend("Two");

            assertNotEquals(first, list.first());
        }

        @Test
        public void Prepend_UpdatesListSize(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            int size = list.size();
            int expected = size + 1;

            list.prepend("Elem");
            size = list.size();

            assertEquals(expected, size);
        }
    }

    @Nested
    @DisplayName("Append")
    class Append{
        @Test
        public void Append_NullValue_ReturnsDoubleLinkedQueueException(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            assertThrows(DoubleLinkedQueueException.class, ()->{list.append(null);});
        }

        @Test
        public void Append_EmptyList_FirstAndLastAreTheSame(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.append("First");

            assertEquals(list.first(), list.last());
        }

        @Test
        public void Append_NonEmptyList_UpdatesLastNode(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.append("One");
            String last = list.last();

            list.append("Two");

            assertNotEquals(last, list.last());
        }

        @Test
        public void Append_UpdatesListSize(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            int size = list.size();
            int expected = size + 1;

            list.append("Elem");
            size = list.size();

            assertEquals(expected, size);
        }

    }

    @Nested
    @DisplayName("DeleteFirst")
    class DeleteFirst{
        @Test
        public void DeleteFirst_EmptyList_ReturnsDoubleLinkedQueueException(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            assertThrows(DoubleLinkedQueueException.class, ()->{list.deleteFirst();});
        }

        @Test
        public void DeleteFirst_ListHasOneElement_ListSizeIsZero(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.append("First");
            list.deleteFirst();

            assertEquals(0, list.size());

        }

        @Test
        public void DeleteFirst_SecondNodeIsNewFirstNode(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.prepend("First");
            String second = "second";
            list.append(second);
            list.deleteFirst();
            assertEquals(second, list.first());
        }

        @Test
        public void DeleteFirst_NonEmptyList_UpdatesListSize(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.prepend("First");
            int size = list.size();
            int expected = size - 1;

            list.deleteFirst();
            size = list.size();

            assertEquals(expected, size);

        }

    }

    @Nested
    @DisplayName("DeleteLast")
    class DeleteLast{
        @Test
        public void DeleteLast_EmptyList_ReturnsDoubleLinkedQueueException(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            assertThrows(DoubleLinkedQueueException.class, ()->{list.deleteLast();});
        }

        @Test
        public void DeleteLast_ListHasOneNode_ListSizeIsZero(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.append("First");
            list.deleteLast();

            assertEquals(0, list.size());

        }

        @Test
        public void DeleteLast_PreviousToLastNodeIsNewLastNode(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            String previous = "Previous";
            list.prepend(previous);
            list.append("Last");
            list.deleteLast();
            assertEquals(previous, list.last());
        }

        @Test
        public void DeleteLast_NonEmptyList_UpdatesListSize(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.prepend("First");
            int size = list.size();
            int expected = size - 1;

            list.deleteLast();
            size = list.size();

            assertEquals(expected, size);

        }


    }

    @Nested
    @DisplayName("First")
    class First{
        @Test
        public void First_EmptyList_ReturnsDoubleLinkedQueueException(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            assertThrows(DoubleLinkedQueueException.class, ()->{list.first();});
        }

        @Test
        public void First_NonEmptyList_ReturnsFirstNodeItem(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            String first = "First";
            list.append(first);
            list.append("Second");

            assertEquals(first, list.first());
        }

    }

    @Nested
    @DisplayName("Last")
    class Last{
        @Test
        public void Last_EmptyList_ReturnsDoubleLinkedQueueException(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            assertThrows(DoubleLinkedQueueException.class, ()->{list.last();});
        }

        @Test
        public void Last_NonEmptyList_ReturnsLastNodeItem(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.append("First");
            list.append("Second");
            String last = "Last";
            list.append(last);

            assertEquals(last, list.last());
        }
    }

    @Nested
    @DisplayName("Size")
    class Size{
        @Test
        public void Size_EmptyList_ReturnsZero(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();

            assertEquals(0, list.size());
        }
        @Test
        public void Size_NonEmptyList_ReturnsCorrectSize(){
            DoubleLinkedList<String> list = new DoubleLinkedList<>();
            list.append("One");
            list.append("Two");
            list.append("Three");
            int expected_size = 3;

            assertEquals(expected_size, list.size());
        }


    }
}
