package app.classeMorta.ClasseMorta.logic.controller;

import app.classeMorta.ClasseMorta.logic.dto.ConditionalResponseEntity;
import app.classeMorta.ClasseMorta.logic.dto.MediaRequest;
import app.classeMorta.ClasseMorta.logic.PeriodoVoto;
import app.classeMorta.ClasseMorta.logic.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static app.classeMorta.ClasseMorta.logic.dto.ConditionalResponseEntity.*;

@Slf4j
@RestController
@RequestMapping("/media")
public class MediaController {
    private final MediaService mediaService;

    @Autowired
    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping("/calcolaMedia")
    public ResponseEntity<ConditionalResponseEntity<Object>> getMediaPerMateriaEStudente(@RequestBody MediaRequest mediaRequest) {
        log.trace("Attempt to get media per materia {} e studente {}", mediaRequest.idMateria(), mediaRequest.idStudente());

        return mediaService.calcolaMediaPerMateria(mediaRequest) == 0.0F ?
                notFound("Nessuna media") : success(mediaService.calcolaMediaPerMateria(mediaRequest));
    }

    @PostMapping("/calcolaMediaIpotetica")
    public ResponseEntity<ConditionalResponseEntity<Object>> getMediaPerMateriaEStudente_ipotetica(@RequestBody MediaRequest mediaRequest) {
        log.trace("Attempt to get media per materia {} e studente {} with {} as voto ipotetico", mediaRequest.idMateria(), mediaRequest.idStudente(), mediaRequest.ipotetico());

        return mediaService.calcolaMediaPerMateria(mediaRequest) == 0.0F ?
                notFound("Nessuna media") : success(mediaService.calcolaMediaPerMateria(mediaRequest));
    }

    @PostMapping("/mediaAll")
    public ResponseEntity<ConditionalResponseEntity<Object>> getMediaTot(@RequestBody MediaRequest mediaRequest) {
        log.trace("Attempt to get media per materia");
        return mediaService.calcolaMediaTot(mediaRequest.idStudente(), mediaRequest.periodoVoto()) == 0.0F ?
                notFound("Nessuna media totale") : success(mediaService.calcolaMediaTot(mediaRequest.idStudente(), PeriodoVoto.PENTAMESTRE));
    }

}