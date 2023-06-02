package logic;

import java.awt.*;
import java.util.*;

import gui.Menuetafel;

public class Board {

    private int rows;
    private int cols;
    private Field[][] board;
    Color[] colors = new Color[9];

    public Board(int rows, int cols) {

        rows = Menuetafel.selected_num_of_rows;
        cols = Menuetafel.selected_num_of_cols;

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

        Random random = new Random();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int random_color_index = random.nextInt(colors.length);
                while (checkNeighbourForSameColors(i, j, random_color_index)) {
                    random_color_index = random.nextInt(colors.length);
                }
                board[i][j] = new Field(i, j, random_color_index);
            }
        }
    }

    private boolean checkNeighbourForSameColors(int row, int col, int colorIndex) {

        if (row > 0 && board[row - 1][col].getColor() == colorIndex) { // Über dem aktuellen Feld
            return true;
        }
        if (row < rows - 1 && board[row + 1][col].getColor() == colorIndex) { // Unter dem aktuellen Feld
            return true;
        }
        if (col > 0 && board[row][col - 1].getColor() == colorIndex) { // Links neben dem aktuellen Feld
            return true;
        }
        if (col < cols - 1 && board[row][col + 1].getColor() == colorIndex) { // Rechts neben dem aktuellen Feld
            return true;
        }

        return false;
    }
}
