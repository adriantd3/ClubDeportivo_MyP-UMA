// Authors: Adrián Torremocha Doblas
//          Ezequiel Sánchez García
package org.mps;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mps.crossover.OnePointCrossover;
import org.mps.mutation.SwapMutation;
import org.mps.selection.TournamentSelection;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Evolutionary algorithm")
public class EvolutionaryAlgorithmTest {

    EvolutionaryAlgorithm evolutionaryAlgorithm;

    @Nested
    @DisplayName("when new")
    class Constructor {
        @DisplayName("null crossover operator throws exception")
        @Test
        public void Constructor_CrossoverOperatorNull_ThrowsEvolutionaryAlgorithmException() {
            OnePointCrossover crossover = null;
            SwapMutation mutation = new SwapMutation();
            try {
                TournamentSelection selection = new TournamentSelection(5);

                assertThrows(EvolutionaryAlgorithmException.class, () -> {
                    evolutionaryAlgorithm = new EvolutionaryAlgorithm(selection, mutation, crossover);
                });
            } catch (Exception e) {
            }

        }

        @DisplayName("null mutation operator throws exception")
        @Test
        public void Constructor_MutationOperatorNull_ThrowsEvolutionaryAlgorithmException() {
            OnePointCrossover crossover = new OnePointCrossover();
            SwapMutation mutation = null;
            try {
                TournamentSelection selection = new TournamentSelection(5);

                assertThrows(EvolutionaryAlgorithmException.class, () -> {
                    evolutionaryAlgorithm = new EvolutionaryAlgorithm(selection, mutation, crossover);
                });
            } catch (Exception e) {
            }

        }

        @DisplayName("null selection operator throws exception")
        @Test
        public void Constructor_SelectionOperatorNull_ThrowsEvolutionaryAlgorithmException() {
            OnePointCrossover crossover = new OnePointCrossover();
            SwapMutation mutation = new SwapMutation();
            TournamentSelection selection = null;

            assertThrows(EvolutionaryAlgorithmException.class, () -> {
                evolutionaryAlgorithm = new EvolutionaryAlgorithm(selection, mutation, crossover);
            });
        }

        @DisplayName("Non null parameters creates object")
        @Test
        public void Constructor_NonNullParameters_DoesNotThrowEvolutionaryAlgorithmException() {
            OnePointCrossover crossover = new OnePointCrossover();
            SwapMutation mutation = new SwapMutation();
            try {
                TournamentSelection selection = new TournamentSelection(5);

                assertDoesNotThrow(() -> {
                    evolutionaryAlgorithm = new EvolutionaryAlgorithm(selection, mutation, crossover);
                });
            } catch (Exception e) {
            }
        }
    }

    @Nested
    @DisplayName("optimize method")
    class Optimize {
        @BeforeEach
        public void setup(){
            OnePointCrossover crossover = new OnePointCrossover();
            SwapMutation mutation = new SwapMutation();
            try {
                TournamentSelection selection = new TournamentSelection(5);

                assertDoesNotThrow(() -> {
                    evolutionaryAlgorithm = new EvolutionaryAlgorithm(selection, mutation, crossover);
                });
            } catch (Exception e) {
            }
        }

        @DisplayName("null population throws exception")
        @Test
        public void Optimize_NullPopulation_ThrowsEvolutionaryAlgorithmException() {
            int [][] population = null;

            assertThrows(EvolutionaryAlgorithmException.class, () ->{
               evolutionaryAlgorithm.optimize(population);
            });
        }

        @DisplayName("empty population throws exception")
        @Test
        public void Optimize_EmptyPopulation_ThrowsEvolutionaryAlgorithmException(){
            int [][] population = new int[0][0];

            assertThrows(EvolutionaryAlgorithmException.class, () ->{
                evolutionaryAlgorithm.optimize(population);
            });
        }

        @DisplayName("null population subarray throws exception")
        @Test
        public void Optimize_NullPopulationSubarray_ThrowsEvolutionaryAlgorithmException(){
            int [][] population = new int[1][];

            assertThrows(EvolutionaryAlgorithmException.class, () ->{
                evolutionaryAlgorithm.optimize(population);
            });
        }

