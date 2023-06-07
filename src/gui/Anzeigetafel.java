package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import logic.Board;

public class Anzeigetafel extends JPanel {
    private Board board;
    public static boolean drawBoard;

    public Anzeigetafel(Frame f, Menuetafel m) {
        Anzeigetafel.drawBoard = false;
        setBackground(Color.white);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (drawBoard) {
            int originX = 25;
            int originY = 25;
            int width = getWidth() - originX * 2;
            int height = getHeight() - originY * 2;

            int fieldSize = Math.min(width / Menuetafel.selected_num_of_cols, height / Menuetafel.selected_num_of_rows);

            int legend_field_size = width / 9;

            int legendOriginX = originX;
            int legendOriginY = originY + Menuetafel.selected_num_of_rows * fieldSize;

            int legendX = legendOriginX;
            int legendY = legendOriginY + fieldSize;

            List<Color> selectedColors = board.getSelectedColors();

            for (Color selectedColor : selectedColors) {
                g.setColor(selectedColor);
                g.fillRect(legendX, legendY, fieldSize, fieldSize);
                g.setColor(Color.BLACK);
                g.drawRect(legendX, legendY, fieldSize, fieldSize);
                g.setColor(Color.BLACK); // Schriftfarbe auf Schwarz setzen
                g.drawString(Integer.toString(selectedColors.indexOf(selectedColor) + 1), legendX + fieldSize / 2,
                        legendY + fieldSize / 2);
                legendX += fieldSize;
            }

            for (int i = 0; i < Menuetafel.selected_num_of_rows; i++) {
                for (int j = 0; j < Menuetafel.selected_num_of_cols; j++) {
                    int x = originX + j * fieldSize;
                    int y = originY + i * fieldSize;
                    Color fieldColor = board.getFieldColor(i, j);
                    g.setColor(fieldColor);
                    g.fillRect(x, y, fieldSize, fieldSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, fieldSize, fieldSize);
                }
            }

            // Wenn drawBoard = false ist (wenn stop geklickt wird), gemaltes Board wieder
            // entfernen
        } else {
            super.paintComponent(g);
        }
    }

    public boolean isDrawBoard() {
        return drawBoard;
    }

    public void setDrawBoard(boolean drawBoard) {
        this.drawBoard = drawBoard;
    }

    public void setBoard(Board board) {
        this.board = board;
        repaint();
    }

    public Board getBoard() {
        return board;
    }

}