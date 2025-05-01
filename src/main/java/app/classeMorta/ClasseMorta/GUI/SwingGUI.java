package app.classeMorta.ClasseMorta.GUI;

import app.classeMorta.ClasseMorta.Logic.LogicUtil;
import app.classeMorta.ClasseMorta.Logic.Materie.Materie;
import app.classeMorta.ClasseMorta.Logic.Materie.MaterieService;
import app.classeMorta.ClasseMorta.Logic.Studenti.StudentiService;
import app.classeMorta.ClasseMorta.Logic.Voti.VotiService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.*;
import java.awt.*;
import static app.classeMorta.ClasseMorta.GUI.GUIUtils.*;

@Component
public class SwingGUI {
    private Long id;

    private final LogicUtil logicUtil;
    private final StudentiService studentiService;
    private final MaterieService materieService;
    private final VotiService votiService;

    @Autowired
    public SwingGUI(LogicUtil logicUtil, StudentiService studentiService, MaterieService materieService, VotiService votiService) {
        this.logicUtil = logicUtil;
        this.studentiService = studentiService;
        this.materieService = materieService;
        this.votiService = votiService;
    }

    @PostConstruct
    public void createGUI() {
        System.out.println("DENTRO");
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); // Imposta Nimbus
            } catch (Exception e) {
                System.out.println("ERRORE : errore nello style");
            }
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = screenSize.width;
            int screenHeight = screenSize.height;

            JFrame frame = new JFrame("App Ordini");
            frame.setSize(screenWidth, screenHeight);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setBackground(Color.white);
            frame.setLayout(null);

            CardLayout cardLayout = new CardLayout();
            JPanel panelContainer = new JPanel(cardLayout);
            panelContainer.setBounds(0, getY(0), screenWidth, screenHeight);
            panelContainer.add(accediPanel(frame, cardLayout, panelContainer), "accesso");
            panelContainer.add(creaPanel(cardLayout, panelContainer), "crea");
            // Icona
            ImageIcon icon = new ImageIcon("src/main/java/img/icona.png");
            frame.setIconImage(icon.getImage());
            frame.add(panelContainer);
            cardLayout.show(panelContainer, "accesso");
            frame.setVisible(true);
        });
    }

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
                if (studentiService.isEmailUsed(areaEmail.getText())) {
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
    private JPanel accediPanel(JFrame frame, CardLayout cardLayout, JPanel panelContainer) {
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
                    panelContainer.add(mainPage(logicUtil.calcolaMediaTot(id), cardLayout, panelContainer), "main");
                    frame.add(panelContainer);
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
    public JPanel mainPage(Float mediaTotale, CardLayout cardLayout, JPanel panelContainer) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, getX(100), getY(100));
        panel.setBackground(new Color(171, 89, 106));

        //stampo delle medie
        JPanel panel1 = sottoPanelMedie(5, 5, 33, 80);
        panel1.setLayout(null);
        CerchioMedia cerchioMedia = new CerchioMedia(mediaTotale, 14, "Media");
        cerchioMedia.setBounds(getX(14), getY(5), getX(10), getY(10));
        panel1.add(cerchioMedia);
        int x = 2, y = 20, i = 0;
        for (Materie materia : materieService.getAllMaterie()) {
            Float mediaMateria = logicUtil.calcolaMediaPerMateria(materia.getIdMateria(), id);
            if (i == 3) {
                i = 0;
                x = 2;
                y += 13;
            }
            CerchioMedia cerchioMediaPiccolo = new CerchioMedia(mediaMateria, 14, materia.getNomeMateria());
            cerchioMediaPiccolo.setBounds(getX(x), getY(y), getX(10), getY(10));
            JButton cancella = creabottone("",x-2, y,2, 4, 3);
            cancella.addActionListener(_ -> {
                if(logicUtil.cancellaMateria(materia))
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
        panel.add(panel1);

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
                cardLayout.show(panelContainer,"interno");
            });
            panel2.add(bottone);
            xBtn += 11;
            j++;
        }
        panel.add(panel2);

        //bottoni per aggiungere materia
        JButton aggiungiMateria = creabottone("Aggiungi Materia", 40, 3, 20, 5, 20);
        aggiungiMateria.addActionListener(_ -> cardLayout.show(panelContainer, "aggiungiMateria"));
        panel.add(aggiungiMateria);

        return panel;
    }
    public void aggiornaMainPage(CardLayout cardLayout, JPanel panelContainer) {
        panelContainer.removeAll();
        panelContainer.add(creaMateria(cardLayout,panelContainer), "aggiungiMateria");
        panelContainer.add(mainPage(logicUtil.calcolaMediaTot(id), cardLayout, panelContainer), "main");
        cardLayout.show(panelContainer, "main");
    }
    public JPanel creaMateria(CardLayout cardLayout, JPanel panelContainer) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(171, 89, 106));
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
            aggiornaMainPage(cardLayout,panelContainer);
            cardLayout.show(panelContainer, "main");
        });

        panel1.add(ritorno);
        panel1.add(areaNome);
        panel1.add(button);
        panel.add(panel1);

        return panel;
    }

    public JPanel internoMateria(CardLayout cardLayout, JPanel panelContainer, Materie materia){
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, getX(100), getY(100));
        panel.setBackground(new Color(171, 89, 106));

        panel.add(creaLabel(materia.getNomeMateria(), 0, 0, 100, 10, 35));
        JButton ritorno = creabottone("Ritorna", 0, 85, 10, 5, 18);
        ritorno.addActionListener(_ -> {
            aggiornaMainPage(cardLayout,panelContainer);
            cardLayout.show(panelContainer, "main");
        });
        panel.add(ritorno);
        JPanel panel1 = sottoPanelMedie(1, 3 , 40, 70);
        Float mediaMat = logicUtil.calcolaMediaPerMateria(materia.getIdMateria(), id);
        CerchioMedia cerchioMedia = new CerchioMedia(mediaMat, 14, "Media");
        cerchioMedia.setBounds(getX(5), getY(5), getX(10), getY(10));
        panel1.add(cerchioMedia);

        int x = 2, y = 20;
        float votoIpotetico = 0.0F;
        for(int i = 0, j = 0; i < 20; i++, j++){
            //aumenti
            if(j == 4){
                j = 0;
                x = 2;
                y += 10;
            }
            mediaMat = logicUtil.calcolaMediaPerMateria(materia.getIdMateria(), id , votoIpotetico);
            CerchioMedia cerchioMediaIp = new CerchioMedia(mediaMat, 14, "Media con " + votoIpotetico);
            cerchioMediaIp.setBounds(getX(x), getY(y), getX(10), getY(10));
            votoIpotetico += 0.5F;
            panel1.add(cerchioMediaIp);


            x += 10;
        }




        panel.add(panel1);
        return panel;
    }

    public JPanel sottoPanelMedie(int x, int y, int width, int height) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(getX(x), getY(y), getX(width), getY(height));
        panel.setBackground(new Color(170, 60, 70));
        return panel;
    }
}

