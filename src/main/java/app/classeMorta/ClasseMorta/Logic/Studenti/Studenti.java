package app.classeMorta.ClasseMorta.Logic.Studenti;

import jakarta.persistence.*;

@Entity
@Table(name = "Studenti")
public class Studenti {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private char[] email;

    //getter e setter

    public Long getId(){return id;}
    public String getName(){return name;}
    public char[] getEmail(){return email;}

    public void setName(String name){this.name = name;}
    public void setEmail(char[] email){this.email = email;}

}
