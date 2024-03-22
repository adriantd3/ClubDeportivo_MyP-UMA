// Auhtors: Adrián Torremocha Doblas
//          Ezequiel Sánchez García
package clubdeportivo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class GrupoTest {

    @ParameterizedTest
    @CsvSource({
            ",Pilates,8,5,50.0",
            "4568,,8,5,50.0"
    })
    public void Grupo_StringVacio_LanzaClubException(String codigo, String actividad, int nplazas, int matriculados, double tarifa) {
        assertThrows(ClubException.class, () ->
                new Grupo(codigo, actividad, nplazas, matriculados, tarifa));
    }

    @ParameterizedTest
    @CsvSource({
            "456B,Pilates,-8,5,50.0",
            "456B,Pilates,8,-5,50.0",
            "456B,Pilates,8,5,-50.0"
    })
    public void Grupo_ValoresNegativos_LanzaClubException(String codigo, String actividad, int nplazas, int matriculados, double tarifa) {
        assertThrows(ClubException.class, () ->
                new Grupo(codigo, actividad, nplazas, matriculados, tarifa));
    }

    @Test
    public void Grupo_MatriculasMayorQuePlazas_LanzaClubException() {
        int nplazas = 10;
        int matriculas = 15;

        assertThrows(ClubException.class, () ->
                new Grupo("456B", "Pilates", nplazas, matriculas, 50.0));
    }

    @Test
    public void Grupo_ParametrosEnRango_CreaGrupo() {
        String codigo = "456B";
        String actividad = "Pilates";
        int nplazas = 8;
        int matriculados = 5;
        double tarifa = 50.0;

        try {
            Grupo grupo = new Grupo(codigo, actividad, nplazas, matriculados, tarifa);

            assertEquals(codigo, grupo.getCodigo());
            assertEquals(actividad, grupo.getActividad());
            assertEquals(nplazas, grupo.getPlazas());
            assertEquals(matriculados, grupo.getMatriculados());
            assertEquals(tarifa, grupo.getTarifa());
        } catch (ClubException e) {

        }
    }

    @Test
    public void ActualizarPlazas_NumeroNegativo_LanzaClubException() {
        int nPlazas = -10;
        try {
            Grupo grupo;
            grupo = new Grupo("456B", "Pilates", 15, 10, 50.0);

            assertThrows(ClubException.class, () ->
                    grupo.actualizarPlazas(nPlazas));
        } catch (ClubException e) {

        }
    }

    @Test
    public void ActualizarPlazas_NumeroMenorQueMatriculas_LanzaClubException() {
        int nPLazas = 5;
        try {
            Grupo grupo;
            grupo = new Grupo("456B", "Pilates", 15, 10, 50.0);

            grupo.actualizarPlazas(nPLazas);

            assertThrows(ClubException.class, () ->
                    grupo.actualizarPlazas(nPLazas));
        } catch (ClubException e) {

        }
    }

    @Test
    public void ActualizarPlazas_ParametroEnRango_ActualizaPlazas() {
        int nPlazas = 20;
        try {
            Grupo grupo;
            grupo = new Grupo("456B", "Pilates", 15, 10, 50.0);

            grupo.actualizarPlazas(nPlazas);

            assertEquals(nPlazas, grupo.getPlazas());
        } catch (ClubException e) {

        }
    }

    @Test
    public void Matricular_NumeroNegativo_LanzaClubException() {
        int nMatriculas = -10;
        try {
            Grupo grupo;
            grupo = new Grupo("456B", "Pilates", 15, 10, 50.0);

            assertThrows(ClubException.class, () ->
                    grupo.matricular(nMatriculas));
        } catch (ClubException e) {

        }
    }

    @Test
    public void Matricular_PlazasInsuficientes_LanzaClubException() {
        int nMatriculas = 10;
        try {
            Grupo grupo;
            grupo = new Grupo("456B", "Pilates", 15, 10, 50.0);

            assertThrows(ClubException.class, () ->
                    grupo.matricular(nMatriculas));
        } catch (ClubException e) {

        }
    }

    @Test
    public void Matricular_ParametroEnRango_IncrementaNumMatriculados() {
        int nuevasMatriculas = 5;
        int matriculas = 10;
        try {
            Grupo grupo;
            grupo = new Grupo("456B", "Pilates", 15, matriculas, 50.0);

            grupo.matricular(nuevasMatriculas);

            assertEquals(matriculas + nuevasMatriculas, grupo.getMatriculados());
        } catch (ClubException e) {

        }
    }

    @Test
    public void ToString_ParametrosEnRango_ReturnsCadenaCorrecta() {
        String codigo = "456B";
        String actividad = "Pilates";
        int nplazas = 8;
        int matriculados = 5;
        double tarifa = 50.0;
        Grupo grupo;

        try {
            String cadenaRes = "(" + codigo + " - " + actividad + " - " + tarifa + " euros " + "- P:" + nplazas + " - M:" + matriculados + ")";
            grupo = new Grupo(codigo, actividad, nplazas, matriculados, tarifa);

            assertEquals(cadenaRes, grupo.toString());
        } catch (ClubException e) {

        }
    }

    @Test
    public void Equals_ObjetoNulo_ReturnsFalse() {
        try {
            Grupo grupo = new Grupo("456B", "Pilates", 15, 10, 50.0);
            Grupo grupoNull = null;

            assertFalse(grupo.equals(grupoNull));
        } catch (ClubException e) {

        }
    }

    @ParameterizedTest
    @CsvSource({
            "678H,Pilates",
            "456B,Futbol"
    })
    public void Equals_DistintasClaves_ReturnsFalse(String codigo, String actividad) {
        try {
            Grupo grupoTest = new Grupo(codigo, actividad, 15, 10, 50.0);
            Grupo grupo = new Grupo("456B", "Pilates", 15, 10, 50.0);

            assertFalse(grupo.equals(grupoTest));
        } catch (ClubException e) {

        }
    }

    @Test
    public void Equals_MismasClaves_ReturnsTrue() {
        String codigo = "456B";
        String actividad = "Pilates";

        try {
            Grupo grupoTest = new Grupo(codigo, actividad, 15, 10, 50.0);
            Grupo grupo = new Grupo(codigo, actividad, 15, 10, 50.0);

            assertTrue(grupo.equals(grupoTest));
        } catch (ClubException e) {

        }
    }

    @ParameterizedTest
    @CsvSource({
            "678H,Pilates",
            "456B,Futbol"
    })
    public void HashCode_DistintasClaves_ReturnsValoresDistintos(String codigo, String actividad) {
        try {
            Grupo grupoTest = new Grupo(codigo, actividad, 15, 10, 50.0);
            Grupo grupo = new Grupo("456B", "Pilates", 15, 10, 50.0);

            assertNotEquals(grupo.hashCode(), grupoTest.hashCode());
        } catch (ClubException e) {

        }
    }

    @Test
    public void HashCode_MismasClaves_ReturnsMismosValores() {
        String codigo = "456B";
        String actividad = "Pilates";

        try {
            Grupo grupoTest = new Grupo(codigo, actividad, 15, 10, 50.0);
            Grupo grupo = new Grupo(codigo, actividad, 15, 10, 50.0);

            assertEquals(grupo.hashCode(), grupoTest.hashCode());
        } catch (ClubException e) {

        }
    }

}
