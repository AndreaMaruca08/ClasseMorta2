package app.classeMorta.ClasseMorta.Logic.repository;

import app.classeMorta.ClasseMorta.Logic.models.Materie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterieRepository extends JpaRepository<Materie, Long> {
    boolean existsByNomeMateria(String nomeMateria);
}
