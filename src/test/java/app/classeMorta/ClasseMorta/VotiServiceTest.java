package app.classeMorta.ClasseMorta;
import app.classeMorta.ClasseMorta.Logic.Materie.Materie;
import app.classeMorta.ClasseMorta.Logic.Materie.MaterieRepository;
import app.classeMorta.ClasseMorta.Logic.Studenti.Studenti;
import app.classeMorta.ClasseMorta.Logic.Studenti.StudentiRepository;
import app.classeMorta.ClasseMorta.Logic.Voti.Voti;
import app.classeMorta.ClasseMorta.Logic.Voti.VotiRepository;
import app.classeMorta.ClasseMorta.Logic.Voti.VotiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class VotiServiceTest {

    private final VotiService votiService;
    private final VotiRepository votiRepository;
    private final StudentiRepository studentiRepository;
    private final MaterieRepository materieRepository;

    @Autowired
    public VotiServiceTest(VotiService votiService, VotiRepository votiRepository, StudentiRepository studentiRepository, MaterieRepository materieRepository) {
        this.votiService = votiService;
        this.votiRepository = votiRepository;
        this.studentiRepository = studentiRepository;
        this.materieRepository = materieRepository;
    }
    //test per vedere se la funzione restituisce la lista giusta
    @Test
    void testGetVotiPerMateriaEID(){
        //creazione di uno studente, materia e voto provvisorio
        Studenti studente = new Studenti("Test", "test@gmail.com", "test".toCharArray());
        studentiRepository.save(studente);
        Materie materia = new Materie("info");
        materieRepository.save(materia);
        Voti voto = new Voti(7.5F, studente, materia, LocalDate.now());
        votiRepository.save(voto);
        Voti voto1 = new Voti(9F, studente, materia, LocalDate.now());
        votiRepository.save(voto1);

        //creazione della lista
        List<Voti> lista = List.of(voto, voto1);

        //controllo se la lista fatta a mano è uguale alla lista fatta dalla funzione del service
        assertEquals(lista, votiService.getVotiPerMateriaEID(materia.getIdMateria(), studente.getId()));

    }


}
