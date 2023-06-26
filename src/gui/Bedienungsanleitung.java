package gui;

import java.awt.*;

import javax.swing.*;

public class Bedienungsanleitung extends JFrame {

    JLabel bedienungsanleitung_lbl;

    public Bedienungsanleitung() {

        setSize(new Dimension(400, 400));
        bedienungsanleitung_lbl = new JLabel(
                "<html>Du kannst die Farbe für deinen nächsten Zug auf<br>folgende Art und Weise wählen: entweder klickst du<br>ein Feld auf dem Spielbrett an, was die Farbe hat, die du möchtest.<br>Oder du klickst die Legende unter dem Spielbrett an.<br>Oder du wählst per Tastatur eine der Farben (die zu klickende Zahl findest du auf der Legende.<br>Viel Spaß!</html>");
        add(bedienungsanleitung_lbl);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
