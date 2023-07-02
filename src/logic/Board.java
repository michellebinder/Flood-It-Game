
package logic;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import gui.Frame;

public class Board {

    private Frame frame;

    private int rows;
    private int cols;

    private Field[][] board;

    private Color[] colors = new Color[9];
    private ArrayList<Color> selectedColors;
    private int selected_num_of_colors;

    // liste die die komponente von einem spieler darstellt
    private ArrayList<Field> component_player_1;
    private ArrayList<Field> component_player_2;

    // default: spieler 1 fängt an
    private boolean p1_ist_dran = true;
    private boolean p2_ist_dran = false;

    // stores the colors of each player
    private int color_of_player_1;
    private int color_of_player_2;

    private boolean has_valid_num_of_unique_colors = false;
    private boolean has_valid_neighbours = false;
    private boolean has_valid_starting_fields = false;

    private static boolean is_start_klar = false;

    private Timer timer;

    private boolean component_has_grown = false;

    private int num_of_moves_without_growth = 0;

    private boolean is_game_over = false;

    private int input = -1;

    // array to store the num of occurence of each color for strategy
    private int[] num_of_color_occurence;

    public Board(int rows, int cols, Frame frame) {

        this.rows = rows;
        this.cols = cols;
        this.frame = frame;

        board = new Field[rows][cols];

        component_player_1 = new ArrayList<Field>();
        component_player_2 = new ArrayList<Field>();

        colors[0] = Color.LIGHT_GRAY;
        colors[1] = Color.GRAY;
        colors[2] = Color.ORANGE;
        colors[3] = Color.BLUE;
        colors[4] = Color.CYAN;
        colors[5] = Color.GREEN;
        colors[6] = Color.MAGENTA;
        colors[7] = Color.PINK;
        colors[8] = Color.YELLOW;

        selected_num_of_colors = frame.getMenuetafel().getSelected_num_of_colors();
        while (!has_valid_num_of_unique_colors || !has_valid_starting_fields) {
            createBoard();
        }

        if (frame.getMenuetafel().isStart_btn_is_clicked()) {
            component_player_1.clear();
            component_player_2.clear();
        }

        // stores the number of the players starting colors
        color_of_player_1 = board[rows - 1][0].getColor();
        color_of_player_2 = board[0][cols - 1].getColor();
    }

