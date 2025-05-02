package app.classeMorta.ClasseMorta.Logic.Materie;

import app.classeMorta.ClasseMorta.Logic.Voti.Voti;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Materie")
public class Materie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMateria;

    @Column(nullable = false)
    private String nomeMateria;

    @OneToMany(mappedBy = "materia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Voti> voti;

    public Materie() {}

    public Materie(String nomeMateria) {
        this.nomeMateria = nomeMateria;
    }

    public Long getIdMateria() { return idMateria; }

    public String getNomeMateria() { return nomeMateria; }

    public void setNomeMateria(String nomeMateria) { this.nomeMateria = nomeMateria; }

    public List<Voti> getVoti() { return voti; }

    public void setVoti(List<Voti> voti) { this.voti = voti; }
}
