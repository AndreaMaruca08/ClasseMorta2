package app.classeMorta.ClasseMorta.Logic.controller.materie;

import app.classeMorta.ClasseMorta.Logic.controller.MaterieController;
import app.classeMorta.ClasseMorta.Logic.models.Materie;
import app.classeMorta.ClasseMorta.Logic.repository.MaterieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    @Autowired
    MaterieControllerTest(MockMvc mockMvc, MaterieRepository materieRepository, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.materieRepository = materieRepository;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void setup() {
        materieRepository.deleteAll(); // pulizia prima di ogni test
    }

    @Test
    void testGetAllMaterie_Vuoto() throws Exception {
        var materia = new Materie("info");
        materieRepository.save(materia);
        mockMvc.perform(get("/materie"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testCreaMateria() throws Exception {
        MaterieController.MateriaRequest request = new MaterieController.MateriaRequest();
        request.setNome("Storia");

        mockMvc.perform(post("/materie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Materia salvata con successo"));

        // Verifica dal DB
        var tutte = materieRepository.findAll();
        assert(tutte.size() == 1);
        assert(tutte.getFirst().getNomeMateria().equals("Storia"));
    }

    @Test
    void testCreaMateria_GiaEsistente() throws Exception {
        Materie esistente = new Materie();
        esistente.setNomeMateria("Matematica");
        materieRepository.save(esistente);

        MaterieController.MateriaRequest request = new MaterieController.MateriaRequest();
        request.setNome("Matematica");

        mockMvc.perform(post("/materie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Materia già esistente"));
    }

    @Test
    void testCheckMateria() throws Exception {
        Materie m = new Materie();
        m.setNomeMateria("Arte");
        materieRepository.save(m);

        mockMvc.perform(get("/materie/exists")
                        .param("nome", "Arte"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        mockMvc.perform(get("/materie/exists")
                        .param("nome", "Informatica"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void testEliminaMateria() throws Exception {
        Materie m = new Materie();
        m.setNomeMateria("Fisica");
        Materie materia = materieRepository.save(m);

        mockMvc.perform(delete("/materie/" + materia.getIdMateria()))
                .andExpect(status().isOk())
                .andExpect(content().string("Materia eliminata"));

        assert(materieRepository.findAll().isEmpty());
    }

    @Test
    void testEliminaMateria_NonEsistente() throws Exception {
        mockMvc.perform(delete("/materie/999"))
                .andExpect(status().isNotFound());
    }
}
