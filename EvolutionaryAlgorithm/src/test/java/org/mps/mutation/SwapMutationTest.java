package org.mps.mutation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mps.EvolutionaryAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Swap mutation")
public class SwapMutationTest {

    @Nested
    @DisplayName("mutate method")
    class SwapMutation {
        @Test
        @DisplayName("null array throws exception")
        public void Mutate_NullArray_ThrowsEvolutionaryAlgorithmException(){
            org.mps.mutation.SwapMutation swapMutation = new org.mps.mutation.SwapMutation();
            int [] individual = null;

            assertThrows(EvolutionaryAlgorithmException.class, ()->{
                int [] res = swapMutation.mutate(individual);
            });
        }

        @Test
        @DisplayName("empty array throws exception")
        public void Mutate_EmptyArray_ThrowsEvolutionaryAlgorithmException(){
            org.mps.mutation.SwapMutation swapMutation = new org.mps.mutation.SwapMutation();
            int [] individual = new int[] {};

            assertThrows(EvolutionaryAlgorithmException.class, ()->{
                int [] res = swapMutation.mutate(individual);
            });
        }

        @Test
        @DisplayName("valid array does not change array size")
        public void Mutate_ValidArray_ArraySizeIsNotChanged() throws EvolutionaryAlgorithmException {
            org.mps.mutation.SwapMutation swapMutation = new org.mps.mutation.SwapMutation();
            int [] individual = new int[] {1,2,3,4};
            int expected_length = 4;

            int [] mutated = swapMutation.mutate(individual);

            int res = mutated.length;

            assertEquals(expected_length,res);
        }



    }

}
