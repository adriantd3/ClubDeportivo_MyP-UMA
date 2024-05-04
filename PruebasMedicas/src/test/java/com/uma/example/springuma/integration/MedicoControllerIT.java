package com.uma.example.springuma.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uma.example.springuma.integration.base.AbstractIntegration;
import com.uma.example.springuma.model.Medico;
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

public class MedicoControllerIT extends AbstractIntegration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Medico medico;

    @BeforeEach
    public void setUp() throws Exception {
        medico = new Medico();
        medico.setDni("12345678V");
        medico.setNombre("Pedro");
        medico.setEspecialidad("dermatologo");
        medico.setId(1);
    }

    private void createMedico() throws Exception {
        this.mockMvc.perform(post("/medico")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("create a new medico and obtain it by a get request")
    public void SaveMedico_NonExistingMedico_isObtainedWithGet() throws Exception {

        // Creamos el medico
        createMedico();
        // Comprobamos que se ha creado correctamente
        this.mockMvc.perform(get("/medico/" + medico.getId()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", equalTo( (int) medico.getId())))
                .andExpect(jsonPath("$.dni", equalTo(medico.getDni())))
                .andExpect(jsonPath("$.nombre", equalTo(medico.getNombre())))
                .andExpect(jsonPath("$.especialidad", equalTo(medico.getEspecialidad())));

    }

    @Test
    @DisplayName("update an existing medico")
    public void UpdateMedico_ExistingMedico_isObtainedWithGet() throws Exception {

        createMedico();

        // Actualizamos el medico
        medico.setDni("87654321Y");
        medico.setNombre("Paco");
        medico.setEspecialidad("urologo");


        this.mockMvc.perform(put("/medico")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().is2xxSuccessful());

        // Comprobamos que el medico se ha actualizado correctamente
        this.mockMvc.perform(get("/medico/" + medico.getId()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", equalTo( (int) medico.getId())))
                .andExpect(jsonPath("$.dni", equalTo(medico.getDni())))
                .andExpect(jsonPath("$.nombre", equalTo(medico.getNombre())))
                .andExpect(jsonPath("$.especialidad", equalTo(medico.getEspecialidad())));

    }

    @Test
    @DisplayName("delete an existing medico")
    public void DeleteMedico_ExistingMedico_isNotObtainedWithGet() throws Exception {
        // Creamos el medico
        createMedico();

        // Eliminamos el medico
        this.mockMvc.perform(delete("/medico/" + medico.getId()))
                .andExpect(status().is2xxSuccessful());

        // Comprobamos que se ha eliminado
        this.mockMvc.perform(get("/medico/" + medico.getId()))
                .andDo(print())
                .andExpect(status().isInternalServerError());


    }

    @Test
    @DisplayName("delete a non-existing medico")
    public void DeleteMedico_NonExistingMedico_isInternalServerError() throws Exception {
        // Creamos el medico
        createMedico();

        long invalid_id = 10;

        // Eliminamos el medico
        this.mockMvc.perform(delete("/medico/" + invalid_id))
                .andDo(print())
                .andExpect(status().isInternalServerError());

    }

    @Test
    @DisplayName("retrieve an existing medico by their DNI")
    public void GetMedicoByDni_ExistingMedico_isObtainedWithGet() throws Exception {

        createMedico();

        this.mockMvc.perform(get("/medico/dni/" + medico.getDni()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", equalTo( (int) medico.getId())))
                .andExpect(jsonPath("$.dni", equalTo(medico.getDni())))
                .andExpect(jsonPath("$.nombre", equalTo(medico.getNombre())))
                .andExpect(jsonPath("$.especialidad", equalTo(medico.getEspecialidad())));
    }

    @Test
    @DisplayName("retrieve a non-existing medico by their DNI")
    public void GetMedicoByDni_NonExistingMedico_isNotFound() throws Exception {

        String non_existing_dni = "1111111K";

        this.mockMvc.perform(get("/medico/dni/" + non_existing_dni))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
