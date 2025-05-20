package app.classeMorta.ClasseMorta.logic.repository;

import app.classeMorta.ClasseMorta.logic.PeriodoVoto;
import app.classeMorta.ClasseMorta.logic.models.Voti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VotiRepository extends JpaRepository<Voti, Long> {

    List<Voti> findAllByStudente_IdAndMateria_IdMateriaAndPeriodo(Long idStudente, Long idMateria, PeriodoVoto periodoVoto);

    List<Voti> findAllByStudente_IdAndMateria_IdMateria(Long idStudente, Long idMateria);
}
