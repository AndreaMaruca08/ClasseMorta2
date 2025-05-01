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

    public Materie(){

    }
    public Materie(String nomeMateria){
        this.nomeMateria = nomeMateria;
    }


    public Long getIdMateria(){return idMateria;}
    public String getNomeMateria(){return nomeMateria;}

    public void setNomeMateria(String nomeMateria){this.nomeMateria = nomeMateria;}

}
