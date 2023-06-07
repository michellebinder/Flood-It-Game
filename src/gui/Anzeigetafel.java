package gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JPanel;
import logic.Board;
import logic.Field;

public class Anzeigetafel extends JPanel implements MouseListener {

    private Board board;
    private boolean start_btn_is_clicked;
    private boolean play_btn_is_clicked;
    private int fieldSize;
    private int farbe_fuer_naechsten_zug;

    public Anzeigetafel(Frame f, Menuetafel m) {
        start_btn_is_clicked = false;
        play_btn_is_clicked = false;
        setBackground(Color.white);
        setFocusable(true);
        requestFocusInWindow();
        // addKeyListener(this);
        addMouseListener(this);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Board erst zeichnen, wenn auf Start-Button geklickt wurde
        if (start_btn_is_clicked) {
            setFocusable(true);
            requestFocusInWindow();
            int originX = 25;
            int originY = 25;
            int width = getWidth() - originX * 2;
            int height = getHeight() - originY * 2;

            // Berechnung der Größe eines Legendenfeldes
            int legend_field_size = width / 7;

            // Berechnung der Größe eines Feldes basierend auf der Anzahl der Spalten und
            // Zeilen
            fieldSize = Math.min(width / Menuetafel.selected_num_of_cols, height / Menuetafel.selected_num_of_rows);
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
                    // setz für das jeweilige feld die x und y werte
                    board.getBoard()[i][j].setX_coordinate(x);
                    board.getBoard()[i][j].setY_coordinate(y);
                    // System.out
                    // .println("feld (" + i + ", " + j + "): x Wert: " +
                    // board.getBoard()[i][j].getX_coordinate()
                    // + " y-Wert: " + board.getBoard()[i][j].getY_coordinate());
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

    // Getter & Setter

    public void setBoard(Board board) {
        this.board = board;
        repaint();
    }

    public Board getBoard() {
        return board;
    }

    public boolean isStart_btn_is_clicked() {
        return start_btn_is_clicked;
    }

    public void setStart_btn_is_clicked(boolean start_btn_is_clicked) {
        this.start_btn_is_clicked = start_btn_is_clicked;
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

    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX(); // X-Koordinate des Klicks
        int mouseY = e.getY(); // Y-Koordinate des Klicks

        // Überprüfe, ob der Klick innerhalb eines Feldes liegt
        for (int i = 0; i < Menuetafel.selected_num_of_rows; i++) {
            for (int j = 0; j < Menuetafel.selected_num_of_cols; j++) {
                Field field = board.getBoard()[i][j];
                if (mouseX >= field.getX_coordinate() && mouseX < field.getX_coordinate() + fieldSize &&
                        mouseY >= field.getY_coordinate() && mouseY < field.getY_coordinate() + fieldSize) {
                    // System.out.println("Klick auf Feld (" + i + ", " + j + ")");
                    farbe_fuer_naechsten_zug = field.getColor();
                    // System.out.println("die ausgewählte faarbe ist" + field.getColor());
                }
            }
        }

        // Überprüfe, ob der Klick innerhalb eines Legenden-Elements liegt
        List<Color> selectedColors = board.getSelectedColors();
        int legendOriginX = 25;
        int legendOriginY = 25 + Menuetafel.selected_num_of_rows * fieldSize + 20; // 20 Pixel Verschiebung nach unten
        int legend_field_size = getWidth() / 7;
        int legendX = legendOriginX + 5; // 5 Pixel Abstand vom Anfang der Anzeigetafel
        int legendY = legendOriginY;

        for (int i = 0; i < selectedColors.size(); i++) {
            Color selectedColor = selectedColors.get(i);
            int offsetX = i % 5; // Horizontaler Offset: Rest der Division durch 5
            int offsetY = i / 5; // Vertikaler Offset: Ganzzahlige Division durch 5

            // Überprüfen, ob der Klick innerhalb des aktuellen Legenden-Elements liegt
            if (mouseX >= legendX + offsetX * (legend_field_size + 5)
                    && mouseX < legendX + offsetX * (legend_field_size + 5) + legend_field_size &&
                    mouseY >= legendY + offsetY * (legend_field_size + 20)
                    && mouseY < legendY + offsetY * (legend_field_size + 20) + legend_field_size) {
                farbe_fuer_naechsten_zug = selectedColors.indexOf(selectedColor);
                // System.out.println("die ausgewählte farbe ist" + farbe_fuer_naechsten_zug);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
