package app.classeMorta.ClasseMorta.logic.service;

import app.classeMorta.ClasseMorta.logic.PeriodoVoto;
import app.classeMorta.ClasseMorta.logic.models.Materie;
import app.classeMorta.ClasseMorta.logic.models.Studenti;
import app.classeMorta.ClasseMorta.logic.models.Voti;
import app.classeMorta.ClasseMorta.logic.repository.MaterieRepository;
import app.classeMorta.ClasseMorta.logic.repository.StudentiRepository;
import app.classeMorta.ClasseMorta.logic.repository.VotiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VotiService {

    private final VotiRepository votiRepository;
    private final MaterieRepository materieRepository;
    private final StudentiRepository studentiRepository;

    @Autowired
    public VotiService(VotiRepository votiRepository, MaterieRepository materieRepository, StudentiRepository studentiRepository) {
        this.votiRepository = votiRepository;
        this.materieRepository = materieRepository;
        this.studentiRepository = studentiRepository;
    }

    public List<Voti> getVotiPerMateriaEIDAndPeriodo(Long idMateria, Long idStudente, PeriodoVoto periodo) {
        if (periodo == PeriodoVoto.ANNO) {
            // prendi tutti i voti della materia per lo studente, indipendentemente dal periodo
            return votiRepository.findAllByStudente_IdAndMateria_IdMateria(idStudente, idMateria);
        } else {
            return votiRepository.findAllByStudente_IdAndMateria_IdMateriaAndPeriodo(idStudente, idMateria, periodo);
        }
    }

    public void salvaVoto(Float voto, Materie materia, Studenti studenti, PeriodoVoto periodoVoto) {
        Voti voto1 = new Voti(voto, studenti, materia, LocalDate.now(), periodoVoto);
        votiRepository.save(voto1);
    }

    public boolean cancellaVoto(Long id) {
        Optional<Voti> voto = votiRepository.findById(id);
        if (voto.isPresent()) {
            votiRepository.delete(voto.get());
            return true;
        }
        return false; // se non esiste la materia
    }

    public void importaVotiFromClasseViva(String phpCodice, Long idStudente){
        new ImportaVotiFromSpaggiariService(materieRepository, studentiRepository).importaVoti(phpCodice, idStudente );
    }

    public String getCodiceSpaggiari(String codice, String password){
        return new ImportaVotiFromSpaggiariService(materieRepository, studentiRepository).ottieniPhpsessid(codice, password);
    }

}