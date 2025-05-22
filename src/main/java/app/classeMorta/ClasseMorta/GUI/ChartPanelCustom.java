package app.classeMorta.ClasseMorta.GUI;

import app.classeMorta.ClasseMorta.logic.models.Voti;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;

public class ChartPanelCustom extends JPanel {
    public ChartPanelCustom(String titoloGrafico, String titoloAsseX, String titoloAsseY, List<Voti> votiList) {
        // Ordina i voti per data crescente
        votiList.sort(Comparator.comparing(Voti::getData));

        XYSeries series = new XYSeries("Voti");

        for (Voti voto : votiList) {
            // Converte la LocalDate in milliseconds since epoch (long)
            double x = voto.getData().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            double y = voto.getVoto();
            series.add(x, y);
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                titoloGrafico,
                titoloAsseX + " (data)",  // es: Data
                titoloAsseY,              // es: Voto
                dataset
        );

        // Migliora la leggibilità mostrando l'asse delle X come date anziché numeri long
        var plot = chart.getXYPlot();
        var domainAxis = new org.jfree.chart.axis.DateAxis(titoloAsseX);
        plot.setDomainAxis(domainAxis);

        ChartPanel chartPanel = new ChartPanel(chart);
        setLayout(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);
    }
}