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
    //funzioni per il calcolo della media

    /**
     * <b>Funzione che restituisce la media calcolata per materia di uno studente</b>
     * @param idMateria per calcolare la media della materia giusta
     * @param idStudente per sapere di quale studente è la media da calcolare
     * @return valore in <code>Float</code> della media per materia
     */
    public Float calcolaMediaPerMateria(Long idMateria, Long idStudente) {
        return calcolaMediaPerMateria(idMateria, idStudente, 0);
    }

    /**
     * <b>Funzione che restituisce la media calcolata per materia di uno studente ma con un voto in più</b>
     * @param idMateria per calcolare la media della materia giusta
     * @param idStudente per sapere di quale studente è la media da calcolare
     * @param votoIpotetico valore in più per calcolare una media ipotetica con un voto in più
     * @return valore in <code>Float</code> della media per materia
     */
    public Float calcolaMediaPerMateria(Long idMateria, Long idStudente, float votoIpotetico) {
        var somma = 0.0F;
        int aumento = 0;
        List<Voti> listaVoti = votiService.getVotiPerMateriaEID(idMateria, idStudente);

        if (listaVoti == null || listaVoti.isEmpty()) {
            System.out.println("Nessun voto per questa materia");
            return 0.0F;
        }
        for (Voti voti : listaVoti) {
            somma += voti.getVoto();
        }
        somma += votoIpotetico;
        if(votoIpotetico != 0)
            aumento = 1;
        return somma / (listaVoti.size() + aumento);
    }

    /**
     * <b>Funzione che calcola la media totale tra tutte le materie</b>
     * @param idStudente dello studente della quale serve la media totale
     * @return valore in <code>Float</code> della media totale tra tutte le materie
     */
    public Float calcolaMediaTot(Long idStudente) {
        float sommaTot = 0.0F;
        int materieConVoti = 0;
        List<Materie> listaMaterie = materieService.getAllMaterie();
        if (listaMaterie == null) {
            System.out.println("Errore in calcolaMediaTot in LogicUtil");
            return null;
        }
        for (Materie materia : listaMaterie) {
            Long idM = materia.getIdMateria();
            Float mediaMat = calcolaMediaPerMateria(idM, idStudente);
            if (mediaMat != null) { //controllo aggiuntivo
                if (mediaMat != 0.0F) {
                    sommaTot += mediaMat;
                    materieConVoti++;
                }
            }
        }

        if (materieConVoti == 0) {
            System.out.println("Nessuna materia con voti");
            return 0.0F;
        }

        return sommaTot / materieConVoti;
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
