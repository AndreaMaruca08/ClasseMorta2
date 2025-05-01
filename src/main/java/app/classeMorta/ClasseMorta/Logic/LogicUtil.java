package app.classeMorta.ClasseMorta.Logic;

import app.classeMorta.ClasseMorta.Logic.Materie.Materie;
import app.classeMorta.ClasseMorta.Logic.Materie.MaterieService;
import app.classeMorta.ClasseMorta.Logic.Studenti.Studenti;
import app.classeMorta.ClasseMorta.Logic.Studenti.StudentiRepository;
import app.classeMorta.ClasseMorta.Logic.Studenti.StudentiService;
import app.classeMorta.ClasseMorta.Logic.Voti.Voti;
import app.classeMorta.ClasseMorta.Logic.Voti.VotiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static app.classeMorta.ClasseMorta.GUI.GUIUtils.mostraErrore;
import static app.classeMorta.ClasseMorta.GUI.GUIUtils.sceltaYN;

@Component
public class LogicUtil {
    private final StudentiService studentiService;
    private final StudentiRepository studentiRepository;
    private final MaterieService materieService;
    private final VotiService votiService;

    @Autowired
    public LogicUtil(StudentiService studentiService, StudentiRepository studentiRepository, MaterieService materieService, VotiService votiService) {
        this.studentiService = studentiService;
        this.studentiRepository = studentiRepository;
        this.materieService = materieService;
        this.votiService = votiService;
    }
    public boolean isStudentPresent(String email, char[] password){
        Optional<Studenti> tempStudent = studentiRepository.findByEmail(email);
        if (tempStudent.isPresent()) {
            if (studentiService.verificaCredenziali(email, password)) {
                return true;
            }
            else {
                mostraErrore("ERRORE", "Password errata");
                return false;
            }
        }
        else {
            mostraErrore("ERRORE", "Utente non esistente");
            return false;
        }

    }
    public Float calcolaMediaPerMateria(Long idMateria, Long idStudente){
        Float somma = 0.0F;
        List<Voti> listaVoti = votiService.getVotiPerMateriaEID(idMateria, idStudente);
        if(listaVoti == null || listaVoti.isEmpty()){ // AGGIUNGI QUESTO CONTROLLO
            System.out.println("Nessun voto per questa materia");
            return 0.0F;
        }
        for(Voti voti : listaVoti){
            somma += voti.getVoto();
        }
        return somma / listaVoti.size();
    }
    public Float calcolaMediaPerMateria(Long idMateria, Long idStudente, Float votoIpotetico){
        Float somma = 0.0F;
        List<Voti> listaVoti = votiService.getVotiPerMateriaEID(idMateria, idStudente);

        if(listaVoti == null || listaVoti.isEmpty()){ // AGGIUNGI QUESTO CONTROLLO
            System.out.println("Nessun voto per questa materia");
            return 0.0F;
        }
        for(Voti voti : listaVoti){
            somma += voti.getVoto();
        }
        somma += votoIpotetico;
        return somma / (listaVoti.size() + 1);
    }
    public Float calcolaMediaTot(Long idStudente){
        float sommaTot = 0.0F;
        int materieConVoti = 0;
        List<Materie> listaMaterie = materieService.getAllMaterie();
        if(listaMaterie == null){
            System.out.println("Errore in calcolaMediaTot in LogicUtil");
            return null;
        }
        for(Materie materia : listaMaterie){
            Long idM = materia.getIdMateria();
            Float mediaMat = calcolaMediaPerMateria(idM, idStudente);
            if(mediaMat != null){ //controllo aggiuntivo
                if(mediaMat != 0.0F) {
                    sommaTot += mediaMat;
                    materieConVoti++;
                }
            }
        }

        if(materieConVoti == 0){
            System.out.println("Nessuna materia con voti");
            return 0.0F;
        }

        return sommaTot / materieConVoti;
    }
    public boolean cancellaMateria(Materie materia){
        System.out.println("ENTRO IN CANCELLA");
        if(sceltaYN("cancellare", "Vuoi davvero cancellare questa Materia?") == 0) {
            return materieService.eliminaMateria(materia.getIdMateria());
        }
        else return false;
    }




}
