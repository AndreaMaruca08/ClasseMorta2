package app.classeMorta.ClasseMorta.logic.service.Studenti;

import app.classeMorta.ClasseMorta.logic.dto.LoginRequest;
import app.classeMorta.ClasseMorta.logic.models.Studenti;
import app.classeMorta.ClasseMorta.logic.repository.StudentiRepository;
import app.classeMorta.ClasseMorta.logic.service.StudentiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
public class StudenteServiceTest {

    private final StudentiService studentiService;
    private final StudentiRepository studentiRepository;

    @Autowired
    public StudenteServiceTest(StudentiService studentiService, StudentiRepository studentiRepository) {
        this.studentiService = studentiService;
        this.studentiRepository = studentiRepository;
    }

    //verifica che salvaStudente funzioni correttamente
    @Test
    void testSalvaStudenteNelDatabase() {
        //creo studente provvisorio
        studentiService.salvaStudente("Test", "test@gmail.com", "test".toCharArray());
        //prendo tutti gli studenti dal database
        List<Studenti> studenti = studentiRepository.findAll();
        //controllo che ci sia un solo utente(per essere sicuri che abbia effettivamente creato un utente)
        assertEquals(1, studenti.size());
        //controllo che il nome sia giusto
        assertEquals("Test", studenti.getFirst().getName());
        //controllo l'email
        assertEquals("test@gmail.com", studenti.getFirst().getEmail());
    }

    //verifica credenziali
    @Test
    void testVerificaCredenziali_giuste() {
        //creazioni dello studente per il test
        Studenti studente = new Studenti("Test", "test@gmail.com", "test".toCharArray());
        //salvo in h2
        studentiRepository.save(studente);
        //guardo se le credenziali sono corrette
        LoginRequest loginRequest = new LoginRequest("test@gmail.com", "test".toCharArray());
        boolean result = studentiService.verificaCredenziali(loginRequest);
        //controllo se ha dato true
        assertTrue(result);
    }

    @Test
    void testVerificaCredenziali_sbagliate() {
        //creazioni dello studente per il test
        Studenti studente = new Studenti("Test", "test@gmail.com", "test".toCharArray());
        //salvo in h2
        studentiRepository.save(studente);
        LoginRequest loginRequest = new LoginRequest("test@gmail.com", "testSbagliato".toCharArray());
        boolean result = studentiService.verificaCredenziali(loginRequest);
        //controllo se ha dato true
        assertFalse(result);
    }

    //verifica se la email è già stata usata
    @Test
    void testIsEmailUsed() {
        //creazioni dello studente per il test
        Studenti studente = new Studenti("Test", "test@gmail.com", "test".toCharArray());
        studentiRepository.save(studente);

        boolean risposta = studentiService.isEmailUsed("test@gmail.com");

        assertTrue(risposta);

    }

    @Test
    void testIsEmailNotUsed() {
        //creazioni dello studente per il test
        Studenti studente = new Studenti("Test", "test@gmail.com", "test".toCharArray());
        studentiRepository.save(studente);

        boolean risposta = studentiService.isEmailUsed("test1@gmail.com");

        assertFalse(risposta);

    }

    //verifica che testStudenteIdByEmail restituisca il giusto
    @Test
    void testStudenteIdByEmail_giusto() {
        //creazioni dello studente per il test
        Studenti studente = new Studenti("Test", "test@gmail.com", "test".toCharArray());
        studentiRepository.save(studente);
        assertEquals(studente.getId(), studentiService.getStudentIdByEmail("test@gmail.com"));

    }

    @Test
    void testStudenteIdByEmail_Error() {
        //creazioni dello studente per il test
        Studenti studente = new Studenti("Test", "test@gmail.com", "test".toCharArray());
        studentiRepository.save(studente);
        assertNotEquals(studente.getId(), studentiService.getStudentIdByEmail("test1@gmail.com"));

    }

    //verifica che getStudenteById funziona
    @Test
    void testGetStudenteById() {
        //creazioni dello studente per il test
        Studenti studente = new Studenti("Test", "test@gmail.com", "test".toCharArray());
        studentiRepository.save(studente);


        assertEquals(studente, studentiService.getStudenteByID(studente.getId()));
    }


}
