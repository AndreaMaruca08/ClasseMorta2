package app.classeMorta.ClasseMorta.logic.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Materie")
public class Materie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMateria;

    @Column(nullable = false)
    private String nomeMateria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_studente", referencedColumnName = "id")
    private Studenti studente;

    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Voti> voti = new ArrayList<>();

    public void addVoto(Voti voto) {
        voti.add(voto);
        voto.setMateria(this);
    }

    public Materie(String nomeMateria, Studenti studente) {
        this.nomeMateria = nomeMateria;
        this.studente = studente;
        this.voti = new ArrayList<>();
    }
}