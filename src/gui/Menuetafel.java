package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Menuetafel extends JPanel {

    private JButton bedienungsanleitung_btn;
    private JButton start_btn;
    private JButton play_btn;
    private JLabel starting_player_lbl;
    private JComboBox<String> starting_player_dropdown;
    private JLabel num_of_colors_lbl;
    private JComboBox<String> num_of_colors_dropdown;
    private JLabel num_of_rows_lbl;
    private JComboBox<String> num_of_rows_dropdown;
    private JLabel num_of_cols_lbl;
    private JComboBox<String> num_of_cols_dropdown;
    private JLabel pc_strategy_lbl;
    private JComboBox<String> pc_strategy_dropdown;
    private JLabel component_size_s1;
    private JLabel component_size_s2;
    private Timer timer;
    private int elapsedTime;
    private JLabel time_lbl;
    private JLabel timer_lbl;
    LineBorder line;

    public Menuetafel(Anzeigetafel a, Frame f) {

        setBackground(Color.lightGray);
        setLayout(new GridLayout(17, 1));

        bedienungsanleitung_btn = new JButton("Bedienungsanleitung");
        start_btn = new JButton("Start");
        play_btn = new JButton("Play");
        starting_player_lbl = new JLabel("Welcher Spieler beginnt?");
        starting_player_dropdown = new JComboBox<>(new String[] { "S1 beginnt", "S2 beginnt" });
        starting_player_dropdown.setEnabled(true);
        num_of_colors_lbl = new JLabel("Mit wie vielen Farben willst du spielen?");
        num_of_colors_dropdown = new JComboBox<>(new String[] { "4", "5", "6", "7", "8", "9" });
        num_of_colors_dropdown.setSelectedItem("5");
        num_of_rows_lbl = new JLabel("Wie viele Zeilen soll das Spielbrett haben?");
        num_of_cols_lbl = new JLabel("Wie viele Spalten soll das Spielbrett haben?");
        num_of_rows_dropdown = new JComboBox<>(new String[] { "3", "4", "5", "6", "7", "8", "9", "10" });
        num_of_rows_dropdown.setSelectedItem("6");
        num_of_cols_dropdown = new JComboBox<>(new String[] { "3", "4", "5", "6", "7", "8", "9", "10" });
        num_of_cols_dropdown.setSelectedItem("6");
        pc_strategy_lbl = new JLabel("Welche Strategie soll der PC spielen?");
        pc_strategy_dropdown = new JComboBox<>(new String[] { "Strategie 01", "Strategie 02", "Strategie 03" });
        pc_strategy_dropdown.setSelectedItem("Strategie 01");
        // TODO: tatsächliche component size einfügen
        component_size_s1 = new JLabel("Größe der Komponente von S1: ");
        // TODO: tatsächliche component size einfügen
        component_size_s2 = new JLabel("Größe der Komponente von S2: ");
        timer_lbl = new JLabel("Gespielte Zeit");
        time_lbl = new JLabel("00:00:00"); // Initialer Text der Timer-Anzeige
        line = new LineBorder(Color.white, 3, true);
        time_lbl.setOpaque(true);
        time_lbl.setBackground(Color.white);
        time_lbl.setBorder(line);

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

        play_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (play_btn.getText().equals("Play")) {
                    play_btn.setText("Pause");
                    startTimer();
                } else {
                    play_btn.setText("Play");
                    pauseTimer();
                }
            }
        });

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime++;
                updateTimerLabel();
            }
        });

        add(bedienungsanleitung_btn);
        add(start_btn);
        add(play_btn);
        add(starting_player_lbl);
        add(starting_player_dropdown);
        add(num_of_colors_lbl);
        add(num_of_colors_dropdown);
        add(num_of_rows_lbl);
        add(num_of_rows_dropdown);
        add(num_of_cols_lbl);
        add(num_of_cols_dropdown);
        add(pc_strategy_lbl);
        add(pc_strategy_dropdown);
        add(component_size_s1);
        add(component_size_s2);
        add(timer_lbl);
        add(time_lbl);
    }

    private void startTimer() {
        timer.start();
    }

    private void pauseTimer() {
        timer.stop();
    }

    private void updateTimerLabel() {
        int hours = elapsedTime / 3600;
        int minutes = (elapsedTime % 3600) / 60;
        int seconds = elapsedTime % 60;

        String hoursStr = String.format("%02d", hours);
        String minutesStr = String.format("%02d", minutes);
        String secondsStr = String.format("%02d", seconds);

        time_lbl.setText(hoursStr + ":" + minutesStr + ":" + secondsStr);
    }
}
