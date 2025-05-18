package app.classeMorta.ClasseMorta.GUI;

import javax.swing.*;
import java.awt.*;

public class CerchioMedia extends JPanel {

    private final double media; // da 0 a 10
    private final int thickness;
    private final String messaggio;
    private double animazioneMedia = 0.000;

    public CerchioMedia(double media, int thickness, String messaggio, Color colore) {
        this.media = media;
        this.thickness = thickness;
        this.messaggio = messaggio;


        Timer timer = new Timer(10, e -> {
            if (animazioneMedia < media - 0.001) { // piccola tolleranza
                animazioneMedia += 0.1;
                repaint();
            } else {
                animazioneMedia = media; // forza valore finale corretto
                repaint();
                ((Timer) e.getSource()).stop();
            }
            this.setBackground(colore);
        });

        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int size = Math.min(getWidth(), getHeight());
        int pad = thickness / 2;

        Graphics2D grafica = (Graphics2D) g;
        grafica.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Sfondo
        grafica.setColor(Color.red);
        grafica.fillOval(pad, pad, size - thickness, size - thickness);

        // Cerchio vuoto
        grafica.setColor(new Color(246, 207, 204));
        grafica.fillOval(pad, pad, size - thickness, size - thickness);

        // Cerchio riempito
        grafica.setStroke(new BasicStroke(thickness));
        if (media >= 6)
            grafica.setColor(new Color(102, 204, 0)); // verde
        else if (media >= 5)
            grafica.setColor(new Color(204, 102, 0)); // arancione
        else
            grafica.setColor(new Color(204, 0, 0)); // rosso

        double angolo = 360 * (animazioneMedia / 10.0);
        grafica.drawArc(pad, pad, size - thickness, size - thickness, 90, -(int) angolo);

        // Testo della media
        grafica.setColor(Color.black);
        grafica.setFont(new Font("SansSerif", Font.BOLD, 30));
        String testo = String.format("%.2f", animazioneMedia);
        FontMetrics fm = grafica.getFontMetrics();
        int textWidth = fm.stringWidth(testo);
        int textHeight = fm.getAscent();
        grafica.drawString(testo, (getWidth() - textWidth) / 2 - 27, getHeight() / 2 - 5);

        // Testo del messaggio (ridimensionato)
        int maxLabelWidth = (int) (size * 0.8); // massimo 80% del cerchio
        int fontSize = 20;
        Font labelFont;
        FontMetrics labelFm;
        do {
            labelFont = new Font("SansSerif", Font.PLAIN, fontSize);
            grafica.setFont(labelFont);
            labelFm = grafica.getFontMetrics();
            if (labelFm.stringWidth(messaggio) <= maxLabelWidth)
                break;
            fontSize--;
        } while (fontSize > 8); // limite minimo

        grafica.setFont(labelFont);
        int labelWidth = labelFm.stringWidth(messaggio);
        grafica.drawString(messaggio, (getWidth() - labelWidth) / 2 - 27, getHeight() / 2 + textHeight - 15);
    }
}
