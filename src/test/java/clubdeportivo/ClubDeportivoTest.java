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


    @DisplayName("Prueba que lance una excepcion al crear un club con un numero 0 o negativo de grupos")
    @ParameterizedTest
    @CsvSource({
            "UMA,0",
            "1Ma,-1",
    })
    public void ClubDeportivo_NumeroNegativoGrupos_LanzaExcepcion(String nombre, int numero_grupos){

            assertThrows(ClubException.class,()->{ new ClubDeportivo(nombre,numero_grupos);});

    }

    @Test
    @DisplayName("Prueba que no se lance una excepcion al crear un constructor pasansole solo el nombre")
    public void ClubDeportivo_PasoNombre_NoLanzaExcepcion(){
        String nombre = "UMA";
        assertDoesNotThrow(()->{new ClubDeportivo(nombre);});

    }


    @Test
    @DisplayName("Prueba que lance una excepcion al añadir un nuevo grupo nulo")
    public void AnyadirActividad_GrupoNulo_LanzaExcepcion(){

        try {
            // Arrange
            ClubDeportivo club = new ClubDeportivo("UMA",1);
            Grupo zumba = null;

            // Assert
            assertThrows(ClubException.class, ()->{ club.anyadirActividad(zumba);});

        } catch (ClubException e){

        }
    }

    @Test
    @DisplayName("Prueba que no se lance una excepcion al añadir correctamente un grupo con un array de Strings")
    public void AnyadirActividad_ArrayStringCorrecto_NoLanzaExcepcion(){
        try {
            // Arrange
            ClubDeportivo club = new ClubDeportivo("UMA",2);
            String [] grupo = {"756B","Comba","20","12","30.0"};

            // Assert
            assertDoesNotThrow(()->{club.anyadirActividad(grupo);});
        }catch (ClubException e){

        }
    }

    @Test
    @DisplayName("Prueba que lance una excepcion al intentar añadir un nuevo grupo al club " +
            "cuando ya este completa la capacidad maxima de dicho club")
    public void AnyadirActividad_CapacidadClubCompleta_LanzaExcepcion(){
        try {
            // Arrange
            int capacidad = 1;
            ClubDeportivo club = new ClubDeportivo("UMA",capacidad);
            Grupo zumba = new Grupo("11AB","Zumba",10,5,25.0);
            club.anyadirActividad(zumba);
            Grupo gimnasio = new Grupo("22XX","Gimnasio",20,12,30.0);

            // Assert
            assertThrows(ClubException.class, ()-> {
                club.anyadirActividad(gimnasio);
            });

        }catch (ClubException e){

        }

    }

    @Test
    @DisplayName("Prueba que se actualicen las plazas de un grupo ya existente en el club")
    public void AnyadirActividad_GrupoExistente_CambiaNumeroPlazas(){
        try {
            // Arrange
            ClubDeportivo club = new ClubDeportivo("UMA",2);
            Grupo zumba = new Grupo("11AB","Zumba",10,5,25.0);
            club.anyadirActividad(zumba);
            Grupo zumba2 = new Grupo("11AB","Zumba",20,5,25.0);
            int plazas_esperadas = 20;

            // Act
            club.anyadirActividad(zumba2);
            int plazas_devueltas = zumba.getPlazas();

            // Assert
            assertEquals(plazas_esperadas, plazas_devueltas);

        }catch (ClubException e){

        }
    }

    @ParameterizedTest
    @DisplayName("Prueba que se lance una excepcion al añadir grupos con parametros incorrectos")
    @CsvSource({
            "123A,Kizomba,True,-10,25.0",
            "22HG,Zumba,10,False,-25.0",
            "78JK,Gimnasio,21,0,False"

    })
    public void AnyadirActividad_GrupoValoresIncorrectos_LanzaExcepcion(String codigo, String actividad,
                                                                        String nplazas, String matriculados,
                                                                        String tarifa){
        try {
            // Arrange
            ClubDeportivo club = new ClubDeportivo("UMA",2);
            String[] datos = {codigo, actividad, nplazas, matriculados, tarifa};

            // Assert
           assertThrows(ClubException.class, ()->{
                club.anyadirActividad(datos);
           });

        }catch (ClubException e){

        }
    }



    @Test
    @DisplayName("Prueba que devuelva la cantidad correcta de plazas libres de una actividad ")
    public void PlazasLibres_ActividadPerteneciente_DevuelvePlazasLibresActividad(){
        try {
            // Arrange
            ClubDeportivo club = new ClubDeportivo("UMA",1);
            String actividad = "Zumba";
            Grupo zumba = new Grupo("11AB",actividad,8,6,100.0);
            club.anyadirActividad(zumba);
            int plazas_libres_esperadas = 2;

            // Act
            int plazas_libres_devueltas = club.plazasLibres("Zumba");

            // Assert
            assertEquals(plazas_libres_esperadas, plazas_libres_devueltas);
        }catch (ClubException e){

        }
    }

    @Test
    @DisplayName("Prueba que devuelva cero plazas libres cuando la actividad no pertenece al club ")
    public void PlazasLibres_ActividadNoPerteneciente_DevuelveCero(){
        try {
            // Arrange
            ClubDeportivo club = new ClubDeportivo("UMA",1);
            String actividad = "Gimnasio";
            Grupo gimnasio = new Grupo("11AB",actividad,8,6,100.0);
            int plazas_libres_esperadas = 0;

            // Act
            int plazas_libres_devueltas = club.plazasLibres(actividad);

            // Assert
            assertEquals(plazas_libres_esperadas, plazas_libres_devueltas);
        }catch (ClubException e){

        }
    }

    @Test
    @DisplayName("Prueba que lance una excepcion al intentar matricular cuando hay mas personas que plazas libres")
    public void Matricular_MasPersonasQuePlazas_LanzaExcepcion(){
        try {
            // Arrange
            ClubDeportivo club = new ClubDeportivo("UMA",2);
            String actividad = "Gimnasio";
            Grupo gimnasio = new Grupo("11AB",actividad,8,6,100.0);
            club.anyadirActividad(gimnasio);

            // Assert
            assertThrows(ClubException.class, ()->{
                club.matricular(actividad,10);
            });

        }catch (ClubException e){

        }
    }

    @Test
    @DisplayName("Prueba que se matriculen correctamente un grupo de personas a una determinada actividad")
    public void Matricular_GrupoPersonas_GruposActualizasNumeroPlazasLibres(){
        try {
            // Arrange
            ClubDeportivo club = new ClubDeportivo("UMA",2);
            String actividad = "Gimnasio";
            Grupo gimnasio1 = new Grupo("11AB",actividad,8,6,100.0);
            Grupo gimnasio2 = new Grupo("22CD",actividad,10,5,20.0);
            club.anyadirActividad(gimnasio1);
            club.anyadirActividad(gimnasio2);

            // Act
            club.matricular(actividad, 5);

            int plazasLibresGimnasio1_Esperadas = 0;
            int plazasLibresGimnasio2_Esperadas = 2;

            int plazasLibresGimnasio1 = gimnasio1.getPlazas()-gimnasio1.getMatriculados();
            int plazasLibresGimnasio2 = gimnasio2.getPlazas()- gimnasio2.getMatriculados();

            // Assert
            assertEquals(plazasLibresGimnasio1_Esperadas,plazasLibresGimnasio1);
            assertEquals(plazasLibresGimnasio2_Esperadas, plazasLibresGimnasio2);

        }catch (ClubException e){

        }
    }

    @Test
    @DisplayName("Prueba que los ingresos sean correctos")
    public void Ingresos_ClubDeportivo_DevuelveCantidadCorrecta(){
        try {
            // Arrange
            ClubDeportivo club = new ClubDeportivo("UMA",2);
            Grupo gimnasio = new Grupo("11AB","Gimnasio",8,6,100.0);
            Grupo zumba = new Grupo("22CD","Zumba",10,5,20.0);
            club.anyadirActividad(gimnasio);
            club.anyadirActividad(zumba);
            double ingresos_esperados = 700.0;

            // Act
            double ingresos_obtenidos = club.ingresos();

            // Assert
            assertEquals(ingresos_esperados, ingresos_obtenidos);

        } catch (ClubException e){

        }

    }

    @Test
    @DisplayName("Prueba que la representacion del objeto sea correcta")
    public void ToString_ClubConGrupos_DevuelveRepresentacionCorrecta(){
        try {
            // Arrange
            ClubDeportivo club = new ClubDeportivo("UMA",2);
            Grupo gimnasio = new Grupo("11AB","Gimnasio",8,6,100.0);
            club.anyadirActividad(gimnasio);
            String representacion_gimnasio = gimnasio.toString();

            Grupo zumba = new Grupo("22CD","Zumba",10,7,25.0);
            club.anyadirActividad(zumba);
            String representacion_zumba = zumba.toString();

            String representacion_esperada ="UMA --> [ " + representacion_gimnasio + ", " + representacion_zumba + " ]";

            // Act
            String representacion_obtenida = club.toString();

            // Assert
            assertEquals(representacion_esperada, representacion_obtenida);

        } catch (ClubException e){

        }
    }

    @Test
    @DisplayName("Prueba que la representacion del grupo vacio sea la correcta")
    public void ToString_ClubSinGrupos_DevuelveRepresentacionCorrecta(){
        try {
            // Arrange
            String nombre = "UMA";
            ClubDeportivo club = new ClubDeportivo(nombre,2);
            String representacion_esperada =  nombre + " --> [  ]";

            // Act
            String representacion_obtenida = club.toString();

            // Assert
            assertEquals(representacion_esperada, representacion_obtenida);
        }catch (ClubException e){

        }
    }
    
}
