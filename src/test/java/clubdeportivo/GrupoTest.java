package clubdeportivo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GrupoTest {

    @ParameterizedTest
    @CsvSource({
            ",Pilates,8,5,50.0",
            "4568,,8,5,50.0"
    })
    public void Grupo_StringVacio_ReturnsClubException(String codigo, String actividad, int nplazas, int matriculados, double tarifa) {
        assertThrows(ClubException.class, () ->
                new Grupo(codigo, actividad, nplazas, matriculados, tarifa));
    }

    @ParameterizedTest
    @CsvSource({
            "456B,Pilates,-8,5,50.0",
            "456B,Pilates,8,-5,50.0",
            "456B,Pilates,8,5,-50.0"
    })
    public void Grupo_ValoresNegativos_ReturnsClubException(String codigo, String actividad, int nplazas, int matriculados, double tarifa) {
        assertThrows(ClubException.class, () ->
                new Grupo(codigo, actividad, nplazas, matriculados, tarifa));
    }

    @Test
    public void Grupo_MatriculasMayorQuePlazas_ReturnsClubException() {
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
    public void ActualizarPlazas_NumeroNegativo_ReturnsClubException() {
        int nPlazas = -10;
        Grupo grupo;
        try {
            grupo = new Grupo("456B", "Pilates", 15, 10, 50.0);

            assertThrows(ClubException.class, () ->
                    grupo.actualizarPlazas(nPlazas));
        } catch (ClubException e) {

        }
    }

    @Test
    public void ActualizarPlazas_NumeroMenorQueMatriculas_ReturnsClubException() {
        int nPLazas = 5;
        Grupo grupo;
        try {
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
        Grupo grupo;
        try {
            grupo = new Grupo("456B", "Pilates", 15, 10, 50.0);

            grupo.actualizarPlazas(nPlazas);

            assertEquals(nPlazas, grupo.getPlazas());
        } catch (ClubException e) {

        }
    }

    @Test
    public void Matricular_NumeroNegativo_ReturnsClubException() {
        int nMatriculas = -10;
        Grupo grupo;
        try {
            grupo = new Grupo("456B", "Pilates", 15, 10, 50.0);

            assertThrows(ClubException.class, () ->
                    grupo.matricular(nMatriculas));
        } catch (ClubException e) {

        }
    }

    @Test
    public void Matricular_PlazasInsuficientes_ReturnsClubException() {
        int nMatriculas = 10;
        Grupo grupo;
        try {
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
        Grupo grupo;
        try {
            grupo = new Grupo("456B", "Pilates", 15, matriculas, 50.0);

            grupo.matricular(nuevasMatriculas);

            assertEquals(matriculas + nuevasMatriculas, grupo.getMatriculados());
        } catch (ClubException e) {

        }
    }

    @Test
    public void ToString_ParametrosEnRango_ReturnsCadenaCorrecta(){
        String codigo = "456B";
        String actividad = "Pilates";
        int nplazas = 8;
        int matriculados = 5;
        double tarifa = 50.0;

        String cadenaRes =  "("+ codigo + " - "+actividad+" - " + tarifa + " euros "+ "- P:" + nplazas +" - M:" +matriculados+")";

        Grupo grupo;
        try{
            grupo = new Grupo("456B", "Pilates", 15, matriculados, 50.0);

            assertEquals(cadenaRes,grupo.toString());
        }catch (ClubException e){

        }
    }
    
}
