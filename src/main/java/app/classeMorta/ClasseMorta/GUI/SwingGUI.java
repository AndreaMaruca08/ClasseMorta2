package app.classeMorta.ClasseMorta.GUI;

import app.classeMorta.ClasseMorta.logic.dto.LoginRequest;
import app.classeMorta.ClasseMorta.logic.dto.MediaRequest;
import app.classeMorta.ClasseMorta.logic.PeriodoVoto;
import app.classeMorta.ClasseMorta.logic.models.Materie;
import app.classeMorta.ClasseMorta.logic.models.Studenti;
import app.classeMorta.ClasseMorta.logic.models.Voti;
import app.classeMorta.ClasseMorta.logic.service.MaterieService;
import app.classeMorta.ClasseMorta.logic.service.MediaService;
import app.classeMorta.ClasseMorta.logic.service.StudentiService;
import app.classeMorta.ClasseMorta.logic.service.VotiService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static app.classeMorta.ClasseMorta.GUI.GUIUtils.*;
/**
 * La classe {@code SwingGUI} si occupa della creazione e gestione dell'interfaccia grafica (GUI)
 * per un'applicazione di gestione studenti utilizzando Swing.
 * <p>
 * Responsabilità principali:
 * <ul>
 *     <li>Gestisce i diversi pannelli dell'interfaccia dell'applicazione, tra cui login, elenco materie, gestione voti e statistiche.</li>
 *     <li>Fornisce funzionalità per la navigazione tra le sezioni dell'applicazione.</li>
 *     <li>Si occupa dell'interazione con le logiche di business relative agli studenti, materie e voti, compreso il calcolo delle medie.</li>
 *     <li>Gestisce la logica di logging per attività ed errori applicativi.</li>
 * </ul>
 *
 * <b>Campi principali:</b>
 * <ul>
 *     <li>{@code Logger log} – Logger utilizzato per debug e logging delle attività.</li>
 *     <li>{@code StudentiService studentiService} – Service per la gestione degli studenti.</li>
 *     <li>{@code MaterieService materieService} – Service per la gestione delle materie.</li>
 *     <li>{@code VotiService votiService} – Service per la gestione dei voti.</li>
 *     <li>{@code MediaService mediaService} – Service per il calcolo delle medie.</li>
 *     <li>{@code String id} – Identificativo sessione o utente corrente.</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class SwingGUI {
    private static final Logger log = LoggerFactory.getLogger(SwingGUI.class);
    private final StudentiService studentiService;
    private final MaterieService materieService;
    private final VotiService votiService;
    private final MediaService mediaService;
    private Long id;

    private Color coloreMain = new Color(171, 89, 106);
    private Color coloreSecondario = new Color(170, 60, 70);
    private Color coloreTesto = new Color(0,0,0);

    @PostConstruct
    public void createGUI() {
        // Esegue la creazione della GUI in modo thread-safe
        SwingUtilities.invokeLater(() -> {
            // Carica l'icona dell'applicazione
            URL imageUrl = getClass().getResource("/img.png");
            if (imageUrl == null) {
                System.err.println("Immagine non trovata! Controlla il percorso.");
                return;
            }
            ImageIcon icon = new ImageIcon(imageUrl);
            Image image = icon.getImage();

            // Imposta l'icona nella taskbar se supportato
            if (Desktop.isDesktopSupported()) {
                try {
                    Taskbar taskbar = Taskbar.getTaskbar();
                    if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                        taskbar.setIconImage(image);
                    }
                } catch (UnsupportedOperationException | SecurityException e) {
                    System.err.println("Impossibile impostare l'icona del Dock: " + e.getMessage());
                }
            }

            // Imposta il look and feel Nimbus
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                System.out.println("ERRORE : errore nello style");
            }

            // Ottiene le dimensioni dello schermo
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = screenSize.width;
            int screenHeight = screenSize.height;

            // Crea e configura il frame principale
            JFrame frame = new JFrame("Classe Morta2");
            frame.setIconImage(image);
            frame.setSize(screenWidth, screenHeight);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setBackground(Color.white);
            frame.setLayout(null);

            // Configura il contenitore principale con CardLayout
            CardLayout cardLayout = new CardLayout();
            JPanel panelContainer = new JPanel(cardLayout);
            panelContainer.setBounds(0, getY(0), screenWidth, screenHeight);
            panelContainer.add(accediPanel(cardLayout, panelContainer), "accesso");
            panelContainer.add(creaPanel(cardLayout, panelContainer), "crea");

            // Imposta l'icona finale e mostra il frame
            ImageIcon icon1 = new ImageIcon("src/main/java/img/icona.png");
            frame.setIconImage(icon1.getImage());
            frame.add(panelContainer);
            cardLayout.show(panelContainer, "accesso");
            frame.setVisible(true);
        });
    }

    /**
     * Crea il pannello per la registrazione di un nuovo utente
     */
    private JPanel creaPanel(CardLayout cardLayout, JPanel panelContainer) {
        // Inizializza il pannello principale
        JPanel creaPanel = new JPanel();
        creaPanel.setLayout(null);
        creaPanel.setBounds(0, 0, getX(100), getY(100));
        creaPanel.setBackground(coloreSecondario);

        // Aggiunge le etichette
        creaPanel.add(creaLabel("Benvenuto su ClasseMorta2", 30, 15, 40, 5, 30, coloreTesto));
        creaPanel.add(creaLabel("Nome", 30, 25, 10, 5, 18, coloreTesto));
        creaPanel.add(creaLabel("Email : ", 30, 30, 10, 5, 18, coloreTesto));
        creaPanel.add(creaLabel("Password : ", 30, 35, 10, 5, 18, coloreTesto));

        // Crea i campi di input
        JTextArea areaNome = creaArea("inserisci qui il tuo Nome", 45, 25, 20, 5, 18);
        JTextArea areaEmail = creaArea("inserisci qui la Email ", 45, 30, 20, 5, 18);
        JPasswordField areaPassword = creaPassField(45, 35, 20, 5, 18);

        // Crea i pulsanti
        JButton creaAccount = creabottone("crea account", 30, 40, 40, 8, 20);
        JButton accesso = creabottone("hai già un account?", 30, 50, 15, 5, 18);
        JButton esci = creabottone("ESCI", 90, 82, 10, 10, 18);

        // Configura i listener dei pulsanti
        esci.addActionListener(_ -> System.exit(0));
        creaAccount.addActionListener(_ -> {
            try {
                if (!studentiService.isEmailUsed(areaEmail.getText().trim())) {
                    char[] rawPassword = areaPassword.getPassword();
                    String passwordString = new String(rawPassword);
                    passwordString = passwordString.trim();

                    studentiService.salvaStudente(areaNome.getText().trim(), areaEmail.getText().trim(), passwordString);
                    mostraInformazioni("SUCCESSO", "Account creato con successo");
                }
            } catch (Exception e) {
                mostraErrore("ERRORE", "Errore nella creazione");
            }
        });
        accesso.addActionListener(_ -> cardLayout.show(panelContainer, "accesso"));

        // Aggiunge i componenti al pannello
        creaPanel.add(areaEmail);
        creaPanel.add(areaPassword);
        creaPanel.add(creaAccount);
        creaPanel.add(accesso);
        creaPanel.add(areaNome);
        creaPanel.add(esci);

        return creaPanel;
    }

    /**
     * Crea il pannello di login
     */
    private JPanel accediPanel(CardLayout cardLayout, JPanel panelContainer) {
        // Inizializza il pannello principale
        JPanel accediPanel = new JPanel();
        accediPanel.setLayout(null);
        accediPanel.setBounds(0, 0, getX(100), getY(100));
        accediPanel.setBackground(coloreSecondario);

        // Aggiunge le etichette
        accediPanel.add(creaLabel("Bentornato su ClasseMorta2", 30, 15, 40, 5, 30, coloreTesto));
        accediPanel.add(creaLabel("email : ", 30, 25, 10, 5, 18, coloreTesto));
        accediPanel.add(creaLabel("password : ", 30, 30, 10, 5, 18, coloreTesto));

        // Crea i campi di input
        JTextArea areaEmail = creaArea("inserisci qui la Email ", 45, 25, 20, 5, 18);
        JPasswordField areaPassword = creaPassField(45, 30, 20, 5, 18);

        // Crea i pulsanti
        JButton accedi = creabottone("accedi", 30, 40, 40, 8, 20);
        JButton creazione = creabottone("non hai un account?", 30, 50, 15, 5, 18);
        JButton esci = creabottone("ESCI", 90, 82, 10, 10, 18);
        esci.addActionListener(_ -> System.exit(0));

        // Gestisce il login
        accedi.addActionListener(_ -> {
            String email = areaEmail.getText().trim();

            // Converti array di char in String corretta!
            char[] passChars = areaPassword.getPassword();
            String password = new String(passChars);

            // Pulizia dati sensibili subito dopo l'uso
            Arrays.fill(passChars, '\0');

            if (email.isEmpty() || password.isEmpty()) {
                mostraErrore("ERRORE", "Email e password sono obbligatorie.");
                return;
            }

            LoginRequest loginRequest = new LoginRequest(email, password);

            if (isStudentPresent(loginRequest)) {
                id = studentiService.getStudentIdByEmail(email); // qui va bene Long (come nel service)
                panelContainer.add(importaVoti(cardLayout, panelContainer), "importaVoti");
                panelContainer.add(creaMateria(cardLayout, panelContainer), "aggiungiMateria");
                panelContainer.add(mainPage(cardLayout, panelContainer), "main");
                cardLayout.show(panelContainer, "main");
            }
        });

        creazione.addActionListener(_ -> cardLayout.show(panelContainer, "crea"));

        // Aggiunge i componenti al pannello
        accediPanel.add(areaEmail);
        accediPanel.add(areaPassword);
        accediPanel.add(accedi);
        accediPanel.add(creazione);
        accediPanel.add(esci);

        return accediPanel;
    }


    /**
     * Crea la pagina principale dell'applicazione
     */
    public JPanel mainPage(CardLayout cardLayout, JPanel panelContainer) {
        // Inizializza il pannello principale
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, getX(100), getY(100));
        panel.setBackground(coloreMain);

        // Aggiunge il pannello dei bottoni per le materie
        panel.add(pannelloStampaBottoni(cardLayout, panelContainer));

        // Crea i pannelli per le medie dei periodi
        JPanel panelMedieTr = pannelloStampaMedie(mediaService.calcolaMediaTot(id, PeriodoVoto.TRIMESTRE), cardLayout, panelContainer, PeriodoVoto.TRIMESTRE);
        panel.add(panelMedieTr);
        JPanel panelMediePe = pannelloStampaMedie(mediaService.calcolaMediaTot(id, PeriodoVoto.PENTAMESTRE), cardLayout, panelContainer, PeriodoVoto.PENTAMESTRE);

        // Pulsante per aggiungere materia
        JButton aggiungiMateria = creabottone("Aggiungi Materia", 40, 3, 20, 5, 15);
        aggiungiMateria.addActionListener(_ -> cardLayout.show(panelContainer, "aggiungiMateria"));
        panel.add(aggiungiMateria);

        // Pulsante per cambiare tema
        JButton tema = creabottone("Cambia tema", 0, 0, 10, 5, 15);
        tema.addActionListener(_ -> {
            if (!Objects.equals(coloreMain, new Color(0, 0, 0))) {
                coloreMain = new Color(0, 0, 0);
                coloreSecondario = Color.darkGray;
                coloreTesto = Color.white;
            } else {
                coloreMain = new Color(171, 89, 106);
                coloreSecondario = new Color(170, 60, 70);
                coloreTesto = Color.black;
            }
            aggiornaMainPage(cardLayout, panelContainer);
        });
        panel.add(tema);

        // Gestione del cambio periodo
        final boolean[] periodo = {true};

        // Pulsante per importare voti
        JButton importaVoti = creabottone("Importa voti", 10, 0, 10, 5, 15);
        importaVoti.addActionListener(_ -> cardLayout.show(panelContainer, "importaVoti"));
        panel.add(importaVoti);

        // Configurazione del contenitore per i voti
        CardLayout cardLayoutVoti = new CardLayout();
        JPanel panelContainerVoti = new JPanel(cardLayoutVoti);
        panelContainerVoti.setBounds(getX(5), getY(5), getX(33), getY(74));
        panelContainerVoti.add(panelMedieTr, "TRIMESTRE");
        panelContainerVoti.add(panelMediePe, "PENTAMESTRE");
        panel.add(panelContainerVoti);

        // Pulsante per cambiare periodo
        JButton cambiaPeriodo = creabottone("Cambia periodo", 20, 0, 10, 5, 15);
        cambiaPeriodo.addActionListener(_ -> {
            if(periodo[0]) {
                periodo[0] = false;
                cardLayoutVoti.show(panelContainerVoti, "PENTAMESTRE");
            }
            else {
                periodo[0] = true;
                cardLayoutVoti.show(panelContainerVoti, "TRIMESTRE");
            }
        });
        panel.add(cambiaPeriodo);

        // Pulsante per disconnettersi
        JButton disconnetti = creabottone("Disconnetti", 80, 80, 20, 10, 20);
        disconnetti.addActionListener(_ -> {
            panelContainer.add(accediPanel(cardLayout, panelContainer), "accesso");
            cardLayout.show(panelContainer, "accesso");
        });
        panel.add(disconnetti);

        return panel;
    }

    /**
     * Crea il pannello per aggiungere una nuova materia
     *
     * @param cardLayout     Layout manager per la gestione dei pannelli
     * @param panelContainer Container principale dei pannelli
     * @return JPanel configurato per l'aggiunta di una nuova materia
     */
    public JPanel creaMateria(CardLayout cardLayout, JPanel panelContainer) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(coloreMain);
        panel.add(pannelloAggiuntaMateria(cardLayout, panelContainer));
        return panel;
    }

    /**
     * Crea il pannello interno di gestione di una materia specifica
     * Contiene le visualizzazioni delle medie, voti e la possibilità di aggiungere nuovi voti
     *
     * @param cardLayout     Layout manager per la gestione dei pannelli
     * @param panelContainer Container principale dei pannelli
     * @param materia        Oggetto Materie da gestire
     * @return JPanel configurato per la gestione della materia
     */
    public JPanel internoMateria(CardLayout cardLayout, JPanel panelContainer, Materie materia) {
        // Inizializzazione pannello principale
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, getX(100), getY(100));
        panel.setBackground(coloreMain);

        panel.add(creaLabel(materia.getNomeMateria(), -2, -3, 100, 10, 35, coloreTesto));

        // Pulsante di ritorno alla pagina principale
        JButton ritorno = creabottone("Ritorna", 0, 85, 10, 5, 18);
        ritorno.addActionListener(_ -> {
            aggiornaMainPage(cardLayout, panelContainer);
            cardLayout.show(panelContainer, "main");
        });
        panel.add(ritorno);

        // Gestione pannelli per le medie ipotetiche
        JPanel panelMedieTr = pannelloVotiIpotetici(materia, PeriodoVoto.TRIMESTRE);
        panel.add(panelMedieTr);
        JPanel panelMediePe = pannelloVotiIpotetici(materia, PeriodoVoto.PENTAMESTRE);

        // Container per le medie con CardLayout
        CardLayout cardLayoutMedie = new CardLayout();
        JPanel panelContainerMedie = new JPanel(cardLayoutMedie);
        panelContainerMedie.setBounds(getX(2), getY(5), getX(38), getY(80));
        panelContainerMedie.add(panelMedieTr, "TRIMESTREMedie");
        panelContainerMedie.add(panelMediePe, "PENTAMESTREMedie");
        panel.add(panelContainerMedie);

        // Gestione cambio periodo per medie ipotetiche
        boolean[] periodob = {false};
        JButton cambiaPeriodo = creabottone("Cambia periodo", 20, 0, 10, 5, 15);
        cambiaPeriodo.addActionListener(_ -> {
            if (!periodob[0]) {
                periodob[0] = true;
                cardLayoutMedie.show(panelContainerMedie, "PENTAMESTREMedie");
            } else {
                periodob[0] = false;
                cardLayoutMedie.show(panelContainerMedie, "TRIMESTREMedie");
            }
        });
        panel.add(cambiaPeriodo);

        // Componenti per l'aggiunta di nuovi voti
        Float[] voti = {
                0.0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.5f,
                3.0f, 3.5f, 4.0f, 4.5f, 5.0f, 5.5f, 6.0f,
                6.5f, 7.0f, 7.5f, 8.0f, 8.5f, 9.0f, 9.5f, 10.0f
        };
        PeriodoVoto[] periodi = {PeriodoVoto.TRIMESTRE, PeriodoVoto.PENTAMESTRE};

        // ComboBox per selezione voto e periodo
        JComboBox<Float> comboBox = new JComboBox<>(voti);
        comboBox.setBounds(getX(60), getY(5), getX(9), getY(6));

        JComboBox<PeriodoVoto> periodoVotoComboBox = new JComboBox<>(periodi);
        periodoVotoComboBox.setBounds(getX(70), getY(5), getX(9), getY(6));

        // Pulsante per aggiungere un nuovo voto
        JButton aggiungiVoto = creabottone("Aggiungi Voto", 82, 5, 16, 7, 20);
        aggiungiVoto.addActionListener(_ -> {
            Float voto = (Float) comboBox.getSelectedItem();
            PeriodoVoto periodo = (PeriodoVoto) periodoVotoComboBox.getSelectedItem();
            aggiungiVoto(voto, materia, studentiService.getStudenteByID(id), periodo);
            aggiornaPaginaMateria(cardLayout, panelContainer, materia);
        });

        // Gestione visualizzazione voti per periodo
        JPanel panelVotiTr = pannelloVoti(materia, cardLayout, panelContainer, PeriodoVoto.TRIMESTRE);
        panel.add(panelVotiTr);
        JPanel panelVotiPe = pannelloVoti(materia, cardLayout, panelContainer, PeriodoVoto.PENTAMESTRE);

        // Pulsanti per cambio periodo visualizzazione voti
        JButton trimestre = creabottone("Trimestre", 45, 11, 10, 4, 18);
        JButton pentamestre = creabottone("Pentamestre", 56, 11, 10, 4, 18);

        // Container per i voti con CardLayout
        CardLayout cardLayoutVoti = new CardLayout();
        JPanel panelContainerVoti = new JPanel(cardLayoutVoti);
        panelContainerVoti.setBounds(getX(45), getY(15), getX(50), getY(70));
        panelContainerVoti.add(panelVotiTr, "TRIMESTRE");
        panelContainerVoti.add(panelVotiPe, "PENTAMESTRE");
        panel.add(panelContainerVoti);

        trimestre.addActionListener(_ -> cardLayoutVoti.show(panelContainerVoti, "TRIMESTRE"));
        pentamestre.addActionListener(_ -> cardLayoutVoti.show(panelContainerVoti, "PENTAMESTRE"));

        // Aggiunta componenti finali al pannello
        panel.add(trimestre);
        panel.add(pentamestre);
        panel.add(aggiungiVoto);
        panel.add(comboBox);
        panel.add(periodoVotoComboBox);

        return panel;
    }

    /**
     * Crea il pannello per importare i voti da ClasseViva
     * Gestisce l'autenticazione e l'importazione dei dati
     *
     * @param cardLayout     Layout manager per la gestione dei pannelli
     * @param panelContainer Container principale dei pannelli
     * @return JPanel configurato per l'importazione dei voti
     */
    public JPanel importaVoti(CardLayout cardLayout, JPanel panelContainer) {
        // Inizializzazione pannello principale
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, getX(100), getY(100));
        panel.setBackground(coloreMain);

        // Aggiunta etichette
        panel.add(creaLabel("Importa voti", 0, 0, 100, 10, 35, coloreTesto));
        panel.add(creaLabel("inserisci email/codice studente", 15, 23, 30, 10, 20, coloreTesto));
        panel.add(creaLabel("inserisci la password di ClasseViva", 15, 27, 30, 10, 20, coloreTesto));

        // Campi input per credenziali
        JTextArea areaEmail = creaArea("email : ", 45, 25, 20, 5, 18);
        panel.add(areaEmail);
        JPasswordField areaPassword = creaPassField(45, 30, 20, 5, 18);
        panel.add(areaPassword);

        // Pulsante ritorno
        JButton ritorno = creabottone("Ritorna", 0, 85, 10, 5, 18);
        ritorno.addActionListener(_ -> {
            aggiornaMainPage(cardLayout, panelContainer);
            cardLayout.show(panelContainer, "main");
        });
        panel.add(ritorno);

        // Pulsante importazione voti
        JButton importaVoti = creabottone("Importa voti", 70, 5, 20, 7, 20);
        importaVoti.addActionListener(_ -> {
            String email = areaEmail.getText();
            char[] passwordChars = areaPassword.getPassword();
            String password = new String(passwordChars);
            try {
                materieService.deleteAll(studentiService.getStudenteByID(id));
                String phpSess = votiService.getCodiceSpaggiari(email, password);
                votiService.importaVotiFromClasseViva(phpSess, id);
                mostraInformazioni("successo", "Importazione voti avvenuta con successo");
            } catch (RuntimeException ex) {
                mostraErrore("Errore di autenticazione", "Errore durante il login: " + ex.getMessage() + "\n\nControlla le credenziali e riprova.");
            }
        });
        panel.add(importaVoti);

        return panel;
    }

    /**
     * Crea il pannello contenente i bottoni per le materie
     *
     * @param cardLayout     Layout manager per la gestione dei pannelli
     * @param panelContainer Container principale dei pannelli
     * @return JPanel configurato con i bottoni delle materie
     */
    public JPanel pannelloStampaBottoni(CardLayout cardLayout, JPanel panelContainer) {
        JPanel panel2 = sottoPanelMedie(40, 10, 60, 70);
        int xBtn = 5, yBtn = 5, j = 0;

        // Ciclo creazione bottoni per ogni materia
        for (Materie materia : materieService.getAllMaterie(studentiService.getStudenteByID(id))) {
            if (j == 4) { // Nuova riga dopo 4 bottoni
                j = 0;
                xBtn = 5;
                yBtn += 13;
            }

            JButton bottone = creabottone(materia.getNomeMateria(), xBtn, yBtn, 10, 5, 20);
            bottone.addActionListener(_ -> {
                panelContainer.add(internoMateria(cardLayout, panelContainer, materia), "interno");
                cardLayout.show(panelContainer, "interno");
            });
            panel2.add(bottone);

            xBtn += 11;
            j++;
        }
        return panel2;
    }

    /**
     * Crea il pannello per la visualizzazione delle medie
     *
     * @param mediaTotale    Media totale da visualizzare
     * @param cardLayout     Layout manager per la gestione dei pannelli
     * @param panelContainer Container principale dei pannelli
     * @param periodoVoto    Periodo di riferimento per le medie
     * @return JPanel configurato con le medie
     */
    public JPanel pannelloStampaMedie(double mediaTotale, CardLayout cardLayout, JPanel panelContainer, PeriodoVoto periodoVoto) {
        // Inizializzazione pannello
        JPanel panel1 = sottoPanelMedie(5, 5, 33, 75);
        panel1.setLayout(null);

        // Visualizzazione media periodo
        CerchioMedia cerchioMedia = new CerchioMedia(mediaTotale, 14, "Media periodo", coloreSecondario);
        cerchioMedia.setBounds(getX(5), getY(5), getX(10), getY(10));
        panel1.add(cerchioMedia);

        // Visualizzazione media totale annuale
        CerchioMedia cerchioMediaTotale = new CerchioMedia(mediaService.calcolaMediaTot(id, PeriodoVoto.ANNO), 14, "Media totale", coloreSecondario);
        cerchioMediaTotale.setBounds(getX(23), getY(5), getX(10), getY(10));
        panel1.add(cerchioMediaTotale);

        panel1.add(creaLabel("" + periodoVoto, 0, -3, 18, 10, 22, coloreTesto));

        // Ciclo per aggiunta medie per materia
        int x = 2, y = 20, i = 0;
        for (Materie materia : materieService.getAllMaterie(studentiService.getStudenteByID(id))) {
            MediaRequest mediaRequest = new MediaRequest(materia.getIdMateria(), id, -1, periodoVoto);
            float mediaMateria = mediaService.calcolaMediaPerMateria(mediaRequest);

            if (i == 3) { // Nuova riga dopo 3 medie
                i = 0;
                x = 2;
                y += 13;
            }

            // Visualizzazione media materia
            CerchioMedia cerchioMediaPiccolo = new CerchioMedia(mediaMateria, 14, materia.getNomeMateria(), coloreSecondario);
            cerchioMediaPiccolo.setBounds(getX(x), getY(y), getX(10), getY(10));

            // Pulsante cancellazione materia
            JButton cancella = creabottone("", x - 2, y, 2, 4, 3);
            cancella.addActionListener(_ -> {
                if (cancellaMateria(materia)) {
                    aggiornaMainPage(cardLayout, panelContainer);
                } else {
                    mostraErrore("ERRORE", "errore nella cancellazione, materia non esistente");
                }
            });
            cancella.setForeground(Color.white);
            cancella.setBackground(Color.gray);

            panel1.add(cancella);
            panel1.add(cerchioMediaPiccolo);
            x += 12;
            i++;
        }

        panel1.add(creaLabel("Media totale", 20, -3, 10, 10, 25, coloreTesto));
        panel1.add(creaLabel("Medie materie", 12, 13, 10, 10, 22, coloreTesto));
        return panel1;
    }

    /**
     * Crea il pannello per la visualizzazione dei voti di una materia
     *
     * @param materia        Materia di cui visualizzare i voti
     * @param cardLayout     Layout manager per la gestione dei pannelli
     * @param panelContainer Container principale dei pannelli
     * @param periodoVoto    Periodo di riferimento per i voti
     * @return JPanel configurato con i voti della materia
     */
    public JPanel pannelloVoti(Materie materia, CardLayout cardLayout, JPanel panelContainer, PeriodoVoto periodoVoto) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(getX(45), getY(15), getX(50), getY(68));
        panel.setBackground(coloreSecondario);

        int x = 2, y = 8, i = 0;
        List<Voti> listaVoti = votiService.getVotiPerMateriaEIDAndPeriodo(materia.getIdMateria(), id, periodoVoto);

        if (listaVoti.isEmpty()) {
            log.error("ERRORE : listaVoti null o vuota nella funzione pannelloVoti");
        }

        // Ciclo visualizzazione voti
        for (Voti voto : listaVoti) {
            if (voto.getPeriodo() != periodoVoto)
                continue;
            if (i == 4) { // Nuova riga dopo 4 voti
                i = 0;
                x = 2;
                y += 10;
            }

            // Pulsante cancellazione voto
            JButton cancella = creabottone("", x - 2, y, 2, 4, 3);
            cancella.addActionListener(_ -> {
                if (cancellaVoto(voto)) {
                    aggiornaPaginaMateria(cardLayout, panelContainer, materia);
                } else {
                    mostraErrore("ERRORE", "errore nella cancellazione, voto non esistente");
                }
            });
            cancella.setForeground(Color.white);
            cancella.setBackground(Color.black);

            panel.add(cancella);

            // Visualizzazione voto
            CerchioMedia cerchioMediaIp = new CerchioMedia(voto.getVoto(), 14, "" + voto.getData(), coloreSecondario);
            cerchioMediaIp.setBounds(getX(x), getY(y), getX(10), getY(10));
            panel.add(cerchioMediaIp);

            i++;
            x += 12;
        }

        panel.add(creaLabel("Voti (dal più vecchio)", -5, -2, 30, 10, 25, coloreTesto));
        panel.add(creaLabel(periodoVoto.toString(), 20, -2, 18, 10, 22, coloreTesto));
        return panel;
    }

    /**
     * Crea un pannello per visualizzare le medie ipotetiche per una materia
     * Mostra la media attuale e le medie calcolate aggiungendo voti ipotetici da 0 a 10
     *
     * @param materia     Materia di cui calcolare le medie ipotetiche
     * @param periodoVoto Periodo di riferimento (trimestre/pentamestre)
     * @return Pannello contenente le medie ipotetiche
     */
    public JPanel pannelloVotiIpotetici(Materie materia, PeriodoVoto periodoVoto) {
        JPanel panel1 = sottoPanelMedie(-2, 5, 50, 80);
        float mediaMat = mediaService.calcolaMediaPerMateria(new MediaRequest(materia.getIdMateria(), id, -1, periodoVoto));

        CerchioMedia cerchioMedia = new CerchioMedia(mediaMat, 14, "Media", coloreSecondario);
        cerchioMedia.setBounds(getX(1), getY(5), getX(10), getY(10));
        panel1.add(cerchioMedia);

        panel1.add(creaLabel(periodoVoto.toString(), 20, -3, 18, 10, 22, coloreTesto));

        int x = 2, y = 20;
        float votoIpotetico = 0.0F;

        // Ciclo per calcolare e visualizzare le medie con voti ipotetici da 0 a 10
        for (int i = 0, j = 0; i < 21; i++, j++) {
            if (j == 4) { // Nuova riga dopo 4 medie
                j = 0;
                x = 2;
                y += 10;
            }

            MediaRequest mediaRequest = new MediaRequest(materia.getIdMateria(), id, votoIpotetico, periodoVoto);
            mediaMat = mediaService.calcolaMediaPerMateria(mediaRequest);

            CerchioMedia cerchioMediaIp = new CerchioMedia(mediaMat, 14, "Media con " + votoIpotetico, coloreSecondario);
            cerchioMediaIp.setBounds(getX(x), getY(y), getX(10), getY(10));
            votoIpotetico += 0.5F;
            panel1.add(cerchioMediaIp);

            x += 10;
        }

        panel1.add(creaLabel("Medie ipotetiche", -1, 12, 20, 10, 25, coloreTesto));
        panel1.add(creaLabel("Media " + materia.getNomeMateria(), -1, -3, 20, 10, 25, coloreTesto));
        return panel1;
    }

    /**
     * Crea il pannello per aggiungere una nuova materia
     * Contiene un campo di testo per il nome e pulsanti per aggiungere/annullare
     *
     * @param cardLayout     Layout manager per la gestione dei pannelli
     * @param panelContainer Container principale dei pannelli
     * @return Pannello per l'aggiunta di una nuova materia
     */
    public JPanel pannelloAggiuntaMateria(CardLayout cardLayout, JPanel panelContainer) {
        JPanel panel1 = sottoPanelMedie(25, 12, 50, 35);
        panel1.add(creaLabel("Aggiungi materia", 5, 5, 40, 5, 30, coloreTesto));
        panel1.add(creaLabel("Nome della materia:", 5, 15, 15, 5, 20, coloreTesto));

        JTextArea areaNome = creaArea("NomeMateria", 21, 15, 20, 5, 20);
        JButton button = creabottone("Aggiungi", 5, 20, 35, 5, 18);
        button.addActionListener(_ -> {
            if (materieService.existByName(areaNome.getText().trim(), studentiService.getStudenteByID(id))) {
                mostraErrore("ERRORE", "Materia già esistente");
            } else {
                materieService.saveMateria(areaNome.getText().trim(), studentiService.getStudenteByID(id));
            }
        });

        JButton ritorno = creabottone("Ritorna", 5, 25, 10, 5, 18);
        ritorno.addActionListener(_ -> {
            aggiornaMainPage(cardLayout, panelContainer);
            cardLayout.show(panelContainer, "main");
        });

        panel1.add(ritorno);
        panel1.add(areaNome);
        panel1.add(button);
        return panel1;
    }

    /**
     * Crea un pannello base con sfondo colorato e dimensioni specifiche
     *
     * @param x      Coordinata x del pannello
     * @param y      Coordinata y del pannello
     * @param width  Larghezza del pannello
     * @param height Altezza del pannello
     * @return Pannello configurato con le dimensioni specificate
     */
    public JPanel sottoPanelMedie(int x, int y, int width, int height) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(getX(x), getY(y), getX(width), getY(height));
        panel.setBackground(coloreSecondario);
        return panel;
    }

    /**
     * Aggiorna la pagina principale ricostruendo tutti i pannelli
     *
     * @param cardLayout     Layout manager per la gestione dei pannelli
     * @param panelContainer Container principale dei pannelli
     */
    public void aggiornaMainPage(CardLayout cardLayout, JPanel panelContainer) {
        panelContainer.removeAll();
        panelContainer.add(creaMateria(cardLayout, panelContainer), "aggiungiMateria");
        panelContainer.add(creaPanel(cardLayout, panelContainer), "crea");
        panelContainer.add(mainPage(cardLayout, panelContainer), "main");
        panelContainer.add(importaVoti(cardLayout, panelContainer), "importaVoti");
        cardLayout.show(panelContainer, "main");
    }

    /**
     * Aggiorna la pagina di una specifica materia ricostruendo tutti i pannelli
     *
     * @param cardLayout     Layout manager per la gestione dei pannelli
     * @param panelContainer Container principale dei pannelli
     * @param materia        Materia da visualizzare
     */
    public void aggiornaPaginaMateria(CardLayout cardLayout, JPanel panelContainer, Materie materia) {
        panelContainer.removeAll();
        panelContainer.add(creaMateria(cardLayout, panelContainer), "aggiungiMateria");
        panelContainer.add(creaPanel(cardLayout, panelContainer), "crea");
        panelContainer.add(internoMateria(cardLayout, panelContainer, materia), "interno");
        panelContainer.add(mainPage(cardLayout, panelContainer), "main");
        cardLayout.show(panelContainer, "interno");
    }

    /**
     * Gestisce la cancellazione di una materia con conferma dell'utente
     *
     * @param materia Materia da cancellare
     * @return true se la materia è stata cancellata, false altrimenti
     */
    public boolean cancellaMateria(Materie materia) {
        log.info("cancello materia");
        if (sceltaYN("cancellare", "Vuoi davvero cancellare questa Materia?") == 0) {
            return materieService.eliminaMateria(materia.getIdMateria());
        }
        return false;
    }

    /**
     * Verifica le credenziali di accesso di uno studente
     *
     * @param loginRequest Oggetto contenente email e password
     * @return true se le credenziali sono valide, false altrimenti
     */
    public boolean isStudentPresent(LoginRequest loginRequest) {
        Optional<Studenti> tempStudent = studentiService.getStudenteByEmail(loginRequest.email());
        if (tempStudent.isPresent()) {
            if (studentiService.verificaCredenziali(loginRequest)) {
                return true;
            }
            mostraErrore("ERRORE", "Password errata");
            return false;
        }
        mostraErrore("ERRORE", "Utente non esistente");
        return false;
    }

    /**
     * Aggiunge un nuovo voto per una materia
     *
     * @param voto        Valore del voto
     * @param materia     Materia a cui aggiungere il voto
     * @param studenti    Studente a cui assegnare il voto
     * @param periodoVoto Periodo in cui registrare il voto
     */
    public void aggiungiVoto(Float voto, Materie materia, Studenti studenti, PeriodoVoto periodoVoto) {
        votiService.salvaVoto(voto, materia, studenti, periodoVoto);
    }

    /**
     * Gestisce la cancellazione di un voto con conferma dell'utente
     *
     * @param voto Voto da cancellare
     * @return true se il voto è stato cancellato, false altrimenti
     */
    public boolean cancellaVoto(Voti voto) {
        int scelta = sceltaYN("cancellare", "Vuoi davvero cancellare questo Voto?");
        if (scelta == 0) {
            return votiService.cancellaVoto(voto.getVotoID());
        }
        return false;
    }
}