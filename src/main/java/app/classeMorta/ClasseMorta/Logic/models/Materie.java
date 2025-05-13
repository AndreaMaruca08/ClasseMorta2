package app.classeMorta.ClasseMorta.Logic.models;

import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Voti> voti;

    public Materie(String nomeMateria) {
        this.nomeMateria = nomeMateria;
    }
}
