package app.classeMorta.ClasseMorta.logic.models;

import app.classeMorta.ClasseMorta.logic.PeriodoVoto;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    @JoinColumn(name = "idMateria", nullable = false)
    private Materie materia;

    @Column(nullable = false)
    private Float voto;

    @Column(nullable = false)
    private LocalDate data;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private PeriodoVoto periodo;


    public Voti(Float voto, Studenti studente, Materie materia, LocalDate data, PeriodoVoto periodo) {
        this.data = data;
        this.voto = voto;
        this.studente = studente;
        this.materia = materia;
        this.periodo = periodo;
    }
}
