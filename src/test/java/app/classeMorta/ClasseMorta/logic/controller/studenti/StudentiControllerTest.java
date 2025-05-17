package app.classeMorta.ClasseMorta.logic.controller.studenti;

import app.classeMorta.ClasseMorta.logic.dto.LoginRequest;
import app.classeMorta.ClasseMorta.logic.models.Studenti;
import app.classeMorta.ClasseMorta.logic.repository.StudentiRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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

        mockMvc.perform(get("/studenti/id-by-email")
                        .param("email", studente.getEmail())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successMessage").value(true))
                .andExpect(jsonPath("$.message").value(studente.getId()));
    }

    @Test
    void testGetIdByEmail_notFound() throws Exception {
        //simulo una richiesta http ma non ho salvato niente quindi deve sbagliare
        mockMvc.perform(get("/studenti/id-by-email")
                        .param("email", "test@gmail.com") //passa la email
                        .accept(MediaType.APPLICATION_JSON))              //passo in formato json
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.successMessage").value(false))   //controllo che i valori siano giusti
                .andExpect(jsonPath("$.message.used").value(false))
                .andExpect(jsonPath("$.message.message").value("Email non trovata"));
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
    void testVerificaCredenziali_error() throws Exception {
        var request = new LoginRequest("test@gmail.com", "test".toCharArray());
        mockMvc.perform(post("/studenti/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.successMessage").value(false))
                .andExpect(jsonPath("message").value("Login fallito"));
    }

    //test per la email
    @Test
    void testIsEmailUsed() throws Exception{
        var studente = new Studenti("test", "test@gmail.com", "test".toCharArray());
        studentiRepository.save(studente);

        mockMvc.perform(get("/studenti/email-used")
                .param("email", studente.getEmail()) //passa la email
                .accept(MediaType.APPLICATION_JSON)            //passo in formato json
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successMessage").value(true))
                //prendo i valori dall'oggetto passato
                .andExpect(jsonPath("$.message.used").value(true))
                .andExpect(jsonPath("$.message.message").value("Email già utilizzata"));
    }

    @Test
    void testIsEmailUsed_error() throws Exception{
        mockMvc.perform(get("/studenti/email-used")
                        .param("email", "test") //passa la email
                        .accept(MediaType.APPLICATION_JSON)            //passo in formato json
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.successMessage").value(false))
                //prendo i valori dall'oggetto passato
                .andExpect(jsonPath("$.message.used").value(false))
                .andExpect(jsonPath("$.message.message").value("Email non utilizzata"));
    }

    @Test
    void testSalvaStudente()throws Exception{
        var studente = new Studenti("test", "test@gmail.com", "test".toCharArray());

        mockMvc.perform(post("/studenti/saveStudente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(studente))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successMessage").value(true))
                .andExpect(jsonPath("$.message.saved").value(true))
                .andExpect(jsonPath("$.message.message").value("Studente salvato con successo"));
    }

    @Test
    void testSalvaStudente_error()throws Exception{
        studentiRepository.save(new Studenti("test", "testSbagliato@gmail.com", "test".toCharArray()));

        mockMvc.perform(post("/studenti/saveStudente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new Studenti("test", "testSbagliato@gmail.com", "test".toCharArray())))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.successMessage").value(false))
                .andExpect(jsonPath("$.message.saved").value(false))
                .andExpect(jsonPath("$.message.message").value("Errore nel salvataggio dello studente"));
    }
}