        @DisplayName("empty population subarray throws exception")
        @Test
        public void Optimize_EmptyPopulationSubarray_ThrowsEvolutionaryAlgorithmException(){
            int [][] population = new int[1][0];

            assertThrows(EvolutionaryAlgorithmException.class, () ->{
                evolutionaryAlgorithm.optimize(population);
            });
        }

        @DisplayName("valid parameters do not alter size")
        @Test
        public void Optimize_ValidParameters_DoNotAlterSize() throws EvolutionaryAlgorithmException {
            int [][] population = new int[][] {{7,7,7,7},{8,8,8,8}};
            int expected_columns = population.length;
            int expected_rows = population[0].length;

            population = evolutionaryAlgorithm.optimize(population);
            int res_columns = population.length;
            int res_rows = population[0].length;

            assertEquals(expected_columns, res_columns);
            assertEquals(expected_rows, res_rows);
        }

        @DisplayName("valid parameters return altered population")
        @Test
        public void Optimize_InRangeParameters_AltersPopulation() throws EvolutionaryAlgorithmException {
            int [][] population = new int[][] {{7,7,7,7},{8,8,8,8}};

            population = evolutionaryAlgorithm.optimize(population);

            boolean hasChanged = false;
            for (int i = 0; i < population[0].length; i++){
                if (population[0][i] != 7 || population[1][i] != 8) {
                    hasChanged = true;
                    break;
                }
            }
            assertTrue(hasChanged);
        }
    }
    @Nested
    @DisplayName("set methods")
    class Setters{

        @BeforeEach
        public void setup(){
            OnePointCrossover crossover = new OnePointCrossover();
            SwapMutation mutation = new SwapMutation();
            try {
                TournamentSelection selection = new TournamentSelection(5);

                assertDoesNotThrow(() -> {
                    evolutionaryAlgorithm = new EvolutionaryAlgorithm(selection, mutation, crossover);
                });
            } catch (Exception e) {
            }
        }

        @DisplayName("null selection operator")
        @Test
        public void SetSelection_NullSelectionOperator_ThrowsEvolutionaryAlgorithmException() {
            TournamentSelection selection = null;

            assertThrows(EvolutionaryAlgorithmException.class, () -> {
                evolutionaryAlgorithm.setSelectionOperator(selection);
            });
        }

        @DisplayName("non null selection operator")
        @Test
        public void SetSelection_NonNullSelectionOperator_UpdatesOperator() throws EvolutionaryAlgorithmException {
            TournamentSelection selection = new TournamentSelection(10);

            evolutionaryAlgorithm.setSelectionOperator(selection);

            assertEquals(selection,evolutionaryAlgorithm.getSelectionOperator());
        }

        @DisplayName("null mutation operator")
        @Test
        public void SetMutation_NullMutationOperator_ThrowsEvolutionaryAlgorithmException() {
            SwapMutation mutation = null;

            assertThrows(EvolutionaryAlgorithmException.class, () -> {
                evolutionaryAlgorithm.setMutationOperator(mutation);
            });
        }

        @DisplayName("non null mutation operator")
        @Test
        public void SetMutation_NonNullMutationOperator_UpdatesOperator() throws EvolutionaryAlgorithmException {
            SwapMutation mutation = new SwapMutation();

            evolutionaryAlgorithm.setMutationOperator(mutation);

            assertEquals(mutation,evolutionaryAlgorithm.getMutationOperator());
        }

        @DisplayName("null crossover operator")
        @Test
        public void SetCrossover_NullCrossoverOperator_ThrowsEvolutionaryAlgorithmException() {
            OnePointCrossover crossover = null;

            assertThrows(EvolutionaryAlgorithmException.class, () -> {
                evolutionaryAlgorithm.setCrossoverOperator(crossover);
            });
        }

        @DisplayName("non null crossover operator")
        @Test
        public void SetCrossover_NonNullCrossoverOperator_UpdatesOperator() throws EvolutionaryAlgorithmException {
            OnePointCrossover crossover = new OnePointCrossover();

            evolutionaryAlgorithm.setCrossoverOperator(crossover);

            assertEquals(crossover,evolutionaryAlgorithm.getCrossoverOperator());
        }
    }

}
