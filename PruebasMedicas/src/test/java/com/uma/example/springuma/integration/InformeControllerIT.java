/*
    Autores: Adrián Torremocha Doblas y Ezequiel Sánchez García
 */
package com.uma.example.springuma.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uma.example.springuma.integration.base.AbstractIntegration;
import com.uma.example.springuma.model.Imagen;
import com.uma.example.springuma.model.Informe;
import com.uma.example.springuma.model.Medico;
import com.uma.example.springuma.model.Paciente;
import jakarta.annotation.PostConstruct;
import org.aspectj.lang.annotation.Before;
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

import java.awt.*;
import java.io.File;
import java.time.Duration;
import java.util.Date;

public class InformeControllerIT extends AbstractIntegration {

    @LocalServerPort
    private Integer port;

    @Autowired
    private ObjectMapper objectMapper;

    private WebTestClient client;

    private Informe informe;
    private Medico medico;
    private Paciente paciente;
    private Imagen imagen;

    private final String HEALTHY_IMAGE = "./src/test/resources/healthy.png";

    // After dependency injection
    @PostConstruct
    public void init() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:"+port)
                .responseTimeout(Duration.ofMillis(20000)).build();
    }

    @BeforeEach
    public void setup(){
        medico = new Medico();
        medico.setDni("12345678V");
        medico.setNombre("Pedro");
        medico.setEspecialidad("dermatologo");
        medico.setId(1);

        paciente = new Paciente();
        paciente.setNombre("Luis");
        paciente.setId(1);
        paciente.setDni("22222222T");
        paciente.setEdad(18);
        paciente.setMedico(medico);
        paciente.setCita("cita");

        // Insertamos un medico, un paciente y una imagen antes de cada test
        createMedico();
        createPaciente();
        createImagen(HEALTHY_IMAGE);
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
    @DisplayName("Crear un nuevo informe y recuperarlo con una get request")
    public void GetInforme_NuevoInforme_DevuelveInforme() throws Exception{

        FluxExchangeResult<Imagen> result = client.get().uri("/imagen/info/1")
                .exchange()
                .expectStatus().isOk().returnResult(Imagen.class);

        imagen  = result.getResponseBody().blockFirst();

        informe = new Informe();
        informe.setId(1);
        informe.setPrediccion("Not cancer (label 0),  score: 0.984481368213892");
        informe.setContenido("Contenido del informe");
        informe.setImagen(imagen);

        //SUBIMOS A LA BASE DE DATOS EL INFORME
        client.post().uri("/informe")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(informe))
                .exchange()
                .expectStatus().isCreated();

        //RECUPERAMOS EL INFORME DE LA BASE DE DATOS
        client.get().uri("/informe/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.prediccion").isEqualTo("Not cancer (label 0),  score: 0.984481368213892")
                .jsonPath("$.contenido").isEqualTo("Contenido del informe")
                .jsonPath("$.imagen.id").isEqualTo(1);
    }

    @Test
    @DisplayName("Recuperar la lista de informes asociados a una imagen")
    public void GetInformes_2NuevosInformes_DevuelveInformes() throws Exception{

        FluxExchangeResult<Imagen> result = client.get().uri("/imagen/info/1")
                .exchange()
                .expectStatus().isOk().returnResult(Imagen.class);

        imagen  = result.getResponseBody().blockFirst();

        informe = new Informe();
        informe.setId(1);
        //informe.setPrediccion("Not cancer (label 0),  score: 0.984481368213892");
        informe.setContenido("Contenido del informe");
        informe.setImagen(imagen);

        //SUBIMOS A LA BASE DE DATOS EL INFORME
        client.post().uri("/informe")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(informe))
                .exchange()
                .expectStatus().isCreated();

        Informe informe2 = new Informe();
        informe2.setId(2);
        //informe2.setPrediccion("Not cancer (label 0),  score: 0.984481368213892");
        informe2.setContenido("Contenido del informe");
        informe2.setImagen(imagen);

        //SUBIMOS A LA BASE DE DATOS EL INFORME
        client.post().uri("/informe")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(informe2))
                .exchange()
                .expectStatus().isCreated();

        //RECUPERAMOS LOS INFORMES DE LA BASE DE DATOS
        client.get().uri("/informe/imagen/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[1].id").isEqualTo(2);
        ;
    }

    @Test
    @DisplayName("Crear un nuevo informe y eliminarlo")
    public void DeleteInforme_NuevoInforme_SeEliminaCorrectamente(){

        FluxExchangeResult<Imagen> result = client.get().uri("/imagen/info/1")
                .exchange()
                .expectStatus().isOk().returnResult(Imagen.class);

        imagen  = result.getResponseBody().blockFirst();

        informe = new Informe();
        informe.setId(1);
        informe.setPrediccion("Not cancer (label 0),  score: 0.984481368213892");
        informe.setContenido("Contenido del informe");
        informe.setImagen(imagen);

        //SUBIMOS A LA BASE DE DATOS EL INFORME
        client.post().uri("/informe")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(informe)
                .exchange()
                .expectStatus().isCreated();

        //ELIMINAMOS EL INFORME DE LA BASE DE DATOS
        client.delete().uri("/informe/1")
                .exchange()
                .expectStatus().isNoContent();

        client.get().uri("/informe/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();
    }

}
