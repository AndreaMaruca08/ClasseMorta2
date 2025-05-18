package app.classeMorta.ClasseMorta.logic.service;

import app.classeMorta.ClasseMorta.logic.dto.MediaRequest;
import app.classeMorta.ClasseMorta.logic.models.Materie;
import app.classeMorta.ClasseMorta.logic.models.Studenti;
import app.classeMorta.ClasseMorta.logic.models.Voti;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;


@Service
public class MediaService {
    private final VotiService votiService;
    private final MaterieService materieService;
    private final StudentiService studentiService;
    private Logger log;

    @Autowired
    public MediaService(VotiService votiService, MaterieService materieService, StudentiService studentiService) {
        this.votiService = votiService;
        this.materieService = materieService;
        this.studentiService = studentiService;
    }

    /**
     * Calcola la media dei voti per una materia di uno studente, con opzione per includere un voto ipotetico
     *
     * @param mediaRequest oggetto contenente idMateria, idStudente e voto ipotetico
     * @return la media calcolata come valore float, 0.0 se non ci sono voti
     */
    public float calcolaMediaPerMateria(MediaRequest mediaRequest) {
        var somma = 0.0F;
        int aumento = 0;
        List<Voti> listaVoti = votiService.getVotiPerMateriaEID(mediaRequest.idMateria(), mediaRequest.idStudente());

        if (listaVoti == null || listaVoti.isEmpty()) {
            return 0.0F;
        }
        for (Voti voti : listaVoti) {
            somma += voti.getVoto();
        }
        if (mediaRequest.ipotetico() != -1) {
            somma += mediaRequest.ipotetico();
            aumento = 1;
        }
        return somma / (listaVoti.size() + aumento);
    }

    /**
     * Calcola la media totale di tutte le materie per uno studente
     *
     * @param idStudente l'ID dello studente per cui calcolare la media
     * @return la media totale come Float, null se non ci sono materie,
     * 0.0F se non ci sono voti in nessuna materia
     */
    public float calcolaMediaTot(Long idStudente) {
        float sommaTot = 0.0F;
        int materieConVoti = 0;
        List<Materie> listaMaterie = materieService.getAllMaterie(studentiService.getStudenteByID(idStudente));
        if (listaMaterie == null) {
            return 0.0F;
        }
        for (Materie materia : listaMaterie) {
            Long idM = materia.getIdMateria();
            float mediaMat = calcolaMediaPerMateria(new MediaRequest(idM, idStudente, -1));
            if (mediaMat != 0.0F) {
                sommaTot += mediaMat;
                materieConVoti++;
            }
        }
        if (materieConVoti == 0) {
            System.out.println("Nessuna materia con voti");
            return 0.0F;
        }

        return sommaTot / materieConVoti;
    }

}