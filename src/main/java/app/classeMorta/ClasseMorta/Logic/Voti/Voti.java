package app.classeMorta.ClasseMorta.Logic.Voti;

import app.classeMorta.ClasseMorta.Logic.Materie.Materie;
import app.classeMorta.ClasseMorta.Logic.Studenti.Studenti;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
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

    public Voti(Float voto, Studenti studente, Materie materia, LocalDate data){
        this.data = data;
        this.voto = voto;
        this.studente = studente;
        this.materia = materia;
    }
    public Voti(){}



    public void setVoto(Float voto){this.voto = voto;}
    public void setStudente(Studenti studente){this.studente = studente;}
    public void setMateria(Materie materia){this.materia = materia;}
    public void setData(LocalDate data){this.data = data;}

    public Long getVotoID(){return votoID;}
    public Float getVoto(){return voto;}
    public Studenti getStudente(){return studente;}
    public Materie getMateria(){return materia;}
    public LocalDate getData(){return data;}




}
