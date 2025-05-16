package app.classeMorta.ClasseMorta.logic.repository;

import app.classeMorta.ClasseMorta.logic.models.Materie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterieRepository extends JpaRepository<Materie, Long> {
    boolean existsByNomeMateria(String nomeMateria);
}
