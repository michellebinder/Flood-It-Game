package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Menuetafel extends JPanel {

    private JButton bedienungsanleitung_btn;
    private JButton start_btn;

    public Menuetafel(Anzeigetafel a, Frame f) {

        setBackground(Color.lightGray);
        setLayout(new GridLayout(8, 1));

        bedienungsanleitung_btn = new JButton("Bedienungsanleitung");
        start_btn = new JButton("Start");

        bedienungsanleitung_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Bedienungsanleitung();
            }
        });

        start_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // If the Start button is clicked, change its text to "Stop"
                if (start_btn.getText().equals("Start")) {
                    start_btn.setText("Stop");
                } else {
                    // If the Stop button is clicked, change its text to "Start"
                    start_btn.setText("Start");
                }
            }
        });

        add(bedienungsanleitung_btn);
        add(start_btn);

    }
}
