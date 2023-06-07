package gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.List;
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

        // Board erst zeichnen, wenn auf Start-Button geklickt wurde
        if (drawBoard) {
            int originX = 25;
            int originY = 25;
            int width = getWidth() - originX * 2;
            int height = getHeight() - originY * 2;

            // Berechnung der Größe eines Legendenfeldes
            int legend_field_size = width / 7;

            // Berechnung der Größe eines Feldes basierend auf der Anzahl der Spalten und
            // Zeilen
            int fieldSize = Math.min(width / Menuetafel.selected_num_of_cols, height / Menuetafel.selected_num_of_rows);
            fieldSize = Math.min(fieldSize, legend_field_size);

            int legendOriginX = originX;
            int legendOriginY = originY + Menuetafel.selected_num_of_rows * fieldSize + 20; // 20 Pixel Verschiebung
                                                                                            // nach unten

            int legendX = legendOriginX + 5; // 5 Pixel Abstand vom Anfang der Anzeigetafel
            int legendY = legendOriginY;

            List<Color> selectedColors = board.getSelectedColors();
            int colorsInTopRow = Math.min(selectedColors.size(), 5);

            // Zeichnen der Legenden-Elemente
            for (int i = 0; i < selectedColors.size(); i++) {
                Color selectedColor = selectedColors.get(i);

                int currentRow;
                int offsetX;
                int offsetY;

                // Bestimmen der aktuellen Zeile basierend auf der Position des Elements
                if (i < colorsInTopRow) {
                    currentRow = 0; // Element befindet sich in der oberen Zeile
                    offsetX = i;
                } else {
                    currentRow = 1; // Element befindet sich in der unteren Zeile
                    offsetX = i - colorsInTopRow;
                }

                // Berechnen der vertikalen Verschiebung basierend auf der aktuellen Zeile
                offsetY = currentRow * (legend_field_size + 20); // 20 Pixel Abstand zwischen den Zeilen

                // Zeichnen des Legenden-Elements
                drawLegendElement(g, selectedColor, legendX + offsetX * (legend_field_size + 5), legendY + offsetY,
                        legend_field_size);
            }

            // Zeichnen der Spielfeld-Felder
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
        } else {
            // Wenn auf Stop geklickt wird, soll das Board wieder verschwinden
            super.paintComponent(g);
        }
    }

    // Zeichnen eines Legenden-Elements
    private void drawLegendElement(Graphics g, Color color, int x, int y, int size) {
        g.setColor(color);
        g.fillRect(x, y, size, size);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, size, size);
        g.setColor(Color.BLACK);
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth("1");
        int textHeight = fm.getHeight();
        int textX = x + (size - textWidth) / 2;
        int textY = y + size + textHeight; // Anpassung der y-Koordinate für die Positionierung unter der Farbe
        g.drawString(Integer.toString(board.getSelectedColors().indexOf(color) + 1), textX, textY);
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
