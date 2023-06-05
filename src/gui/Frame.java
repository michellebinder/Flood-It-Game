package gui;

import java.awt.*;

import javax.swing.JFrame;

import logic.Board;

public class Frame extends JFrame {

    private Menuetafel menuetafel;
    private Anzeigetafel anzeigetafel;

    public Frame() {
        // Set the size and minimum size of the JFrame
        setSize(new Dimension(600, 600));
        setMinimumSize(new Dimension(600, 600));

        menuetafel = new Menuetafel(this);
        anzeigetafel = new Anzeigetafel(this, Menuetafel.getBoard());

        GridLayout gridLayout = new GridLayout(1, 2);
        setLayout(gridLayout);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(anzeigetafel);
        add(menuetafel);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
