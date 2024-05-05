package com.uma.example.springuma.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uma.example.springuma.integration.base.AbstractIntegration;
import com.uma.example.springuma.model.Imagen;
import com.uma.example.springuma.model.Medico;
import com.uma.example.springuma.model.Paciente;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.io.File;
import java.time.Duration;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ImagenControllerIT extends AbstractIntegration {

    @LocalServerPort
    private Integer port;

    private WebTestClient client;

    @Autowired
    private ObjectMapper objectMapper;

    private Paciente paciente;
    private Medico medico;

    private final String HEALTHY_IMAGE = "./src/test/resources/healthy.png";
    private final String UNHEALTHY_IMAGE = "./src/test/resources/unhealthy.png";

    @PostConstruct
    public void init() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:"+port)
                .responseTimeout(Duration.ofMillis(20000)).build();
    }

    @BeforeEach
    public void setUp() {
        // Creamos el medico
        medico = new Medico();
        medico.setDni("12345678V");
        medico.setNombre("Pedro");
        medico.setEspecialidad("dermatologo");
        medico.setId(1);

        // Creamos el paciente
        paciente = new Paciente();
        paciente.setNombre("Luis");
        paciente.setId(1);
        paciente.setDni("22222222T");
        paciente.setEdad(18);
        paciente.setMedico(medico);
        paciente.setCita("cita");
    }

    private void createMedico() {
        // Insertamos el medico en la base de datos
        client.post().uri("/medico")
                .body(Mono.just(medico), Medico.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().returnResult();
    }

    private void createPaciente() {
        // Insertamos el paciente en la base de datos
        client.post().uri("/paciente")
                .body(Mono.just(paciente), Paciente.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().returnResult();
    }

    private String createImagen(String image) {
        // Insertamos la imagen asociada a nuestro paciente en la base de datos
        File uploadFile = new File(image);

        LinkedMultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("image", new FileSystemResource(uploadFile));
        parts.add("paciente", paciente);

        FluxExchangeResult<String> responseBody = client.post()
                .uri("/imagen")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(parts))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(String.class);


        String result = responseBody.getResponseBody().blockFirst();

        return result;
    }

    @Test
    @DisplayName("Subimos una nueva imagen")
    public void UploadImage_ValidImage_ReturnsGoodResponse() {
        createMedico();
        createPaciente();

        String result = createImagen(HEALTHY_IMAGE);

        assertEquals("{\"response\" : \"file uploaded successfully : healthy.png\"}", result);
    }

    @Test
    @DisplayName("Descargamos una imagen por su id")
    public void DownloadImage_ValidId_ReturnsCorrectImagen() throws JsonProcessingException {
        createMedico();
        createPaciente();
        createImagen(HEALTHY_IMAGE);

        FluxExchangeResult<Imagen> result = client.get().uri("/imagen/1")
                .exchange()
                .expectStatus().isOk().returnResult(Imagen.class);
    }

    @Test
    @DisplayName("Eliminamos una imagen por su id")
    public void DeleteCuenta_ValidId_ReturnsNoContent() {
        createMedico();
        createPaciente();
        createImagen(HEALTHY_IMAGE);

        client.delete()
                .uri("/imagen/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("Obtenemos las imagenes de un paciente")
    public void GetImagenes_ValidId_ReturnsOneImagen() {
        createMedico();
        createPaciente();
        // Introducimos dos imagenes para nuestro paciente
        createImagen(HEALTHY_IMAGE);
        createImagen(UNHEALTHY_IMAGE);

        client.get().uri("/imagen/paciente/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json")
                .expectBody().jsonPath("$", hasSize(2));

    }

    @Test
    @DisplayName("Obtenemos una imagen por su id")
    public void GetImagen_ValidId_ReturnsCorrectImagen(){
        createMedico();
        createPaciente();
        createImagen(HEALTHY_IMAGE);

        FluxExchangeResult<Imagen> result = client.get().uri("/imagen/info/1")
                .exchange()
                .expectStatus().isOk().returnResult(Imagen.class);

        Imagen imagenObtenida = result.getResponseBody().blockFirst();
        String expected_name = "healthy.png";

        String actual_name = imagenObtenida.getNombre();

        assertEquals(expected_name, actual_name);

    }

    @Test
    @DisplayName("Obtenemos prediccion de cancer con resultado negativo")
    public void GetImagenPrediction_ValidId_ReturnsNotCancer(){
        createMedico();
        createPaciente();
        createImagen(HEALTHY_IMAGE);

        FluxExchangeResult<String> responseBody = client.get()
                .uri("/imagen/predict/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(String.class);

        String result = responseBody.getResponseBody().blockFirst();
        String expected = "Not cancer (label 0)";

        assertTrue(result.contains(expected));

    }

    @Test
    @DisplayName("Obtenemos prediccion de cancer con resultado positivo")
    public void GetImagenPrediction_ValidId_ReturnsCancer(){
        createMedico();
        createPaciente();
        createImagen(UNHEALTHY_IMAGE);

        FluxExchangeResult<String> responseBody = client.get()
                .uri("/imagen/predict/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(String.class);

        String result = responseBody.getResponseBody().blockFirst();
        String expected = "Cancer (label 1)";

        assertTrue(result.contains(expected));

    }

    @Test
    @DisplayName("Hacemos prediccion con id de imagen invalido")
    public void GetImagenPrediction_InvalidId_ReturnsInternalServerError(){
        createMedico();
        createPaciente();
        createImagen(UNHEALTHY_IMAGE);

        int invalid_id = 10;

        FluxExchangeResult<String> responseBody = client.get()
                .uri("/imagen/predict/" + invalid_id)
                .exchange()
                .expectStatus().is5xxServerError()
                .returnResult(String.class);

        String result = responseBody.getResponseBody().blockFirst();
        String expected = "Error al realizar la prediccion";

        assertTrue(result.contains(expected));

    }


}
