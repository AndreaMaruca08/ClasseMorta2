package app.classeMorta.ClasseMorta.Logic.Materie;

import jakarta.persistence.*;

@Entity
@Table(name = "Materie")
public class Materie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMateria;

    @Column(nullable = false)
    private String nomeMateria;

}
