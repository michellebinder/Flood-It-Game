package testing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * Siehe Hinweise auf dem Aufgabenblatt. 
 */

import logic.Field;

public class Testing {

	private Field[][] board;

	public Testing(Field[][] initBoard) {
		this.board = initBoard;

	}

	public boolean isStartklar() {

		Set<Integer> uniqueColors = new HashSet<>();
		int totalColors = 0; // Zählt die Gesamtanzahl der Farben im Spielbrett

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				Field field = board[i][j];
				int color = field.getColor();

				// Überprüfen, ob die Farbe bereits hinzugefügt wurde
				if (!uniqueColors.contains(color)) {
					uniqueColors.add(color);
					totalColors++;
				}

				// Anforderung 1: Für jedes Feld aus dem Spielbrett gilt, dass seine Nachbarn
				// eine andere Farbe haben als das Feld selbst.
				// Überprüfen, ob die Nachbarn eine andere Farbe haben
				if (!checkIfNeighborColorsAreValid(field)) {
					return false;
				}
			}
		}
		// Anforderung 2: Wenn es t viele Farben im Spiel gibt, gibt es auch t viele
		// Farben im Spielbrett.
		int numColors = uniqueColors.size();
		if (totalColors != numColors) {
			return false;
		}

		// Anforderung 3: Das Feld in der Ecke links unten hat nicht die gleiche Farbe
		// wie das Feld in der Ecke rechts oben.
		Field bottomLeftField = board[board.length - 1][0];
		Field topRightField = board[0][board[0].length - 1];
		if (bottomLeftField.getColor() == topRightField.getColor()) {
			return false;
		}

		return true;
	}

	public boolean isEndConfig() {

		return false;
	}

	public int testStrategy01() {

		return 0;
	}

	public int testStrategy02() {

		return 0;
	}

	public int testStrategy03() {

		return 0;
	}

	public boolean toBoard(Field[][] anotherBoard, int moves) {

		return false;
	}

	public int minMoves(int row, int col) {

		return 0;
	}

	public int minMovesFull() {

		return 0;
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

	/*
	 * Getter und Setter
	 */
	public Field[][] getBoard() {
		return board;
	}

	public void setBoard(Field[][] board) {
		this.board = board;
	}

}
