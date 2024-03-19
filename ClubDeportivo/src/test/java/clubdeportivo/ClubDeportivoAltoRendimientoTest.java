package clubdeportivo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClubDeportivoAltoRendimientoTest {

    @ParameterizedTest
    @DisplayName("Prueba que se lance una excepcion al crear un club de alto rendimiento" +
            "con un maximo o incremento negativos")
    @CsvSource({
            "UMA1,-1,10.0",
            "UMA2,12,-1.0",
            "UMA3,0,12.0",
            "UMA4,10,0.0"
    })
    public void ClubDeportivoAltoRendimiento_ValoresNegativos_LanzaExcepcion(String nombre, int maximo, double incremento){
        // Assert
        assertThrows(ClubException.class, ()->{
            new ClubDeportivoAltoRendimiento(nombre, maximo, incremento);
        });
    }

    @ParameterizedTest
    @DisplayName("Prueba que se lance una excepcion al crear un club de alto rendimiento con tamaño cero o negativo")
    @CsvSource({
            "UMA1,0,10,10.0",
            "UMA2,-1,10,12.0",
    })
    public void ClubDeportivoAltoRendimiento_TamanoNegativo_LanzaExcepcion(String nombre, int tam, int maximo, double incremento){
        // Assert
        assertThrows(ClubException.class, ()->{
            new ClubDeportivoAltoRendimiento(nombre, tam, maximo, incremento);
        });
    }

    @Test
    @DisplayName("Prueba que los ingresos sean correctos")
    public void Ingresos_ClubDeportivoAltoRendimiento_DevuelveCantidadCorrecta(){
        try {
            // Arrange
            ClubDeportivo club = new ClubDeportivoAltoRendimiento("UMA",2,10,10.0);
            Grupo gimnasio = new Grupo("11AB","Gimnasio",8,6,100.0);
            Grupo zumba = new Grupo("22CD","Zumba",10,5,20.0);
            club.anyadirActividad(gimnasio);
            club.anyadirActividad(zumba);
            double ingresos_esperados = 770.0;

            // Act
            double ingresos_obtenidos = club.ingresos();

            // Assert
            assertEquals(ingresos_esperados, ingresos_obtenidos);

        } catch (ClubException e){

        }

    }

    @Test
    @DisplayName("Prueba que lance una excepcion al proporcionar menos datos de los necesarios")
    public void AnyadirActividad_PocosDatos_LanzaExcepcion(){
        try {
            // Arrange
            ClubDeportivo club = new ClubDeportivoAltoRendimiento("UMA",2,10,10.0);
            String [] datos = {"111A","Kizomba","10","6"};

            // Assert
            assertThrows(ClubException.class, ()->{
               club.anyadirActividad(datos);
            });


        }catch (ClubException e){

        }
    }

    @Test
    @DisplayName("Prueba que se aplique correctamente el maximo numero de personas por grupo")
    public void AnyadirActividad_MaximoNumeroPersonas_AcortaNumeroPersonas(){
        try {
            // Arrange
            ClubDeportivo club = new ClubDeportivoAltoRendimiento("UMA",2,10,10.0);
            String [] datos = {"111A","Kizomba","20","6","25.0"};
            //Grupo g = new Grupo("111A","Kizomba",20,6,25.0);
            //int tamano_esperado = 10;
            String esperada = "UMA --> [ (111A - Kizomba - 25.0 euros - P:10 - M:6) ]";

            // Act
            club.anyadirActividad(datos);
            String resultado = club.toString();

            // Assert
            assertEquals(esperada, resultado);


        }catch (ClubException e){

        }
    }



}
