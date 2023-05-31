package gui;

import java.awt.*;

import javax.swing.*;

public class Bedienungsanleitung extends JFrame {

    JLabel bedienungsanleitung_lbl;

    public Bedienungsanleitung() {

        setSize(new Dimension(300, 300));

        // TODO: text hinzufügen
        bedienungsanleitung_lbl = new JLabel("Hier kommt noch die Bedienungsanleitung rein");
        add(bedienungsanleitung_lbl);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
