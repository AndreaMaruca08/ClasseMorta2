package app.classeMorta.ClasseMorta;

import app.classeMorta.ClasseMorta.Logic.Materie.Materie;
import app.classeMorta.ClasseMorta.Logic.Materie.MaterieRepository;
import app.classeMorta.ClasseMorta.Logic.Materie.MaterieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class MaterieServiceTest {

    private final MaterieService materieService;
    private final MaterieRepository materieRepository;

    @Autowired
    public MaterieServiceTest(MaterieService materieService, MaterieRepository materieRepository) {
        this.materieService = materieService;
        this.materieRepository = materieRepository;
    }
    //test di getAllMaterie
    @Test
    void testGetAllMaterie(){
        //creo due materia
        Materie materia = new Materie("info");
        materieRepository.save(materia);
        Materie materia2 = new Materie("mate");
        materieRepository.save(materia2);

        //lista fatta da me
        List<Materie> listaP = List.of(materia, materia2);
        //lista fatta con la funzione del service
        List<Materie> lista = materieService.getAllMaterie();

        assertEquals(lista, listaP);
    }
    //test per vedere se salva la materia
    @Test
    void testSaveMateria(){
        //creo una materia
        Materie materia = new Materie("info");
        materieRepository.save(materia);
        //lista delle materie che dovrebbe essere solo 1
        List<Materie> lista = materieRepository.findAll();
        //controllo che la materia e il primo nella lista siano uguali
        assertEquals(materia, lista.getFirst());
    }

    //test per vedere se existsByName funziona
    @Test
    void testExistByName_giusto(){
        //creo una materia
        Materie materia = new Materie("info");
        materieRepository.save(materia);
        //controllo che esista con il nome "info"
        assertTrue(materieService.existByName("info"));
    }
    @Test
    void testExistByName_sbagliato(){
        //creo una materia
        Materie materia = new Materie("info");
        materieRepository.save(materia);
        //controllo che esista con il nome "informatica"
        assertFalse(materieService.existByName("informatica"));
    }

    //test per vedere se eliminaMateria funziona
    @Test
    void testCancellaMateria(){
        //creo una materia
        Materie materia = new Materie("info");
        materieRepository.save(materia);

        assertTrue(materieService.eliminaMateria(materia.getIdMateria()));
    }







}
