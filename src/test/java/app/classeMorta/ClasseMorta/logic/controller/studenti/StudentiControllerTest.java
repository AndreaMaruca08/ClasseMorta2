package app.classeMorta.ClasseMorta.logic.controller.studenti;

import app.classeMorta.ClasseMorta.logic.dto.LoginRequest;
import app.classeMorta.ClasseMorta.logic.models.Studenti;
import app.classeMorta.ClasseMorta.logic.repository.StudentiRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class StudentiControllerTest {
    private final MockMvc mockMvc;
    private final StudentiRepository studentiRepository;
    private final ObjectMapper mapper;

    @Autowired
    StudentiControllerTest(MockMvc mockMvc, StudentiRepository studentiRepository, ObjectMapper mapper) {
        this.mockMvc = mockMvc;
        this.studentiRepository = studentiRepository;
        this.mapper = mapper;
    }


    //verifica id by email
    @Test
    void testGetIdByEmail() throws Exception {
        Studenti studente = new Studenti("test", "test@gmail.com", "test".toCharArray());
        studentiRepository.save(studente);
        //simula una richiesta http
        mockMvc.perform(get("/studenti/id-by-email")
                        .param("email", "test@gmail.com") //passa la email
                        .accept(MediaType.APPLICATION_JSON))              //passo in formato json
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successMessage").value(true))   //controllo che i valori siano giusti
                .andExpect(jsonPath("$.id").value(studente.getId()));
    }

    @Test
    void testGetIdByEmail_notFound() throws Exception {
        //simulo una richiesta http ma non ho salvato niente quindi deve sbagliare
        mockMvc.perform(get("/studenti/id-by-email")
                        .param("email", "test@gmail.com") //passa la email
                        .accept(MediaType.APPLICATION_JSON))              //passo in formato json
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.successMessage").value(false))   //controllo che i valori siano giusti
                .andExpect(jsonPath("message").value("ID non trovato"));
    }

    //verifica delle credenziali
    @Test
    void testVerificaCredenziali() throws Exception {
        var studente = new Studenti("test", "test@gmail.com", "test".toCharArray());
        studentiRepository.save(studente);
        var request = new LoginRequest("test@gmail.com", "test".toCharArray());

        mockMvc.perform(post("/studenti/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successMessage").value(true))
                .andExpect(jsonPath("message").value("Login riuscito"));
    }

    @Test
    void testVerificaCredenziali_errate() throws Exception {
        var request = new LoginRequest("test@gmail.com", "test".toCharArray());
        ;

        mockMvc.perform(post("/studenti/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.successMessage").value(false))
                .andExpect(jsonPath("message").value("Login fallito"));
    }

}
