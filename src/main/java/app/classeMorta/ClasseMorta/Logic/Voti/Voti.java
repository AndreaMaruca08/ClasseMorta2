package app.classeMorta.ClasseMorta.Logic.Voti;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "Voti")
public class Voti {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long votoID;

    @Column
    private Long studenteID;


    @Column(nullable = false)
    private Float voto;

    @Column(nullable = false)
    private LocalDate data;




}
