package app.classeMorta.ClasseMorta.logic.controller;

import static app.classeMorta.ClasseMorta.logic.dto.ConditionalResponseEntity.*;

import app.classeMorta.ClasseMorta.logic.dto.ConditionalResponseEntity;
import app.classeMorta.ClasseMorta.logic.models.Materie;
import app.classeMorta.ClasseMorta.logic.models.Studenti;
import app.classeMorta.ClasseMorta.logic.service.MaterieService;
import app.classeMorta.ClasseMorta.logic.service.StudentiService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/materie")
public class MaterieController {

    private final MaterieService materieService;
    private final StudentiService studentiService;

    @Autowired
    public MaterieController(MaterieService materieService, StudentiService studentiService) {
        this.materieService = materieService;
        this.studentiService = studentiService;
    }

    @GetMapping
    public ResponseEntity<ConditionalResponseEntity<Object>> getAllMaterie(@RequestParam Long idStudente) {
        log.trace("Attempt to get all materie");
        Studenti studente = studentiService.getStudenteByID(idStudente);
        List<Materie> materie = materieService.getAllMaterie(studente);
        if (materie.isEmpty()) return notFound("Nessuna materia trovata");
        List<MateriaRequest> output = materie.stream()
                .map(MateriaRequest::from)
                .toList();
        return success(output);
    }

    @PostMapping
    public ResponseEntity<ConditionalResponseEntity<Object>> creaMateria(@RequestBody MateriaRequest request) {
        log.trace("Attempt to create materia: {}", request.getNome());
        Studenti studente = studentiService.getStudenteByID(request.getIdStudente());
        if (materieService.existByName(request.getNome(), studente)) {
            return failed("Materia già esistente");
        }
        materieService.saveMateria(request.getNome(), studente);
        return success("Materia salvata con successo");
    }

    @PostMapping("/exists")
    public ResponseEntity<ConditionalResponseEntity<Object>> checkMateria(@RequestBody MateriaRequest request) {
        log.trace("Check if materia exists: {}", request.getNome());
        Studenti studente = studentiService.getStudenteByID(request.getIdStudente());
        return success(materieService.existByName(request.getNome(), studente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ConditionalResponseEntity<Object>> eliminaMateria(@PathVariable Long id) {
        log.trace("Attempt to delete materia with id: {}", id);
        if (materieService.eliminaMateria(id)) {
            return success("Materia eliminata");
        }
        return notFound("Materia non trovata");
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MateriaRequest {
        private String nome;
        private Long idStudente;
        public static MateriaRequest from(Materie m) {
            return new MateriaRequest(m.getNomeMateria(), m.getIdMateria());
        }

    }
}