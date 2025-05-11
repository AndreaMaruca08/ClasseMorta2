package app.classeMorta.ClasseMorta.GUI;

import app.classeMorta.ClasseMorta.Logic.LogicUtil;
import app.classeMorta.ClasseMorta.Logic.Materie.Materie;
import app.classeMorta.ClasseMorta.Logic.Materie.MaterieService;
import app.classeMorta.ClasseMorta.Logic.MediaService;
import app.classeMorta.ClasseMorta.Logic.Studenti.StudentiService;
import app.classeMorta.ClasseMorta.Logic.Voti.Voti;
import app.classeMorta.ClasseMorta.Logic.Voti.VotiService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

import static app.classeMorta.ClasseMorta.GUI.GUIUtils.*;

@Component
public class SwingGUI {
    private Long id;

    private final LogicUtil logicUtil;
    private final StudentiService studentiService;
    private final MaterieService materieService;
    private final VotiService votiService;
    private final MediaService mediaService;

    @Autowired
    public SwingGUI(LogicUtil logicUtil, StudentiService studentiService, MaterieService materieService, VotiService votiService, MediaService mediaService) {
        this.logicUtil = logicUtil;
        this.studentiService = studentiService;
        this.materieService = materieService;
        this.votiService = votiService;
        this.mediaService = mediaService;
    }

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
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); // Imposta Nimbus
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

            // Icona
            ImageIcon icon1 = new ImageIcon("src/main/java/img/icona.png");
            frame.setIconImage(icon1.getImage());
            frame.add(panelContainer);
            cardLayout.show(panelContainer, "accesso");
            frame.setVisible(true);
        });
    }

    //PAGINE EFFETTIVE

    /**
     * <b>Pagina per la creazione di un account</b>
     */
    private JPanel creaPanel(CardLayout cardLayout, JPanel panelContainer) {
        JPanel creaPanel = new JPanel();
        creaPanel.setLayout(null);
        creaPanel.setBounds(0, 0, getX(100), getY(100));
        creaPanel.setBackground(new Color(171, 89, 106));

        // Componenti GUI
        creaPanel.add(creaLabel("Benvenuto su ClasseMorta2", 30, 15, 40, 5, 30));

        creaPanel.add(creaLabel("Nome", 30, 25, 10, 5, 18));
        creaPanel.add(creaLabel("Email : ", 30, 30, 10, 5, 18));
        creaPanel.add(creaLabel("Password : ", 30, 35, 10, 5, 18));

        JTextArea areaNome = creaArea("inserisci qui il tuo Nome", 45, 25, 20, 5, 18);
        JTextArea areaEmail = creaArea("inserisci qui la Email ", 45, 30, 20, 5, 18);
        JPasswordField areaPassword = creaPassField(45, 35, 20, 5, 18);

        JButton creaAccount = creabottone("crea account", 30, 40, 40, 8, 20);
        JButton accesso = creabottone("hai già un account?", 30, 50, 15, 5, 18);
        JButton esci = creabottone("ESCI", 90, 82, 10, 10, 18);

        esci.addActionListener(_ -> System.exit(0));
        creaAccount.addActionListener(_ -> {
            try {
                if (!studentiService.isEmailUsed(areaEmail.getText())) {
                    studentiService.salvaStudente(areaNome.getText(), areaEmail.getText(), (areaPassword.getPassword()));
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

    /**
     *  <b>Pagina per l'accesso con un account</b>
     */
    private JPanel accediPanel(CardLayout cardLayout, JPanel panelContainer) {
        JPanel accediPanel = new JPanel();
        accediPanel.setLayout(null);
        accediPanel.setBounds(0, 0, getX(100), getY(100));
        accediPanel.setBackground(new Color(171, 89, 106));
        // Componenti GUI
        accediPanel.add(creaLabel("Bentornato su ClasseMorta2", 30, 15, 40, 5, 30));
        accediPanel.add(creaLabel("email : ", 30, 25, 10, 5, 18));
        accediPanel.add(creaLabel("password : ", 30, 30, 10, 5, 18));

        JTextArea areaEmail = creaArea("inserisci qui la Email ", 45, 25, 20, 5, 18);
        JPasswordField areaPassword = creaPassField(45, 30, 20, 5, 18);

        JButton accedi = creabottone("accedi", 30, 40, 40, 8, 20);
        JButton creazione = creabottone("non hai un account?", 30, 50, 15, 5, 18);
        JButton esci = creabottone("ESCI", 90, 82, 10, 10, 18);
        esci.addActionListener(_ -> System.exit(0));

        //uso di try catch siccome si gestisce l'input
        try {
            accedi.addActionListener(_ -> {

                String email = areaEmail.getText();
                char[] password = areaPassword.getPassword();

                if (logicUtil.isStudentPresent(email, password)) {
                    id = studentiService.getStudentIdByEmail(email);

                    panelContainer.add(creaMateria(cardLayout, panelContainer), "aggiungiMateria");
                    panelContainer.add(mainPage(mediaService.calcolaMediaTot(id), cardLayout, panelContainer), "main");
                    cardLayout.show(panelContainer, "main");
                }

            });
        } catch (NullPointerException e) {
            System.out.println("ERRORE : errore nel caricamento dei dati --NullPointerException--");
        } catch (Exception e) {
            System.out.println("ERRORE : c'e un errore ignoto");
        }
        creazione.addActionListener(_ -> cardLayout.show(panelContainer, "crea"));

        accediPanel.add(areaEmail);
        accediPanel.add(areaPassword);
        accediPanel.add(accedi);
        accediPanel.add(creazione);
        accediPanel.add(esci);

        return accediPanel;
    }

    /**
     * <b>Pagina principale, contiene tutte le medie e gli accessi alle materie</b>
     */
    public JPanel mainPage(Float mediaTotale, CardLayout cardLayout, JPanel panelContainer) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, getX(100), getY(100));
        panel.setBackground(new Color(171, 89, 106));
        //aggiunta del pannello per le medie
        panel.add(pannelloStampaMedie(mediaTotale, cardLayout, panelContainer));
        //aggiunta del pannello per i bottoni per accedere alle materie
        panel.add(pannelloStampaBottoni(cardLayout, panelContainer));
        //bottone per aggiungere materia
        JButton aggiungiMateria = creabottone("Aggiungi Materia", 40, 3, 20, 5, 20);
        aggiungiMateria.addActionListener(_ -> cardLayout.show(panelContainer, "aggiungiMateria"));
        panel.add(aggiungiMateria);
        //bottone per disconnettersi
        JButton disconnetti = creabottone("Disconnetti", 80, 80, 20, 10, 20);
        disconnetti.addActionListener(_ -> {
            panelContainer.add(accediPanel(cardLayout, panelContainer), "accesso");
            cardLayout.show(panelContainer, "accesso");
        });
        panel.add(disconnetti);

        return panel;
    }

    /**
     *<b>Pagina per la creazione di una materia</b>
     */
    public JPanel creaMateria(CardLayout cardLayout, JPanel panelContainer) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(171, 89, 106));
        panel.add(pannelloAggiuntaMateria(cardLayout, panelContainer));
        return panel;
    }

    /**
     *<b>Pagina interna a una materia per vedere medie ipotetiche e voti</b>
     */
    public JPanel internoMateria(CardLayout cardLayout, JPanel panelContainer, Materie materia) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, getX(100), getY(100));
        panel.setBackground(new Color(171, 89, 106));

        panel.add(creaLabel(materia.getNomeMateria(), 0, 0, 100, 10, 35));
        JButton ritorno = creabottone("Ritorna", 0, 85, 10, 5, 18);
        ritorno.addActionListener(_ -> {
            aggiornaMainPage(cardLayout, panelContainer);
            cardLayout.show(panelContainer, "main");
        });
        panel.add(ritorno);
        //aggiunta del pannello per vedere le medie ipotetiche
        panel.add(pannelloVotiIpotetici(materia));
        //voti attuali
        panel.add(pannelloVoti(materia, cardLayout, panelContainer));

        //menù per la selezione del voto
        Float[] voti = {
                (float) 0.0, (float) 0.5, (float) 1, (float) 1.5, (float) 2, (float) 2.5,
                (float) 3, (float) 3.5, (float) 4, (float) 4.5, (float) 5, (float) 5.5, (float) 6,
                (float) 6.5, (float) 7, (float) 7.5, (float) 8, (float) 8.5, (float) 9, (float) 9.5, (float) 10
        };
        JComboBox<Float> comboBox = new JComboBox<>(voti);
        comboBox.setBounds(getX(60), getY(5), getX(9), getY(6)); // Posizione e dimensione

        //bottone per aggiungere voto
        JButton aggiungiVoto = creabottone("Aggiungi Voto", 70, 5, 20, 7, 20);
        aggiungiVoto.addActionListener(_ -> {
            Float voto = (Float) comboBox.getSelectedItem();
            logicUtil.aggiungiVoto(voto, materia, studentiService.getStudenteByID(id));
            aggiornaPaginaMateria(cardLayout, panelContainer, materia);
        });

        panel.add(aggiungiVoto);
        panel.add(comboBox);

        return panel;
    }

    //SOTTO PANNELLI PER UN CODICE PIU' LEGGIBILE

    /**
     *<b>Sotto pannello di <code>mainPage()</code> per mettere i bottoni per accedere ai voti delle singole materie</b>
     */
    public JPanel pannelloStampaBottoni(CardLayout cardLayout, JPanel panelContainer) {
        //stampo bottoni
        JPanel panel2 = sottoPanelMedie(40, 10, 60, 70);
        int xBtn = 5, yBtn = 5, j = 0;
        for (Materie materia : materieService.getAllMaterie()) {
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

    /**
     * <b>Sotto pannello di <code>mainPage()</code> per far vedere le medie, quella totale e quelle delle singole materie </b>
     */
    public JPanel pannelloStampaMedie(double mediaTotale, CardLayout cardLayout, JPanel panelContainer) {
        //stampo delle medie
        JPanel panel1 = sottoPanelMedie(5, 5, 33, 75);
        panel1.setLayout(null);
        CerchioMedia cerchioMedia = new CerchioMedia(mediaTotale, 14, "Media");
        cerchioMedia.setBounds(getX(14), getY(5), getX(10), getY(10));
        panel1.add(cerchioMedia);
        int x = 2, y = 20, i = 0;
        for (Materie materia : materieService.getAllMaterie()) {
            Float mediaMateria = mediaService.calcolaMediaPerMateria(materia.getIdMateria(), id);
            if (i == 3) {
                i = 0;
                x = 2;
                y += 13;
            }
            CerchioMedia cerchioMediaPiccolo = new CerchioMedia(mediaMateria, 14, materia.getNomeMateria());
            cerchioMediaPiccolo.setBounds(getX(x), getY(y), getX(10), getY(10));
            JButton cancella = creabottone("", x - 2, y, 2, 4, 3);
            cancella.addActionListener(_ -> {
                if (!logicUtil.cancellaMateria(materia))
                    mostraErrore("ERRORE", "errore nella cancellazione, materia non esistente");
                else
                    aggiornaMainPage(cardLayout, panelContainer);
            });
            cancella.setForeground(Color.white);
            cancella.setBackground(Color.black);
            //aggiunte
            panel1.add(cancella);
            panel1.add(cerchioMediaPiccolo);
            x += 12;
            i++;
        }
        //messaggi
        panel1.add(creaLabel("Media totale", 12, -3, 10, 10, 25));
        panel1.add(creaLabel("Medie materie", 12, 13, 10, 10, 22));
        return panel1;
    }

    /**
     *<b>Sotto pannello di <code>internoMateria()</code> e costituisce la parte con i voti della materia</b>
     */
    public JPanel pannelloVoti(Materie materia, CardLayout cardLayout, JPanel panelContainer) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(getX(45), getY(15), getX(50), getY(68));
        panel.setBackground(new Color(170, 60, 70));

        //stampa media Ipotetica
        int x = 2, y = 8, i = 0;
        List<Voti> listaVoti = votiService.getVotiPerMateriaEID(materia.getIdMateria(), id);
        for (Voti voto : listaVoti) {
            //aumenti
            if (i == 4) {
                i = 0;
                x = 2;
                y += 10;
            }
            JButton cancella = creabottone("", x - 2, y, 2, 4, 3);
            cancella.addActionListener(_ -> {
                if (!logicUtil.cancellaVoto(voto))
                    mostraErrore("ERRORE", "errore nella cancellazione, voto non esistente");
                else
                    aggiornaPaginaMateria(cardLayout, panelContainer, materia);
            });
            cancella.setForeground(Color.white);
            cancella.setBackground(Color.black);
            //aggiunte
            panel.add(cancella);
            CerchioMedia cerchioMediaIp = new CerchioMedia(voto.getVoto(), 14, "" + voto.getData());
            cerchioMediaIp.setBounds(getX(x), getY(y), getX(10), getY(10));
            panel.add(cerchioMediaIp);

            i++;
            x += 12;
        }
        panel.add(creaLabel("Voti (dal più vecchio)", -5, -2, 30, 10, 25));

        return panel;
    }

    /**
     *<b>Sotto pannello di <code>internoMateria()</code> per far vedere la media della materia attuale e tutte le medie ipotetiche</b>
     */
    public JPanel pannelloVotiIpotetici(Materie materia) {
        JPanel panel1 = sottoPanelMedie(1, 3, 40, 80);
        Float mediaMat = mediaService.calcolaMediaPerMateria(materia.getIdMateria(), id);
        CerchioMedia cerchioMedia = new CerchioMedia(mediaMat, 14, "Media");
        cerchioMedia.setBounds(getX(5), getY(5), getX(10), getY(10));
        panel1.add(cerchioMedia);

        //stampa media Ipotetica
        int x = 2, y = 20;
        float votoIpotetico = 0.0F;
        for (int i = 0, j = 0; i < 21; i++, j++) {
            //aumenti
            if (j == 4) {
                j = 0;
                x = 2;
                y += 10;
            }
            mediaMat = mediaService.calcolaMediaPerMateria(materia.getIdMateria(), id, votoIpotetico);
            CerchioMedia cerchioMediaIp = new CerchioMedia(mediaMat, 14, "Media con " + votoIpotetico);
            cerchioMediaIp.setBounds(getX(x), getY(y), getX(10), getY(10));
            votoIpotetico += 0.5F;
            panel1.add(cerchioMediaIp);


            x += 10;
        }
        panel1.add(creaLabel("Medie ipotetiche", -1, 12, 20, 10, 25));
        panel1.add(creaLabel("Media " + materia.getNomeMateria(), -1, -3, 20, 10, 25));
        return panel1;
    }

    /**
     *<b>Sotto pannello di <code>creaMateria()</code> per aggiungere la materia</b>
     */
    public JPanel pannelloAggiuntaMateria(CardLayout cardLayout, JPanel panelContainer) {
        JPanel panel1 = sottoPanelMedie(25, 12, 50, 35);
        panel1.add(creaLabel("Aggiungi materia", 5, 5, 40, 5, 30));
        panel1.add(creaLabel("Nome della materia:", 5, 15, 15, 5, 20));
        JTextArea areaNome = creaArea("NomeMateria", 21, 15, 20, 5, 20);
        JButton button = creabottone("Aggiungi", 5, 20, 35, 5, 18);
        button.addActionListener(_ -> {
            if (materieService.existByName(areaNome.getText())) {
                mostraErrore("ERRORE", "Materia già esistente");
            } else {
                materieService.saveMateria(areaNome.getText());
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

    //PANELLO DI SUPPORTO

    /**
     *<b>Semplice pannello per fare delle piccole zone di colore differente in modo che sia più bello</b>
     */
    public JPanel sottoPanelMedie(int x, int y, int width, int height) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(getX(x), getY(y), getX(width), getY(height));
        panel.setBackground(new Color(170, 60, 70));
        return panel;
    }

    //PANNELLI DI AGGIORNAMENTO

    /** <b>Aggiorna la pagina principale</b>
     */
    public void aggiornaMainPage(CardLayout cardLayout, JPanel panelContainer) {
        panelContainer.removeAll();
        panelContainer.add(creaMateria(cardLayout, panelContainer), "aggiungiMateria");
        panelContainer.add(creaPanel(cardLayout, panelContainer), "crea");
        panelContainer.add(mainPage(mediaService.calcolaMediaTot(id), cardLayout, panelContainer), "main");
        cardLayout.show(panelContainer, "main");
    }

    /**
     * <b>Aggiorna la pagina delle materie</b>
     */
    public void aggiornaPaginaMateria(CardLayout cardLayout, JPanel panelContainer, Materie materia) {
        panelContainer.removeAll();
        panelContainer.add(creaMateria(cardLayout, panelContainer), "aggiungiMateria");
        panelContainer.add(creaPanel(cardLayout, panelContainer), "crea");
        panelContainer.add(internoMateria(cardLayout, panelContainer, materia), "interno");
        panelContainer.add(mainPage(mediaService.calcolaMediaTot(id), cardLayout, panelContainer), "main");
        cardLayout.show(panelContainer, "interno");
    }
}

