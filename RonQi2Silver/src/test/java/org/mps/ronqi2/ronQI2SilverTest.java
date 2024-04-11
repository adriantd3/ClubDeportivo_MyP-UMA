// Authors: Adrián Torremocha Doblas
//          Ezequiel Sánchez García
package org.mps.ronqi2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mps.dispositivo.DispositivoSilver;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ronQI2SilverTest {


    /*
     * Analiza con los caminos base qué pruebas se han de realizar para comprobar que al inicializar funciona como debe ser.
     * El funcionamiento correcto es que si es posible conectar ambos sensores y configurarlos,
     * el método inicializar de ronQI2 o sus subclases,
     * debería devolver true. En cualquier otro caso false. Se deja programado un ejemplo.
     */
    @Nested
    @DisplayName("inicializar")
    class Inicializar {

        @Test
        @DisplayName("Dispositivo nulo")
        public void Inicializar_DispositivoNulo_ThrowsException(){
            RonQI2Silver r = new RonQI2Silver();

            assertThrows(RuntimeException.class, () -> {
               r.inicializar();
            });
        }

        @Test
        @DisplayName("Sensor de presion conectado y configurado")
        public void Inicializar_ConectaYConfiguraSensorPresion_ReturnsFalse() {
            RonQI2Silver r = new RonQI2Silver();
            DispositivoSilver d = mock(DispositivoSilver.class);
            r.anyadirDispositivo(d);
            when(d.conectarSensorPresion()).thenReturn(true);
            when(d.conectarSensorSonido()).thenReturn(false);
            when(d.configurarSensorPresion()).thenReturn(true);
            when(d.configurarSensorSonido()).thenReturn(false);

            boolean res = r.inicializar();

            assertFalse(res);
        }

        @Test
        @DisplayName("Sensor de presion conectado y no configurado")
        public void Inicializar_SoloConectaSensorPresion_ReturnsFalse() {
            RonQI2Silver r = new RonQI2Silver();
            DispositivoSilver d = mock(DispositivoSilver.class);
            r.anyadirDispositivo(d);
            when(d.conectarSensorPresion()).thenReturn(true);
            when(d.conectarSensorSonido()).thenReturn(false);
            when(d.configurarSensorPresion()).thenReturn(false);
            when(d.configurarSensorSonido()).thenReturn(false);

            boolean res = r.inicializar();

            assertFalse(res);
        }

        @Test
        @DisplayName("Sensor de sonido conectado y configurado")
        public void Inicializar_ConectaYConfiguraSensorSonido_ReturnsFalse() {
            RonQI2Silver r = new RonQI2Silver();
            DispositivoSilver d = mock(DispositivoSilver.class);
            r.anyadirDispositivo(d);
            when(d.conectarSensorPresion()).thenReturn(true);
            when(d.conectarSensorSonido()).thenReturn(true);
            when(d.configurarSensorPresion()).thenReturn(false);
            when(d.configurarSensorSonido()).thenReturn(true);

            boolean res = r.inicializar();

            assertFalse(res);
        }

        @Test
        @DisplayName("Sensor de sonido conectado y no configurado")
        public void Inicializar_SoloConectaSensorSonido_ReturnsFalse() {
            RonQI2Silver r = new RonQI2Silver();
            DispositivoSilver d = mock(DispositivoSilver.class);
            r.anyadirDispositivo(d);
            when(d.conectarSensorPresion()).thenReturn(true);
            when(d.conectarSensorSonido()).thenReturn(true);
            when(d.configurarSensorPresion()).thenReturn(false);
            when(d.configurarSensorSonido()).thenReturn(false);

            boolean res = r.inicializar();

            assertFalse(res);
        }

        @Test
        @DisplayName("Ningun sensor conectado ni configurado")
        public void Inicializar_NingunSensorConecta_ReturnsFalse() {
            RonQI2Silver r = new RonQI2Silver();
            DispositivoSilver d = mock(DispositivoSilver.class);
            r.anyadirDispositivo(d);
            when(d.conectarSensorPresion()).thenReturn(false);
            when(d.conectarSensorSonido()).thenReturn(false);
            when(d.configurarSensorPresion()).thenReturn(false);
            when(d.configurarSensorSonido()).thenReturn(false);

            boolean res = r.inicializar();

            assertFalse(res);
        }

        @Test
        @DisplayName("Ambos sensores conectados y configurados")
        public void Inicializar_AmbosSensoresConectan_ReturnsTrue() {
            RonQI2Silver r = new RonQI2Silver();
            DispositivoSilver d = mock(DispositivoSilver.class);
            r.anyadirDispositivo(d);
            when(d.conectarSensorPresion()).thenReturn(true);
            when(d.conectarSensorSonido()).thenReturn(true);
            when(d.configurarSensorPresion()).thenReturn(true);
            when(d.configurarSensorSonido()).thenReturn(true);

            boolean res = r.inicializar();

            assertTrue(res);
        }

        /*
         * Un inicializar debe configurar ambos sensores, comprueba que cuando se inicializa de forma correcta (el conectar es true),
         * se llama una sola vez al configurar de cada sensor.
         */

        @Test
        @DisplayName("Ambos sensores solo llaman a configurar una vez")
        public void Inicializar_SensoresConectarTrueConfigurarTrue_SoloLlamanUnaVezConfigurar() {
            DispositivoSilver disp = mock(DispositivoSilver.class);
            RonQI2Silver ron = new RonQI2Silver();
            ron.anyadirDispositivo(disp);
            when(disp.conectarSensorPresion()).thenReturn(true);
            when(disp.conectarSensorSonido()).thenReturn(true);
            when(disp.configurarSensorPresion()).thenReturn(true);
            when(disp.configurarSensorSonido()).thenReturn(true);

            ron.inicializar();

            verify(disp, times(1)).configurarSensorPresion();
            verify(disp, times(1)).configurarSensorSonido();
        }
    }

    @Nested
    @DisplayName("reconectar")
    class Reconectar {

        /*
         * Un reconectar, comprueba si el dispositivo desconectado, en ese caso, conecta ambos y devuelve true si ambos han sido conectados.
         * Genera las pruebas que estimes oportunas para comprobar su correcto funcionamiento.
         * Centrate en probar sitodo va bien, o si no, y si se llama a los métodos que deben ser llamados.
         */

        @Test
        @DisplayName("Dispositivo nulo")
        public void Inicializar_DispositivoNulo_ThrowsException(){
            RonQI2Silver r = new RonQI2Silver();

            assertThrows(RuntimeException.class, () -> {
                r.reconectar();
            });
        }

        @Test
        @DisplayName("El dispositivo esta conectado")
        public void Reconectar_DispositivoConectado_ReturnsFalse() {
            RonQI2Silver r = new RonQI2Silver();
            DispositivoSilver d = mock(DispositivoSilver.class);
            r.anyadirDispositivo(d);
            when(d.estaConectado()).thenReturn(true);

            boolean res = r.reconectar();

            assertFalse(res);
            verify(d, times(1)).estaConectado();
        }

        @Test
        @DisplayName("El dispositivo esta desconectado y no se ha podido conectar el sensor de presion")
        public void Reconectar_DispositivoDesconectadoYNoConectaSensorPresion_ReturnsFalse() {
            RonQI2Silver r = new RonQI2Silver();
            DispositivoSilver d = mock(DispositivoSilver.class);
            r.anyadirDispositivo(d);
            when(d.estaConectado()).thenReturn(false);
            when(d.conectarSensorPresion()).thenReturn(false);

            boolean res = r.reconectar();

            assertFalse(res);
            verify(d, times(1)).estaConectado();
            verify(d, times(0)).conectarSensorSonido();
            verify(d, times(1)).conectarSensorPresion();
        }

        @Test
        @DisplayName("El dispositivo esta desconectado y no se ha podido conectar el sensor de sonido")
        public void Reconectar_DispositivoDesconectadoYNoConectaSensorSonido_ReturnsFalse() {
            RonQI2Silver r = new RonQI2Silver();
            DispositivoSilver d = mock(DispositivoSilver.class);
            r.anyadirDispositivo(d);
            when(d.estaConectado()).thenReturn(false);
            when(d.conectarSensorPresion()).thenReturn(true);
            when(d.conectarSensorSonido()).thenReturn(false);

            boolean res = r.reconectar();

            assertFalse(res);
            verify(d, times(1)).estaConectado();
            verify(d, times(1)).conectarSensorSonido();
            verify(d, times(1)).conectarSensorPresion();
        }

        @Test
        @DisplayName("El dispositivo esta desconectado y se han podido conectar ambos sensores")
        public void Reconectar_DispositivoDesconectadoYSensoresSeHanConectado_ReturnsTrue() {
            RonQI2Silver r = new RonQI2Silver();
            DispositivoSilver d = mock(DispositivoSilver.class);
            r.anyadirDispositivo(d);
            when(d.estaConectado()).thenReturn(false);
            when(d.conectarSensorPresion()).thenReturn(true);
            when(d.conectarSensorSonido()).thenReturn(true);

            boolean res = r.reconectar();

            assertTrue(res);
            verify(d, times(1)).estaConectado();
            verify(d, times(1)).conectarSensorSonido();
            verify(d, times(1)).conectarSensorPresion();
        }
    }

    @Nested
    @DisplayName("evaluar apnea sueño")
    public class EvaluarApneaSuenyo {

        /*
         * El método evaluarApneaSuenyo, evalua las últimas 5 lecturas realizadas con obtenerNuevaLectura(),
         * y si ambos sensores superan o son iguales a sus umbrales, que son thresholdP = 20.0f y thresholdS = 30.0f;,
         * se considera que hay una apnea en proceso. Si hay menos de 5 lecturas también debería realizar la media.
         * /

         */
        @Test
        @DisplayName("Ambos sensores superan el umbral")
        public void EvaluarApneaSuenyo_LecturasPresionSonidoPositivas_ReturnsTrue() {
            RonQI2Silver r = new RonQI2Silver();
            DispositivoSilver d = mock(DispositivoSilver.class);
            r.anyadirDispositivo(d);
            when(d.leerSensorPresion()).thenReturn(25.0f);
            when(d.leerSensorSonido()).thenReturn(30.f);

            r.obtenerNuevaLectura();
            boolean res = r.evaluarApneaSuenyo();

            assertTrue(res);
        }

        @Test
        @DisplayName("Sensor presion supera umbral pero sonido no")
        public void EvaluarApneaSuenyo_LecturaPresionPositivaSonidoNegativa_ReturnsFalse() {
            RonQI2Silver r = new RonQI2Silver();
            DispositivoSilver d = mock(DispositivoSilver.class);
            r.anyadirDispositivo(d);
            when(d.leerSensorPresion()).thenReturn(25.0f);
            when(d.leerSensorSonido()).thenReturn(15.0f);

            r.obtenerNuevaLectura();
            boolean res = r.evaluarApneaSuenyo();

            assertFalse(res);
        }

        @Test
        @DisplayName("Sensor presion no supera umbral pero sonido si")
        public void EvaluarApneaSuenyo_LecturaPresionNegativaSonidoPositiva_ReturnsFalse() {
            RonQI2Silver r = new RonQI2Silver();
            DispositivoSilver d = mock(DispositivoSilver.class);
            r.anyadirDispositivo(d);
            when(d.leerSensorPresion()).thenReturn(15.0f);
            when(d.leerSensorSonido()).thenReturn(35.0f);

            r.obtenerNuevaLectura();
            boolean res = r.evaluarApneaSuenyo();

            assertFalse(res);
        }

        @Test
        @DisplayName("Ninguno de los sensores supera umbral")
        public void EvaluarApneaSuenyo_LecturaPresionSonidoNegativo_ReturnsFalse() {
            RonQI2Silver r = new RonQI2Silver();
            DispositivoSilver d = mock(DispositivoSilver.class);
            r.anyadirDispositivo(d);
            when(d.leerSensorPresion()).thenReturn(15.0f);
            when(d.leerSensorSonido()).thenReturn(15.0f);

            r.obtenerNuevaLectura();
            boolean res = r.evaluarApneaSuenyo();

            assertFalse(res);
        }

        /* Realiza un primer test para ver que funciona bien independientemente del número de lecturas.
         * Usa el ParameterizedTest para realizar un número de lecturas previas a calcular si hay apnea o no (por ejemplo 4, 5 y 10 lecturas).
         * https://junit.org/junit5/docs/current/user-guide/index.html#writing-tests-parameterized-tests
         */

        @ParameterizedTest
        @DisplayName("Funciona con numero variable de lecturas")
        @CsvSource({
                "4",
                "5",
                "10"
        })
        public void EvaluarApneaSuenyo_NumeroVariableLecturas_ReturnsTrue(int numLecturas) {
            RonQI2Silver r = new RonQI2Silver();
            DispositivoSilver d = mock(DispositivoSilver.class);
            r.anyadirDispositivo(d);
            when(d.leerSensorPresion()).thenReturn(20.0f);
            when(d.leerSensorSonido()).thenReturn(30.0f);

            for (int i = 0; i < numLecturas; i++) {
                r.obtenerNuevaLectura();
            }
            boolean res = r.evaluarApneaSuenyo();

            assertTrue(res);
        }
    }

    @Nested
    @DisplayName("añadir dispositivo")
    class AnyadirDispositivo{

        @Test
        @DisplayName("Nulo")
        public void AnyadirDispositivo_DispositivoNulo_ThrowsException(){
            RonQI2Silver r = new RonQI2Silver();
            DispositivoSilver d = null;

            assertThrows(RuntimeException.class, ()-> {
                r.anyadirDispositivo(d);
            });
        }

        @Test
        @DisplayName("No nulo")
        public void AnyadirDispositivo_DispositivoNoNulo_DoesNotThrowException(){
            RonQI2Silver r = new RonQI2Silver();
            DispositivoSilver d = mock(DispositivoSilver.class);

            assertDoesNotThrow(() -> {
                r.anyadirDispositivo(d);
            });
        }
    }
    @Nested
    @DisplayName("esta conectado")
    class EstaConectado{

        @Test
        @DisplayName("dispositivo nulo")
        public void EstaConectado_DispositivoNulo_ThrowsException(){
            RonQI2Silver r = new RonQI2Silver();

            assertThrows(RuntimeException.class, () -> {
                r.estaConectado();
            });
        }

        @Test
        @DisplayName("dispositivo conectado")
        public void EstaConectado_DispositivoConectado_ReturnsTrue(){
            RonQI2Silver r = new RonQI2Silver();
            DispositivoSilver d = mock(DispositivoSilver.class);
            when(d.estaConectado()).thenReturn(true);
            r.anyadirDispositivo(d);

            boolean res = r.estaConectado();

            assertTrue(res);
        }

        @Test
        @DisplayName("dispositivo desconectado")
        public void EstaConectado_DispositivoDesconectado_ReturnsFalse(){
            RonQI2Silver r = new RonQI2Silver();
            DispositivoSilver d = mock(DispositivoSilver.class);
            when(d.estaConectado()).thenReturn(false);
            r.anyadirDispositivo(d);

            boolean res = r.estaConectado();

            assertFalse(res);
        }
    }

    @Nested
    @DisplayName("obtener nueva lectura")
    class ObtenerNuevaLectura{

        @Test
        @DisplayName("dispositivo nulo")
        public void ObtenerNuevaLectura_DispositivoNulo_ThrowsException(){
            RonQI2Silver r = new RonQI2Silver();

            assertThrows(RuntimeException.class, () -> {
                r.obtenerNuevaLectura();
            });
        }

        @Test
        @DisplayName("dispositivo no nulo")
        public void ObtenerNuevaLectura_DispositivoNoNulo_DoesNotThrowException(){
            RonQI2Silver r = new RonQI2Silver();
            DispositivoSilver d = mock(DispositivoSilver.class);
            r.anyadirDispositivo(d);

            assertDoesNotThrow(() -> {
                r.obtenerNuevaLectura();
            });
        }
    }
}
