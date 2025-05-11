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

    /**
     * <b>Funzione che controlla che email e password siano corrette per effettuare l'accesso con un account</b>
     * @param email necessaria per il controllo
     * @param password necessaria per il controllo
     * @return valore booleano, se <code>true</code> significa che email e password sono corrette, se <code>false</code> c'è stato un errore
     */
    public boolean isStudentPresent(String email, char[] password) {
        Optional<Studenti> tempStudent = studentiRepository.findByEmail(email);
        if (tempStudent.isPresent()) {
            if (studentiService.verificaCredenziali(email, password)) {
                return true;
            } else {
                mostraErrore("ERRORE", "Password errata");
                return false;
            }
        } else {
            mostraErrore("ERRORE", "Utente non esistente");
            return false;
        }

    }




    /**
     * <b>Funzione che cancella una materia che si vuole</b>
     * @param materia da cancellare
     * @return valore <code>boolean</code> che restituisce vero se la materia è stata cancellata correttamente
     */
    public boolean cancellaMateria(Materie materia) {
        System.out.println("ENTRO IN CANCELLA");
        if (sceltaYN("cancellare", "Vuoi davvero cancellare questa Materia?") == 0) {
            return materieService.eliminaMateria(materia.getIdMateria());
        } else return false;
    }

    /**
     * <b>Funzione che aggiunge un voto <code>Voti</code> a uno studente in una specifica materia</b>
     * @param voto da mettere
     * @param materia alla quale aggiungere il voto
     * @param studenti alla quale appartiene il voto
     */
    public void aggiungiVoto(Float voto, Materie materia, Studenti studenti) {
        votiService.salvaVoto(voto, materia, studenti);
    }

    /**
     * <b>Funzione che cancella un voto </b>
     * @param voto da cancellare
     * @return valore <code>boolean</code>, se <code>true</code> vuol dire che è andato tutto a buon fine
     */
    public boolean cancellaVoto(Voti voto) {
        if (sceltaYN("cancellare", "Vuoi davvero cancellare questo Voto?") == 0) {
            return votiService.cancellaVoto(voto.getVotoID());
        } else return false;
    }


}
