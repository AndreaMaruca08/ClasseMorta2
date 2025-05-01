package app.classeMorta.ClasseMorta.Logic.Studenti;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface StudentiRepository extends JpaRepository<Studenti, Long>{
    @Query("SELECT u.id FROM Studenti u WHERE u.email = :email")
    Long getStudentiIDByEmail(@Param("email") String email);
    Optional<Studenti> findByEmail(String email);

}
