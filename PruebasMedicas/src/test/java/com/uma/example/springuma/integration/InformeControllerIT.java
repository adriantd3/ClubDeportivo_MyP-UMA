package com.uma.example.springuma.integration;

import com.uma.example.springuma.integration.base.AbstractIntegration;
import com.uma.example.springuma.model.Informe;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;
import java.time.Duration;

public class InformeControllerIT extends AbstractIntegration {

    @LocalServerPort
    private Integer port;

    private WebTestClient client;

    private Informe informe;

    // After dependency injection
    @PostConstruct
    public void init() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:"+port)
                .responseTimeout(Duration.ofMillis(30000)).build();

        informe = new Informe();
        informe.setId(1);
        informe.setPrediccion("Prediccion");
        informe.setContenido("Contenido");
        //informe.setImagen();
    }

}
