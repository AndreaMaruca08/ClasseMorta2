package app.classeMorta.ClasseMorta.Logic.controller;

import app.classeMorta.ClasseMorta.Logic.service.MaterieService;
import app.classeMorta.ClasseMorta.Logic.models.Materie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materie") // Tutte le rotte iniziano con /materie
public class MaterieController {

    private final MaterieService materieService;

    @Autowired
    public MaterieController(MaterieService materieService) {
        this.materieService = materieService;
    }

    // ➤ GET /materie
    // Ritorna la lista di tutte le materie
    @GetMapping
    public ResponseEntity<List<Materie>> getAllMaterie() {
        List<Materie> materie = materieService.getAllMaterie();
        return ResponseEntity.ok(materie); // HTTP 200 OK
    }

    // ➤ POST /materie
    // Salva una nuova materia. Riceve il nome come JSON (es: {"nome": "Matematica"})
    @PostMapping
    public ResponseEntity<String> creaMateria(@RequestBody MateriaRequest request) {
        if (materieService.existByName(request.getNome())) {
            return ResponseEntity.badRequest().body("Materia già esistente");
        }

        materieService.saveMateria(request.getNome());
        return ResponseEntity.ok("Materia salvata con successo");
    }

    // ➤ GET /materie/exists?nome=Matematica
    // Controlla se esiste una materia con un certo nome
    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkMateria(@RequestParam String nome) {
        boolean exists = materieService.existByName(nome);
        return ResponseEntity.ok(exists);
    }

    // ➤ DELETE /materie/5
    // Elimina una materia tramite il suo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminaMateria(@PathVariable Long id) {
        boolean eliminata = materieService.eliminaMateria(id);

        if (eliminata) {
            return ResponseEntity.ok("Materia eliminata");
        } else {
            return ResponseEntity.notFound().build(); // HTTP 404
        }
    }

    // Classe di supporto per ricevere il corpo JSON del POST
    public static class MateriaRequest {
        private String nome;

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }
    }
}
