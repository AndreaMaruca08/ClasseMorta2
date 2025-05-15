package app.classeMorta.ClasseMorta.Logic.controller;

import app.classeMorta.ClasseMorta.Logic.dto.LoginRequest;
import app.classeMorta.ClasseMorta.Logic.models.Studenti;
import app.classeMorta.ClasseMorta.Logic.service.StudentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
                    "success", false,
                    "message", "ID non trovato"
            ));
        }
        return ResponseEntity.ok(Map.of(
                "success", true,
                "id", id
        ));
    }

    //POST /studenti/login  con JSON nel body: {"email": "...", "password": "..."}
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> verificaCredenziali(@RequestBody LoginRequest loginRequest) {
        boolean accesso = studentiService.verificaCredenziali(loginRequest);
        if (accesso) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Login riuscito"
            ));
        } else {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Login fallito"
            ));
        }
    }

    //GET /studenti/email-used?email=...
    @GetMapping("/email-used")
    public ResponseEntity<Map<String, Object>> isEmailUsed(@RequestParam String email) {
        boolean exists = studentiService.isEmailUsed(email);
        if (exists) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "emailUsed", true
            ));
        } else {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "emailUsed", false,
                    "message", "Email non trovata"
            ));
        }
    }

    @PostMapping("/saveStudente")
    public ResponseEntity<Map<String, Object>> salvaStudente(@RequestBody Studenti studente){
        boolean risposta = studentiService.salvaStudente(studente.getName(), studente.getEmail(), studente.getPassword());
        if(risposta){
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "studentSaved", true
            ));
        } else {
            return ResponseEntity.status(400).body(Map.of(
                    "success", false,
                    "studentSaved", false,
                    "messaggio", "email già in uso"
            ));
        }
    }
}
