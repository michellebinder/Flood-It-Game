package testing;
/*
 * Siehe Hinweise auf dem Aufgabenblatt. 
 */

import logic.Field;

public class Testing {

	private Field[][] board;
	// in testing wird immer mit 6 farben getestet

	public Testing(Field[][] initBoard) {
		this.board = initBoard;

	}

	public boolean isStartklar() {

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
