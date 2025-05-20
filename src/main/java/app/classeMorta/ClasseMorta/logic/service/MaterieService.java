package app.classeMorta.ClasseMorta.logic.service;

import app.classeMorta.ClasseMorta.logic.models.Materie;
import app.classeMorta.ClasseMorta.logic.models.Studenti;
import app.classeMorta.ClasseMorta.logic.repository.MaterieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MaterieService {
    private final MaterieRepository materieRepository;

    @Autowired
    public MaterieService(MaterieRepository materieRepository) {
        this.materieRepository = materieRepository;
    }

    @Transactional
    public Materie getMateriaById(Long id) {
        // la sessione è aperta in questo metodo
        Materie materia = materieRepository.findById(id).get();
        Studenti studente = materia.getStudente(); // ok, Hibernate può caricare i dati
        // ...
        return materia;
    }

    public Materie getMateriaByID(Long id) {return materieRepository.getReferenceById(id);}

    public List<Materie> getAllMaterie(Studenti studente) {
        return materieRepository.findAllByStudente(studente);
    }

    public void saveMateria(String nomeMateria, Studenti studente) {
        try {
            Materie newMateria = new Materie(nomeMateria, studente);
            materieRepository.save(newMateria);
        } catch (Exception e) {
            System.out.println("ERRORE in 'salvaMateria' in MateriaService");
        }
    }

    public boolean existByName(String nomeMateria, Studenti studente) {
        return materieRepository.existsByNomeMateriaAndStudente(nomeMateria, studente);
    }

    public boolean eliminaMateria(Long idMateria) {
        var materia = materieRepository.findById(idMateria);
        if (materia.isPresent()) {
            materieRepository.delete(materia.get()); // grazie a Cascade.ALL elimina anche i voti
            return true;
        }

        return false; // se non esiste la materia
    }

    public void deleteAll(Studenti studente){
        materieRepository.deleteAllByStudente(studente);
    }

}