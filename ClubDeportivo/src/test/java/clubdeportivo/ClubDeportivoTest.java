// Auhtors: Adrián Torremocha Doblas
//          Ezequiel Sánchez García
package clubdeportivo;

import clubdeportivo.ClubDeportivo;
import clubdeportivo.ClubException;
import clubdeportivo.Grupo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class ClubDeportivoTest {


    @ParameterizedTest
    @CsvSource({
            "UMA,0",
            "1Ma,-1",
    })
    public void ClubDeportivo_NumeroNegativoGrupos_LanzaClubException(String nombre, int numero_grupos) {

        assertThrows(ClubException.class, () -> {
            new ClubDeportivo(nombre, numero_grupos);
        });

    }

    @Test
    public void ClubDeportivo_NombreNulo_LanzaClubException() {
        assertThrows(ClubException.class, () -> {
            new ClubDeportivo(null);
        });
    }

    @Test
    public void ClubDeportivo_PasoNombre_NoLanzaClubException() {
        String nombre = "UMA";
        assertDoesNotThrow(() -> {
            new ClubDeportivo(nombre);
        });

    }

    @Test
    public void AnyadirActividad_GrupoNulo_LanzaClubException() {

        try {
            ClubDeportivo club = new ClubDeportivo("UMA", 1);
            Grupo zumba = null;

            assertThrows(ClubException.class, () -> {
                club.anyadirActividad(zumba);
            });

        } catch (ClubException e) {

        }
    }

    @Test
    public void AnyadirActividad_ArrayStringCorrecto_NoLanzaClubException() {
        try {
            ClubDeportivo club = new ClubDeportivo("UMA", 2);
            String[] grupo = {"756B", "Comba", "20", "12", "30.0"};

            assertDoesNotThrow(() -> {
                club.anyadirActividad(grupo);
            });
        } catch (ClubException e) {

        }
    }

    @Test
    public void AnyadirActividadGrupo_CapacidadClubCompleta_LanzaClubException() {
        try {
            int capacidad = 1;
            ClubDeportivo club = new ClubDeportivo("UMA", capacidad);
            Grupo zumba = new Grupo("11AB", "Zumba", 10, 5, 25.0);
            club.anyadirActividad(zumba);
            Grupo gimnasio = new Grupo("22XX", "Gimnasio", 20, 12, 30.0);

            assertThrows(ClubException.class, () -> {
                club.anyadirActividad(gimnasio);
            });

        } catch (ClubException e) {

        }
    }

    @Test
    public void AnyadirActividadString_CapacidadClubCompleta_LanzaClubException() {
        try {
            int capacidad = 1;
            ClubDeportivo club = new ClubDeportivo("UMA", capacidad);
            Grupo zumba = new Grupo("11AB", "Zumba", 10, 5, 25.0);
            club.anyadirActividad(zumba);
            String[] datos = new String[]{"223B", "Baloncesto", "10", "5", "50.0"};

            assertThrows(ClubException.class, () -> {
                club.anyadirActividad(datos);
            });

        } catch (ClubException e) {

        }
    }

    @Test
    public void AnyadirActividad_GrupoExistente_CambiaNumeroPlazas() {
        try {
            ClubDeportivo club = new ClubDeportivo("UMA", 2);
            Grupo zumba = new Grupo("11AB", "Zumba", 10, 5, 25.0);
            club.anyadirActividad(zumba);
            Grupo zumba2 = new Grupo("11AB", "Zumba", 20, 5, 25.0);

            club.anyadirActividad(zumba2);
            int plazas_esperadas = 20;
            int plazas_devueltas = zumba.getPlazas();

            assertEquals(plazas_esperadas, plazas_devueltas);

        } catch (ClubException e) {

        }
    }

    @ParameterizedTest
    @CsvSource({
            "123A,Kizomba,True,-10,25.0",
            "22HG,Zumba,10,False,-25.0",
            "78JK,Gimnasio,21,0,False",
            "78JK,Gimnasio,21.5,2,25.0",
            "78JK,Gimnasio,21,3.5,False"

    })
    public void AnyadirActividad_GrupoValoresIncorrectos_LanzaClubException(String codigo, String actividad,
                                                                            String nplazas, String matriculados,
                                                                            String tarifa) {
        try {
            ClubDeportivo club = new ClubDeportivo("UMA", 2);
            String[] datos = {codigo, actividad, nplazas, matriculados, tarifa};

            assertThrows(ClubException.class, () -> {
                club.anyadirActividad(datos);
            });

        } catch (ClubException e) {

        }
    }

    @Test
    public void AnyadirActividad_GrupoCodigoNulo_LanzaClubException() {
        try {
            ClubDeportivo club = new ClubDeportivo("UMA", 2);
            String[] datos = {null, "Gimnasio", "10", "6", "25.0"};

            assertThrows(ClubException.class, () -> {
                club.anyadirActividad(datos);
            });

        } catch (ClubException e) {

        }
    }

    @Test
    public void AnyadirActividad_GrupoActividadNulo_LanzaClubException() {
        try {
            ClubDeportivo club = new ClubDeportivo("UMA", 2);
            String[] datos = {"111A", null, "10", "6", "25.0"};

            assertThrows(ClubException.class, () -> {
                club.anyadirActividad(datos);
            });

        } catch (ClubException e) {

        }
    }


    @Test
    public void PlazasLibres_ActividadPerteneciente_DevuelvePlazasLibresActividad() {
        try {
            ClubDeportivo club = new ClubDeportivo("UMA", 1);
            String actividad = "Zumba";
            Grupo zumba = new Grupo("11AB", actividad, 8, 6, 100.0);
            club.anyadirActividad(zumba);

            int plazas_libres_esperadas = 2;
            int plazas_libres_devueltas = club.plazasLibres("Zumba");

            assertEquals(plazas_libres_esperadas, plazas_libres_devueltas);
        } catch (ClubException e) {

        }
    }

    @Test
    public void PlazasLibres_ActividadNoPerteneciente_DevuelveCero() {
        try {
            ClubDeportivo club = new ClubDeportivo("UMA", 1);
            Grupo zumba = new Grupo("22CD", "Zumba", 10, 2, 50.0);
            club.anyadirActividad(zumba);
            String actividad = "Gimnasio";
            Grupo gimnasio = new Grupo("11AB", actividad, 8, 6, 100.0);

            int plazas_libres_esperadas = 0;
            int plazas_libres_devueltas = club.plazasLibres(actividad);

            assertEquals(plazas_libres_esperadas, plazas_libres_devueltas);
        } catch (ClubException e) {

        }
    }

    @Test
    public void PlazasLibres_ActividadNula_LanzaClubException() {
        try {
            ClubDeportivo club = new ClubDeportivo("UMA", 1);

            assertThrows(ClubException.class, () -> {
                int p = club.plazasLibres(null);
            });

        } catch (ClubException e) {

        }
    }

    @Test
    public void Matricular_MasPersonasQuePlazas_LanzaClubException() {
        try {
            ClubDeportivo club = new ClubDeportivo("UMA", 2);
            String actividad = "Gimnasio";
            Grupo gimnasio = new Grupo("11AB", actividad, 8, 6, 100.0);
            club.anyadirActividad(gimnasio);

            assertThrows(ClubException.class, () -> {
                club.matricular(actividad, 10);
            });

        } catch (ClubException e) {

        }
    }

    @Test
    public void Matricular_GrupoPersonas_GruposActualizasNumeroPlazasLibres() {
        try {
            ClubDeportivo club = new ClubDeportivo("UMA", 4);
            String actividad = "Gimnasio";
            Grupo gimnasio1 = new Grupo("11AB", actividad, 8, 6, 100.0);
            Grupo baile = new Grupo("33FG", "Baile", 10, 3, 30.0);
            Grupo gimnasio2 = new Grupo("22CD", actividad, 10, 5, 20.0);
            club.anyadirActividad(baile);
            club.anyadirActividad(gimnasio1);
            club.anyadirActividad(gimnasio2);

            club.matricular(actividad, 5);

            int plazasLibresGimnasio1_Esperadas = 0;
            int plazasLibresGimnasio2_Esperadas = 2;

            int plazasLibresGimnasio1 = gimnasio1.getPlazas() - gimnasio1.getMatriculados();
            int plazasLibresGimnasio2 = gimnasio2.getPlazas() - gimnasio2.getMatriculados();

            assertEquals(plazasLibresGimnasio1_Esperadas, plazasLibresGimnasio1);
            assertEquals(plazasLibresGimnasio2_Esperadas, plazasLibresGimnasio2);

        } catch (ClubException e) {

        }
    }

    @Test
    public void Matricular_ActividadNula_LanzaClubException() {
        try {
            ClubDeportivo club = new ClubDeportivo("UMA", 2);
            assertThrows(ClubException.class, () -> {
                club.matricular(null, 10);
            });
        } catch (ClubException e) {

        }
    }

    @Test
    public void Matricular_ActividadNoPerteneciente_NoActualizaClubDeportivo(){
        try {
            ClubDeportivo club = new ClubDeportivo("UMA", 2);
            Grupo gimnasio = new Grupo("11AB", "Gimnasio", 8, 6, 100.0);
            Grupo baile = new Grupo("33FG", "Baile", 10, 3, 30.0);
            club.anyadirActividad(gimnasio);
            club.anyadirActividad(baile);
            String representacion = club.toString();
            String actividadNoPerteneciente = "Zumba";

            club.matricular(actividadNoPerteneciente, 0);

            assertEquals(representacion, club.toString());


        } catch (ClubException e) {

        }
    }

    @Test
    public void Matricular_CeroPersonas_NoActualizaClubDeportivo(){
        try {
            ClubDeportivo club = new ClubDeportivo("UMA", 2);
            String actividad = "Gimnasio";
            Grupo gimnasio = new Grupo("11AB", actividad, 8, 6, 100.0);
            club.anyadirActividad(gimnasio);
            String representacion = club.toString();
            club.matricular(actividad, 0);

            assertEquals(representacion, club.toString());


        } catch (ClubException e) {

        }
    }

    @Test
    public void Ingresos_ClubDeportivo_DevuelveCantidadCorrecta() {
        try {
            ClubDeportivo club = new ClubDeportivo("UMA", 2);
            Grupo gimnasio = new Grupo("11AB", "Gimnasio", 8, 6, 100.0);
            Grupo zumba = new Grupo("22CD", "Zumba", 10, 5, 20.0);
            club.anyadirActividad(gimnasio);
            club.anyadirActividad(zumba);
            double ingresos_esperados = 700.0;

            double ingresos_obtenidos = club.ingresos();

            assertEquals(ingresos_esperados, ingresos_obtenidos);

        } catch (ClubException e) {

        }

    }

    @Test
    public void ToString_ClubConGrupos_DevuelveRepresentacionCorrecta() {
        try {
            ClubDeportivo club = new ClubDeportivo("UMA", 2);
            Grupo gimnasio = new Grupo("11AB", "Gimnasio", 8, 6, 100.0);
            club.anyadirActividad(gimnasio);
            String representacion_gimnasio = gimnasio.toString();
            Grupo zumba = new Grupo("22CD", "Zumba", 10, 7, 25.0);
            club.anyadirActividad(zumba);
            String representacion_zumba = zumba.toString();

            String representacion_esperada = "UMA --> [ " + representacion_gimnasio + ", " + representacion_zumba + " ]";
            String representacion_obtenida = club.toString();

            assertEquals(representacion_esperada, representacion_obtenida);

        } catch (ClubException e) {

        }
    }

    @Test
    public void ToString_ClubSinGrupos_DevuelveRepresentacionCorrecta() {
        try {
            String nombre = "UMA";
            ClubDeportivo club = new ClubDeportivo(nombre, 2);

            String representacion_esperada = nombre + " --> [  ]";
            String representacion_obtenida = club.toString();

            assertEquals(representacion_esperada, representacion_obtenida);
        } catch (ClubException e) {

        }
    }

}
