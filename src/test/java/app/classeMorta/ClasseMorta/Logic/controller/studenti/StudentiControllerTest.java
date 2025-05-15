package app.classeMorta.ClasseMorta.Logic.controller.studenti;

import app.classeMorta.ClasseMorta.Logic.models.Studenti;
import app.classeMorta.ClasseMorta.Logic.repository.StudentiRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class StudentiControllerTest {
    private final MockMvc mockMvc;
    private final StudentiRepository studentiRepository;
    @Autowired
    StudentiControllerTest(MockMvc mockMvc, StudentiRepository studentiRepository) {
        this.mockMvc = mockMvc;
        this.studentiRepository = studentiRepository;
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
                .andExpect(jsonPath("$.success").value(true))   //controllo che i valori siano giusti
                .andExpect(jsonPath("$.id").value(studente.getId()));
    }
    @Test
    void testGetIdByEmail_notFound() throws Exception {
        //simulo una richiesta http ma non ho salvato niente quindi deve sbagliare
        mockMvc.perform(get("/studenti/id-by-email")
                        .param("email", "test@gmail.com") //passa la email
                        .accept(MediaType.APPLICATION_JSON))              //passo in formato json
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))   //controllo che i valori siano giusti
                .andExpect(jsonPath("message").value("ID non trovato"));
    }

    //verifica delle credenziali
    @Test
    void testVerificaCredenziali() throws Exception{
        Studenti studente = new Studenti("test", "test@gmail.com", "test".toCharArray());
        studentiRepository.save(studente);
        String json = """
                {
                    "email" : "test@gmail.com",
                    "password" : "test"
                }
                """;

        mockMvc.perform(post("/studenti/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("message").value("Login riuscito"));
    }
    @Test
    void testVerificaCredenziali_errate() throws Exception {
        String json = """
                {
                    "email" : "test@gmail.com",
                    "password" : "test"
                }
                """;

        mockMvc.perform(post("/studenti/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("message").value("Login fallito"));
    }

}
