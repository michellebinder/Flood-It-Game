package gui;

import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import javax.swing.border.LineBorder;

import logic.Board;

public class Menuetafel extends JPanel implements KeyListener {

    public static Board board;
    private JButton bedienungsanleitung_btn;

    private JButton start_btn;
    public static String start_btn_value = "Start";
    private boolean start_btn_is_clicked;

    private JButton play_btn;
    private boolean play_btn_is_clicked;
    private int farbe_fuer_naechsten_zug;

    private JLabel starting_player_lbl;
    private JComboBox<String> starting_player_dropdown;
    public static String selected_starting_player = "S1 beginnt"; // default s1

    private JLabel num_of_colors_lbl;
    private JComboBox<Integer> num_of_colors_dropdown;
    public static int selected_num_of_colors = 5; // default 5 farben

    private JLabel num_of_rows_lbl;
    private JComboBox<Integer> num_of_rows_dropdown;
    public static int selected_num_of_rows = 6; // default rows 6

    private JLabel num_of_cols_lbl;
    private JComboBox<Integer> num_of_cols_dropdown;
    public static int selected_num_of_cols = 6; // default cols 6

    private JLabel pc_strategy_lbl;
    private JComboBox<String> pc_strategy_dropdown;
    public static String selected_pc_strategy = "Strategie 01"; // default Strategie 1

    private JLabel component_size_s1;
    private JLabel component_size_s2;

    private Timer timer;
    private int elapsedTime;
    private JLabel time_lbl;
    private JLabel timer_lbl;
    LineBorder line;

    public Menuetafel(Frame f) {

        setBackground(Color.lightGray);
        setLayout(new GridLayout(17, 1));

        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);

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

        // TODO: wie kann man den fehler beheben?
        // component_size_s1 = new JLabel(
        // "Größe der Komponente von S1: "
        // + f.getAnzeigetafel().getBoard().getComponent_player_1().size());
        // component_size_s2 = new JLabel(
        // "Größe der Komponente von S2: "
        // + f.getAnzeigetafel().getBoard().getComponent_player_1().size());

        component_size_s1 = new JLabel(
                "Größe der Komponente von S1: ");
        component_size_s2 = new JLabel(
                "Größe der Komponente von S2: ");

        timer_lbl = new JLabel("Gespielte Zeit");
        time_lbl = new JLabel("00:00:00"); // Initialer Text der Timer-Anzeige
        line = new LineBorder(Color.white, 3, true);
        time_lbl.setOpaque(true);
        time_lbl.setBackground(Color.white);
        time_lbl.setBorder(line);

        f.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeComponents(f);
            }
        });

        resizeComponents(f);

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
                    board = new Board(selected_num_of_rows, selected_num_of_cols);
                    f.getAnzeigetafel().setBoard(board);
                    f.getAnzeigetafel().setStart_btn_is_clicked(true);
                    start_btn_is_clicked = true;
                    f.getAnzeigetafel().repaint();
                    start_btn.setText("Stop");
                    start_btn_value = "Stop";
                } else {
                    // If the Stop button is clicked, change its text to "Start"
                    start_btn.setText("Start");
                    f.getAnzeigetafel().setStart_btn_is_clicked(false);
                    start_btn_is_clicked = false;
                    start_btn_value = "Start";
                    f.getAnzeigetafel().repaint();
                }
                setFocusable(true);
                requestFocusInWindow();
            }
        });

        play_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (play_btn.getText().equals("Play")) {
                    f.getAnzeigetafel().setPlay_btn_is_clicked(true);
                    play_btn_is_clicked = true;
                    play_btn.setText("Pause");
                    farbe_fuer_naechsten_zug = f.getAnzeigetafel().getFarbe_fuer_naechsten_zug();
                    disable_buttons();
                    startTimer();
                } else {
                    play_btn.setText("Play");
                    f.getAnzeigetafel().setPlay_btn_is_clicked(false);
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

    public static Board getBoard() {
        return board;
    }

    public static void setBoard(Board board) {
        Menuetafel.board = board;
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

    @Override
    public void keyTyped(KeyEvent e) {
    }

    // // diese nummern werden returned, wenn man auf die zahlen tasten klickt
    // // 48 -- 0
    // // 49 -- 1
    // // 50 -- 2
    // // 51 -- 3
    // // 52 -- 4
    // // 53 -- 5
    // // 54 -- 6
    // // 55 -- 7
    // // 56 -- 8
    // // 57 -- 9
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyChar() - '0'; // Konvertierung von char zu int
        if (key >= 1 && key <= 9) {
            List<Color> selectedColors = board.getSelectedColors();
            if (key <= selectedColors.size()) {
                farbe_fuer_naechsten_zug = key - 1;
                System.out.println("Die ausgewählte Farbe ist: " + farbe_fuer_naechsten_zug);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}