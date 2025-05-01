package app.classeMorta.ClasseMorta.Logic.Materie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterieRepository extends JpaRepository<Materie, Long> {
    List<Materie> findAll();
    boolean existsByNomeMateria(String nomeMateria);

}
