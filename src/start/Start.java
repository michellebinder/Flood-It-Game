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
		/************** TESTFÄLLE **************/

		/* TEST BOARD 1 */

		/* TEST BOARD 2 */

		Testing test2 = new Testing(board2);

		// System.out.println("ist startklar: " + test2.isStartklar());

		// System.out.println("ist endkonfig: " + test2.isEndConfig());

		// System.out.println("strat 1: " + test2.testStrategy01());

		// System.out.println("strat 2: " + test2.testStrategy02());

		// System.out.println("strat 3: " + test2.testStrategy03());

	}

}
