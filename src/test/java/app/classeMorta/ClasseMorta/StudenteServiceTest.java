package app.classeMorta.ClasseMorta;

import app.classeMorta.ClasseMorta.Logic.Studenti.Studenti;
import app.classeMorta.ClasseMorta.Logic.Studenti.StudentiRepository;
import app.classeMorta.ClasseMorta.Logic.Studenti.StudentiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class StudenteServiceTest {

    private final StudentiService studentiService;
    private final StudentiRepository studentiRepository;

    @Autowired
    public StudenteServiceTest(StudentiService studentiService, StudentiRepository studentiRepository) {
        this.studentiService = studentiService;
        this.studentiRepository = studentiRepository;
    }

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

}
