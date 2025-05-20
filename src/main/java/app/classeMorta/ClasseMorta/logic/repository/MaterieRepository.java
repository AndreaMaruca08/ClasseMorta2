package app.classeMorta.ClasseMorta.logic.repository;

import app.classeMorta.ClasseMorta.logic.models.Materie;
import app.classeMorta.ClasseMorta.logic.models.Studenti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterieRepository extends JpaRepository<Materie, Long> {
    boolean existsByNomeMateriaAndStudente(String nomeMateria, Studenti studente);

    List<Materie> findAllByStudente(Studenti studente);

    Optional<Materie> findByNomeMateriaIgnoreCaseAndStudente(String nomeMateria, Studenti studente);

    void deleteAllByStudente(Studenti studente);
}
