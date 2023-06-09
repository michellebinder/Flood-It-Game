package gui;

import java.awt.*;

import javax.swing.JFrame;

public class Frame extends JFrame {

    private Menuetafel menuetafel;
    private Anzeigetafel anzeigetafel;

    public Frame() {

        // Set the size and minimum size of the JFrame
        setSize(new Dimension(600, 600));
        setMinimumSize(new Dimension(600, 600));

        menuetafel = new Menuetafel(this);
        anzeigetafel = new Anzeigetafel(this);
        GridLayout gridLayout = new GridLayout(1, 2);
        setLayout(gridLayout);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(anzeigetafel);
        add(menuetafel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Getter & Setter

    public Menuetafel getMenuetafel() {
        return menuetafel;
    }

    public void setMenuetafel(Menuetafel menuetafel) {
        this.menuetafel = menuetafel;
    }

    public Anzeigetafel getAnzeigetafel() {
        return anzeigetafel;
    }

    public void setAnzeigetafel(Anzeigetafel anzeigetafel) {
        this.anzeigetafel = anzeigetafel;
    }

}
