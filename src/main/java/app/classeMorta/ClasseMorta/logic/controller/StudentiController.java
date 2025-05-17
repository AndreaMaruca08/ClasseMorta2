package app.classeMorta.ClasseMorta.logic.controller;

import app.classeMorta.ClasseMorta.logic.ConditionalResponseEntity;
import app.classeMorta.ClasseMorta.logic.dto.EmailFound;
import app.classeMorta.ClasseMorta.logic.dto.LoginRequest;
import app.classeMorta.ClasseMorta.logic.dto.SaveStudent;
import app.classeMorta.ClasseMorta.logic.models.Studenti;
import app.classeMorta.ClasseMorta.logic.service.StudentiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static app.classeMorta.ClasseMorta.logic.ConditionalResponseEntity.*;

@Slf4j
@RestController
@RequestMapping("/studenti")
public class StudentiController {

    private final StudentiService studentiService;

    @Autowired
    public StudentiController(StudentiService studentiService) {
        this.studentiService = studentiService;
    }

    //GET /studenti/id-by-email?email=example@mail.com
    @GetMapping("/id-by-email")
    public ResponseEntity<ConditionalResponseEntity<Object>> getIdByEmail(@RequestParam String email) {
        log.trace("Attempt to get student id by email: {}", email);
        Long id = studentiService.getStudentIdByEmail(email);

        if (id == null)
            return notFound(new EmailFound(false, "Email non trovata"));
        return success(id);
    }

    @PostMapping("/login")
    public ResponseEntity<ConditionalResponseEntity<String>> verificaCredenziali(@RequestBody LoginRequest loginRequest) {
        log.trace("Attempt to login from user: {}", loginRequest.email());
        return studentiService.verificaCredenziali(loginRequest) ? success("Login riuscito") : unauthorized("Login fallito");
    }

    //GET /studenti/email-used?email=...
    @GetMapping("/email-used")
    public ResponseEntity<ConditionalResponseEntity<Object>> isEmailUsed(@RequestParam String email) {
        log.trace("Attempt to check if email '{}' is used", email);
        return studentiService.isEmailUsed(email)
                ? success(new EmailFound(true, "Email già utilizzata"))
                : notFound(new EmailFound(false, "Email non utilizzata"));
    }

    @PostMapping("/saveStudente")
    public ResponseEntity<ConditionalResponseEntity<Object>> salvaStudente(@RequestBody Studenti studente) {
        log.trace("Attempt to save studente: {}", studente);
        return studentiService.salvaStudente(studente.getName(), studente.getEmail(), studente.getPassword())
                ? success(new SaveStudent(true, "Studente salvato con successo"))
                : badRequest(new SaveStudent(false, "Errore nel salvataggio dello studente"));
    }
}
