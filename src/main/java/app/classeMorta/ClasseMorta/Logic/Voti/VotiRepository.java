package app.classeMorta.ClasseMorta.Logic.Voti;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VotiRepository extends JpaRepository <Voti, Long> {

    @Query("SELECT v FROM Voti v WHERE v.studente.id = :idStudente AND v.materia.idMateria = :idMateria")
    List<Voti> findAllByStudenteIdAndMateriaId(@Param("idStudente") Long idStudente, @Param("idMateria") Long idMateria);





}
