package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
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
    private int legend_field_size;

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

    public void calculateFieldSize() {

        int width = getWidth();
        int height = getHeight();

        int minimum = Math.min(width, height);

        // tatsächlich verfügbare höhe abzüglich der ränder
        int available_height = height - 4 * 40;
        // tatsächlich verfügbare breite abzüglich der ränder
        int available_width = width - 2 * 40;

        fieldSize = minimum / Math.max(frame.getMenuetafel().getSelected_num_of_rows(),
                frame.getMenuetafel().getSelected_num_of_cols());

        // falls die größe des felds den zulässigen rahmen überschreitet wird sie
        // angepasst
        if (fieldSize > available_height / frame.getMenuetafel().getSelected_num_of_rows()
                || fieldSize > available_width / frame.getMenuetafel().getSelected_num_of_cols()) {

            fieldSize = Math.min(available_height / frame.getMenuetafel().getSelected_num_of_rows(),
                    available_width / frame.getMenuetafel().getSelected_num_of_cols());
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        calculateFieldSize();

        // Board und legende erst zeichnen, wenn auf Start-Button geklickt wurde
        if (frame.getMenuetafel().isStart_btn_is_clicked()) {

            setFocusable(true);
            requestFocusInWindow();

            /******** SPIELFELD *********/

            int board_width = fieldSize * frame.getMenuetafel().getSelected_num_of_cols();
            int board_height = fieldSize * frame.getMenuetafel().getSelected_num_of_rows();

            int offsetX = (getWidth() - board_width) / 2;
            int offsetY = ((getHeight() - board_height) / 2) - 50;

            // Wenn das Frame in den Vollbildmodus gebracht wird, soll repainted werden
            if ((frame.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
                repaint();
            }

            // Zeichnen der felder
            for (int i = 0; i < frame.getMenuetafel().getSelected_num_of_rows(); i++) {
                for (int j = 0; j < frame.getMenuetafel().getSelected_num_of_cols(); j++) {
                    int x = offsetX + j * fieldSize;
                    int y = offsetY + i * fieldSize;
                    Color fieldColor = board.getFieldColor(i, j);
                    g.setColor(fieldColor);
                    g.fillRect(x, y, fieldSize, fieldSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, fieldSize, fieldSize);
                    // setz für das jeweilige feld die x und y werte
                    board.getBoard()[i][j].setX_coordinate(x);
                    board.getBoard()[i][j].setY_coordinate(y);
                }
            }

            /******** LEGENDE *********/

            int abstand_zwischen_board_und_rand_unten = getHeight() - offsetY - board_height;

            // Berechnung der Position der Legende
            legend_field_size = Math.min(getWidth() / 15, abstand_zwischen_board_und_rand_unten / 2);

            int legend_start_x = offsetX;
            int legend_start_y = offsetY + board_height + 5 + (legend_field_size / 2);

            ArrayList<Color> selectedColors = board.getSelectedColors();

            // legende hat so viele elemente wie selected colors
            legende = new ArrayList<Field>(board.getSelectedColors().size());

            // Zeichnen der Legende
            for (int i = 0; i < selectedColors.size(); i++) {
                Color selectedColor = selectedColors.get(i);
                int color = selectedColors.indexOf(selectedColor);
                Field field = new Field(1, i, color);
                legende.add(field);

                // Berechnung der Position des Legenden-Elements
                int legendElementX = legend_start_x + i * (legend_field_size + 5);
                int legendElementY = legend_start_y;

                // Zeichnen des Legenden-Elements
                drawLegendElement(g, selectedColor, legendElementX, legendElementY, legend_field_size);

                field.setX_coordinate(legendElementX);
                field.setY_coordinate(legendElementY);

                // Zeichnen des Indexes unterhalb des Legenden-Elements
                g.setColor(Color.BLACK);
                g.drawString(Integer.toString(color + 1), legendElementX + legend_field_size / 2,
                        legendElementY + legend_field_size + 15);

            }
            // wenn das spiel vorbei ist soll das board wieder verschwinden
            if (board.isIs_game_over()) {
                super.paintComponent(g);
            }

            if (board.isP1_ist_dran() && frame.getMenuetafel().isPlay_btn_is_clicked()) {
                frame.getAnzeigetafel().getCurrent_player_anzeige_lbl().setText("Du bist dran");
            } else if (board.isP2_ist_dran() &&
                    frame.getMenuetafel().isPlay_btn_is_clicked()) {
                frame.getAnzeigetafel().getCurrent_player_anzeige_lbl().setText("Der Computer ist dran");
            }
            if (board.isIs_game_over()) {
                frame.getAnzeigetafel().getCurrent_player_anzeige_lbl().setText("");
            }
            if (board.getInput() == 0) {
                frame.getMenuetafel().resetComponentSizeLabels();
                frame.getAnzeigetafel().getCurrent_player_anzeige_lbl().setText("");
            }
        } else {
            // wenn auf Stop geklickt wird soll das Board wieder verschwinden
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
        if (frame.getMenuetafel().isPlay_btn_is_clicked()) {

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
                        // zug s1
                        if (board.isP1_ist_dran()) {
                            board.makeMoveS1(board.getComponent_player_1(), farbe_fuer_naechsten_zug);
                        }
                    }
                }
            }
            // Überprüfe, ob der Klick innerhalb eines Legendenfeldes liegt
            boolean colorSelected = false;
            for (Field f : legende) {
                if (!colorSelected && mouseX >= f.getX_coordinate() && mouseX < f.getX_coordinate() +
                        legend_field_size &&
                        mouseY >= f.getY_coordinate() && mouseY < f.getY_coordinate() +
                                legend_field_size) {
                    farbe_fuer_naechsten_zug = f.getColor();
                    // zug s1
                    if (board.isP1_ist_dran()) {
                        board.makeMoveS1(board.getComponent_player_1(), farbe_fuer_naechsten_zug);
                    }
                    colorSelected = true;
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
                    // zug s1
                    if (board.isP1_ist_dran()) {
                        board.makeMoveS1(board.getComponent_player_1(), farbe_fuer_naechsten_zug);
                    }
                }
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