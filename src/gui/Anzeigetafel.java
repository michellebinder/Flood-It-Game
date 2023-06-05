package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import logic.Board;

public class Anzeigetafel extends JPanel {
    private Board board;
    private boolean drawBoard;

    public Anzeigetafel(Frame f, Board board) {
        this.board = board;
        this.drawBoard = true;
        setBackground(Color.darkGray);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // if (drawBoard) {
        int originX = 25;
        int originY = 25;
        int width = getWidth() - originX * 2;
        int height = getHeight() - originY * 2;

        int fieldSize = Math.min(width / Menuetafel.selected_num_of_cols, height / Menuetafel.selected_num_of_rows);

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
        // }

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
}
