package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import logic.Board;
import logic.Field;

public class Anzeigetafel extends JPanel implements MouseListener {

    private Board board;
    private boolean start_btn_is_clicked;
    private boolean play_btn_is_clicked;
    private int fieldSize;
    private int farbe_fuer_naechsten_zug;
    private Frame frame;
    private boolean farbe_wurde_ausgewaehlt;
    private ArrayList<Field> legende;
    private boolean is_color_change_enabled = true;

    public Anzeigetafel(Frame frame) {

        this.frame = frame;
        start_btn_is_clicked = frame.getMenuetafel().isPlay_btn_is_clicked();
        play_btn_is_clicked = frame.getMenuetafel().isPlay_btn_is_clicked();
        setBackground(Color.white);
        setFocusable(true);
        requestFocusInWindow();
        addMouseListener(this);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Board erst zeichnen, wenn auf Start-Button geklickt wurde
        if (start_btn_is_clicked) {

            setFocusable(true);
            requestFocusInWindow();

            /******** SPIELFELD *********/
            int originX = 25;
            int originY = 25;
            int width = getWidth() - originX * 2;
            int height = getHeight() - originY * 2;

            // berechne die größe eines feldes anhand der zeilen und spaltenanzahl
            fieldSize = Math.min(width / frame.getMenuetafel().getSelected_num_of_cols(),
                    height / frame.getMenuetafel().getSelected_num_of_rows());

            // Zeichnen der felder
            for (int i = 0; i < frame.getMenuetafel().getSelected_num_of_rows(); i++) {
                for (int j = 0; j < frame.getMenuetafel().getSelected_num_of_cols(); j++) {
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

            /******** LEGENDE *********/
            int legend_field_size = width / 7;

            ArrayList<Color> selectedColors = board.getSelectedColors();
            // legende besteht aus 2 zeilen -> in obere zeile sollen (max) 5 elemente
            int colorsInTopRow = Math.min(selectedColors.size(), 5);

            int legendX = originX + 5; // 5 Pixel Abstand nach links
            int legendY = originY + frame.getMenuetafel().getSelected_num_of_rows() * fieldSize + 40;

            // legende hat so viele elemente wie selected colors
            legende = new ArrayList<Field>(board.getSelectedColors().size());

            for (int i = 0; i < selectedColors.size(); i++) {
                Color selectedColor = selectedColors.get(i);
                int color = selectedColors.indexOf(selectedColor);
                Field field = new Field(1, i, color);
                legende.add(field);
                int currentRow = i / colorsInTopRow; // Zeile berechnen
                int offsetX = i % colorsInTopRow; // Spalte berechnen

                // x und y position berechnen
                int legendElementX = legendX + offsetX * (legend_field_size + 5);
                int legendElementY = legendY + currentRow * (legend_field_size + 20);

                // Zeichnen des Legenden-Elements
                drawLegendElement(g, selectedColor, legendElementX, legendElementY, legend_field_size);
                field.setX_coordinate(legendElementX);
                field.setY_coordinate(legendElementY);

                // Zeichnen des Indexes unterhalb des Legenden-Elements
                g.setColor(Color.BLACK);
                g.drawString(Integer.toString(color + 1), legendElementX + legend_field_size / 2,
                        legendElementY + legend_field_size + 15);
            }

        } else {
            // Wenn auf Stop geklickt wird, soll das Board wieder verschwinden
            super.paintComponent(g);
        }
    }

    public void drawLegendElement(Graphics g, Color color, int x, int y, int size) {
        g.setColor(color);
        g.fillRect(x, y, size, size);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, size, size);
    }

    // Mouse Behaviour

    @Override
    public void mouseClicked(MouseEvent e) {

        // erst auf maus klicks auf dem board reagieren wenn auf play gedrückt wurde
        if (play_btn_is_clicked && is_color_change_enabled) {
            int mouseX = e.getX(); // X-Koordinate des Klicks
            int mouseY = e.getY(); // Y-Koordinate des Klicks

            // Überprüfe, ob der Klick innerhalb eines Feldes liegt
            for (int i = 0; i < frame.getMenuetafel().getSelected_num_of_rows(); i++) {
                for (int j = 0; j < frame.getMenuetafel().getSelected_num_of_cols(); j++) {
                    Field field = board.getBoard()[i][j];
                    if (mouseX >= field.getX_coordinate() && mouseX < field.getX_coordinate() + fieldSize &&
                            mouseY >= field.getY_coordinate() && mouseY < field.getY_coordinate() + fieldSize) {

                        if (!farbe_wurde_ausgewaehlt) {
                            farbe_fuer_naechsten_zug = field.getColor();
                            System.out.println("Die ausgewählte Farbe ist " + field.getColor());
                            farbe_wurde_ausgewaehlt = true;
                        } else {
                            field.setColor(farbe_fuer_naechsten_zug);
                            repaint();
                            System.out.println("Feldfarbe geändert: " + field.getColor());
                            is_color_change_enabled = false;
                        }
                    }
                }
            }

            is_color_change_enabled = true;
            for (Field field : legende) {
                if (mouseX >= field.getX_coordinate() && mouseX < field.getX_coordinate() + fieldSize &&
                        mouseY >= field.getY_coordinate() && mouseY < field.getY_coordinate() + fieldSize) {

                    if (!farbe_wurde_ausgewaehlt) {
                        farbe_fuer_naechsten_zug = field.getColor();
                        System.out.println("Die ausgewählte Farbe ist " + field.getColor());
                        farbe_wurde_ausgewaehlt = true;
                    } else {
                        field.setColor(farbe_fuer_naechsten_zug);
                        repaint();
                        System.out.println("Feldfarbe geändert: " + field.getColor());
                        is_color_change_enabled = false;
                    }
                }
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

}
