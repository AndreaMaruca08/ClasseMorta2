package app.classeMorta.ClasseMorta.GUI;

import javax.swing.*;
import java.awt.*;

public class CerchioMedia extends JPanel {

    private final double media; // da 0 a 10
    private final int thickness;
    private final String messaggio;
    private double animazioneMedia = 0;

    public CerchioMedia(double media, int thickness, String messaggio) {
        this.media = media;
        this.thickness = thickness;
        this.messaggio = messaggio;


        Timer timer = new Timer(20, e -> {
            if (animazioneMedia < media) {
                animazioneMedia += 0.1;
                repaint();
            } else {
                ((Timer) e.getSource()).stop();
            }
            this.setBackground(new Color(170, 60, 70));
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int size = Math.min(getWidth(), getHeight());
        int pad = thickness / 2;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Sfondo
        g2.setColor(Color.red);
        g2.fillOval(pad, pad, size - thickness, size - thickness);

        // Cerchio vuoto
        g2.setColor(Color.gray);
        g2.fillOval(pad, pad, size - thickness, size - thickness);

        // Cerchio riempito
        g2.setStroke(new BasicStroke(thickness));
        if (media >= 6)
            g2.setColor(new Color(102, 204, 0)); // verde
        else if (media >= 5)
            g2.setColor(new Color(204, 102, 0)); // arancione
        else
            g2.setColor(new Color(204, 0, 0)); // rosso

        double angolo = 360 * (animazioneMedia / 10.0);
        g2.drawArc(pad, pad, size - thickness, size - thickness, 90, -(int) angolo);

        // Testo della media
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 30));
        String testo = String.format("%.1f", animazioneMedia);
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(testo);
        int textHeight = fm.getAscent();
        g2.drawString(testo, (getWidth() - textWidth) / 2 - 27, getHeight() / 2 - 5);

        // Testo del messaggio (ridimensionato)
        int maxLabelWidth = (int)(size * 0.8); // massimo 80% del cerchio
        int fontSize = 20;
        Font labelFont;
        FontMetrics labelFm;
        do {
            labelFont = new Font("SansSerif", Font.PLAIN, fontSize);
            g2.setFont(labelFont);
            labelFm = g2.getFontMetrics();
            if (labelFm.stringWidth(messaggio) <= maxLabelWidth)
                break;
            fontSize--;
        } while (fontSize > 8); // limite minimo

        g2.setFont(labelFont);
        int labelWidth = labelFm.stringWidth(messaggio);
        g2.drawString(messaggio, (getWidth() - labelWidth) / 2 - 27, getHeight() / 2 + textHeight );
    }
}
