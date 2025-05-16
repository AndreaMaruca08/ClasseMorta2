package app.classeMorta.ClasseMorta.logic.repository;

import app.classeMorta.ClasseMorta.logic.models.Studenti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentiRepository extends JpaRepository<Studenti, Long> {
    @Query("SELECT u.id FROM Studenti u WHERE u.email = :email")
    Long getStudentiIDByEmail(@Param("email") String email);

    Optional<Studenti> findByEmail(String email);

}
