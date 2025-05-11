package app.classeMorta.ClasseMorta.Logic;

import app.classeMorta.ClasseMorta.Logic.Materie.Materie;
import app.classeMorta.ClasseMorta.Logic.Materie.MaterieService;
import app.classeMorta.ClasseMorta.Logic.Voti.Voti;
import app.classeMorta.ClasseMorta.Logic.Voti.VotiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MediaService {
    @Autowired
    private VotiService votiService;
    @Autowired
    private MaterieService materieService;
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

}

