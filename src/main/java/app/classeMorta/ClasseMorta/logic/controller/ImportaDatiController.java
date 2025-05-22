package app.classeMorta.ClasseMorta.logic.controller;

import app.classeMorta.ClasseMorta.logic.dto.ConditionalResponseEntity;
import app.classeMorta.ClasseMorta.logic.dto.GetPhpSessidRequest;
import app.classeMorta.ClasseMorta.logic.service.ImportaVotiFromSpaggiariService;
import app.classeMorta.ClasseMorta.logic.service.MaterieService;
import app.classeMorta.ClasseMorta.logic.service.StudentiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static app.classeMorta.ClasseMorta.logic.dto.ConditionalResponseEntity.*;

@Slf4j
@RestController
@RequestMapping("/importa")
public class ImportaDatiController {

    private final ImportaVotiFromSpaggiariService importaVotiService;
    private final MaterieService materieService;
    private final StudentiService studentiService;
    @Autowired
    public ImportaDatiController(ImportaVotiFromSpaggiariService importaVotiService, MaterieService materieService, StudentiService studentiService) {
        this.importaVotiService = importaVotiService;
        this.materieService = materieService;
        this.studentiService = studentiService;
    }

    @PostMapping("/getPHPSESSID")
    public ResponseEntity<ConditionalResponseEntity<String>> getPHPSESSID(@RequestBody GetPhpSessidRequest req) {
        log.trace("Attempt to get PHPSESSID");
        String phpsessid = importaVotiService.ottieniPhpsessid(req.codiceUtente(), req.password());
        return phpsessid.isBlank() ?
                notFound("PHPSESSID non trovato") : success(phpsessid);
    }

    @GetMapping("/voti")
    public ResponseEntity<ConditionalResponseEntity<Object>> importaDati(@RequestParam String phpSessId, @RequestParam Long studenteID) {
        log.trace("Attempt to import data from ClasseViva");
        return importaVotiService.importaVoti(phpSessId, studenteID).isEmpty() ?
                notFound("Nessun dato da importare") : success(importaVotiService.importaVoti(phpSessId, studenteID));
    }

    @PostMapping("/sync-voti")
    public ResponseEntity<ConditionalResponseEntity<String>> syncVoti(@RequestParam String phpSessId, @RequestParam Long studenteID) {
        // 1. Elimina tutte le materie (che elimina anche voti associati)
        materieService.deleteAll(studentiService.getStudenteByID(studenteID));
        // 2. Importa e salva i dati nuovi
        importaVotiService.importaVoti(phpSessId, studenteID);
        return success("Voti sincronizzati correttamente");
    }


}