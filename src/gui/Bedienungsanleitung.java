// Michelle Binder
// 7345155
package gui;

import java.awt.*;

import javax.swing.*;

public class Bedienungsanleitung extends JFrame {

    JLabel bedienungsanleitung_lbl;

    public Bedienungsanleitung() {

        setMinimumSize(new Dimension(400, 400));
        bedienungsanleitung_lbl = new JLabel(
                "<html>Dein Feld ist unten links, das Feld von deinem Gegner ist oben rechts. <br>Du kannst die Farbe für deinen nächsten Zug auf<br>folgende Art und Weise wählen:<br>- Entweder klickst du ein Feld auf dem Spielbrett an, was die Farbe hat, die du möchtest.<br>- Oder du klickst die Legende unter dem Spielbrett an.<br>- Oder du wählst per Tastatur eine der Farben (die zu klickende Zahl findest du auf der Legende).<br><br>Hinweis: Du darfst nicht deine eigene Farbe nochmal oder die Farbe deines Gegners wählen. Solltest du eine unerlaubte Farbe wählen, gibt es eine Meldung.<br><br>Viel Spaß!</html>");
        add(bedienungsanleitung_lbl);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
