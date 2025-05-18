package app.classeMorta.ClasseMorta.logic.service;

import app.classeMorta.ClasseMorta.logic.models.Materie;
import app.classeMorta.ClasseMorta.logic.models.Studenti;
import app.classeMorta.ClasseMorta.logic.repository.MaterieRepository;
import app.classeMorta.ClasseMorta.logic.repository.StudentiRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class MaterieServiceTest {

    private final MaterieService materieService;
    private final MaterieRepository materieRepository;
    private final StudentiRepository studentiRepository;

    @Autowired
    public MaterieServiceTest(MaterieService materieService, MaterieRepository materieRepository, StudentiRepository studentiRepository) {
        this.materieService = materieService;
        this.materieRepository = materieRepository;
        this.studentiRepository = studentiRepository;
    }

    //test di getAllMaterie
    @Test
    void testGetAllMaterieByStudente() {
        Studenti studente = new Studenti("test", "test" , "test".toCharArray());
        studentiRepository.save(studente);
        //creo due materia
        Materie materia = new Materie("info", studente);
        materieRepository.save(materia);
        Materie materia2 = new Materie("mate", studente);
        materieRepository.save(materia2);

        //lista fatta da me
        List<Materie> listaP = List.of(materia, materia2);
        //lista fatta con la funzione del service
        List<Materie> lista = materieService.getAllMaterie(studente);

        assertEquals(lista, listaP);
    }

    //test per vedere se salva la materia
    @Test
    void testSaveMateria() {
        Studenti studente = new Studenti("test", "test" , "test".toCharArray());
        studentiRepository.save(studente);
        //creo una materia
        Materie materia = new Materie("info", studente);
        materieRepository.save(materia);
        //lista delle materie che dovrebbe essere solo 1
        List<Materie> lista = materieRepository.findAll();
        //controllo che la materia e il primo nella lista siano uguali
        assertEquals(materia, lista.getFirst());
    }

    //test per vedere se existsByName funziona
    @Test
    void testExistByName_giusto() {
        Studenti studente = new Studenti("test", "test" , "test".toCharArray());
        studentiRepository.save(studente);
        //creo una materia
        Materie materia = new Materie("info", studente);
        materieRepository.save(materia);
        //controllo che esista con il nome "info"
        assertTrue(materieService.existByName("info", studente));
    }

    @Test
    void testExistByName_sbagliato() {
        Studenti studente = new Studenti("test", "test" , "test".toCharArray());
        studentiRepository.save(studente);
        //creo una materia
        Materie materia = new Materie("info", studente);
        materieRepository.save(materia);
        //controllo che esista con il nome "informatica"
        assertFalse(materieService.existByName("informatica", studente));
    }

    //test per vedere se eliminaMateria funziona
    @Test
    void testCancellaMateria() {
        Studenti studente = new Studenti("test", "test" , "test".toCharArray());
        studentiRepository.save(studente);
        //creo una materia
        Materie materia = new Materie("info", studente);
        materieRepository.save(materia);

        assertTrue(materieService.eliminaMateria(materia.getIdMateria()));
    }


}
