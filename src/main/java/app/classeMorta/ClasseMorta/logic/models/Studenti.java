package app.classeMorta.ClasseMorta.logic.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Studenti")
public class Studenti {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false) // <-- Questo è il campo persistente
    private char[] password;

    public Studenti(String name, String email, char[] password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
