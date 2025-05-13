package app.classeMorta.ClasseMorta.Logic.repository;

import app.classeMorta.ClasseMorta.Logic.models.Voti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VotiRepository extends JpaRepository <Voti, Long> {

    List<Voti> findAllByStudente_IdAndMateria_IdMateria(Long idStudente, Long idMateria);

}
