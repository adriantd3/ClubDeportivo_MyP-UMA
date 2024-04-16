package org.mps;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mps.crossover.OnePointCrossover;
import org.mps.mutation.SwapMutation;
import org.mps.selection.TournamentSelection;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Evolutionary algorithm")
public class EvolutionaryAlgorithmTest {

    EvolutionaryAlgorithm evolutionaryAlgorithm;

    @Nested
    @DisplayName("when new")
    class Constructor {
        @DisplayName("crossover operator null")
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

        @DisplayName("mutation operator null")
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

        @DisplayName("selection operator null")
        @Test
        public void Constructor_SelectionOperatorNull_ThrowsEvolutionaryAlgorithmException() {
            OnePointCrossover crossover = new OnePointCrossover();
            SwapMutation mutation = new SwapMutation();
            TournamentSelection selection = null;

            assertThrows(EvolutionaryAlgorithmException.class, () -> {
                evolutionaryAlgorithm = new EvolutionaryAlgorithm(selection, mutation, crossover);
            });
        }

        @DisplayName("Non null parameters")
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
}
