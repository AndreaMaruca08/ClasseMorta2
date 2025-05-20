package app.classeMorta.ClasseMorta.logic.service;

import app.classeMorta.ClasseMorta.logic.dto.MediaRequest;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class MediaServiceTest {

    private final MediaService mediaService;
    private final StudentiRepository studentiRepository;
    private final MaterieRepository materieRepository;
    private final VotiRepository votiRepository;

    @Autowired
    public MediaServiceTest(MediaService mediaService, StudentiRepository studentiRepository, MaterieRepository materieRepository, VotiRepository votiRepository) {
        this.mediaService = mediaService;
        this.studentiRepository = studentiRepository;
        this.materieRepository = materieRepository;
        this.votiRepository = votiRepository;
    }


    @Test
    void testCalcoloMediaPerMateria() {
        //creazioni dello studente per il test
        Studenti studente = new Studenti("Test", "test@gmail.com", "test".toCharArray());

        //salvo in h2
        studentiRepository.save(studente);
        Materie materia = new Materie("info", studente);
        materieRepository.save(materia);

        //creo 2 voti provvisori
        Voti voto = new Voti(8F, studente, materia, LocalDate.now(), PeriodoVoto.PENTAMESTRE);
        votiRepository.save(voto);
        Voti voto1 = new Voti(4F, studente, materia, LocalDate.now(), PeriodoVoto.PENTAMESTRE);
        votiRepository.save(voto1);

        //lista fatta a mano
        var listaManuale = List.of(voto, voto1, 7);

        //calcolo la media a mano
        float mediaManuale = 19F / listaManuale.size();

        //controllo che la media a mano sia uguale a quella fatta con la funzione
        MediaRequest mediaRequest = new MediaRequest(materia.getIdMateria(), studente.getId(), 7, PeriodoVoto.PENTAMESTRE);
        assertEquals(mediaManuale, mediaService.calcolaMediaPerMateria(mediaRequest));

    }

    @Test
    void testMediaTotale() {
        //creazioni dello studente per il test
        Studenti studente = new Studenti("Test", "test@gmail.com", "test".toCharArray());

        //salvo in h2
        studentiRepository.save(studente);

        //materie
        Materie materia = new Materie("info", studente);
        materieRepository.save(materia);
        Materie materia1 = new Materie("storia", studente);
        materieRepository.save(materia1);

        //creo 2 voti provvisori
        Voti voto = new Voti(8F, studente, materia, LocalDate.now(), PeriodoVoto.PENTAMESTRE);
        votiRepository.save(voto);
        Voti voto1 = new Voti(4F, studente, materia, LocalDate.now(), PeriodoVoto.PENTAMESTRE);
        votiRepository.save(voto1);

        //creo 2 voti provvisori per la seconda materia
        Voti voto2 = new Voti(7F, studente, materia1, LocalDate.now(), PeriodoVoto.PENTAMESTRE);
        votiRepository.save(voto2);
        Voti voto3 = new Voti(4F, studente, materia1, LocalDate.now(), PeriodoVoto.PENTAMESTRE);
        votiRepository.save(voto3);

        //calcolo manuale delle medie
        float mediaMat = (8F + 4F) / 2F;
        float mediaMat1 = (7F + 4F) / 2F;
        //calcolo media totale manuale
        float mediaTotManuale = (mediaMat1 + mediaMat) / 2.0F;

        //controllo che la media manuale sia uguale a quella data dalla funzione
        assertEquals(mediaTotManuale, mediaService.calcolaMediaTot(studente.getId(), PeriodoVoto.PENTAMESTRE));

    }


}
