// Authors: Adrián Torremocha Doblas
//          Ezequiel Sánchez García
package org.mps.crossover;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mps.EvolutionaryAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("OnePoint crossover")
public class OnePointCrossoverTest {

    OnePointCrossover opcrossover;

    @BeforeEach
    public void setup() {
        this.opcrossover = new OnePointCrossover();
    }

    @DisplayName("crossover method")
    @Nested
    class Crossover {

        @DisplayName("parent1 null")
        @Test
        public void Crossover_Parent1Null_ThrowsEvolutionaryAlgorithmException() {
            int[] parent1 = null;
            int[] parent2 = new int[]{1, 2, 3, 4};

            try {
                assertThrows(EvolutionaryAlgorithmException.class, () -> {
                    opcrossover.crossover(parent1, parent2);
                });
            } catch (Exception e) {
            }
        }

        @DisplayName("parent2 null")
        @Test
        public void Crossover_Parent2Null_ThrowsEvolutionaryAlgorithmException() {
            int[] parent1 = new int[]{1, 2, 3, 4};
            int[] parent2 = null;

            try {
                assertThrows(EvolutionaryAlgorithmException.class, () -> {
                    opcrossover.crossover(parent1, parent2);
                });
            } catch (Exception e) {
            }
        }

        @DisplayName("parent1 length 0")
        @Test
        public void Crossover_Parent1Length0_ThrowsEvolutionaryAlgorithmException() {
            int[] parent1 = new int[0];
            int[] parent2 = new int[]{1, 2, 3, 4};
            ;

            try {
                assertThrows(EvolutionaryAlgorithmException.class, () -> {
                    opcrossover.crossover(parent1, parent2);
                });
            } catch (Exception e) {
            }
        }

        @DisplayName("parents with different size")
        @Test
        public void Crossover_DifferentSizeParents_ThrowsEvolutionaryAlgorithmException() {
            int[] parent1 = new int[]{1, 2, 3};
            int[] parent2 = new int[]{1, 2, 3, 4};
            ;

            try {
                assertThrows(EvolutionaryAlgorithmException.class, () -> {
                    opcrossover.crossover(parent1, parent2);
                });
            } catch (Exception e) {
            }
        }

        @DisplayName("offspring size is correct")
        @Test
        public void Crossover_ParametersInRange_ReturnsCorrectSizeArray() {
            int[] parent1 = new int[]{1, 2, 3, 4};
            int[] parent2 = new int[]{1, 2, 3, 4};
            ;

            try {
                int[][] offspring = opcrossover.crossover(parent1, parent2);
                boolean res = offspring.length == 2 && offspring[0].length == 4;

                assertTrue(res);
            } catch (Exception e) {
            }
        }

        @DisplayName("shuffles elements in both arrays")
        @Test
        public void Crossover_ParametersInRange_ReturnsShuffledArray() {
            int[] parent1 = new int[]{1, 1, 1, 1};
            int[] parent2 = new int[]{2, 2, 2, 2};
            ;

            try {
                int[][] offspring = opcrossover.crossover(parent1, parent2);

                //Shuffle is correct if we find elements of the first array in the second
                boolean shuffled = false;
                for (int i = 0; i < offspring[0].length; i++) {
                    if (offspring[0][i] == 2) {
                        shuffled = true;
                        break;
                    }
                }

                assertTrue(shuffled);
            } catch (Exception e) {
            }
        }
    }
}
