package org.mps.boundedqueue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@DisplayName("An ArrayBoundedQueue")
public class ArrayBoundedQueueTest {

    @Nested
    @DisplayName("When new")
    class Constructor {

        @DisplayName("negative capacity throws exception")
        @Test
        public void Constructor_NegativeCapacity_ThrowsIllegalArgumentException() {
            int capacity = -1;

            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
                new ArrayBoundedQueue(capacity);
            });
        }

        @DisplayName("zero capacity throws exception")
        @Test
        public void Constructor_ZeroCapacity_ThrowsIllegalArgumentException() {
            int capacity = 0;

            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
                new ArrayBoundedQueue(capacity);
            });
        }

        @DisplayName("positive capacity creates empty object")
        @Test
        public void Constructor_PositiveCapacity_CreatesEmptyObject() {
            int capacity = 5;
            ArrayBoundedQueue queue = new ArrayBoundedQueue(capacity);

            assertThat(queue.getFirst()).isEqualTo(0);
            assertThat(queue.getLast()).isEqualTo(0);
            assertThat(queue.isEmpty()).isTrue();
        }
    }

    @Nested
    @DisplayName("get method")
    class Get {

        @DisplayName("on empty queue throws exception")
        @Test
        public void Get_EmptyQueue_ThrowsEmptyBoundedQueueException() {
            ArrayBoundedQueue queue = new ArrayBoundedQueue(3);

            assertThatExceptionOfType(EmptyBoundedQueueException.class).isThrownBy(() -> {
                queue.get();
            });
        }

        @DisplayName("decreases queue size")
        @Test
        public void Get_OnRangeParameters_DecreasesQueueSize() {
            ArrayBoundedQueue queue = new ArrayBoundedQueue(3);
            queue.put(1);
            queue.put(2);
            queue.put(3);
            int expected_size = 2;

            queue.get();
            int actual_size = queue.size();

            assertThat(actual_size).isEqualTo(expected_size);
        }

        @DisplayName("returns first element on queue")
        @Test
        public void Get_OnRangeParameters_ReturnsFirstElement() {
            ArrayBoundedQueue queue = new ArrayBoundedQueue(3);
            queue.put(1);
            queue.put(2);
            queue.put(3);
            int expected_element = 1;

            int actual_element = (int) queue.get();

            assertThat(actual_element).isEqualTo(expected_element);
        }

        @DisplayName("updates first element index")
        @Test
        public void Get_OnRangeParameters_UpdatesFirstReference(){
            ArrayBoundedQueue queue = new ArrayBoundedQueue(3);
            queue.put(1);
            queue.put(2);
            int expected_first = 1;

            queue.get();
            int actual_first = queue.getFirst();

            assertThat(actual_first).isEqualTo(expected_first);
        }

        @DisplayName("mantains first element in a circular way")
        @Test
        public void Get_CircularSlice_RestoresGetFirsttoIndex0() {
            ArrayBoundedQueue queue = new ArrayBoundedQueue(3);
            int expected_prevFirst = 0;
            int actual_prevFirst = queue.getFirst();
            queue.put(1);
            queue.put(2);
            queue.put(3);
            queue.get();
            queue.get();
            queue.get();

            int expected_Afterfirst = 0;
            int actual_Afterfirst = queue.getFirst();

            assertThat(actual_prevFirst).isEqualTo(expected_prevFirst);
            assertThat(actual_Afterfirst).isEqualTo(expected_Afterfirst);
        }

    }

    @Nested
    @DisplayName("put method")
    class Put {

        @Test
        @DisplayName("on full queue throws exception")
        public void Put_FullQueue_ThrowsFullBoundedQueueException() {
            ArrayBoundedQueue queue = new ArrayBoundedQueue(1);
            queue.put(1);

            assertThatExceptionOfType(FullBoundedQueueException.class).isThrownBy(() -> {
                queue.put(2);
            });
        }

        @Test
        @DisplayName("null value throws exception")
        public void Put_NullValue_ThrowsIllegalArgumentException() {
            ArrayBoundedQueue queue = new ArrayBoundedQueue(1);

            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
                queue.put(null);
            });
        }

        @Test
        @DisplayName("updates last element index")
        public void Put_OnRangeParameters_UpdatesLastReference(){
            ArrayBoundedQueue queue = new ArrayBoundedQueue(3);
            queue.put(1);
            int expected_last = 2;

            queue.put(2);
            int actual_last = queue.getLast();

            assertThat(actual_last).isEqualTo(expected_last);
        }

        @Test
        @DisplayName("increases queue size")
        public void Put_OnRangeParameters_IncreasesQueueSize(){
            ArrayBoundedQueue queue = new ArrayBoundedQueue(3);
            int expected_size = 1;

            queue.put(1);
            int actual_size = queue.size();

            assertThat(actual_size).isEqualTo(expected_size);
        }

        @Test
        @DisplayName("maintains main positions in a circular way")
        public void Put_OnRangeParameters_UpdatesFirstAndLastPositionProperly(){
            ArrayBoundedQueue queue = new ArrayBoundedQueue(3);
            queue.put(1);
            queue.put(2);
            queue.put(3);
            queue.get();
            queue.get();
            int expected_last = 1;
            int expected_first = 2;

            queue.put(4);
            int actual_last = queue.getLast();
            int actual_first = queue.getFirst();

            assertThat(actual_first).isEqualTo(expected_first);
            assertThat(actual_last).isEqualTo(expected_last);

        }

    }

    @Nested
    @DisplayName("getters")
    class Getters{

        @DisplayName("get first")
        @Test
        public void GetFirst_OnRangeParameters_ReturnsFirst(){
            ArrayBoundedQueue queue = new ArrayBoundedQueue(3);
            int expected_first = 0;

            int actual_first = queue.getFirst();

            assertThat(actual_first).isEqualTo(expected_first);
        }

        @DisplayName("get last")
        @Test
        public void GetLast_OnRangeParameters_ReturnsLast(){
            ArrayBoundedQueue queue = new ArrayBoundedQueue(3);
            queue.put(1);
            queue.put(2);
            int expected_last = 2;

            int actual_last = queue.getLast();

            assertThat(actual_last).isEqualTo(expected_last);
        }

        @DisplayName("get size")
        @Test
        public void GetSize_OnRangeParameters_ReturnsQueueSize(){
            ArrayBoundedQueue queue = new ArrayBoundedQueue(3);
            queue.put(1);
            queue.put(2);
            int expected_size = 2;

            int actual_size = queue.size();

            assertThat(actual_size).isEqualTo(expected_size);
        }
    }

    @Nested
    @DisplayName("boolean methods")
    class BooleanMethods{

        @Test
        @DisplayName("is full on a full queue returns true")
        public void IsFull_FullQueue_ReturnsTrue(){
            ArrayBoundedQueue queue = new ArrayBoundedQueue(2);
            queue.put(1);
            queue.put(2);

            boolean full = queue.isFull();

            assertThat(full).isTrue();
        }

        @Test
        @DisplayName("is full on a non-full queue returns false")
        public void IsFull_NonFullQueue_ReturnsFalse(){
            ArrayBoundedQueue queue = new ArrayBoundedQueue(3);
            queue.put(1);
            queue.put(2);

            boolean full = queue.isFull();

            assertThat(full).isFalse();
        }

        @Test
        @DisplayName("is empty on an empty queue returns true")
        public void IsEmpty_EmptyQueue_ReturnsTrue(){
            ArrayBoundedQueue queue = new ArrayBoundedQueue(2);

            boolean empty = queue.isEmpty();

            assertThat(empty).isTrue();
        }

        @Test
        @DisplayName("is empty on a non-empty queue returns false")
        public void IsEmpty_NonEmptyQueue_ReturnsFalse(){
            ArrayBoundedQueue queue = new ArrayBoundedQueue(2);
            queue.put(1);

            boolean empty = queue.isEmpty();

            assertThat(empty).isFalse();
        }
    }

    @Nested
    @DisplayName("iterator")
    class Iterator {

        @Test
        @DisplayName("has next on an empty queue returns false")
        public void HasNext_EmptyQueue_ReturnsFalse(){
            ArrayBoundedQueue queue = new ArrayBoundedQueue(3);
            java.util.Iterator iterator = queue.iterator();

            boolean hasNext = iterator.hasNext();

            assertThat(hasNext).isFalse();
        }

        @Test
        @DisplayName("has next on a non-empty queue returns true")
        public void HasNext_NonEmptyQueue_ReturnsTrue(){
            ArrayBoundedQueue queue = new ArrayBoundedQueue(3);
            queue.put(1);
            queue.put(2);
            java.util.Iterator iterator = queue.iterator();

            boolean hasNext = iterator.hasNext();

            assertThat(hasNext).isTrue();
        }

        @Test
        @DisplayName("next when there is not next throws exception")
        public void Next_ThereIsNotNext_ThrowsNoSuchElementException(){
            ArrayBoundedQueue queue = new ArrayBoundedQueue(1);
            queue.put(1);
            java.util.Iterator iterator = queue.iterator();
            iterator.next();

            assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {
                iterator.next();
            });
        }

        @Test
        @DisplayName("next returns next element")
        public void Next_ExistingNext_ReturnsNextElement(){
            ArrayBoundedQueue queue = new ArrayBoundedQueue(1);
            queue.put(1);
            java.util.Iterator iterator = queue.iterator();
            int expected_next = 1;

            int next = (int) iterator.next();

            assertThat(next).isEqualTo(expected_next);

        }

        @Test
        @DisplayName("iterating over the list")
        public void Iterator_IteratesTheQueue(){
            ArrayBoundedQueue queue = new ArrayBoundedQueue(3);
            queue.put(1);
            queue.put(2);
            queue.put(3);
            java.util.Iterator iterator = queue.iterator();

            int i = 1;
            while (iterator.hasNext()) {
                int elem = (int) iterator.next();
                assertThat(elem).isEqualTo(i);
                i++;
            }
        }
    }
}
