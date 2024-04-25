package org.mps.boundedqueue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
}
