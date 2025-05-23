package app.classeMorta.ClasseMorta.logic.service;

import app.classeMorta.ClasseMorta.logic.PeriodoVoto;
import app.classeMorta.ClasseMorta.logic.models.Materie;
import app.classeMorta.ClasseMorta.logic.models.Studenti;
import app.classeMorta.ClasseMorta.logic.models.Voti;
import app.classeMorta.ClasseMorta.logic.repository.MaterieRepository;
import app.classeMorta.ClasseMorta.logic.repository.StudentiRepository;
import app.classeMorta.ClasseMorta.logic.repository.VotiRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@SpringBootTest
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

    @Test
    @Transactional
    void testGetVotiPerMateriaEID() {

        //creazione di uno studente, materia e voto provvisorio
        var studente = new Studenti("Test", "test@gmail.com", "test");
        studentiRepository.save(studente);

        var materia = new Materie("info", studente);
        materieRepository.save(materia);

        var voto = new Voti(7.5F, studente, materia, LocalDate.now(), PeriodoVoto.PENTAMESTRE);
        votiRepository.save(voto);

        var voto1 = new Voti(9F, studente, materia, LocalDate.now(), PeriodoVoto.PENTAMESTRE);
        votiRepository.save(voto1);

        //creazione della lista
        var voti = List.of(voto, voto1);

        //controllo se la lista fatta a mano è uguale alla lista fatta dalla funzione del service
        assertIterableEquals(voti, votiService.getVotiPerMateriaEIDAndPeriodo(materia.getIdMateria(), studente.getId(), PeriodoVoto.PENTAMESTRE));

    }

}
