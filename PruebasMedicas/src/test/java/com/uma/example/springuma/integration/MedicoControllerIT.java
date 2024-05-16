/*
    Autores: Adrián Torremocha Doblas y Ezequiel Sánchez García
 */
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

        // Insertamos el medico antes de cada uno de los tests
        createMedico();
    }

    private void createMedico() throws Exception {
        this.mockMvc.perform(post("/medico")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("crear un nuevo medico y obtenerlo con una peticion por get")
    public void SaveMedico_NuevoMedico_esObtenidoPorGet() throws Exception {

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
    @DisplayName("actualizar un medico existente")
    public void UpdateMedico_MedicoExistente_esActualizadoCorrectamente() throws Exception {

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
    @DisplayName("borrar un medico existente")
    public void DeleteMedico_MedicoExistente_esEliminadoCorrectamente() throws Exception {

        // Eliminamos el medico
        this.mockMvc.perform(delete("/medico/" + medico.getId()))
                .andExpect(status().is2xxSuccessful());

        // Comprobamos que se ha eliminado
        this.mockMvc.perform(get("/medico/" + medico.getId()))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("borrar un medico no existente devuelve internal server error")
    public void DeleteMedico_MedicoNoExistente_ReturnsInternalServerError() throws Exception {

        long invalid_id = 10;

        // Eliminamos el medico
        this.mockMvc.perform(delete("/medico/" + invalid_id))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("obtener un medico existente por su DNI")
    public void GetMedicoByDni_MedicoExistente_esObtenidoPorGet() throws Exception {

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
    @DisplayName("obtener un medico no existente por su DNI devuelve not found")
    public void GetMedicoByDni_MedicoNoExistente_ReturnsNotFound() throws Exception {

        String non_existing_dni = "1111111K";

        this.mockMvc.perform(get("/medico/dni/" + non_existing_dni))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
