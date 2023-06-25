package start;

import gui.Frame;
import logic.Field;
import testing.Testing;

/*
 * Siehe Hinweise auf dem Aufgabenblatt.  
 */

public class Start {

	public static void main(String[] args) {

		new Frame();

		Field[][] board2 = new Field[3][3];
		board2[0][0] = new Field(0, 0, 5);
		board2[0][1] = new Field(0, 1, 4);
		board2[0][2] = new Field(0, 2, 2);

		board2[1][0] = new Field(1, 0, 5);
		board2[1][1] = new Field(1, 1, 3);
		board2[1][2] = new Field(1, 2, 1);

		board2[2][0] = new Field(2, 0, 3);
		board2[2][1] = new Field(2, 1, 4);
		board2[2][2] = new Field(2, 2, 6);

		// System.out.println("s1 braucht folgende anzahl an zügen: " +
		// minMovesFullTest.minMovesFullHelper(1));
		// System.out.println("s1 braucht folgende anzahl an zügen: " +
		// minMovesFullTest.minMovesFullHelper(2));
		// System.out.println("s1 braucht folgende anzahl an zügen: " +
		// minMovesFullTest.minMovesFullHelper(3));
		// System.out.println("s1 braucht folgende anzahl an zügen: " +
		// minMovesFullTest.minMovesFullHelper(4));
		// System.out.println("s1 braucht folgende anzahl an zügen: " +
		// minMovesFullTest.minMovesFullHelper(5));
		// System.out.println("s1 braucht folgende anzahl an zügen: " +
		// minMovesFullTest.minMovesFullHelper(6));
		// System.out.println("s1 braucht folgende anzahl an zügen: " +
		// minMovesFullTest.minMovesFull());

		/************** TESTFÄLLE **************/

		/* TEST BOARD 1 */

		/* TEST BOARD 2 */

		// System.out.println("ist startklar: " + test2.isStartklar());

		// System.out.println("ist endkonfig: " + test2.isEndConfig());

		// System.out.println("strat 1: " + test2.testStrategy01());

		// System.out.println("strat 2: " + test2.testStrategy02());

		// System.out.println("strat 3: " + test2.testStrategy03());

		Field[][] board = new Field[3][3];
		board[0][0] = new Field(0, 0, 2);
		board[0][1] = new Field(0, 1, 1);
		board[0][2] = new Field(0, 2, 2);

		board[1][0] = new Field(1, 0, 3);
		board[1][1] = new Field(1, 1, 4);
		board[1][2] = new Field(1, 2, 5);

		board[2][0] = new Field(2, 0, 1);
		board[2][1] = new Field(2, 1, 3);
		board[2][2] = new Field(2, 2, 6);

		Field[][] anotherboard = new Field[3][3];
		anotherboard[0][0] = new Field(0, 0, 2);
		anotherboard[0][1] = new Field(0, 1, 1);
		anotherboard[0][2] = new Field(0, 2, 2);

		anotherboard[1][0] = new Field(1, 0, 4);
		anotherboard[1][1] = new Field(1, 1, 4);
		anotherboard[1][2] = new Field(1, 2, 5);

		anotherboard[2][0] = new Field(2, 0, 4);
		anotherboard[2][1] = new Field(2, 1, 4);
		anotherboard[2][2] = new Field(2, 2, 6);

		Testing t = new Testing(board);
		System.out.println("neuer run in der main----------------------");
		t.step(anotherboard, 0);

		// t.printComponent(t.findComponentS1(anotherboard));
	}

}
