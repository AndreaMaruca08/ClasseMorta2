package app.classeMorta.ClasseMorta.logic.controller;


import app.classeMorta.ClasseMorta.logic.dto.SaveVotoRequest;
import app.classeMorta.ClasseMorta.logic.models.Materie;
import app.classeMorta.ClasseMorta.logic.models.Studenti;
import app.classeMorta.ClasseMorta.logic.models.Voti;
import app.classeMorta.ClasseMorta.logic.repository.MaterieRepository;
import app.classeMorta.ClasseMorta.logic.repository.StudentiRepository;
import app.classeMorta.ClasseMorta.logic.repository.VotiRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class VotiControllerTest {

    private final MockMvc mockMvc;
    private final VotiRepository votiRepository;
    private final MaterieRepository materieRepository;
    private final StudentiRepository studentiRepository;
    private final ObjectMapper mapper;

    @Autowired
    public VotiControllerTest(MockMvc mockMvc,
                              VotiRepository votiRepository,
                              MaterieRepository materieRepository,
                              StudentiRepository studentiRepository,
                              ObjectMapper mapper) {
        this.mockMvc = mockMvc;
        this.votiRepository = votiRepository;
        this.materieRepository = materieRepository;
        this.studentiRepository = studentiRepository;
        this.mapper = mapper;
    }

    private Materie materia;
    private Studenti studente;


    @BeforeEach
    void setUp() {
        studente = new Studenti("testStudente", "test@gmail.com", "test".toCharArray());
        materia = new Materie("testMateria",studente );
        materieRepository.save(materia);
        studentiRepository.save(studente);
    }

    @Test
    void testGetVotiPerMateria() throws Exception {
        var voto = new Voti(7.5F,
                studentiRepository.getReferenceById(studente.getId()),
                materieRepository.getReferenceById(materia.getIdMateria()),
                LocalDate.now());

        votiRepository.save(voto);

        mockMvc.perform(get("/voti/VotiPerMateria")
                        .param("idMateria", materia.getIdMateria().toString())
                        .param("idStudente", studente.getId().toString())
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successMessage").value(true))
                .andExpect(jsonPath("$.message[0].voto").value(7.5)); //controllo il primo della lista
    }

    @Test
    void testGetVotiPerMateria_notFound() throws Exception {
        mockMvc.perform(get("/voti/VotiPerMateria")
                        .param("idMateria", String.valueOf(1L))
                        .param("idStudente", String.valueOf(2L))
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void testAggiungiVoto() throws Exception {
        var saveVotoRequest = new SaveVotoRequest(7.5F, materia.getIdMateria(), studente.getId());

        mockMvc.perform(post("/voti/saveVoto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(saveVotoRequest))) // gli passo il dto
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successMessage").value(true))
                .andExpect(jsonPath("$.message.saved").value(true))
                .andExpect(jsonPath("$.message.message").value("Voto salvato con successo"));
    }
}