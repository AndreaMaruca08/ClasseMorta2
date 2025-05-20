package app.classeMorta.ClasseMorta.logic;

import app.classeMorta.ClasseMorta.logic.models.Voti;
import app.classeMorta.ClasseMorta.logic.models.Studenti;
import app.classeMorta.ClasseMorta.logic.models.Materie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class VotiParser {

    public static List<Voti> parseVoti(String html, Studenti studente, MateriaResolver materiaResolver) {
        List<Voti> risultati = new ArrayList<>();
        Document doc = Jsoup.parse(html);

        // Seleziona tutte le righe materia (ogni riga = 1 materia e suoi voti)
        Elements righeMateria = doc.select("tr.riga_materia_componente");

        Set<String> votiUnici = new HashSet<>();

        for (Element rigaMateria : righeMateria) {
            // Estrai il nome della materia
            Element divMateria = rigaMateria.selectFirst("div.materia_desc");
            String nomeMateria = divMateria != null ? divMateria.text().trim() : "Sconosciuta";
            Materie materia = materiaResolver.trovaPerNome(nomeMateria);
            if (materia == null) continue;

            // Trova tutti i voti nella riga della materia
            Elements tdVoti = rigaMateria.select("td.cella_voto");

            for (Element tdVoto : tdVoti) {
                // Estrai la data del voto
                Element dataSpan = tdVoto.selectFirst("span.voto_data.cella_data");
                LocalDate data = null;
                if (dataSpan != null && !dataSpan.text().isBlank()) {
                    String dataString = dataSpan.text().trim();
                    try {
                        // Gestisce date tipo "23/01": assume l'anno corrente
                        data = LocalDate.parse(
                                dataString + "/" + LocalDate.now().getYear(),
                                DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        );
                        if (data.isAfter(LocalDate.now())) {
                            data = data.minusYears(1);
                        }
                    } catch (Exception e) {
                        data = null;
                    }
                }

                // Estrai il valore del voto
                Element votoP = tdVoto.selectFirst("p.s_reg_testo.cella_trattino");
                Float voto = null;
                if (votoP != null && !votoP.text().isBlank()) {
                    String votoString = votoP.text().replace("½", ".5").replace(",", ".").trim();
                    try {
                        voto = Float.valueOf(votoString);
                    } catch (Exception ignore) {}
                }

                // Calcola il periodo scolastico a partire dalla data del voto
                PeriodoVoto periodo = getPeriodoVoto(data);

                if (voto != null && data != null && periodo != null) {
                    // Puoi usare una chiave combinata come stringa, ad esempio:
                    String chiave = materia.getIdMateria() + "-" + data + "-" + voto + "-" + periodo;
                    if (!votiUnici.contains(chiave)) {
                        votiUnici.add(chiave);
                        Voti v = new Voti(voto, studente, materia, data, periodo);
                        materia.getVoti().add(v);
                        risultati.add(v);
                    }
                }
            }
        }
        return risultati;
    }

    private static PeriodoVoto getPeriodoVoto(LocalDate data) {
        PeriodoVoto periodo = null;
        if (data != null) {
            int month = data.getMonthValue();
            // Puoi cambiare la logica a seconda della tua suddivisione scolastica reale
            if (month >= 9) { // Da settembre a dicembre: Trimestre
                periodo = PeriodoVoto.TRIMESTRE;
            } else if (month <= 6) { // Da gennaio a giugno: Pentamestre
                periodo = PeriodoVoto.PENTAMESTRE;
            }
        }
        return periodo;
    }

    /**
     * Interfaccia da implementare per risolvere l'oggetto Materie dato il nome.
     */
    public interface MateriaResolver {
        Materie trovaPerNome(String nomeMateria);
    }
}