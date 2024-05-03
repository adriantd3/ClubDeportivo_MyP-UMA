package com.uma.example.springuma.integration;

import com.uma.example.springuma.integration.base.AbstractIntegration;
import com.uma.example.springuma.model.Imagen;
import com.uma.example.springuma.model.Medico;
import com.uma.example.springuma.model.Paciente;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;
import java.util.Calendar;

public class ImagenControllerIT extends AbstractIntegration {

    @LocalServerPort
    private Integer port;

    private WebTestClient client;

    private Imagen imagen;
    private Paciente paciente;
    private Medico medico;

    // After dependency injection
    @PostConstruct
    public void init() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:"+port)
                .responseTimeout(Duration.ofMillis(30000)).build();

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

        // Creamos la imagen
        imagen = new Imagen();
        imagen.setNombre("Imagen 1");
        imagen.setId(1);
        imagen.setPaciente(paciente);
        imagen.setFecha(Calendar.getInstance());
        //imagen.setFile_content();
    }

    
}
