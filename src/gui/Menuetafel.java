package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;
import javax.swing.border.LineBorder;
import logic.Board;

public class Menuetafel extends JPanel {

    private Frame frame;
    private Board board;
    private JButton bedienungsanleitung_btn;

    private JButton start_btn;
    private String start_btn_value = "Start";
    private boolean start_btn_is_clicked;

    private JButton play_btn;
    private boolean play_btn_is_clicked;
    private int farbe_fuer_naechsten_zug;

    private JLabel starting_player_lbl;
    private JComboBox<String> starting_player_dropdown;
    private String selected_starting_player = "S1 beginnt"; // default s1

    private JLabel num_of_colors_lbl;
    private JComboBox<Integer> num_of_colors_dropdown;
    private int selected_num_of_colors = 5; // default 5 farben

    private JLabel num_of_rows_lbl;
    private JComboBox<Integer> num_of_rows_dropdown;
    private int selected_num_of_rows = 6; // default rows 6

    private JLabel num_of_cols_lbl;
    private JComboBox<Integer> num_of_cols_dropdown;
    private int selected_num_of_cols = 6; // default cols 6

    private JLabel pc_strategy_lbl;
    private JComboBox<String> pc_strategy_dropdown;
    private String selected_pc_strategy = "Strategie 01"; // default Strategie 1

    private JLabel component_size_s1;
    private JLabel component_size_s2;

    private Timer timer;
    private int elapsedTime;
    private JLabel time_lbl;
    private JLabel timer_lbl;
    LineBorder line;

