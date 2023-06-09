package logic;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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

    private boolean p1_ist_dran;
    private boolean p2_ist_dran;

    // stores the colors of each player
    private Color[] colors_of_player_1;
    private Color[] colors_of_player_2;

    private int starting_color_of_player_1;
    private int starting_color_of_player_2;

    private boolean has_valid_num_of_unique_colors = false;
    private boolean has_valid_neighbours = false;
    private boolean has_valid_starting_fields = false;

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

        // stores the number of the players starting colors
        starting_color_of_player_1 = board[rows - 1][0].getColor();
        starting_color_of_player_2 = board[0][cols - 1].getColor();

        // System.out.println("color index s1: " + starting_color_of_player_1);
        // System.out.println("color index s2: " + starting_color_of_player_2);
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
                // wenn die tatsächlichen farben auf dem brett der vom nutzer ausgewählten
                // anzahl entsprechen
                if (uniqueColors.size() == selectedColors.size()) {
                    has_valid_num_of_unique_colors = true;

                }
            }
        }

        // Anforderung 3: Das Feld in der Ecke links unten hat nicht die gleiche Farbe
        // wie das Feld in der Ecke rechts oben.
        Field upper_right_field = board[0][board[0].length - 1];
        Field lower_left_field = board[board.length - 1][0];
        // wenn das feld oben rechts und unten links ne andere farbe haben
        if (upper_right_field.getColor() != lower_left_field.getColor()) {
            has_valid_starting_fields = true;
            addFieldToComponentOfPlayer1(lower_left_field);
            addFieldToComponentOfPlayer2(upper_right_field);
        }

        printComponent1();
        printComponent2();
    }

    private void addFieldToComponentOfPlayer1(Field field) {
        component_player_1.add(field);
    }

    private void addFieldToComponentOfPlayer2(Field field) {
        component_player_2.add(field);
    }

    private void printComponent1() {
        for (Field f : component_player_1) {
            int row = f.getRow();
            int col = f.getCol();
            int color = f.getColor();
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

    private boolean checkIfNeighborColorsAreValid(Field field) {
        List<Field> neighbors = getNeighbors(field);
        int color = field.getColor();

        for (Field neighbor : neighbors) {
            if (neighbor.getColor() == color) {
                return false;
            }
        }

        return true;
    }

    private List<Field> getNeighbors(Field field) {
        List<Field> neighbors = new ArrayList<>();
        int row = field.getRow();
        int col = field.getCol();

        // Überprüfen der Nachbarn oben, unten, links und rechts
        // Überprüfen des oberen Nachbarn
        if (row - 1 >= 0 && row - 1 < board.length && col >= 0 && col < board[row - 1].length) {
            neighbors.add(board[row - 1][col]);
        }

        // Überprüfen des unteren Nachbarn
        if (row + 1 >= 0 && row + 1 < board.length && col >= 0 && col < board[row + 1].length) {
            neighbors.add(board[row + 1][col]);
        }

        // Überprüfen des linken Nachbarn
        if (row >= 0 && row < board.length && col - 1 >= 0 && col - 1 < board[row].length) {
            neighbors.add(board[row][col - 1]);
        }

        // Überprüfen des rechten Nachbarn
        if (row >= 0 && row < board.length && col + 1 >= 0 && col + 1 < board[row].length) {
            neighbors.add(board[row][col + 1]);
        }

        return neighbors;
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

    public Color[] getColors_of_player_1() {
        return colors_of_player_1;
    }

    public void setColors_of_player_1(Color[] colors_of_player_1) {
        this.colors_of_player_1 = colors_of_player_1;
    }

    public Color[] getColors_of_player_2() {
        return colors_of_player_2;
    }

    public void setColors_of_player_2(Color[] colors_of_player_2) {
        this.colors_of_player_2 = colors_of_player_2;
    }

    public int getStarting_color_of_player_1() {
        return starting_color_of_player_1;
    }

    public void setStarting_color_of_player_1(int starting_color_of_player_1) {
        this.starting_color_of_player_1 = starting_color_of_player_1;
    }

    public int getStarting_color_of_player_2() {
        return starting_color_of_player_2;
    }

    public void setStarting_color_of_player_2(int starting_color_of_player_2) {
        this.starting_color_of_player_2 = starting_color_of_player_2;
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

}