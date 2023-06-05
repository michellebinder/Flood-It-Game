package logic;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import gui.Menuetafel;
import testing.Testing;

public class Board {

    public static int rows;
    public static int cols;
    private Field[][] board;
    Color[] colors = new Color[9];
    List<Field> component_player_1;
    List<Field> component_player_2;

    // stores the colors of each player
    public Color[] colors_of_player_1;
    public Color[] colors_of_player_2;

    public int starting_color_of_player_1;
    public int starting_color_of_player_2;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        board = new Field[rows][cols];

        colors[0] = Color.LIGHT_GRAY;
        colors[1] = Color.BLACK;
        colors[2] = Color.ORANGE;
        colors[3] = Color.BLUE;
        colors[4] = Color.CYAN;
        colors[5] = Color.GREEN;
        colors[6] = Color.MAGENTA;
        colors[7] = Color.PINK;
        colors[8] = Color.YELLOW;

        generiereStartklaresBoard();

        // stores the number of the players starting colors
        starting_color_of_player_1 = board[rows - 1][0].getColor();
        starting_color_of_player_2 = board[0][cols - 1].getColor();

        // System.out.println("color index s1: " + starting_color_of_player_1);
        // System.out.println("color index s2: " + starting_color_of_player_2);
    }

    private void generiereStartklaresBoard() {
        Random random = new Random();

        do {
            // erstelle liste die so groß ist, wie die ausgewählten farben auf dem
            // spielbrett
            List<Color> selectedColors = new ArrayList<>();

            while (selectedColors.size() < Menuetafel.selected_num_of_colors) {
                int random_color_index = random.nextInt(colors.length);
                Color randomColor = colors[random_color_index];

                // füge zufällig aus der colors liste farben hinzu
                if (!selectedColors.contains(randomColor)) {
                    selectedColors.add(randomColor);
                }
            }

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    // wähle zufällig aus der liste die benötigte anzahl von farben aus
                    int random_color_index = random.nextInt(selectedColors.size());
                    Color randomColor = selectedColors.get(random_color_index);
                    int colorIndex = Arrays.asList(colors).indexOf(randomColor);
                    board[i][j] = new Field(i, j, colorIndex);
                }
            }
        } while (!isStartklar());
    }

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

    public boolean isStartklar() {
        Testing testing = new Testing(board);
        return testing.isStartklar();
    }
}
