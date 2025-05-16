package app.classeMorta.ClasseMorta.logic.service;

import app.classeMorta.ClasseMorta.logic.models.Materie;
import app.classeMorta.ClasseMorta.logic.models.Studenti;
import app.classeMorta.ClasseMorta.logic.models.Voti;
import app.classeMorta.ClasseMorta.logic.repository.VotiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VotiService {

    private final VotiRepository votiRepository;

    @Autowired
    public VotiService(VotiRepository votiRepository) {
        this.votiRepository = votiRepository;
    }

    public List<Voti> getVotiPerMateriaEID(Long idMateria, Long idStudente) {
        return votiRepository.findAllByStudente_IdAndMateria_IdMateria(idStudente, idMateria);
    }

    public void salvaVoto(Float voto, Materie materia, Studenti studenti) {
        Voti voto1 = new Voti(voto, studenti, materia, LocalDate.now());
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

}
