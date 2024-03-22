package deque;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("A node")
public class LinkedNodeTest {

    @Nested
    @DisplayName("constructor")
    class Constructor {
        @Test
        @DisplayName("does not have nor previous nor next node")
        public void LinkedNode_NoPreviousNoNext_CreatesNode() {
            String item = "FirstNode";

            LinkedNode<String> leaf = new LinkedNode<>(item, null, null);

            assertNull(leaf.getPrevious());
            assertNull(leaf.getNext());
        }

        @Test
        @DisplayName("has previous but no next node")
        public void LinkedNode_HasPreviousNoNext_CreatesNode() {
            String item = "FirstNode";
            LinkedNode<String> previous = new LinkedNode<>("Previous", null, null);

            LinkedNode<String> leaf = new LinkedNode<>(item, previous, null);

            assertEquals(previous, leaf.getPrevious());
            assertNull(leaf.getNext());
        }

        @Test
        @DisplayName("does not have previous node but has next node")
        public void LinkedNode_NoPreviousHasNext_CreatesNode() {
            String item = "FirstNode";
            LinkedNode<String> next = new LinkedNode<>("Next", null, null);

            LinkedNode<String> leaf = new LinkedNode<>(item, null, next);

            assertNull(leaf.getPrevious());
            assertEquals(next, leaf.getNext());
        }

        @Test
        @DisplayName("has previous and next node")
        public void LinkedNode_HasPreviousHasNext_CreatesNode() {
            String item = "FirstNode";
            LinkedNode<String> previous = new LinkedNode<>("Previous", null, null);
            LinkedNode<String> next = new LinkedNode<>("Next", null, null);

            LinkedNode<String> leaf = new LinkedNode<>(item, previous, next);

            assertEquals(previous, leaf.getPrevious());
            assertEquals(next, leaf.getNext());
        }

        @Test
        @DisplayName("with a nullable item")
        public void LinkedNode_NullableItem_ThrowsDoubleLinkedQueueException() {
            String item = null;

            assertThrows(DoubleLinkedQueueException.class, () -> {
                new LinkedNode<>(item, null, null);
            });
        }
    }

    @Nested
    @DisplayName("getter")
    class Getters {
        @ParameterizedTest
        @DisplayName("returns node item")
        @CsvSource({
                "String",
                "5",
                "5.0"
        })
        public <T> void GetItem_MultipleTypes_ReturnsItem(T item) {
            LinkedNode<T> node = new LinkedNode<>(item, null, null);

            T nodeItem = node.getItem();

            assertEquals(item, nodeItem);
        }

        @Test
        @DisplayName("returns null previous node")
        public void GetPrevious_NullPrevious_ReturnsNull() {
            LinkedNode<String> node = new LinkedNode<>("Node", null, null);

            LinkedNode<String> previous = node.getPrevious();

            assertNull(previous);
        }

        @Test
        @DisplayName("returns previous node")
        public void GetPrevious_HasPrevious_ReturnsPreviousNode() {
            LinkedNode<String> previous = new LinkedNode<>("Previous", null, null);
            LinkedNode<String> node = new LinkedNode<>("Node", previous, null);

            LinkedNode<String> res_previous = node.getPrevious();

            assertEquals(previous, res_previous);
        }

        @Test
        @DisplayName("returns null next node")
        public void GetNext_NullNext_ReturnsNull() {
            LinkedNode<String> node = new LinkedNode<>("Node", null, null);

            LinkedNode<String> next = node.getNext();

            assertNull(next);
        }

        @Test
        @DisplayName("returns next node")
        public void GetNext_HasPrevious_ReturnsPreviousNode() {
            LinkedNode<String> next = new LinkedNode<>("Next", null, null);
            LinkedNode<String> node = new LinkedNode<>("Node", null, next);

            LinkedNode<String> res_next = node.getNext();

            assertEquals(next, res_next);
        }
    }

    @Nested
    @DisplayName("setter")
    class Setters{
        @ParameterizedTest
        @DisplayName("updates node item")
        @CsvSource({
                "Init, New",
                "5, 10",
                "5.0, 10.0"
        })
        public <T> void SetItem_MultipleTypes_UpdatesItem(T initItem, T newItem) {
            LinkedNode<T> node = new LinkedNode<>(initItem, null, null);

            node.setItem(newItem);
            T nodeItem = node.getItem();

            assertEquals(newItem, nodeItem);
        }

        @Test
        @DisplayName("throws exception on null item")
        public void SetItem_NullItem_ThrowsDoubleLinkedListException(){
            String item = null;
            LinkedNode<String> node = new LinkedNode<>("Node", null, null);

            assertThrows(DoubleLinkedQueueException.class, () -> {
                node.setItem(item);
            });
        }

        @Test
        @DisplayName("updates previous to null node")
        public void SetPrevious_NullNode_UpdatesPreviousNode(){
            LinkedNode<Integer> previous = null;
            LinkedNode<Integer> node = new LinkedNode<>(5, null, null);

            node.setPrevious(previous);
            LinkedNode<Integer> res_prev = node.getPrevious();

            assertNull(res_prev);
        }

        @Test
        @DisplayName("updates previous to non-null node")
        public void SetPrevious_NonNullNode_UpdatesPreviousNode(){
            LinkedNode<Integer> previous = new LinkedNode<>(0, null, null);
            LinkedNode<Integer> node = new LinkedNode<>(5, null, null);

            node.setPrevious(previous);
            LinkedNode<Integer> res_prev = node.getPrevious();

            assertEquals(previous,res_prev);
        }

        @Test
        @DisplayName("updates next to null node")
        public void SetNext_NullNode_UpdatesNextNode(){
            LinkedNode<Integer> next = null;
            LinkedNode<Integer> node = new LinkedNode<>(5, null, null);

            node.setPrevious(next);
            LinkedNode<Integer> res_next = node.getNext();

            assertNull(res_next);
        }

        @Test
        @DisplayName("updates next to non-null node")
        public void SetNext_NonNullNode_UpdatesNextNode(){
            LinkedNode<Integer> next = new LinkedNode<>(0, null, null);
            LinkedNode<Integer> node = new LinkedNode<>(5, null, null);

            node.setNext(next);
            LinkedNode<Integer> res_next = node.getNext();

            assertEquals(next,res_next);
        }
    }

    @Test
    @DisplayName("checks if it is a first node")
    public void IsFirstNode_NullPrevious_ReturnsTrue(){

    }

}
