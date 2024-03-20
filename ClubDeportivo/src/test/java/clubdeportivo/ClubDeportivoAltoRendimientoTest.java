package clubdeportivo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class ClubDeportivoAltoRendimientoTest {

    @ParameterizedTest
    @CsvSource({
            "UMA1,-1,10.0",
            "UMA2,12,-1.0",
            "UMA3,0,12.0",
            "UMA4,10,0.0"
    })
    public void ClubDeportivoAltoRendimiento_ValoresNegativos_LanzaClubException(String nombre, int maximo, double incremento) {
        assertThrows(ClubException.class, () -> {
            new ClubDeportivoAltoRendimiento(nombre, maximo, incremento);
        });
    }

    @Test
    public void ClubDeportivoAltoRendimiento_ParametrosCorrectos_NoLanzaClubException() {
        String nombre = "UMA";
        int maximo = 5;
        double incremento = 10.0;

        assertDoesNotThrow(() -> {
            new ClubDeportivoAltoRendimiento(nombre, maximo, incremento);
        });

    }

    @ParameterizedTest
    @CsvSource({
            "UMA1,0,10,10.0",
            "UMA2,-1,10,10.0",
            "UMA3,10,-10,10.0",
            "UMA4,10,0,10.0",
            "UMA5,10,-10,0",
            "UMA6,10,-10,-10.0"
    })
    public void ClubDeportivoAltoRendimientoTAM_ParametrosFueraDeRango_LanzaClubException(String nombre, int tam, int maximo, double incremento) {
        assertThrows(ClubException.class, () -> {
            new ClubDeportivoAltoRendimiento(nombre, tam, maximo, incremento);
        });
    }

    @Test
    public void Ingresos_ClubDeportivoAltoRendimiento_DevuelveCantidadCorrecta() {
        try {
            ClubDeportivo club = new ClubDeportivoAltoRendimiento("UMA", 2, 10, 10.0);
            Grupo gimnasio = new Grupo("11AB", "Gimnasio", 8, 6, 100.0);
            Grupo zumba = new Grupo("22CD", "Zumba", 10, 5, 20.0);
            club.anyadirActividad(gimnasio);
            club.anyadirActividad(zumba);

            double ingresos_esperados = 770.0;
            double ingresos_obtenidos = club.ingresos();

            assertEquals(ingresos_esperados, ingresos_obtenidos);
        } catch (ClubException e) {

        }

    }

    @Test
    public void AnyadirActividad_PocosDatos_LanzaClubException() {
        try {
            // Arrange
            ClubDeportivo club = new ClubDeportivoAltoRendimiento("UMA", 2, 10, 10.0);
            String[] datos = {"111A", "Kizomba", "10", "6"};

            // Assert
            assertThrows(ClubException.class, () -> {
                club.anyadirActividad(datos);
            });
        } catch (ClubException e) {

        }
    }

    @Test
    public void AnyadirActividad_MaximoNumeroPersonas_AcortaNumeroPersonas() {
        try {
            // Arrange
            ClubDeportivo club = new ClubDeportivoAltoRendimiento("UMA", 2, 10, 10.0);
            String[] datos = {"111A", "Kizomba", "20", "6", "25.0"};
            String esperada = "UMA --> [ (111A - Kizomba - 25.0 euros - P:10 - M:6) ]";

            // Act
            club.anyadirActividad(datos);
            String resultado = club.toString();

            // Assert
            assertEquals(esperada, resultado);

        } catch (ClubException e) {

        }
    }

    @Test
    public void AnyadirActividad_FormatoNumPlazasIncorrecto_LanzaClubException() {
        String numPlazas = "hola";
        try {
            ClubDeportivo club = new ClubDeportivoAltoRendimiento("UMA", 2, 10, 10.0);
            String[] grupo = {"111A", "Kizomba", numPlazas, "6", "25.0"};

            assertThrows(ClubException.class, () -> {
                club.anyadirActividad(grupo);
            });

        } catch (ClubException e) {

        }
    }

    @Test
    public void AnyadirActividad_FormatoMatriculadosIncorrecto_LanzaClubException() {
        String matriculados = "hola";
        try {
            ClubDeportivo club = new ClubDeportivoAltoRendimiento("UMA", 2, 10, 10.0);
            String[] grupo = {"111A", "Kizomba", "15", matriculados, "25.0"};

            assertThrows(ClubException.class, () -> {
                club.anyadirActividad(grupo);
            });

        } catch (ClubException e) {

        }
    }

    @Test
    public void AnyadirActividad_FormatoTarifaIncorrecto_LanzaClubException() {
        String tarifa = "hola";
        try {
            ClubDeportivo club = new ClubDeportivoAltoRendimiento("UMA", 2, 10, 10.0);
            String[] grupo = {"111A", "Kizomba", "15", "6", tarifa};

            assertThrows(ClubException.class, () -> {
                club.anyadirActividad(grupo);
            });

        } catch (ClubException e) {

        }
    }

    @Test
    public void AnyadirActividad_GrupoExcedeMaximoPermitido_LanzaClubException() {
        try {
            int maximoPersonas = 10;
            int tamGrupo = 15;
            ClubDeportivo club = new ClubDeportivoAltoRendimiento("UMA", 2, maximoPersonas, 10.0);
            Grupo grupo = new Grupo("456B", "Pilates", tamGrupo, 10, 50.0);

            assertThrows(ClubException.class, () -> {
                club.anyadirActividad(grupo);
            });

        } catch (ClubException e) {

        }
    }

}
