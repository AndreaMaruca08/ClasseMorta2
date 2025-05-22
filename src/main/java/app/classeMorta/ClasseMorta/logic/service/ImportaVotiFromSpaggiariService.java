package app.classeMorta.ClasseMorta.logic.service;

import app.classeMorta.ClasseMorta.logic.VotiParser;
import app.classeMorta.ClasseMorta.logic.models.Materie;
import app.classeMorta.ClasseMorta.logic.models.Studenti;
import app.classeMorta.ClasseMorta.logic.models.Voti;
import app.classeMorta.ClasseMorta.logic.repository.MaterieRepository;
import app.classeMorta.ClasseMorta.logic.repository.StudentiRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportaVotiFromSpaggiariService {

    private final MaterieRepository materieRepository;
    private final StudentiRepository studentiRepository;

    public List<Voti> importaVoti(String phpsessid, Long studenteID) {

        Studenti studente = studentiRepository.findById(studenteID)
                .orElseThrow(() -> new EntityNotFoundException("Studente non trovato"));


        WebClient webClient = WebClient.builder()
                .baseUrl("https://web.spaggiari.eu")
                .defaultHeader("Cookie", "PHPSESSID=" + phpsessid)
                .defaultHeader("User-Agent", "Mozilla/5.0")
                .build();

        String html = webClient.get()
                .uri("/cvv/app/default/genitori_voti.php")
                .retrieve()
                .bodyToMono(String.class)
                .block();


        // 2. Risolvi materia per nome e studente; crea se non esiste
        VotiParser.MateriaResolver resolver = (nomeMateria) -> materieRepository
                .findByNomeMateriaIgnoreCaseAndStudente(nomeMateria, studente)
                .orElseGet(() -> {
                    Materie nuovaMateria = new Materie();
                    nuovaMateria.setNomeMateria(nomeMateria.trim());
                    nuovaMateria.setStudente(studente);
                    return materieRepository.save(nuovaMateria);
                });

        // 3. Parsing
        return VotiParser.parseVoti(html, studente, resolver);
    }

    public String ottieniPhpsessid(String uid, String password) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://web.spaggiari.eu")
                .defaultHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .defaultHeader("Origin", "https://web.spaggiari.eu")
                .defaultHeader("User-Agent", "Mozilla/5.0")
                .build();

        String body = "cid=&uid=" + uid + "&pwd=" + password + "&pin=&target=";

        return webClient.post()
                .uri("/auth-p7/app/default/AuthApi4.php?a=aLoginPwd")
                .bodyValue(body)
                .exchangeToMono(resp -> {
                    // Stampa tutti i cookie disponibili a console per debug
                    System.out.println("[DEBUG] Cookies presenti nella risposta: " + resp.cookies().toString());
                    ResponseCookie phpsessid = resp.cookies().getFirst("PHPSESSID");
                    if (phpsessid == null) {
                        System.err.println("[ERRORE] Cookie PHPSESSID non trovato.");
                        return Mono.error(new RuntimeException("PHPSESSID non trovato"));
                    }
                    System.out.println("[DEBUG] PHPSESSID trovato: " + phpsessid.getValue());
                    return Mono.just(phpsessid.getValue());
                })
                .block();
    }
}