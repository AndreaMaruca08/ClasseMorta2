package app.classeMorta.ClasseMorta.logic.controller;

import app.classeMorta.ClasseMorta.logic.controller.MaterieController.MateriaRequest;
import app.classeMorta.ClasseMorta.logic.models.Materie;
import app.classeMorta.ClasseMorta.logic.models.Studenti;
import app.classeMorta.ClasseMorta.logic.repository.MaterieRepository;
import app.classeMorta.ClasseMorta.logic.repository.StudentiRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MaterieControllerTest {

    private final MockMvc mockMvc;
    private final MaterieRepository materieRepository;
    private final StudentiRepository studentiRepository;
    private final ObjectMapper objectMapper;

    private Studenti studente;

    @Autowired
    MaterieControllerTest(MockMvc mockMvc, MaterieRepository materieRepository, StudentiRepository studentiRepository, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.materieRepository = materieRepository;
        this.studentiRepository = studentiRepository;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void setup() {
        materieRepository.deleteAll();
        studentiRepository.deleteAll();
        studente = new Studenti("test", "test", "test".toCharArray());
        studentiRepository.save(studente);
    }

    @Test
    void testGetAllMaterie_Vuoto() throws Exception {
        // Non salva nessuna materia, quindi il risultato dovrebbe essere vuoto
        mockMvc.perform(get("/materie")
                        .param("idStudente", studente.getId().toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.successMessage").value(false))
                .andExpect(jsonPath("$.message").value("Nessuna materia trovata"));
    }

    @Test
    void testGetAllMaterie() throws Exception {
        // Salvo e riassegno lo studente per essere sicuro di avere l'id corretto
        studente = studentiRepository.save(studente);
        Materie materia = new Materie("info", studente);
        materieRepository.save(materia);

        mockMvc.perform(get("/materie")
                        .param("idStudente", studente.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successMessage").value(true))
                .andExpect(jsonPath("$.message", hasSize(1)))
                .andExpect(jsonPath("$.message[0].nome").value("info"));
    }

    @Test
    void testCreaMateria() throws Exception {
        final var request = new MateriaRequest();
        request.setNome("Storia");
        request.setIdStudente(studente.getId());

        mockMvc.perform(post("/materie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successMessage").value(true))
                .andExpect(jsonPath("$.message").value("Materia salvata con successo"));

        // Verifica dal DB
        var tutte = materieRepository.findAll();
        Assertions.assertFalse(tutte.isEmpty());
        assert (tutte.getFirst().getNomeMateria().equals("Storia"));
    }

    @Test
    void testCreaMateria_GiaEsistente() throws Exception {
        Materie esistente = new Materie("Matematica", studente);
        materieRepository.save(esistente);

        MateriaRequest request = new MateriaRequest();
        request.setNome("Matematica");
        request.setIdStudente(studente.getId());

        mockMvc.perform(post("/materie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.successMessage").value(false))
                .andExpect(jsonPath("$.message").value("Materia già esistente"));
    }

    @Test
    void testCheckMateria() throws Exception {
        Materie m = new Materie("Arte", studente);
        materieRepository.save(m);

        MateriaRequest reqExists = new MateriaRequest();
        reqExists.setNome("Arte");
        reqExists.setIdStudente(studente.getId());

        MateriaRequest reqNonExists = new MateriaRequest();
        reqNonExists.setNome("Informatica");
        reqNonExists.setIdStudente(studente.getId());

        mockMvc.perform(post("/materie/exists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqExists)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successMessage").value(true))
                .andExpect(jsonPath("$.message").value(true));
    }

    @Test
    void testEliminaMateria() throws Exception {
        Materie m = new Materie("Fisica", studente);
        Materie materia = materieRepository.save(m);

        mockMvc.perform(delete("/materie/" + materia.getIdMateria()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successMessage").value(true))
                .andExpect(jsonPath("$.message").value("Materia eliminata"));

        assert (materieRepository.findAll().isEmpty());
    }

    @Test
    void testEliminaMateria_NonEsistente() throws Exception {
        mockMvc.perform(delete("/materie/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.successMessage").value(false))
                .andExpect(jsonPath("$.message").value("Materia non trovata"));
    }
}