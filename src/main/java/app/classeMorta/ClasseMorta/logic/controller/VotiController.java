package app.classeMorta.ClasseMorta.logic.controller;

import static app.classeMorta.ClasseMorta.logic.dto.ConditionalResponseEntity.*;

import app.classeMorta.ClasseMorta.logic.dto.ConditionalResponseEntity;
import app.classeMorta.ClasseMorta.logic.PeriodoVoto;
import app.classeMorta.ClasseMorta.logic.dto.SaveObject;
import app.classeMorta.ClasseMorta.logic.dto.SaveVotoRequest;
import app.classeMorta.ClasseMorta.logic.service.MaterieService;
import app.classeMorta.ClasseMorta.logic.service.StudentiService;
import app.classeMorta.ClasseMorta.logic.service.VotiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/voti")
public class VotiController {
    private final VotiService votiService;
    private final StudentiService studentiService;
    private final MaterieService materieService;

    @Autowired
    public VotiController(VotiService votiService, StudentiService studentiService, MaterieService materieService) {
        this.votiService = votiService;
        this.studentiService = studentiService;
        this.materieService = materieService;
    }

    @GetMapping("/VotiPerMateria")
    public ResponseEntity<ConditionalResponseEntity<Object>> getVotiPerMateriaEID(@RequestParam Long idMateria, @RequestParam Long idStudente, @RequestParam PeriodoVoto periodo) {
        log.trace("Attempt to get voti per materia {} e studente {}", idMateria, idStudente);
        return votiService.getVotiPerMateriaEIDAndPeriodo(idMateria, idStudente, periodo).isEmpty() ?
                notFound("Nessun voto ") : success(votiService.getVotiPerMateriaEIDAndPeriodo(idMateria, idStudente, periodo));
    }

    @PostMapping("/saveVoto")
    public ResponseEntity<ConditionalResponseEntity<Object>> salvaVoto(@RequestBody SaveVotoRequest saveVotoRequest) {
        log.trace("Attempt to save vote: {}", saveVotoRequest);
        votiService.salvaVoto(
                saveVotoRequest.voto(),
                materieService.getMateriaByID(saveVotoRequest.idMateria()),
                studentiService.getStudenteByID(saveVotoRequest.idStudente()),
                saveVotoRequest.periodoVoto());

        return success(new SaveObject(true, "Voto salvato con successo"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ConditionalResponseEntity<Object>> cancellaVoto(@PathVariable Long id) {
        log.trace("Attempt to delete vote with id: {}", id);
        if (votiService.cancellaVoto(id)) {
            return success("Voto cancellato con successo");
        }
        return notFound("Voto non trovato");
    }
}