package app.classeMorta.ClasseMorta.logic.service;

import app.classeMorta.ClasseMorta.logic.dto.LoginRequest;
import app.classeMorta.ClasseMorta.logic.models.Studenti;
import app.classeMorta.ClasseMorta.logic.repository.StudentiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
public class StudentiService {
    private final StudentiRepository studentiRepository;

    @Autowired
    public StudentiService(StudentiRepository studentiRepository) {
        this.studentiRepository = studentiRepository;
    }

    public Long getStudentIdByEmail(String email) {
        return studentiRepository.getStudentiIDByEmail(email);
    }

    public boolean verificaCredenziali(LoginRequest loginRequest) {
        Optional<Studenti> studentiOpt = studentiRepository.findByEmail(loginRequest.getEmail());

        if (studentiOpt.isPresent()) {
            Studenti utente = studentiOpt.get();
            return Arrays.equals(utente.getPassword(), loginRequest.getPassword()); // (NON SICURO)
        }

        return false;
    }

    public boolean isEmailUsed(String email) {
        return studentiRepository.findByEmail(email).isPresent();
    }

    public boolean salvaStudente(String nome, String email, char[] password) {
        try {
            Studenti studente = new Studenti(nome, email, password);
            studentiRepository.save(studente);
            studentiRepository.flush();  // Forza il flush subito dopo il salvataggio
        } catch (Exception e) {
            log.info("ERRORE in 'salvaStudente' in StudentiService");
            return false;
        }
        return true;
    }

    public Studenti getStudenteByID(Long id) {
        return studentiRepository.getReferenceById(id);
    }

    public Optional<Studenti> getStudenteByEmail(String email) {
        return studentiRepository.findByEmail(email);
    }


}
