package app.classeMorta.ClasseMorta.GUI;

import app.classeMorta.ClasseMorta.logic.dto.LoginRequest;
import app.classeMorta.ClasseMorta.logic.dto.MediaRequest;
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
        SwingUtilities.invokeLater(() -> {
            URL imageUrl = getClass().getResource("/img.png");
            if (imageUrl == null) {
                System.err.println("Immagine non trovata! Controlla il percorso.");
                return;
            }
            ImageIcon icon = new ImageIcon(imageUrl);
            Image image = icon.getImage();

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

            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                System.out.println("ERRORE : errore nello style");
            }

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = screenSize.width;
            int screenHeight = screenSize.height;

            JFrame frame = new JFrame("Classe Morta2");
            frame.setIconImage(image);
            frame.setSize(screenWidth, screenHeight);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setBackground(Color.white);
            frame.setLayout(null);

            CardLayout cardLayout = new CardLayout();
            JPanel panelContainer = new JPanel(cardLayout);
            panelContainer.setBounds(0, getY(0), screenWidth, screenHeight);
            panelContainer.add(accediPanel(cardLayout, panelContainer), "accesso");
            panelContainer.add(creaPanel(cardLayout, panelContainer), "crea");

            ImageIcon icon1 = new ImageIcon("src/main/java/img/icona.png");
            frame.setIconImage(icon1.getImage());
            frame.add(panelContainer);
            cardLayout.show(panelContainer, "accesso");
            frame.setVisible(true);
        });
    }

    //pagine vere e proprie
    private JPanel creaPanel(CardLayout cardLayout, JPanel panelContainer) {
        JPanel creaPanel = new JPanel();
        creaPanel.setLayout(null);
        creaPanel.setBounds(0, 0, getX(100), getY(100));
        creaPanel.setBackground(coloreMain);

        creaPanel.add(creaLabel("Benvenuto su ClasseMorta2", 30, 15, 40, 5, 30, coloreTesto));
        creaPanel.add(creaLabel("Nome", 30, 25, 10, 5, 18, coloreTesto));
        creaPanel.add(creaLabel("Email : ", 30, 30, 10, 5, 18, coloreTesto));
        creaPanel.add(creaLabel("Password : ", 30, 35, 10, 5, 18,coloreTesto));

        JTextArea areaNome = creaArea("inserisci qui il tuo Nome", 45, 25, 20, 5, 18);
        JTextArea areaEmail = creaArea("inserisci qui la Email ", 45, 30, 20, 5, 18);
        JPasswordField areaPassword = creaPassField(45, 35, 20, 5, 18);

        JButton creaAccount = creabottone("crea account", 30, 40, 40, 8, 20);
        JButton accesso = creabottone("hai già un account?", 30, 50, 15, 5, 18);
        JButton esci = creabottone("ESCI", 90, 82, 10, 10, 18);

        esci.addActionListener(_ -> System.exit(0));
        creaAccount.addActionListener(_ -> {
            try {
                if (!studentiService.isEmailUsed(areaEmail.getText().trim())) {
                    char[] rawPassword = areaPassword.getPassword();
                    // Conversione da char[] a String
                    String passwordString = new String(rawPassword);
                    // trim della stringa
                    passwordString = passwordString.trim();
                    // Se ti serve ancora come char[], fai così:
                    char[] trimmedPassword = passwordString.toCharArray();

                    studentiService.salvaStudente(areaNome.getText().trim(), areaEmail.getText().trim(), trimmedPassword);
                    mostraInformazioni("SUCCESSO", "Account creato con successo");
                }
            } catch (Exception e) {
                mostraErrore("ERRORE", "Errore nella creazione");
            }
        });
        accesso.addActionListener(_ -> cardLayout.show(panelContainer, "accesso"));

        creaPanel.add(areaEmail);
        creaPanel.add(areaPassword);
        creaPanel.add(creaAccount);
        creaPanel.add(accesso);
        creaPanel.add(areaNome);
        creaPanel.add(esci);

        return creaPanel;
    }

    private JPanel accediPanel(CardLayout cardLayout, JPanel panelContainer) {
        JPanel accediPanel = new JPanel();
        accediPanel.setLayout(null);
        accediPanel.setBounds(0, 0, getX(100), getY(100));
        accediPanel.setBackground(coloreMain);

        accediPanel.add(creaLabel("Bentornato su ClasseMorta2", 30, 15, 40, 5, 30, coloreTesto));
        accediPanel.add(creaLabel("email : ", 30, 25, 10, 5, 18, coloreTesto));
        accediPanel.add(creaLabel("password : ", 30, 30, 10, 5, 18, coloreTesto));

        JTextArea areaEmail = creaArea("inserisci qui la Email ", 45, 25, 20, 5, 18);
        JPasswordField areaPassword = creaPassField(45, 30, 20, 5, 18);

        JButton accedi = creabottone("accedi", 30, 40, 40, 8, 20);
        JButton creazione = creabottone("non hai un account?", 30, 50, 15, 5, 18);
        JButton esci = creabottone("ESCI", 90, 82, 10, 10, 18);
        esci.addActionListener(_ -> System.exit(0));

        try {
            accedi.addActionListener(_ -> {
                String email = areaEmail.getText();
                char[] password = areaPassword.getPassword();
                LoginRequest loginRequest = new LoginRequest(email, password);
                if (isStudentPresent(loginRequest)) {
                    id = studentiService.getStudentIdByEmail(email);
                    panelContainer.add(creaMateria(cardLayout, panelContainer), "aggiungiMateria");
                    panelContainer.add(mainPage(mediaService.calcolaMediaTot(id), cardLayout, panelContainer), "main");
                    cardLayout.show(panelContainer, "main");
                }
            });
        } catch (NullPointerException e) {
            log.info("ERRORE : errore nel caricamento dei dati in accedi : NullPointerException ");
        } catch (Exception e) {
            log.info("ERRORE in accedi ");
        }

        creazione.addActionListener(_ -> cardLayout.show(panelContainer, "crea"));

        accediPanel.add(areaEmail);
        accediPanel.add(areaPassword);
        accediPanel.add(accedi);
        accediPanel.add(creazione);
        accediPanel.add(esci);

        return accediPanel;
    }

    public JPanel mainPage(Float mediaTotale, CardLayout cardLayout, JPanel panelContainer) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, getX(100), getY(100));
        panel.setBackground(coloreMain);

        panel.add(pannelloStampaMedie(mediaTotale, cardLayout, panelContainer));
        panel.add(pannelloStampaBottoni(cardLayout, panelContainer));

        JButton aggiungiMateria = creabottone("Aggiungi Materia", 40, 3, 20, 5, 20);
        aggiungiMateria.addActionListener(_ -> cardLayout.show(panelContainer, "aggiungiMateria"));
        panel.add(aggiungiMateria);

        JButton tema = creabottone("Cambia tema", 0, 0, 10, 5, 15);
        tema.addActionListener(_ -> {
            if(!Objects.equals(coloreMain, new Color(0, 0, 0))) {
                coloreMain = new Color(0,0,0);
                coloreSecondario = Color.darkGray;
                coloreTesto = Color.white;
            }
            else{
                coloreMain = new Color(171, 89, 106);
                coloreSecondario = new Color(170, 60, 70);
                coloreTesto = Color.black;
            }
            aggiornaMainPage(cardLayout, panelContainer);
        });
        panel.add(tema);

        JButton disconnetti = creabottone("Disconnetti", 80, 80, 20, 10, 20);
        disconnetti.addActionListener(_ -> {
            panelContainer.add(accediPanel(cardLayout, panelContainer), "accesso");
            cardLayout.show(panelContainer, "accesso");
        });
        panel.add(disconnetti);

        return panel;
    }

    public JPanel creaMateria(CardLayout cardLayout, JPanel panelContainer) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(coloreMain);
        panel.add(pannelloAggiuntaMateria(cardLayout, panelContainer));
        return panel;
    }

    public JPanel internoMateria(CardLayout cardLayout, JPanel panelContainer, Materie materia) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, getX(100), getY(100));
        panel.setBackground(coloreMain);

        panel.add(creaLabel(materia.getNomeMateria(), 0, 0, 100, 10, 35, coloreTesto));

        JButton ritorno = creabottone("Ritorna", 0, 85, 10, 5, 18);
        ritorno.addActionListener(_ -> {
            aggiornaMainPage(cardLayout, panelContainer);
            cardLayout.show(panelContainer, "main");
        });
        panel.add(ritorno);

        panel.add(pannelloVotiIpotetici(materia));
        panel.add(pannelloVoti(materia, cardLayout, panelContainer));

        Float[] voti = {
                0.0f, 0.5f, 1.0f, 1.5f, 2.0f, 2.5f,
                3.0f, 3.5f, 4.0f, 4.5f, 5.0f, 5.5f, 6.0f,
                6.5f, 7.0f, 7.5f, 8.0f, 8.5f, 9.0f, 9.5f, 10.0f
        };

        JComboBox<Float> comboBox = new JComboBox<>(voti);
        comboBox.setBounds(getX(60), getY(5), getX(9), getY(6));

        JButton aggiungiVoto = creabottone("Aggiungi Voto", 70, 5, 20, 7, 20);
        aggiungiVoto.addActionListener(_ -> {
            Float voto = (Float) comboBox.getSelectedItem();
            aggiungiVoto(voto, materia, studentiService.getStudenteByID(id));
            aggiornaPaginaMateria(cardLayout, panelContainer, materia);
        });

        panel.add(aggiungiVoto);
        panel.add(comboBox);

        return panel;
    }

    //sotto pannelli
    public JPanel pannelloStampaBottoni(CardLayout cardLayout, JPanel panelContainer) {
        JPanel panel2 = sottoPanelMedie(40, 10, 60, 70);
        int xBtn = 5, yBtn = 5, j = 0;



        for (Materie materia : materieService.getAllMaterie(studentiService.getStudenteByID(id))) {
            if (j == 4) {
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

    public JPanel pannelloStampaMedie(double mediaTotale, CardLayout cardLayout, JPanel panelContainer) {
        JPanel panel1 = sottoPanelMedie(5, 5, 33, 75);
        panel1.setLayout(null);

        CerchioMedia cerchioMedia = new CerchioMedia(mediaTotale, 14, "Media", coloreSecondario);
        cerchioMedia.setBounds(getX(14), getY(5), getX(10), getY(10));
        panel1.add(cerchioMedia);

        int x = 2, y = 20, i = 0;
        for (Materie materia : materieService.getAllMaterie(studentiService.getStudenteByID(id))) {
            MediaRequest mediaRequest = new MediaRequest(materia.getIdMateria(), id, -1);
            float mediaMateria = mediaService.calcolaMediaPerMateria(mediaRequest);

            if (i == 3) {
                i = 0;
                x = 2;
                y += 13;
            }

            CerchioMedia cerchioMediaPiccolo = new CerchioMedia(mediaMateria, 14, materia.getNomeMateria(), coloreSecondario);
            cerchioMediaPiccolo.setBounds(getX(x), getY(y), getX(10), getY(10));

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

        panel1.add(creaLabel("Media totale", 12, -3, 10, 10, 25, coloreTesto));
        panel1.add(creaLabel("Medie materie", 12, 13, 10, 10, 22, coloreTesto));
        return panel1;
    }

    public JPanel pannelloVoti(Materie materia, CardLayout cardLayout, JPanel panelContainer) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(getX(45), getY(15), getX(50), getY(68));
        panel.setBackground(coloreSecondario);

        int x = 2, y = 8, i = 0;
        List<Voti> listaVoti = votiService.getVotiPerMateriaEID(materia.getIdMateria(), id);

        for (Voti voto : listaVoti) {
            if (i == 4) {
                i = 0;
                x = 2;
                y += 10;
            }

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

            CerchioMedia cerchioMediaIp = new CerchioMedia(voto.getVoto(), 14, "" + voto.getData(), coloreSecondario);
            cerchioMediaIp.setBounds(getX(x), getY(y), getX(10), getY(10));
            panel.add(cerchioMediaIp);

            i++;
            x += 12;
        }

        panel.add(creaLabel("Voti (dal più vecchio)", -5, -2, 30, 10, 25, coloreTesto));
        return panel;
    }

    public JPanel pannelloVotiIpotetici(Materie materia) {
        JPanel panel1 = sottoPanelMedie(1, 3, 40, 80);
        float mediaMat = mediaService.calcolaMediaPerMateria(new MediaRequest(materia.getIdMateria(), id, -1));

        CerchioMedia cerchioMedia = new CerchioMedia(mediaMat, 14, "Media", coloreSecondario);
        cerchioMedia.setBounds(getX(5), getY(5), getX(10), getY(10));
        panel1.add(cerchioMedia);

        int x = 2, y = 20;
        float votoIpotetico = 0.0F;

        for (int i = 0, j = 0; i < 21; i++, j++) {
            if (j == 4) {
                j = 0;
                x = 2;
                y += 10;
            }

            MediaRequest mediaRequest = new MediaRequest(materia.getIdMateria(), id, votoIpotetico);
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

    public JPanel pannelloAggiuntaMateria(CardLayout cardLayout, JPanel panelContainer) {
        JPanel panel1 = sottoPanelMedie(25, 12, 50, 35);
        panel1.add(creaLabel("Aggiungi materia", 5, 5, 40, 5, 30, coloreTesto));
        panel1.add(creaLabel("Nome della materia:", 5, 15, 15, 5, 20, coloreTesto));

        JTextArea areaNome = creaArea("NomeMateria", 21, 15, 20, 5, 20);
        JButton button = creabottone("Aggiungi", 5, 20, 35, 5, 18);
        button.addActionListener(_ -> {
            if (materieService.existByName(areaNome.getText().trim(),studentiService.getStudenteByID(id))) {
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

    public JPanel sottoPanelMedie(int x, int y, int width, int height) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(getX(x), getY(y), getX(width), getY(height));
        panel.setBackground(coloreSecondario);
        return panel;
    }

    //funzioni per aggiornare le pagine
    public void aggiornaMainPage(CardLayout cardLayout, JPanel panelContainer) {
        panelContainer.removeAll();
        panelContainer.add(creaMateria(cardLayout, panelContainer), "aggiungiMateria");
        panelContainer.add(creaPanel(cardLayout, panelContainer), "crea");
        panelContainer.add(mainPage(mediaService.calcolaMediaTot(id), cardLayout, panelContainer), "main");
        cardLayout.show(panelContainer, "main");
    }

    public void aggiornaPaginaMateria(CardLayout cardLayout, JPanel panelContainer, Materie materia) {
        panelContainer.removeAll();
        panelContainer.add(creaMateria(cardLayout, panelContainer), "aggiungiMateria");
        panelContainer.add(creaPanel(cardLayout, panelContainer), "crea");
        panelContainer.add(internoMateria(cardLayout, panelContainer, materia), "interno");
        panelContainer.add(mainPage(mediaService.calcolaMediaTot(id), cardLayout, panelContainer), "main");
        cardLayout.show(panelContainer, "interno");
    }

    //funzioni di supporto
    public boolean cancellaMateria(Materie materia) {
        log.info("cancello materia");
        if (sceltaYN("cancellare", "Vuoi davvero cancellare questa Materia?") == 0) {
            return materieService.eliminaMateria(materia.getIdMateria());
        }
        return false;
    }

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

    public void aggiungiVoto(Float voto, Materie materia, Studenti studenti) {
        votiService.salvaVoto(voto, materia, studenti);
    }

    public boolean cancellaVoto(Voti voto) {
        int scelta = sceltaYN("cancellare", "Vuoi davvero cancellare questo Voto?");
        if (scelta == 0) {
            return votiService.cancellaVoto(voto.getVotoID());
        }
        return false;
    }
}