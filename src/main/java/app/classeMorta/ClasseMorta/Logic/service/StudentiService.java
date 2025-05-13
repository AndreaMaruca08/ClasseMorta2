package app.classeMorta.ClasseMorta.Logic.service;

import app.classeMorta.ClasseMorta.Logic.repository.StudentiRepository;
import app.classeMorta.ClasseMorta.Logic.models.Studenti;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class StudentiService {
    private final StudentiRepository studentiRepository;

    @Autowired
    public StudentiService(StudentiRepository studentiRepository) {
        this.studentiRepository = studentiRepository;
    }

    public Long getStudentIdByEmail(String email){
        return studentiRepository.getStudentiIDByEmail(email);
    }
    public boolean verificaCredenziali(String email, char[] password) {
        Optional<Studenti> studentiOpt = studentiRepository.findByEmail(email);

        if (studentiOpt.isPresent()) {
            Studenti utente = studentiOpt.get();
            return Arrays.equals(utente.getPassword(), password); // (NON SICURO)
        }

        return false;
    }
    public boolean isEmailUsed(String email){
        return studentiRepository.findByEmail(email).isPresent();
    }

    public void salvaStudente(String nome, String email, char[] password){
        try {
            Studenti studente = new Studenti(nome, email, password);
            studentiRepository.save(studente);
            studentiRepository.flush();  // Forza il flush subito dopo il salvataggio
        } catch (Exception e) {
            System.out.println("ERRORE in 'salvaStudente' in StudentiService");
        }
    }

    public Studenti getStudenteByID(Long id){
        return studentiRepository.getReferenceById(id);
    }
    public Optional<Studenti> getStudenteByEmail(String email){
        return studentiRepository.findByEmail(email);
    }


}
