package app.classeMorta.ClasseMorta.logic.controller;


import app.classeMorta.ClasseMorta.logic.dto.MediaRequest;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class MediaControllerTest{
    private final MockMvc mockMvc;
    private final StudentiRepository studentiRepository;
    private final VotiRepository votiRepository;
    private final MaterieRepository materieRepository;
    private final ObjectMapper mapper;

    private Materie materia;
    private Studenti studente;

    @Autowired
    public MediaControllerTest(MockMvc mockMvc, StudentiRepository studentiRepository, ObjectMapper mapper, VotiRepository votiRepository, MaterieRepository materieRepository) {
        this.mockMvc = mockMvc;
        this.studentiRepository = studentiRepository;
        this.mapper = mapper;
        this.votiRepository = votiRepository;
        this.materieRepository = materieRepository;
    }

    @BeforeEach
    void setup(){

        studente = new Studenti("testStudente", "test@gmail.com", "test".toCharArray());
        materia = new Materie("testMateria", studente);
        Materie materia1 = new Materie("testMateria1", studente);
        materieRepository.save(materia);
        materieRepository.save(materia1);
        studentiRepository.save(studente);

        //voti
        var voto = new Voti(4F, studente, materia, LocalDate.now());
        votiRepository.save(voto);
        var voto1 = new Voti(8F, studente, materia, LocalDate.now());
        votiRepository.save(voto1);

        var voto2 = new Voti(8F, studente, materia1, LocalDate.now());
        votiRepository.save(voto2);
    }

    //medie per materie e studenti
    @Test
    void testGetMediaPerMateria() throws Exception {
        var mediaRequest = new MediaRequest(materia.getIdMateria(), studente.getId(), -1);
        mockMvc.perform(post("/media/calcolaMedia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(mediaRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successMessage").value(true))
                .andExpect(jsonPath("$.message").value(6F));
    }

    @Test
    void testGetMediaPerMateria_errore() throws Exception {
        votiRepository.deleteAll();
        var mediaRequest = new MediaRequest(materia.getIdMateria(), studente.getId(), -1);
        mockMvc.perform(post("/media/calcolaMedia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(mediaRequest))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.successMessage").value(false))
                .andExpect(jsonPath("$.message").value("Nessuna media"));
    }

    //medie ipotetiche
    @Test
    void testGetMediaPerMateriaIpotetico() throws Exception {
        var mediaRequest = new MediaRequest(materia.getIdMateria(), studente.getId(), 8F);
        mockMvc.perform(post("/media/calcolaMedia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(mediaRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successMessage").value(true))
                .andExpect(jsonPath("$.message").value(6.6666667F));
    }

    @Test
    void testGetMediaPerMateriaIpotetico_errore() throws Exception {
        votiRepository.deleteAll();
        var mediaRequest = new MediaRequest(materia.getIdMateria(), studente.getId(), 7);
        mockMvc.perform(post("/media/calcolaMedia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(mediaRequest))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.successMessage").value(false))
                .andExpect(jsonPath("$.message").value("Nessuna media"));
    }

    //medie totali
    @Test
    void testGetMediaTot()throws Exception{
        mockMvc.perform(post("/media/mediaAll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(studente.getId()))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successMessage").value(true))
                .andExpect(jsonPath("$.message").value(7F));
    }

}