    public Menuetafel(Frame frame) {

        setBackground(Color.lightGray);
        setLayout(new GridLayout(17, 1));

        this.frame = frame;

        bedienungsanleitung_btn = new JButton("Bedienungsanleitung");
        start_btn = new JButton("Start");
        play_btn = new JButton("Play");

        starting_player_lbl = new JLabel("Welcher Spieler beginnt?");
        starting_player_dropdown = new JComboBox<>(new String[] { "S1 beginnt", "S2 beginnt" });
        starting_player_dropdown.setEnabled(true);

        num_of_colors_lbl = new JLabel("Mit wie vielen Farben willst du spielen?");
        num_of_colors_dropdown = new JComboBox<>(new Integer[] { 4, 5, 6, 7, 8, 9 });
        num_of_colors_dropdown.setSelectedItem(5);

        num_of_rows_lbl = new JLabel("Wie viele Zeilen soll das Spielbrett haben?");
        num_of_rows_dropdown = new JComboBox<>(new Integer[] { 3, 4, 5, 6, 7, 8, 9, 10 });
        num_of_rows_dropdown.setSelectedItem(6);

        num_of_cols_lbl = new JLabel("Wie viele Spalten soll das Spielbrett haben?");
        num_of_cols_dropdown = new JComboBox<>(new Integer[] { 3, 4, 5, 6, 7, 8, 9, 10 });
        num_of_cols_dropdown.setSelectedItem(6);

        pc_strategy_lbl = new JLabel("Welche Strategie soll der PC spielen?");
        pc_strategy_dropdown = new JComboBox<>(new String[] { "Strategie 01", "Strategie 02", "Strategie 03" });
        pc_strategy_dropdown.setSelectedItem("Strategie 01");

        component_size_s1 = new JLabel(
                "Größe der Komponente von S1: 0");
        component_size_s2 = new JLabel(
                "Größe der Komponente von S2: 0");

        timer_lbl = new JLabel("Gespielte Zeit");
        time_lbl = new JLabel("00:00:00"); // Initialer Text der Timer-Anzeige
        line = new LineBorder(Color.white, 3, true);
        time_lbl.setOpaque(true);
        time_lbl.setBackground(Color.white);
        time_lbl.setBorder(line);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeComponents(frame);
            }
        });

        resizeComponents(frame);

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
                    board = new Board(selected_num_of_rows, selected_num_of_cols, frame);
                    frame.getAnzeigetafel().setBoard(board);
                    start_btn_is_clicked = true;
                    frame.getAnzeigetafel().repaint();
                    start_btn.setText("Stop");
                    start_btn_value = "Stop";
                } else {
                    // If the Stop button is clicked, change its text to "Start"
                    start_btn.setText("Start");
                    start_btn_is_clicked = false;
                    frame.getAnzeigetafel().getBoard().setComponent_player_1(null);
                    frame.getAnzeigetafel().getBoard().setComponent_player_2(null);
                    start_btn_value = "Start";
                    frame.getAnzeigetafel().repaint();
                }
                setFocusable(true);
                requestFocusInWindow();
            }
        });

        play_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (play_btn.getText().equals("Play") && isStart_btn_is_clicked()) {
                    play_btn_is_clicked = true;
                    play_btn.setText("Pause");
                    farbe_fuer_naechsten_zug = frame.getAnzeigetafel().getFarbe_fuer_naechsten_zug();
                    component_size_s1.setText("Größe der Komponente von S1: "
                            + frame.getAnzeigetafel().getBoard().getComponent_player_1().size());
                    component_size_s2.setText("Größe der Komponente von S2: "
                            + frame.getAnzeigetafel().getBoard().getComponent_player_2().size());
                    disable_buttons();
                    startTimer();
                    frame.getAnzeigetafel().requestFocusInWindow();
                    frame.getAnzeigetafel().requestFocus();
                } else {
                    play_btn.setText("Play");
                    play_btn_is_clicked = false;
                    enable_buttons();
                    pauseTimer();
                }
                setFocusable(true);
                requestFocusInWindow();
            }
        });

        starting_player_dropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == starting_player_dropdown) {
                    String selectedItem = (String) starting_player_dropdown.getSelectedItem();
                    selected_starting_player = selectedItem;
                }
                if (selected_starting_player.equals("S2 beginnt")) {
                    frame.getAnzeigetafel().getBoard().setP2_ist_dran(true);
                    frame.getAnzeigetafel().getBoard().setP1_ist_dran(false);
                } else {
                    frame.getAnzeigetafel().getBoard().setP1_ist_dran(true);
                    frame.getAnzeigetafel().getBoard().setP2_ist_dran(false);
                }

            }
        });

        num_of_colors_dropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == num_of_colors_dropdown) {
                    int selectedItem = (int) num_of_colors_dropdown.getSelectedItem();
                    selected_num_of_colors = selectedItem;
                }
            }
        });

        num_of_rows_dropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == num_of_rows_dropdown) {
                    int selectedItem = (int) num_of_rows_dropdown.getSelectedItem();
                    selected_num_of_rows = selectedItem;
                }
            }
        });

        num_of_cols_dropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == num_of_cols_dropdown) {
                    int selectedItem = (int) num_of_cols_dropdown.getSelectedItem();
                    selected_num_of_cols = selectedItem;
                }
            }
        });

        pc_strategy_dropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == pc_strategy_dropdown) {
                    String selectedItem = (String) pc_strategy_dropdown.getSelectedItem();
                    selected_pc_strategy = selectedItem;
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

    private void disable_buttons() {
        bedienungsanleitung_btn.setEnabled(false);
        start_btn.setEnabled(false);
        starting_player_lbl.setEnabled(false);
        starting_player_dropdown.setEnabled(false);
        num_of_colors_lbl.setEnabled(false);
        num_of_colors_dropdown.setEnabled(false);
        num_of_rows_lbl.setEnabled(false);
        num_of_rows_dropdown.setEnabled(false);
        num_of_cols_lbl.setEnabled(false);
        num_of_cols_dropdown.setEnabled(false);
        pc_strategy_lbl.setEnabled(false);
        pc_strategy_dropdown.setEnabled(false);
    }

    private void enable_buttons() {
        bedienungsanleitung_btn.setEnabled(true);
        start_btn.setEnabled(true);
        starting_player_lbl.setEnabled(true);
        starting_player_dropdown.setEnabled(true);
        num_of_colors_lbl.setEnabled(true);
        num_of_colors_dropdown.setEnabled(true);
        num_of_rows_lbl.setEnabled(true);
        num_of_rows_dropdown.setEnabled(true);
        num_of_cols_lbl.setEnabled(true);
        num_of_cols_dropdown.setEnabled(true);
        pc_strategy_lbl.setEnabled(true);
        pc_strategy_dropdown.setEnabled(true);
    }

    private void resizeComponents(Frame f) {
        setPreferredSize(new Dimension((int) (f.getWidth() * 0.3), f.getHeight()));
        revalidate();
        repaint();

        // anzeigetafel.setPreferredSize(new Dimension((int) (f.getWidth() * 0.7),
        // f.getHeight()));
        // anzeigetafel.revalidate();
        // anzeigetafel.repaint();
    }

    // Getter & Setter

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public JButton getBedienungsanleitung_btn() {
        return bedienungsanleitung_btn;
    }

    public void setBedienungsanleitung_btn(JButton bedienungsanleitung_btn) {
        this.bedienungsanleitung_btn = bedienungsanleitung_btn;
    }

    public JButton getStart_btn() {
        return start_btn;
    }

    public void setStart_btn(JButton start_btn) {
        this.start_btn = start_btn;
    }

    public String getStart_btn_value() {
        return start_btn_value;
    }

    public void setStart_btn_value(String start_btn_value) {
        this.start_btn_value = start_btn_value;
    }

    public boolean isStart_btn_is_clicked() {
        return start_btn_is_clicked;
    }

    public void setStart_btn_is_clicked(boolean start_btn_is_clicked) {
        this.start_btn_is_clicked = start_btn_is_clicked;
    }

    public JButton getPlay_btn() {
        return play_btn;
    }

    public void setPlay_btn(JButton play_btn) {
        this.play_btn = play_btn;
    }

    public boolean isPlay_btn_is_clicked() {
        return play_btn_is_clicked;
    }

    public void setPlay_btn_is_clicked(boolean play_btn_is_clicked) {
        this.play_btn_is_clicked = play_btn_is_clicked;
    }

    public int getFarbe_fuer_naechsten_zug() {
        return farbe_fuer_naechsten_zug;
    }

    public void setFarbe_fuer_naechsten_zug(int farbe_fuer_naechsten_zug) {
        this.farbe_fuer_naechsten_zug = farbe_fuer_naechsten_zug;
    }

    public JLabel getStarting_player_lbl() {
        return starting_player_lbl;
    }

    public void setStarting_player_lbl(JLabel starting_player_lbl) {
        this.starting_player_lbl = starting_player_lbl;
    }

    public JComboBox<String> getStarting_player_dropdown() {
        return starting_player_dropdown;
    }

    public void setStarting_player_dropdown(JComboBox<String> starting_player_dropdown) {
        this.starting_player_dropdown = starting_player_dropdown;
    }

    public String getSelected_starting_player() {
        return selected_starting_player;
    }

    public void setSelected_starting_player(String selected_starting_player) {
        this.selected_starting_player = selected_starting_player;
    }

    public JLabel getNum_of_colors_lbl() {
        return num_of_colors_lbl;
    }

    public void setNum_of_colors_lbl(JLabel num_of_colors_lbl) {
        this.num_of_colors_lbl = num_of_colors_lbl;
    }

    public JComboBox<Integer> getNum_of_colors_dropdown() {
        return num_of_colors_dropdown;
    }

    public void setNum_of_colors_dropdown(JComboBox<Integer> num_of_colors_dropdown) {
        this.num_of_colors_dropdown = num_of_colors_dropdown;
    }

    public int getSelected_num_of_colors() {
        return selected_num_of_colors;
    }

    public void setSelected_num_of_colors(int selected_num_of_colors) {
        this.selected_num_of_colors = selected_num_of_colors;
    }

    public JLabel getNum_of_rows_lbl() {
        return num_of_rows_lbl;
    }

    public void setNum_of_rows_lbl(JLabel num_of_rows_lbl) {
        this.num_of_rows_lbl = num_of_rows_lbl;
    }

    public JComboBox<Integer> getNum_of_rows_dropdown() {
        return num_of_rows_dropdown;
    }

    public void setNum_of_rows_dropdown(JComboBox<Integer> num_of_rows_dropdown) {
        this.num_of_rows_dropdown = num_of_rows_dropdown;
    }

    public int getSelected_num_of_rows() {
        return selected_num_of_rows;
    }

    public void setSelected_num_of_rows(int selected_num_of_rows) {
        this.selected_num_of_rows = selected_num_of_rows;
    }

    public JLabel getNum_of_cols_lbl() {
        return num_of_cols_lbl;
    }

    public void setNum_of_cols_lbl(JLabel num_of_cols_lbl) {
        this.num_of_cols_lbl = num_of_cols_lbl;
    }

    public JComboBox<Integer> getNum_of_cols_dropdown() {
        return num_of_cols_dropdown;
    }

    public void setNum_of_cols_dropdown(JComboBox<Integer> num_of_cols_dropdown) {
        this.num_of_cols_dropdown = num_of_cols_dropdown;
    }

    public int getSelected_num_of_cols() {
        return selected_num_of_cols;
    }

    public void setSelected_num_of_cols(int selected_num_of_cols) {
        this.selected_num_of_cols = selected_num_of_cols;
    }

    public JLabel getPc_strategy_lbl() {
        return pc_strategy_lbl;
    }

    public void setPc_strategy_lbl(JLabel pc_strategy_lbl) {
        this.pc_strategy_lbl = pc_strategy_lbl;
    }

    public JComboBox<String> getPc_strategy_dropdown() {
        return pc_strategy_dropdown;
    }

    public void setPc_strategy_dropdown(JComboBox<String> pc_strategy_dropdown) {
        this.pc_strategy_dropdown = pc_strategy_dropdown;
    }

    public String getSelected_pc_strategy() {
        return selected_pc_strategy;
    }

    public void setSelected_pc_strategy(String selected_pc_strategy) {
        this.selected_pc_strategy = selected_pc_strategy;
    }

    public JLabel getComponent_size_s1() {
        return component_size_s1;
    }

    public void setComponent_size_s1(JLabel component_size_s1) {
        this.component_size_s1 = component_size_s1;
    }

    public JLabel getComponent_size_s2() {
        return component_size_s2;
    }

    public void setComponent_size_s2(JLabel component_size_s2) {
        this.component_size_s2 = component_size_s2;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public JLabel getTime_lbl() {
        return time_lbl;
    }

    public void setTime_lbl(JLabel time_lbl) {
        this.time_lbl = time_lbl;
    }

    public JLabel getTimer_lbl() {
        return timer_lbl;
    }

    public void setTimer_lbl(JLabel timer_lbl) {
        this.timer_lbl = timer_lbl;
    }

    public LineBorder getLine() {
        return line;
    }

    public void setLine(LineBorder line) {
        this.line = line;
    }

}