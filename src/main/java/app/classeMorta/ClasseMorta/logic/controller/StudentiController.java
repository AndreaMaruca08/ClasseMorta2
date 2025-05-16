package app.classeMorta.ClasseMorta.logic.controller;

import app.classeMorta.ClasseMorta.logic.ConditionalResponseEntity;
import app.classeMorta.ClasseMorta.logic.dto.LoginRequest;
import app.classeMorta.ClasseMorta.logic.models.Studenti;
import app.classeMorta.ClasseMorta.logic.service.StudentiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static app.classeMorta.ClasseMorta.logic.ConditionalResponseEntity.success;
import static app.classeMorta.ClasseMorta.logic.ConditionalResponseEntity.unauthorized;

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
    public ResponseEntity<Map<String, Object>> getIdByEmail(@RequestParam String email) {
        Long id = studentiService.getStudentIdByEmail(email);
        if (id == null) {
            return ResponseEntity.status(404).body(Map.of(
                    "successMessage", false,
                    "message", "ID non trovato"
            ));
        }
        return ResponseEntity.ok(Map.of(
                "successMessage", true,
                "id", id
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<ConditionalResponseEntity<String>> verificaCredenziali(@RequestBody LoginRequest loginRequest) {
        log.trace("Attempt to login from user: {}", loginRequest.getEmail());
        return studentiService.verificaCredenziali(loginRequest) ? success("Login riuscito") : unauthorized("Login fallito");
    }

    //GET /studenti/email-used?email=...
    @GetMapping("/email-used")
    public ResponseEntity<Map<String, Object>> isEmailUsed(@RequestParam String email) {
        boolean exists = studentiService.isEmailUsed(email);
        if (exists) {
            return ResponseEntity.ok(Map.of(
                    "successMessage", true,
                    "emailUsed", true
            ));
        } else {
            /**
             * {
             *   success: false,
             *   message: {
             *    emailUserd: false
             *   }
             * }
             */

            return ResponseEntity.status(404).body(Map.of(
                    "successMessage", false,
                    "emailUsed", false,
                    "message", "Email non trovata"
            ));
        }
    }

    @PostMapping("/saveStudente")
    public ResponseEntity<Map<String, Object>> salvaStudente(@RequestBody Studenti studente) {
        boolean risposta = studentiService.salvaStudente(studente.getName(), studente.getEmail(), studente.getPassword());
        if (risposta) {
            return ResponseEntity.ok(Map.of(
                    "successMessage", true,
                    "studentSaved", true
            ));
        } else {
            return ResponseEntity.status(400).body(Map.of(
                    "successMessage", false,
                    "studentSaved", false,
                    "messaggio", "email già in uso"
            ));
        }
    }
}
