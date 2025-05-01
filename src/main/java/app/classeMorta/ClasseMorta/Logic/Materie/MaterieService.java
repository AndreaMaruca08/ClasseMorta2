package app.classeMorta.ClasseMorta.Logic.Materie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterieService {
    private final MaterieRepository materieRepository;

    @Autowired
    public MaterieService(MaterieRepository materieRepository) {
        this.materieRepository = materieRepository;
    }

    public List<Materie> getAllMaterie(){
        return materieRepository.findAll();
    }
    public void saveMateria(String nomeMateria){
        try {
            Materie newMateria = new Materie(nomeMateria);
            materieRepository.save(newMateria);
        }catch (Exception e){
            System.out.println("ERRORE in 'salvaMateria' in MateriaService");
        }
    }

    public boolean existByName(String nomeMateria){
        return materieRepository.existsByNomeMateria(nomeMateria);
    }
    public boolean eliminaMateria(Long idMateria) {
        Optional<Materie> materia = materieRepository.findById(idMateria);
        if (materia.isPresent()) {
            materieRepository.delete(materia.get()); // grazie a Cascade.ALL elimina anche i voti
            return true;
        }

        return false; // se non esiste la materia
    }
}
