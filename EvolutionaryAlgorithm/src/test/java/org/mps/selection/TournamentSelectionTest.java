package org.mps.selection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mps.EvolutionaryAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Tournament Selection")
public class TournamentSelectionTest {

    @Nested
    @DisplayName("constructor")
    class Constructor {
        @Test
        @DisplayName("invalid tournament size")
        public void TournamentSelection_InvalidSize_ThrowsEvolutionaryAlgorithmException(){
            int tournament_size = 0;
            assertThrows(EvolutionaryAlgorithmException.class, ()->{
                org.mps.selection.TournamentSelection tournamentSelection = new org.mps.selection.TournamentSelection(tournament_size);
            });
        }
    }

    @Nested
    @DisplayName("select method")
    class Select {
        @Test
        @DisplayName("null array throws exception")
        public void Select_NullArray_ThrowsEvolutionaryAlgorithmException() throws EvolutionaryAlgorithmException {
            org.mps.selection.TournamentSelection tournamentSelection = new org.mps.selection.TournamentSelection(2);
            int [] population = null;

            assertThrows(EvolutionaryAlgorithmException.class, ()->{
                int [] res = tournamentSelection.select(population);
            });
        }

        @Test
        @DisplayName("empty array throws exception")
        public void Select_EmptyArray_ThrowsEvolutionaryAlgorithmException() throws EvolutionaryAlgorithmException {
            org.mps.selection.TournamentSelection tournamentSelection = new org.mps.selection.TournamentSelection(2);
            int [] population = new int[] {};

            assertThrows(EvolutionaryAlgorithmException.class, ()->{
                int [] res = tournamentSelection.select(population);
            });
        }

        @Test
        @DisplayName("valid array does not change array size")
        public void Select_ValidArray_ArraySizeIsNotChanged() throws EvolutionaryAlgorithmException {
            org.mps.selection.TournamentSelection tournamentSelection = new org.mps.selection.TournamentSelection(2);
            int [] population = new int[] {1,2,3,4};
            int expected_length = 4;

            int res = tournamentSelection.select(population).length;

            assertEquals(expected_length, res);
        }



    }
}