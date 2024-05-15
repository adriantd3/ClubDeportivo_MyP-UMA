package com.uma.example.springuma.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uma.example.springuma.integration.base.AbstractIntegration;
import com.uma.example.springuma.model.Medico;
import com.uma.example.springuma.model.Paciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class PacienteControllerIT extends AbstractIntegration {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Medico medico;
    private Paciente paciente;

    @BeforeEach
    public void setup() {
        medico = new Medico();
        medico.setDni("12345678V");
        medico.setNombre("Pedro");
        medico.setEspecialidad("dermatologo");
        medico.setId(1);
    }

    private void crearPacienteYAnyadirMedico() throws Exception {

        this.mockMvc.perform(post("/medico").
                    contentType("application/json").
                    content(objectMapper.writeValueAsString(medico))).
                andExpect(status().isCreated()).
                andExpect(status().is2xxSuccessful());

        paciente = new Paciente();
        paciente.setDni("12345678V");
        paciente.setNombre("Pedro");
        paciente.setEdad(30);
        paciente.setMedico(medico);
        paciente.setCita("prueba");
        paciente.setId(1);

        this.mockMvc.perform(post("/paciente").
                        contentType("application/json").
                        content(objectMapper.writeValueAsString(paciente))).
                andExpect(status().isCreated()).
                andExpect(status().is2xxSuccessful());

    }

    @Test
    @DisplayName("Crear un nuevo paciente y recuperarlo con una get request")
    public void GetPaciente_AnyadirNuevoPaciente_DevuelvePaciente() throws Exception {

        crearPacienteYAnyadirMedico();

        this.mockMvc.perform(get("/paciente/" + paciente.getId())).
                andExpect(status().is2xxSuccessful()).
                andExpect(content().contentType("application/json")).
                andExpect(jsonPath("$.id",equalTo((int) paciente.getId()))).
                andExpect(jsonPath("$.dni",equalTo(paciente.getDni()))).
                andExpect(jsonPath("$.nombre",equalTo(paciente.getNombre()))).
                andExpect(jsonPath("$.cita",equalTo(paciente.getCita()))).
                andExpect(jsonPath("$.edad",equalTo(paciente.getEdad()))).
                andExpect(jsonPath("$.medico.id",equalTo((int) medico.getId())));

    }

    @Test
    @DisplayName("Asignar un par de pacientes a médico y recuperar la lista de pacientes")
    public void GetPacientes_Anyadir2PacientesAMedico_DevuelveLista() throws Exception{
        crearPacienteYAnyadirMedico();
        Paciente paciente2 = new Paciente();
        paciente2.setId(2);
        paciente2.setDni("12345678A");
        paciente2.setNombre("Juan");
        paciente2.setEdad(35);
        paciente2.setCita("prueba");
        paciente2.setMedico(medico);
        this.mockMvc.perform(post("/paciente").
                        contentType("application/json").
                        content(objectMapper.writeValueAsString(paciente2))).
                andExpect(status().isCreated()).
                andExpect(status().is2xxSuccessful());

        this.mockMvc.perform(get("/paciente/medico/" + medico.getId())).
                andExpect(status().is2xxSuccessful()).
                andExpect(content().contentType("application/json")).
                andExpect(jsonPath("$",hasSize(2))).
                andExpect(jsonPath("$[0].id",equalTo((int) paciente.getId()))).
                andExpect(jsonPath("$[1].id",equalTo((int) paciente2.getId())));

    }

    @Test
    @DisplayName("Actualizar paciente existente")
    public void UpdatePaciente_PacienteExistente_ActualizaPaciente() throws Exception {
        crearPacienteYAnyadirMedico();
        paciente.setNombre("Alberto");
        paciente.setEdad(40);

        this.mockMvc.perform(put("/paciente").
                contentType("application/json").
                content(objectMapper.writeValueAsString(paciente))).
                andExpect(status().is2xxSuccessful());

        this.mockMvc.perform(get("/paciente/" + paciente.getId())).
                andExpect(status().is2xxSuccessful()).
                andExpect(content().contentType("application/json")).
                andExpect(jsonPath("$.id",equalTo((int) paciente.getId()))).
                andExpect(jsonPath("$.nombre",equalTo(paciente.getNombre()))).
                andExpect(jsonPath("$.edad",equalTo(paciente.getEdad())));
    }

    @Test
    @DisplayName("Cambiar el médico de un paciente")
    public void UpdatePaciente_CrearNuevoMedico_ActualizaPaciente() throws Exception {
        crearPacienteYAnyadirMedico();
        Medico medico2 = new Medico();
        medico2.setDni("87654321Y");
        medico2.setNombre("Paco");
        medico2.setEspecialidad("urologo");
        medico2.setId(2);

        this.mockMvc.perform(post("/medico").
                contentType("application/json").
                content(objectMapper.writeValueAsString(medico2))).
                andExpect(status().isCreated()).
                andExpect(status().is2xxSuccessful());

        paciente.setMedico(medico2);

        this.mockMvc.perform(put("/paciente").
                contentType("application/json").
                content(objectMapper.writeValueAsString(paciente))).
                andExpect(status().is2xxSuccessful());

        this.mockMvc.perform(get("/paciente/" + paciente.getId())).
                andExpect(status().is2xxSuccessful()).
                andExpect(content().contentType("application/json")).
                andExpect(jsonPath("$.id",equalTo((int) paciente.getId()))).
                andExpect(jsonPath("$.medico.id",equalTo((int) medico2.getId())));
    }

    @Test
    @DisplayName("Eliminar paciente borra entrada correctamente")
    public void EliminarPaciente_PacienteExistente_DevuelveOK() throws Exception {
        crearPacienteYAnyadirMedico();

        this.mockMvc.perform(delete("/paciente/" + paciente.getId())).
                andExpect(status().isOk());

        this.mockMvc.perform(get("/paciente/" + paciente.getId())).
                andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Eliminar paciente no existente")
    public void EliminarPaciente_PacienteNoExistente_DevuelveError() throws Exception {

        crearPacienteYAnyadirMedico();

        this.mockMvc.perform(delete("/paciente/" + 2)).
                andExpect(status().isInternalServerError());
    }


}
