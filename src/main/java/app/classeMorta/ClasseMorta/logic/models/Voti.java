package app.classeMorta.ClasseMorta.logic.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Voti")
public class Voti {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long votoID;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private Studenti studente;

    @ManyToOne
    @JoinColumn(name = "idMateria", nullable = false)
    private Materie materia;

    @Column(nullable = false)
    private Float voto;

    @Column(nullable = false)
    private LocalDate data;

    public Voti(Float voto, Studenti studente, Materie materia, LocalDate data) {
        this.data = data;
        this.voto = voto;
        this.studente = studente;
        this.materia = materia;
    }
}
