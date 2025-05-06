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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@SpringBootTest
public class VotiServiceTest {

    @Autowired
    private VotiService votiService;

    @Autowired
    private VotiRepository votiRepository;

    @Autowired
    private StudentiRepository studentiRepository;

    @Autowired
    private MaterieRepository materieRepository;

    @Test
    @Transactional
    void testGetVotiPerMateriaEID(){
        //creazione di uno studente, materia e voto provvisorio
        var studente = new Studenti("Test", "test@gmail.com", "test".toCharArray());
        studentiRepository.save(studente);

        var materia = new Materie("info");
        materieRepository.save(materia);

        var voto = new Voti(7.5F, studente, materia, LocalDate.now());
        votiRepository.save(voto);

        var voto1 = new Voti(9F, studente, materia, LocalDate.now());
        votiRepository.save(voto1);

        //creazione della lista
        var voti = List.of(voto, voto1);

        //controllo se la lista fatta a mano è uguale alla lista fatta dalla funzione del service
        assertIterableEquals(voti, votiService.getVotiPerMateriaEID(materia.getIdMateria(), studente.getId()));

    }

}
