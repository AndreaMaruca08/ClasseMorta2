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
    private String email;

    @Column(name = "password", nullable = false) // <-- Questo è il campo persistente
    private char[] password;

    @Transient
    private String passwordDB;

    public Studenti() {}

    public Studenti(String name, String email, char[] password) {
        this.name = name;
        this.email = email;
        setPassword(password);
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public String getEmail() { return email; }

    public char[] getPassword() { return password; }

    public void setName(String name) { this.name = name; }

    public void setEmail(String email) { this.email = email; }

    public void setPassword(char[] password) {
        this.password = password;
        this.passwordDB = new String(password);
    }

    public void setPasswordDB(String passwordDB) {
        this.passwordDB = passwordDB;
        this.password = passwordDB.toCharArray();
    }

    public String getPasswordDB() {
        return passwordDB;
    }
}