    private void createBoard() {

        Random random = new Random();

        int num_of_unique_colors = 0;
        selectedColors = new ArrayList<>();
        Set<Integer> uniqueColors = new HashSet<>();

        // befülle die liste selectedColors mit so vielen farben, wie der user
        // ausgewählt hat
        while (num_of_unique_colors < selected_num_of_colors) {
            selectedColors.add(colors[num_of_unique_colors]);
            num_of_unique_colors++;
        }
        int random_color_index;

        // erstelle das board, indem du random aus der selectedColors liste farben
        // auswählst
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                // entscheide dich random für eine farbe
                random_color_index = random.nextInt(selectedColors.size());

                // erste feld
                if (i == 0 && j == 0) {

                    board[i][j] = new Field(i, j, random_color_index);

                }

                // erste reihe -> vergleiche mit feld links von dem aktuellen
                else if (i == 0) {

                    while (board[i][j - 1].getColor() == random_color_index) {
                        random_color_index = random.nextInt(selectedColors.size());
                    }

                    board[i][j] = new Field(i, j, random_color_index);

                    // spalte ganz links -> vergleiche immer mit dem feld über dir, weil rechts
                    // davon noch nichts existiert
                } else if (j == 0) {
                    while (board[i - 1][j].getColor() == random_color_index) {
                        random_color_index = random.nextInt(selectedColors.size());
                    }

                    board[i][j] = new Field(i, j, random_color_index);
                }
                // alle restlichen felder: vergleiche mit dem feld über dir und mit dem feld
                // links von dir
                else {
                    // drüber
                    while (board[i - 1][j].getColor() == random_color_index
                            // links davon
                            || board[i][j - 1].getColor() == random_color_index) {
                        random_color_index = random.nextInt(selectedColors.size());
                    }
                    board[i][j] = new Field(i, j, random_color_index);
                }
                if (!uniqueColors.contains(board[i][j].getColor())) {
                    uniqueColors.add(board[i][j].getColor());
                }
                // (2) Wenn es t viele Farben im Spiel gibt, gibt es auch t viele Farben im
                // Spielbrett.
                if (uniqueColors.size() == selectedColors.size()) {
                    has_valid_num_of_unique_colors = true;
                }
            }
        }

        // (3) Das Feld in der Ecke links unten hat nicht die gleiche Farbe wie das Feld
        // in der Ecke rechts oben
        Field upper_right_field = board[0][board[0].length - 1];
        Field lower_left_field = board[board.length - 1][0];
        // wenn das feld oben rechts und unten links ne andere farbe haben
        if (upper_right_field.getColor() != lower_left_field.getColor()) {
            has_valid_starting_fields = true;
            addFieldToComponentOfPlayer1(lower_left_field);
            addFieldToComponentOfPlayer2(upper_right_field);
        } else {
            has_valid_starting_fields = false;
        }

        if (has_valid_neighbours && has_valid_num_of_unique_colors && has_valid_starting_fields) {
            is_start_klar = true;
        } else {
            is_start_klar = false;
        }
    }

    private void printComponent1() {
        for (int i = 0; i < component_player_1.size(); i++) {
            int row = component_player_1.get(i).getRow();
            int col = component_player_1.get(i).getCol();
            int color = component_player_1.get(i).getColor();
            System.out.println("comp p1: (" + row + "," + col + "), color: " + color);
            System.out.println();
        }
    }

    private void printComponent2() {
        for (Field f : component_player_2) {
            int row = f.getRow();
            int col = f.getCol();
            int color = f.getColor();
            System.out.println("comp p2: (" + row + "," + col + "), color: " + color);
            System.out.println("end");
        }
    }

    public void makeMoveS1(ArrayList<Field> component, int selected_color) {

        // speichert die größe der komponente bevor der zug gemacht wurde
        int old_size = component_player_1.size();

        if (!isEndConfig()) {

            // prüfe, ob die ausgewählte farbe valide ist -> wenn nicht, gib meldung zurück
            if (checkIfColorSelectionIsValid(selected_color)) {

                // kopie von der komponente, an der die änderungen vorgenommen werden
                ArrayList<Field> updatedComponent = new ArrayList<>(component);

                // gehe alle felder durch, die in der komponente enthalten sind
                for (int i = 0; i < updatedComponent.size(); i++) {
                    Field field = updatedComponent.get(i);
                    field.setColor(selected_color);
                    color_of_player_1 = selected_color;

                    // schau dir die nachbarn von dem aktuellen feld an
                    List<Field> neighbours = getNeighbors(field);
                    for (Field neighbour : neighbours) {
                        // füge den nachbarn zur komponente hinzu, falls
                        // - er nicht schon in der komponente drin ist
                        // - er die ausgewählte farbe hat
                        if (!updatedComponent.contains(neighbour) && neighbour.getColor() == selected_color) {
                            neighbour.setColor(selected_color);
                            color_of_player_1 = selected_color;
                            updatedComponent.add(neighbour);
                            frame.getAnzeigetafel().repaint();
                        }
                    }
                    frame.getAnzeigetafel().repaint();
                    p1_ist_dran = false;
                    p2_ist_dran = true;
                }
                // 1 sekunde puffer bis sich die farbe verändert
                // ausserhalb der for-schleife, damit der puffer für die gesamte komponente
                // wirklich nur 1 sek ist, statt 1 sek pro field
                try {
                    Thread.sleep(1000);
                    frame.getAnzeigetafel().repaint();
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

                component_player_1 = updatedComponent;

                // speichert die größe der komponente nachdem der zug gemacht wurde
                int new_size = component_player_1.size();

                // Überprüfe, ob sich die Komponente vergrößert hat
                if (old_size == new_size) {
                    num_of_moves_without_growth++;
                } else {
                    num_of_moves_without_growth = 0;
                }

                // sorgt dafür, dass das popup erscheint, nachdem der zug gemacht wurde
                SwingUtilities.invokeLater(() -> {
                    if (num_of_moves_without_growth == 4) {
                        showPopUpAfter4InvalidMoves();
                    }
                });

                // zug von s2 nach 1 sekunde auslösen
                timer = new Timer(1000, e -> {
                    makeMoveS2(frame.getMenuetafel().getSelected_pc_strategy());
                    timer.stop();
                });
                timer.setRepeats(false);
                timer.start();
                // updated das label was die komponentengröße anzeigt. wenn spiel vorbei, soll
                // das wieder auf 0 gesetzt werden
                if (input == -1) {
                    frame.getMenuetafel().updateComponentSizeLabels();
                } else {
                    frame.getMenuetafel().resetComponentSizeLabels();
                }

            } else {
                // popup fenster wenn eine ungültige farbe gewählt wurde
                JOptionPane.showMessageDialog(frame.getAnzeigetafel(), "Du hast eine ungültige Farbe gewählt");
            }
        } else {
            showPopUpWhenGameIsOver();
        }
    }

    public void makeMoveS2(String selected_pc_strategy) {
        // gewaährleistet das s2 erst einen move macht, wenn s1 einen validen mpve
        // gemacht hat
        if (!isEndConfig()) {
            if (p2_ist_dran == true) {
                if (selected_pc_strategy.equals("Strategie 1 (Stagnation)")) {
                    Stagnation();
                } else if (selected_pc_strategy.equals("Strategie 2 (Greedy)")) {
                    Greedy();
                } else if (selected_pc_strategy.equals("Strategie 3 (Blocking)")) {
                    Blocking();
                }
                try {
                    Thread.sleep(1000);
                    frame.getAnzeigetafel().repaint();
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

            }
        } else {
            if (input == -1) {
                frame.getMenuetafel().updateComponentSizeLabels();
            } else {
                frame.getMenuetafel().resetComponentSizeLabels();
            }
            showPopUpWhenGameIsOver();
        }
    }

    /******** STRATEGIEN VON S2 *********/
    private void Stagnation() {

        // speichert die größe der komponente bevor der zug gemacht wurde
        int old_size = component_player_2.size();

        // finde raus, wie oft jede farbe vorkommt (gespeichert in
        // num_of_color_occurence array)
        getNumOfOccurenceOfColors(component_player_2);
        num_of_color_occurence[color_of_player_1] = 2000;
        num_of_color_occurence[color_of_player_2] = 2000;

        // nun tatsächlich für die kleinste farbe entscheiden
        // setze die zahl der gewählten farbe extra so hoch, dass sie auf jeden fall von
        // einer tatsächlichen farbe abgelöst wird
        int chosen_color = 20;
        int min = 20;
        for (int i = 0; i < num_of_color_occurence.length; i++) {
            //
            if (num_of_color_occurence[i] < min) {
                chosen_color = i;
                min = num_of_color_occurence[i];
                // wenn es eine zahl gibt die genau so selten vorkommt wie das derzeitige
                // minimum
            } else if (num_of_color_occurence[i] == min) {
                // dann schau ob das eine kleinere zahl ist
                if (i < chosen_color) {
                    chosen_color = i;
                    min = num_of_color_occurence[i];
                }
            }

        }

        // füge nun die felder mit der neuen farbe zu der komponente hinzu
        // gehe alle felder durch, die in der komponente enthalten sind
        for (int i = 0; i < component_player_2.size(); i++) {
            Field field = component_player_2.get(i);
            // setze die farbe von der komponente auf die neu gewählte farbe
            field.setColor(chosen_color);
            setColor_of_player_2(chosen_color);
            // schau dir die nachbarn von dem aktuellen feld an
            List<Field> neighbours = getNeighbors(field);

            for (Field neighbour : neighbours) {
                // wenn das feld noch nicht zu einer der komponenten gehört
                if (!component_player_2.contains(neighbour) && !component_player_2.contains(neighbour)) {
                    // und das feld die neue farbe hat
                    if (neighbour.getColor() == chosen_color) {
                        // dann füge es zur komponente hinzu
                        component_player_2.add(neighbour);
                        // setze die aktuelle farbe von s2 auf die gewählte farbe
                        setColor_of_player_2(chosen_color);
                        frame.getAnzeigetafel().repaint();
                    }
                }
            }
        }

        // speichert die größe der komponente nachdem der zug gemacht wurde
        int new_size = component_player_2.size();

        // Überprüfe, ob sich die Komponente vergrößert hat
        if (old_size == new_size) {
            num_of_moves_without_growth++;
        } else {
            num_of_moves_without_growth = 0;
        }

        frame.getMenuetafel().updateComponentSizeLabels();
        frame.getAnzeigetafel().repaint();

        // sorgt dafür, dass das popup erscheint, nachdem der zug gemacht wurde
        SwingUtilities.invokeLater(() -> {
            if (isEndConfig()) {
                showPopUpWhenGameIsOver();
            }
            if (num_of_moves_without_growth == 4) {
                showPopUpAfter4InvalidMoves();
            }
        });

        p2_ist_dran = false;
        p1_ist_dran = true;
    }

    private void Greedy() {

        // speichert die größe der komponente bevor der zug gemacht wurde
        int old_size = component_player_2.size();

        // finde raus, wie oft jede farbe vorkommt (gespeichert in
        // num_of_color_occurence array)
        getNumOfOccurenceOfColors(component_player_2);
        num_of_color_occurence[color_of_player_1] = -2000;
        num_of_color_occurence[color_of_player_2] = -2000;

        // nun tatsächlich für die größte farbe entscheiden
        // setze die zahl der gewählten farbe extra so niedrig, dass sie auf jeden fall
        // von
        // einer tatsächlichen farbe abgelöst wird
        int chosen_color = -20;
        int max = -20;
        for (int i = 0; i < num_of_color_occurence.length; i++) {
            //
            if (num_of_color_occurence[i] > max) {
                chosen_color = i;
                max = num_of_color_occurence[i];
                // wenn es eine zahl gibt die genau so selten vorkommt wie das derzeitige
                // minimum
            } else if (num_of_color_occurence[i] == max) {
                // dann schau ob das eine kleinere zahl ist
                if (i < chosen_color) {
                    chosen_color = i;
                    max = num_of_color_occurence[i];
                }
            }
        }
        // füge nun die felder mit der neuen farbe zu der komponente hinzu
        // gehe alle felder durch, die in der komponente enthalten sind
        for (int i = 0; i < component_player_2.size(); i++) {
            Field field = component_player_2.get(i);
            // setze die farbe von der komponente auf die neu gewählte farbe
            field.setColor(chosen_color);
            setColor_of_player_2(chosen_color);
            // schau dir die nachbarn von dem aktuellen feld an
            List<Field> neighbours = getNeighbors(field);

            for (Field neighbour : neighbours) {
                // wenn das feld noch nicht zu einer der komponenten gehört
                if (!component_player_2.contains(neighbour) && !component_player_2.contains(neighbour)) {
                    // und das feld die neue farbe hat
                    if (neighbour.getColor() == chosen_color) {
                        // dann füge es zur komponente hinzu
                        component_player_2.add(neighbour);
                        // setze die aktuelle farbe von s2 auf die gewählte farbe
                        setColor_of_player_2(chosen_color);
                    }
                }
            }
        }
        // speichert die größe der komponente nachdem der zug gemacht wurde
        int new_size = component_player_2.size();

        // Überprüfe, ob sich die Komponente vergrößert hat
        if (old_size == new_size) {
            num_of_moves_without_growth++;
        } else {
            num_of_moves_without_growth = 0;
        }
        frame.getMenuetafel().updateComponentSizeLabels();
        frame.getAnzeigetafel().repaint();

        // sorgt dafür, dass das popup erscheint, nachdem der zug gemacht wurde
        SwingUtilities.invokeLater(() -> {
            if (isEndConfig()) {
                showPopUpWhenGameIsOver();
            }
            if (num_of_moves_without_growth == 4) {
                showPopUpAfter4InvalidMoves();
            }
        });

        p2_ist_dran = false;
        p1_ist_dran = true;
    }

    private void Blocking() {

        // speichert die größe der komponente bevor der zug gemacht wurde
        int old_size = component_player_2.size();

        // finde raus, wie oft jede farbe vorkommt (gespeichert in
        // num_of_color_occurence array)
        getNumOfOccurenceOfColors(component_player_1);
        num_of_color_occurence[color_of_player_1] = -2000;
        num_of_color_occurence[color_of_player_2] = -2000;

        // nun tatsächlich für die größte farbe entscheiden
        // setze die zahl der gewählten farbe extra so niedrig, dass sie auf jeden fall
        // von
        // einer tatsächlichen farbe abgelöst wird
        int chosen_color = -20;
        int max = -20;
        for (int i = 0; i < num_of_color_occurence.length; i++) {
            //
            if (num_of_color_occurence[i] > max) {
                chosen_color = i;
                max = num_of_color_occurence[i];
                // wenn es eine zahl gibt die genau so selten vorkommt wie das derzeitige
                // minimum
            } else if (num_of_color_occurence[i] == max) {
                // dann schau ob das eine kleinere zahl ist
                if (i < chosen_color) {
                    chosen_color = i;
                    max = num_of_color_occurence[i];
                }
            }

        }
        // füge nun die felder mit der neuen farbe zu der komponente hinzu
        // gehe alle felder durch, die in der komponente enthalten sind
        for (int i = 0; i < component_player_2.size(); i++) {
            Field field = component_player_2.get(i);
            // setze die farbe von der komponente auf die neu gewählte farbe
            field.setColor(chosen_color);
            setColor_of_player_2(chosen_color);
            // schau dir die nachbarn von dem aktuellen feld an
            List<Field> neighbours = getNeighbors(field);

            for (Field neighbour : neighbours) {
                // wenn das feld noch nicht zu einer der komponenten gehört
                if (!component_player_2.contains(neighbour) && !component_player_2.contains(neighbour)) {
                    // und das feld die neue farbe hat
                    if (neighbour.getColor() == chosen_color) {
                        // dann füge es zur komponente hinzu
                        component_player_2.add(neighbour);
                        // setze die aktuelle farbe von s2 auf die gewählte farbe
                        setColor_of_player_2(chosen_color);
                    }
                }
            }
            frame.getAnzeigetafel().repaint();
        }

        // speichert die größe der komponente nachdem der zug gemacht wurde
        int new_size = component_player_2.size();

        // Überprüfe, ob sich die Komponente vergrößert hat
        if (old_size == new_size) {
            num_of_moves_without_growth++;
        } else {
            num_of_moves_without_growth = 0;
        }

        frame.getMenuetafel().updateComponentSizeLabels();
        frame.getAnzeigetafel().repaint();

        // sorgt dafür, dass das popup erscheint, nachdem der zug gemacht wurde
        SwingUtilities.invokeLater(() -> {
            if (isEndConfig()) {
                showPopUpWhenGameIsOver();
            }
            if (num_of_moves_without_growth == 4) {
                showPopUpAfter4InvalidMoves();
            }
        });

        p2_ist_dran = false;
        p1_ist_dran = true;
    }

    // Hilfsmethode: holt die nachbarn eines feldes
    private ArrayList<Field> getNeighbors(Field field) {
        ArrayList<Field> neighbours = new ArrayList<>();
        int row = field.getRow();
        int col = field.getCol();

        // Überprüfen der Nachbarn oben, unten, links und rechts
        // Überprüfen des oberen Nachbarn
        if (row - 1 >= 0 && row - 1 < board.length && col >= 0 && col < board[row - 1].length) {
            neighbours.add(board[row - 1][col]);
        }

        // Überprüfen des unteren Nachbarn
        if (row + 1 >= 0 && row + 1 < board.length && col >= 0 && col < board[row + 1].length) {
            neighbours.add(board[row + 1][col]);
        }

        // Überprüfen des linken Nachbarn
        if (row >= 0 && row < board.length && col - 1 >= 0 && col - 1 < board[row].length) {
            neighbours.add(board[row][col - 1]);
        }

        // Überprüfen des rechten Nachbarn
        if (row >= 0 && row < board.length && col + 1 >= 0 && col + 1 < board[row].length) {
            neighbours.add(board[row][col + 1]);
        }

        return neighbours;
    }

    // Hilfsmethode: holt alle nachbarn einer komponente
    private ArrayList<Field> getNeighboursOfComponent(ArrayList<Field> component) {

        // neue liste erstellen, in der die nachbarn der komponente gespeichert werden
        ArrayList<Field> neighbours_of_component = new ArrayList<>();

        // gehe alle felder in der komponente durch
        for (Field f : component) {
            // schau dir die nachbarn von dem aktuellen feld an
            List<Field> neighbours = getNeighbors(f);

            // gehe alle diese nachbarn durch
            for (Field neighbour : neighbours) {
                // wenn
                // - der nachbar bereits in der neighbours_of_component liste enthalten ist
                // - oder der nachbar in der komponente enthalten ist
                // dann füge ihn nicht hinzu. ansonsten füge ihn hinzu
                if (!neighbours_of_component.contains(neighbour) && !component.contains(neighbour)) {
                    neighbours_of_component.add(neighbour);
                }
            }
        }
        return neighbours_of_component;
    }

    // Hilfsmethode: findet raus, wie oft die nachbarn der komponente von s2 jeweils
    // vorkommen
    private void getNumOfOccurenceOfColors(ArrayList<Field> component) {
        // erstelle ein array was an der stelle i die anzahl der vorkommnisse von zahl i
        // enthält
        num_of_color_occurence = new int[selectedColors.size()];

        // hol dir alle nachbarn der komponente
        ArrayList<Field> neighbours_of_component = getNeighboursOfComponent(component);

        // laufe alle nachbarn der komponente durch
        for (Field neighbour : neighbours_of_component) {

            // wenn nicht -> feld ist valide
            if (!component_player_1.contains(neighbour) && !component_player_2.contains(neighbour)) {

                num_of_color_occurence[neighbour.getColor()]++;
            }
        }
    }

    private void addFieldToComponentOfPlayer1(Field field) {
        component_player_1.add(field);
    }

    private void addFieldToComponentOfPlayer2(Field field) {
        component_player_2.add(field);
    }

    // hilfsmethode, die geprüft wird, wenn spieler 1 einen zug machen will
    private boolean checkIfColorSelectionIsValid(int selected_color) {
        // Fall 1: s1 ist dran
        // s1 darf nicht die eigene farbe nochmal wählen
        // s1 darf nicht die farbe seines gegners wählen
        if (selected_color != getColor_of_player_1() && selected_color != getColor_of_player_2()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isEndConfig() {

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Field field = board[i][j];
                if (!component_player_1.contains(field) && !component_player_2.contains(field)) {
                    return false; // Wenn ein Feld weder zu s1 noch zu s2 gehört, ist es keine Endkonfiguration
                }
            }
        }
        return true; // Alle Felder gehören entweder zu s1 oder s2
    }

    public void showPopUpWhenGameIsOver() {
        // popup fenster wenn das spiel vorbei ist
        String pop_up_text = "";
        // popup fenster wenn das spiel vorbei ist
        if (component_player_1.size() > component_player_2.size()) {
            pop_up_text = "Du hast gewonnen! Herzlichen Glückwunsch!";
        } else if (component_player_2.size() > component_player_1.size()) {
            pop_up_text = "Der Computer hat gewonnen. Vielleicht klappts ja beim nächsten Mal!";
        } else if (component_player_1.size() == component_player_2.size() || num_of_moves_without_growth == 4) {
            pop_up_text = "Gleichstand";
        }
        is_game_over = true;
        frame.getMenuetafel().pauseTimer();
        input = JOptionPane.showConfirmDialog(null,
                "Das Spiel ist vorbei. " + pop_up_text, "Game over", JOptionPane.DEFAULT_OPTION);
        if (input == 0) {
            frame.getMenuetafel().getComponent_size_s1().setText("Komponentengröße S1: 0");
            frame.getMenuetafel().getComponent_size_s2().setText("Komponentengröße S2: 0");
        }
        frame.getMenuetafel().getStart_btn().setText("Start");
        frame.getMenuetafel().setStart_btn_is_clicked(false);
        frame.getMenuetafel().setStart_btn_value("Start");

        frame.getMenuetafel().getPlay_btn().setText("Play");
        frame.getMenuetafel().setPlay_btn_is_clicked(false);
        frame.getMenuetafel().enable_buttons();
        // label wer dran ist entfernen sobald stop gedrückt wird und board verschwindet
        frame.getAnzeigetafel().getCurrent_player_anzeige_lbl().setText("");
        frame.getMenuetafel().pauseTimer();
        frame.getMenuetafel().setElapsedTime(0);
        frame.getMenuetafel().getTime_lbl().setText("00:00:00");
        component_player_1.clear();
        component_player_2.clear();
        frame.getMenuetafel().getComponent_size_s1().setText("Komponentengröße S1: 0");
        frame.getMenuetafel().getComponent_size_s2().setText("Komponentengröße S2: 0");

        frame.getMenuetafel().repaint();
        frame.getAnzeigetafel().repaint();

    }

    private void showPopUpAfter4InvalidMoves() {
        is_game_over = true;
        frame.getMenuetafel().pauseTimer();
        input = JOptionPane.showConfirmDialog(null,
                "Das Spiel ist vorbei. Gleichstand.", "Game over", JOptionPane.DEFAULT_OPTION);
        if (input == 0) {
            frame.getMenuetafel().getComponent_size_s1().setText("Komponentengröße S1: 0");
            frame.getMenuetafel().getComponent_size_s2().setText("Komponentengröße S2: 0");
        }
        // behavour of menue panel
        frame.getMenuetafel().getStart_btn().setText("Start");
        frame.getMenuetafel().setStart_btn_is_clicked(false);
        frame.getMenuetafel().setStart_btn_value("Start");

        frame.getMenuetafel().getPlay_btn().setText("Play");
        frame.getMenuetafel().setPlay_btn_is_clicked(false);
        frame.getMenuetafel().enable_buttons();
        // label wer dran ist entfernen sobald stop gedrückt wird und board verschwindet
        frame.getAnzeigetafel().getCurrent_player_anzeige_lbl().setText("");
        frame.getMenuetafel().pauseTimer();
        frame.getMenuetafel().setElapsedTime(0);
        frame.getMenuetafel().getTime_lbl().setText("00:00:00");

        frame.getAnzeigetafel().repaint();
        component_player_1.clear();
        component_player_2.clear();
        frame.getMenuetafel().getComponent_size_s1().setText("Komponentengröße S1: 0");
        frame.getMenuetafel().getComponent_size_s2().setText("Komponentengröße S2: 0");
        frame.getMenuetafel().repaint();

    }

    // Getter & Setter

    public Field getField(int row, int col) {
        return board[row][col];
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public Color[] getColors() {
        return colors;
    }

    public void setColors(Color[] colors) {
        this.colors = colors;
    }

    public Field[][] getBoard() {
        return board;
    }

    public void setBoard(Field[][] board) {
        this.board = board;
    }

    public Color getFieldColor(int row, int col) {
        int colorIndex = board[row][col].getColor();
        return colors[colorIndex];
    }

    public Frame getFrame() {
        return frame;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public ArrayList<Color> getSelectedColors() {
        return selectedColors;
    }

    public void setSelectedColors(ArrayList<Color> selectedColors) {
        this.selectedColors = selectedColors;
    }

    public int getSelected_num_of_colors() {
        return selected_num_of_colors;
    }

    public void setSelected_num_of_colors(int selected_num_of_colors) {
        this.selected_num_of_colors = selected_num_of_colors;
    }

    public ArrayList<Field> getComponent_player_1() {
        return component_player_1;
    }

    public void setComponent_player_1(ArrayList<Field> component_player_1) {
        this.component_player_1 = component_player_1;
    }

    public ArrayList<Field> getComponent_player_2() {
        return component_player_2;
    }

    public void setComponent_player_2(ArrayList<Field> component_player_2) {
        this.component_player_2 = component_player_2;
    }

    public int getColor_of_player_1() {
        return color_of_player_1;
    }

    public void setColor_of_player_1(int color_of_player_1) {
        this.color_of_player_1 = color_of_player_1;
    }

    public int getColor_of_player_2() {
        return color_of_player_2;
    }

    public void setColor_of_player_2(int color_of_player_2) {
        this.color_of_player_2 = color_of_player_2;
    }

    public boolean isHas_valid_num_of_unique_colors() {
        return has_valid_num_of_unique_colors;
    }

    public void setHas_valid_num_of_unique_colors(boolean has_valid_num_of_unique_colors) {
        this.has_valid_num_of_unique_colors = has_valid_num_of_unique_colors;
    }

    public boolean isHas_valid_neighbours() {
        return has_valid_neighbours;
    }

    public void setHas_valid_neighbours(boolean has_valid_neighbours) {
        this.has_valid_neighbours = has_valid_neighbours;
    }

    public boolean isHas_valid_starting_fields() {
        return has_valid_starting_fields;
    }

    public void setHas_valid_starting_fields(boolean has_valid_starting_fields) {
        this.has_valid_starting_fields = has_valid_starting_fields;
    }

    public boolean isP1_ist_dran() {
        return p1_ist_dran;
    }

    public void setP1_ist_dran(boolean p1_ist_dran) {
        this.p1_ist_dran = p1_ist_dran;
    }

    public boolean isP2_ist_dran() {
        return p2_ist_dran;
    }

    public void setP2_ist_dran(boolean p2_ist_dran) {
        this.p2_ist_dran = p2_ist_dran;
    }

    public static boolean isIs_start_klar() {
        return is_start_klar;
    }

    public static void setIs_start_klar(boolean is_start_klar) {
        Board.is_start_klar = is_start_klar;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public boolean isComponent_has_grown() {
        return component_has_grown;
    }

    public void setComponent_has_grown(boolean component_has_grown) {
        this.component_has_grown = component_has_grown;
    }

    public int getNum_of_moves_without_growth() {
        return num_of_moves_without_growth;
    }

    public void setNum_of_moves_without_growth(int num_of_moves_without_growth) {
        this.num_of_moves_without_growth = num_of_moves_without_growth;
    }

    public int[] getNum_of_color_occurence() {
        return num_of_color_occurence;
    }

    public void setNum_of_color_occurence(int[] num_of_color_occurence) {
        this.num_of_color_occurence = num_of_color_occurence;
    }

    public boolean isIs_game_over() {
        return is_game_over;
    }

    public void setIs_game_over(boolean is_game_over) {
        this.is_game_over = is_game_over;
    }

    public int getInput() {
        return input;
    }

    public void setInput(int input) {
        this.input = input;
    }

}