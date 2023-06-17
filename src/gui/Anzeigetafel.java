package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import logic.Board;
import logic.Field;

public class Anzeigetafel extends JPanel implements MouseListener, KeyListener {

    private Board board;
    private int fieldSize;
    private int farbe_fuer_naechsten_zug;
    private Frame frame;
    private ArrayList<Field> legende;
    private boolean is_color_change_enabled = true;
    private JLabel current_player_anzeige_lbl;

    public Anzeigetafel(Frame frame) {
        this.frame = frame;
        setBackground(Color.white);

        setFocusable(true);
        requestFocusInWindow();

        addMouseListener(this);
        addKeyListener(this);
        current_player_anzeige_lbl = new JLabel();
        add(current_player_anzeige_lbl);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Board und legende erst zeichnen, wenn auf Start-Button geklickt wurde
        if (frame.getMenuetafel().isStart_btn_is_clicked()) {

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

            int max_field_size = 70;
            // if (fieldSize > max_field_size) {
            // fieldSize = max_field_size;
            // }

            // System.out.println("feldgröße: " + fieldSize);
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

            // TODO evtl noch play pause button checken?
            if (board.isP1_ist_dran() && frame.getMenuetafel().isStart_btn_is_clicked()) {
                current_player_anzeige_lbl.setText("Du bist dran");
                repaint();
            } else if (board.isP2_ist_dran() && frame.getMenuetafel().isStart_btn_is_clicked()) {
                current_player_anzeige_lbl.setText("Der Computer ist dran");
                repaint();
            }

            /******** LEGENDE *********/
            int legend_field_size = width / 7;
            // if (legend_field_size > max_field_size) {
            // legend_field_size = max_field_size;
            // }

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

        } else

        {
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

    /******** MOUSE BEHAVIOUR *********/
    @Override
    public void mouseClicked(MouseEvent e) {

        // erst auf maus klicks auf dem board reagieren wenn auf play gedrückt wurde
        if (frame.getMenuetafel().isPlay_btn_is_clicked() && is_color_change_enabled) {
            requestFocusInWindow();
            int mouseX = e.getX(); // X-Koordinate des Klicks
            int mouseY = e.getY(); // Y-Koordinate des Klicks

            // Überprüfe, ob der Klick innerhalb eines Feldes liegt
            for (int i = 0; i < frame.getMenuetafel().getSelected_num_of_rows(); i++) {
                for (int j = 0; j < frame.getMenuetafel().getSelected_num_of_cols(); j++) {
                    Field field = board.getBoard()[i][j];
                    if (mouseX >= field.getX_coordinate() && mouseX < field.getX_coordinate() + fieldSize &&
                            mouseY >= field.getY_coordinate() && mouseY < field.getY_coordinate() + fieldSize) {

                        farbe_fuer_naechsten_zug = field.getColor();
                        // System.out.println("Die ausgewählte Farbe ist " + farbe_fuer_naechsten_zug);
                        // TODO: while spiel noch nicht beendet
                        // jetzt soll die komponente evtl erweitert werden
                        if (board.isP1_ist_dran()) {
                            board.makeMoveS1(board.getComponent_player_1(), farbe_fuer_naechsten_zug);
                        }
                        // TODO:
                        if (farbe_fuer_naechsten_zug == board.getColor_of_player_1()
                                || farbe_fuer_naechsten_zug == board.getColor_of_player_2()) {

                        }
                    }
                }
            }
            // Überprüfe, ob der Klick innerhalb eines Legendenfeldes liegt
            for (Field f : legende) {
                if (mouseX >= f.getX_coordinate() && mouseX < f.getX_coordinate() +
                        fieldSize &&
                        mouseY >= f.getY_coordinate() && mouseY < f.getY_coordinate() +
                                fieldSize) {

                    farbe_fuer_naechsten_zug = f.getColor();
                    // System.out.println("Die ausgewählte Farbe über legende ist " +
                    // farbe_fuer_naechsten_zug);
                    // TODO: while spiel noch nicht beendet
                    // jetzt soll die komponente evtl erweitert werden
                    if (board.isP1_ist_dran()) {
                        board.makeMoveS1(board.getComponent_player_1(), farbe_fuer_naechsten_zug);
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

    /******** KEY BEHAVIOUR *********/

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
        // erfasse die key events nur dann, wenn das spiel gestartet und play gedrückt
        // wurde
        requestFocusInWindow();

        if (frame.getMenuetafel().isStart_btn_is_clicked() &&
                frame.getMenuetafel().isPlay_btn_is_clicked()) {
            int key = e.getKeyChar() - '0'; // Konvertierung von char zu int
            if (key >= 1 && key <= 9) {
                List<Color> selectedColors = board.getSelectedColors();
                if (key <= selectedColors.size()) {
                    farbe_fuer_naechsten_zug = key - 1;
                    // System.out.println("Die ausgewählte Farbe über tastatur ist: " +
                    // farbe_fuer_naechsten_zug);
                    // TODO: while spiel noch nicht beendet
                    // jetzt soll die komponente evtl erweitert werden
                    if (board.isP1_ist_dran()) {
                        board.makeMoveS1(board.getComponent_player_1(), farbe_fuer_naechsten_zug);
                    }
                }
                // } else if (testchar == 's') {
                // // board.makeMoveS1Player2(board.getComponent_player_2(),
                // // board.Stagnation());
                // board.Stagnation();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    // Getter & Setter

    public void setBoard(Board board) {
        this.board = board;
        repaint();
    }

    public Board getBoard() {
        return board;
    }

    public int getFarbe_fuer_naechsten_zug() {
        return farbe_fuer_naechsten_zug;
    }

    public void setFarbe_fuer_naechsten_zug(int farbe_fuer_naechsten_zug) {
        this.farbe_fuer_naechsten_zug = farbe_fuer_naechsten_zug;
    }

    public int getFieldSize() {
        return fieldSize;
    }

    public void setFieldSize(int fieldSize) {
        this.fieldSize = fieldSize;
    }

    public ArrayList<Field> getLegende() {
        return legende;
    }

    public void setLegende(ArrayList<Field> legende) {
        this.legende = legende;
    }

    public boolean isIs_color_change_enabled() {
        return is_color_change_enabled;
    }

    public void setIs_color_change_enabled(boolean is_color_change_enabled) {
        this.is_color_change_enabled = is_color_change_enabled;
    }

    public JLabel getCurrent_player_anzeige_lbl() {
        return current_player_anzeige_lbl;
    }

    public void setCurrent_player_anzeige_lbl(JLabel current_player_anzeige_lbl) {
        this.current_player_anzeige_lbl = current_player_anzeige_lbl;
    }

}
