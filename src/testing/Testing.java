// Michelle Binder
// 7345155
package testing;
/*
 * Siehe Hinweise auf dem Aufgabenblatt. 
 */

import logic.Field;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Testing {

	private Field[][] board;

	private int rows;
	private int cols;

	public Testing(Field[][] initBoard) {

		// vorgegeben
		this.board = initBoard;

		rows = board.length;
		cols = board[0].length;
	}

	// Hilfsmethode die eine Kopie von dem übergebenen Board erstellt, damit wir
	// nicht auf dem "Original" arbeiten müssen
	private Field[][] copyBoard() {

		int rows = board.length;
		int cols = board[0].length;

		Field[][] copied_board = new Field[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Field field = board[i][j];
				Field copied_field = new Field(field.getRow(), field.getCol(), field.getColor());
				copied_board[i][j] = copied_field;
			}
		}
		return copied_board;
	}

	public boolean isStartklar() {

		// kopie vom board erstellen
		Field[][] currentBoard = copyBoard();

		// zunächst alle bedingungen auf false setzen
		boolean has_valid_neighbours = false;
		boolean has_valid_num_of_unique_colors = false;
		boolean has_valid_starting_fields = false;

		// Anforderung 2: Wenn es t viele Farben im Spiel gibt, gibt es auch t viele
		// Farben im Spielbrett.
		Set<Integer> uniqueColors = new HashSet<>();

		// Anforderung 3: Das Feld in der Ecke links unten hat nicht die gleiche Farbe
		// wie das Feld in der Ecke rechts oben.
		Field upper_right_field = currentBoard[0][currentBoard[0].length - 1];
		Field lower_left_field = currentBoard[currentBoard.length - 1][0];

		// wenn das feld oben rechts und unten links ne andere farbe haben
		if (upper_right_field.getColor() != lower_left_field.getColor()) {
			has_valid_starting_fields = true;
		}

		has_valid_neighbours = true;

		// Anforderung 1: Für jedes Feld aus dem Spielbrett gilt, dass seine Nachbarn
		// eine andere Farbe haben als das Feld selbst.
		for (int i = 0; i < currentBoard.length; i++) {
			for (int j = 0; j < currentBoard[i].length; j++) {
				Field field = currentBoard[i][j];
				ArrayList<Field> neighbours = getNeighbors(field, currentBoard);

				for (Field neighbour : neighbours) {
					if (neighbour.getColor() == field.getColor()) {
						has_valid_neighbours = false;
						break;
					}
				}

				// Anforderung 2: checkt, wie viele farben tatsächlich auf dem feld sind
				if (!uniqueColors.contains(currentBoard[i][j].getColor())) {
					uniqueColors.add(currentBoard[i][j].getColor());
				}
			}

			// wenn bereits ein nachbar gefunden wurde, der einen ungültigen nachbarn hat,
			// brich die for schleife ab
			if (!has_valid_neighbours) {
				break;
			}
		}

		// wenn die tatsächlichen farben auf dem brett 6 entspricht
		if (uniqueColors.size() == 6) {
			has_valid_num_of_unique_colors = true;
		}

		if (has_valid_neighbours && has_valid_num_of_unique_colors && has_valid_starting_fields) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isEndConfig() {

		// kopie vom board erstellen
		Field[][] currentBoard = copyBoard();

		// hole dir die komponenten von beiden spielern über hilfsmethode
		ArrayList<Field> component_s1 = findComponentS1(currentBoard);
		ArrayList<Field> component_s2 = findComponentS2(currentBoard);

		// prüfen ob es sich um eine endkonfiguration handelt
		for (int i = 0; i < currentBoard.length; i++) {
			for (int j = 0; j < currentBoard[i].length; j++) {
				Field field = currentBoard[i][j];
				if (!component_s1.contains(field) && !component_s2.contains(field)) {
					return false; // Wenn ein Feld weder zu s1 noch zu s2 gehört, ist es keine Endkonfiguration
				}
			}
		}
		return true; // Alle Felder gehören entweder zu s1 oder s2
	}

	// STAGNATION
	public int testStrategy01() {

		Field[][] currentboard = copyBoard();
		ArrayList<Field> component_s1 = findComponentS1(currentboard);
		ArrayList<Field> component_s2 = findComponentS2(currentboard);

		int color_s1 = component_s1.get(0).getColor();
		int color_s2 = component_s2.get(0).getColor();

		// finde raus, wie oft jede farbe vorkommt (gespeichert in
		// num_of_color_occurence array)
		int[] num_of_color_occurence = getNumOfOccurenceOfColorsS2(component_s1, component_s2, currentboard);

		// -1 weil die bei testing bei 1 anfangen statt bei 0
		num_of_color_occurence[color_s1 - 1] = 2000;
		num_of_color_occurence[color_s2 - 1] = 2000;

		// nun tatsächlich für die kleinste farbe entscheiden
		// setze die zahl der gewählten farbe extra so hoch, dass sie auf jeden fall von
		// einer tatsächlichen farbe abgelöst wird
		int chosen_color_index = 20;
		int min = 20;
		for (int i = 0; i < num_of_color_occurence.length; i++) {
			//
			if (num_of_color_occurence[i] < min) {
				chosen_color_index = i;
				min = num_of_color_occurence[i];
				// wenn es eine zahl gibt die genau so selten vorkommt wie das derzeitige
				// minimum
			} else if (num_of_color_occurence[i] == min) {
				// dann schau ob das eine kleinere zahl ist
				if (i < chosen_color_index) {
					chosen_color_index = i;
					min = num_of_color_occurence[i];
				}
			}
		}

		int chosen_color = chosen_color_index + 1;
		// weil index bei 0 anfängt aber die zum testen farben von 1-6 nutzen
		return chosen_color;
	}

	// GREEDY
	public int testStrategy02() {

		Field[][] currentboard = copyBoard();
		ArrayList<Field> component_s1 = findComponentS1(currentboard);
		ArrayList<Field> component_s2 = findComponentS2(currentboard);

		int color_s1 = component_s1.get(0).getColor();
		int color_s2 = component_s2.get(0).getColor();

		// finde raus, wie oft jede farbe vorkommt (gespeichert in
		// num_of_color_occurence array)
		int[] num_of_color_occurence = getNumOfOccurenceOfColorsS2(component_s1, component_s2, currentboard);

		// -1 weil die bei testing bei 1 anfangen statt bei 0
		num_of_color_occurence[color_s1 - 1] = -2000;
		num_of_color_occurence[color_s2 - 1] = -2000;

		// nun tatsächlich für die größte farbe entscheiden
		// setze die zahl der gewählten farbe extra so niedrig, dass sie auf jeden fall
		// von
		// einer tatsächlichen farbe abgelöst wird
		int chosen_color_index = -20;
		int max = -20;
		for (int i = 0; i < num_of_color_occurence.length; i++) {
			//
			if (num_of_color_occurence[i] > max) {
				chosen_color_index = i;
				max = num_of_color_occurence[i];
				// wenn es eine zahl gibt die genau so selten vorkommt wie das derzeitige
				// minimum
			} else if (num_of_color_occurence[i] == max) {
				// dann schau ob das eine kleinere zahl ist
				if (i < chosen_color_index) {
					chosen_color_index = i;
					max = num_of_color_occurence[i];
				}
			}
		}
		int chosen_color = chosen_color_index + 1;

		// weil index bei 0 anfängt aber die zum testen farben von 1-6 nutzen
		return chosen_color;
	}

	// BLOCKING
	public int testStrategy03() {

		Field[][] currentboard = copyBoard();
		ArrayList<Field> component_s1 = findComponentS1(currentboard);
		ArrayList<Field> component_s2 = findComponentS2(currentboard);

		int color_s1 = component_s1.get(0).getColor();
		int color_s2 = component_s2.get(0).getColor();

		// finde raus, wie oft jede farbe vorkommt (gespeichert in
		// num_of_color_occurence array)
		int[] num_of_color_occurence = getNumOfOccurenceOfColorsS1(component_s1, component_s2, currentboard);

		// -1 weil die bei testing bei 1 anfangen statt bei 0
		num_of_color_occurence[color_s1 - 1] = -2000;
		num_of_color_occurence[color_s2 - 1] = -2000;

		// nun tatsächlich für die größte farbe entscheiden
		// setze die zahl der gewählten farbe extra so niedrig, dass sie auf jeden fall
		// von
		// einer tatsächlichen farbe abgelöst wird
		int chosen_color_index = -20;
		int max = -20;
		for (int i = 0; i < num_of_color_occurence.length; i++) {
			//
			if (num_of_color_occurence[i] > max) {
				chosen_color_index = i;
				max = num_of_color_occurence[i];
				// wenn es eine zahl gibt die genau so selten vorkommt wie das derzeitige
				// minimum
			} else if (num_of_color_occurence[i] == max) {
				// dann schau ob das eine kleinere zahl ist
				if (i < chosen_color_index) {
					chosen_color_index = i;
					max = num_of_color_occurence[i];
				}
			}
		}
		int chosen_color = chosen_color_index + 1;
		// weil index bei 0 anfängt aber die zum testen farben von 1-6 nutzen
		return chosen_color;
	}

	public boolean toBoard(Field[][] anotherBoard, int moves) {

		// kopie von dem board erstellen, auf dem wir arbeiten können
		Field[][] current_board = copyBoard();

		ArrayList<Field> component_s1 = findComponentS1(current_board);
		ArrayList<Field> component_s2 = findComponentS2(current_board);

		ArrayList<Field> component_s1_another = findComponentS1(anotherBoard);
		ArrayList<Field> component_s2_another = findComponentS2(anotherBoard);

		// Fall 1: board und anotherboard sind gleich -> wir sind fertig
		if (areBoardsEqual(current_board, anotherBoard)) {

			return true;
		}

		// Fall 2: board und anotherboard haben eine unterschiedliche anzahl zeilen und
		// spalten -> wir sind fertig
		if (current_board.length != anotherBoard.length || current_board[0].length != anotherBoard[0].length) {

			return false;
		}

		// Fall 3: die komponenten von s1 und s2 in board müssen <= der komponenten in
		// anotherboard sein
		if (!compareComponents(component_s1, component_s1_another)
				|| !compareComponents(component_s2, component_s2_another)) {

			return false;
		}

		// wenn keiner der vorherigen fälle eintritt -> board ist valide und wir können
		// weitermachen
		ArrayList<ArrayList<Integer>> permutations = createPermutations(moves);
		boolean boards_are_equal = false;

		for (List<Integer> permutation : permutations) {

			if (runCurrentPermutation(permutation, anotherBoard)) {
				boards_are_equal = true;
				break;
			}
		}
		return boards_are_equal;
	}

	public boolean runCurrentPermutation(List<Integer> permutation, Field[][] another_board) {

		Field[][] current_board = copyBoard();
		ArrayList<Field> component_s1 = findComponentS1(current_board);
		ArrayList<Field> component_s2 = findComponentS2(current_board);

		// erster zug: startfeld s1
		int start_color_s1 = current_board[0][current_board[0].length - 1].getColor();
		// zweiter zug: startfeld s2
		int start_color_s2 = current_board[current_board.length - 1][0].getColor();

		boolean boards_are_equal = false;
		int count = 0;

		// nur wenn das erste feld die startfarbe von s1 hat und das zweite die
		// startfarbe von s2
		if (permutation.size() <= 1
				|| !(permutation.get(0) == start_color_s1 || permutation.get(0) == start_color_s2)
						&& !(permutation.get(1) == start_color_s2) && !boards_are_equal) {

			for (int i : permutation) {
				// s1 ist dran
				if (count % 2 == 0) {

					// gehe alle felder durch, die in der komponente enthalten sind
					for (int j = 0; j < component_s1.size(); j++) {
						Field field = component_s1.get(j);
						field.setColor(i);

						// schau dir die nachbarn von dem aktuellen feld an
						List<Field> neighbours = getNeighbors(field, current_board);
						for (Field neighbour : neighbours) {

							// füge den nachbarn zur komponente hinzu, falls
							// - er nicht schon in der komponente drin ist
							// - er die ausgewählte farbe hat
							if (!component_s1.contains(current_board[neighbour.getRow()][neighbour.getCol()])
									&& current_board[neighbour.getRow()][neighbour.getCol()].getColor() == i) {
								neighbour.setColor(i);
								component_s1.add(current_board[neighbour.getRow()][neighbour.getCol()]);
								changeColorOfWholeComponent(component_s1, i);
							}
						}
					}
					count++;
					// überprüfen, ob die boards jetzt gleich sind
					if (areBoardsEqual(current_board, another_board)) {
						// wenn ja aufhören, sonst weitermachen
						boards_are_equal = true;
						// return true;
						break;
					}
				}
				// s2 ist dran
				else {
					// Gehe alle Felder durch, die in der Komponente enthalten sind
					for (int j = 0; j < component_s2.size(); j++) {
						Field field = component_s2.get(j);
						field.setColor(i);

						// Schau dir die Nachbarn des aktuellen Feldes an
						List<Field> neighbours = getNeighbors(field, current_board);
						for (Field neighbour : neighbours) {
							// Füge den Nachbarn zur Komponente hinzu, falls
							// - er nicht schon in der Komponente enthalten ist
							// - er die ausgewählte Farbe hat
							if (!component_s2.contains(current_board[neighbour.getRow()][neighbour.getCol()])
									&& current_board[neighbour.getRow()][neighbour.getCol()].getColor() == i) {
								neighbour.setColor(i);
								component_s2.add(current_board[neighbour.getRow()][neighbour.getCol()]);
							} else if (component_s1.get(0).getColor() != i) {
								changeColorOfWholeComponent(component_s2, i);
							}
						}
					}

					count++;

					if (areBoardsEqual(current_board, another_board)) {
						// wenn ja aufhören, sonst weitermachen
						boards_are_equal = true;
						return true;
					}
				}
			}
		}
		return false;
	}

	public void changeColorOfWholeComponent(ArrayList<Field> component, int selected_color) {

		for (Field f : component) {
			f.setColor(selected_color);
		}
	}

	// generiert alle permutationen, die als moves-abfolge von s1 und s2
	// potenziell genutzt werden können
	public static ArrayList<ArrayList<Integer>> createPermutations(int moves) {
		ArrayList<ArrayList<Integer>> result = new ArrayList<>();
		createPermutationsHelper(moves, new ArrayList<>(), -1, -1, result);
		return result;
	}

	// checkt, ob alle bedingungen bei einer permutation erfüllt sind
	private static void createPermutationsHelper(int remaining_moves,
			ArrayList<Integer> current_move,
			int last_move_s1, int last_move_s2, ArrayList<ArrayList<Integer>> result) {

		// wenn die anzahl züge abgearbeitet wurde
		if (remaining_moves == 0) {
			result.add(new ArrayList<>(current_move));
			return;
		}

		// gehe die farben von 1-6 durch
		for (int i = 1; i <= 6; i++) {
			// current_move ist zu beginn 0, d.h. s1 ist dran wenn es eine gerade zahl ist
			if (current_move.size() % 2 == 0) {
				// wenn die farbe valide ist
				// - nicht die letzte farbe von s1
				// - nicht die letzte farbe von s2
				// füge sie hinzu
				if (i != last_move_s1 && i != last_move_s2) {
					current_move.add(i);
					createPermutationsHelper(remaining_moves - 1, current_move, i, last_move_s2,
							result);
					current_move.remove(current_move.size() - 1);
				}
				// s2 ist dran wenn es eine ungerade zahl ist
			} else {
				// wenn die farbe valide ist
				// - nicht die letzte farbe von s1
				// - nicht die letzte farbe von s2
				// füge sie hinzu
				if (i != last_move_s1 && i != last_move_s2) {
					current_move.add(i);
					createPermutationsHelper(remaining_moves - 1, current_move, last_move_s1, i,
							result);
					current_move.remove(current_move.size() - 1);
				}
			}
		}
	}

	public int minMoves(int row, int col) {

		// zunächst so hoch setzen, das es auf jeden fall von den anzahl zügen abgelöst
		// wird
		int min_moves = Integer.MAX_VALUE;
		int num_of_moves = 0;
		int[] num_of_moves_per_startcolor = new int[6];

		// für den test ob das feld bereits eingenommen ist
		Field[][] board = copyBoard();
		ArrayList<Field> comp_s1 = findComponentS1(board);

		// Fall 1: das gesuchte feld ist bereits in der komponente enthalten
		if (comp_s1.contains(board[row][col])) {
			return 0;
		}

		// Fall 2: das gesuchte feld ist nicht in der komponente enthalten -> finde die
		// minimale anzahl an zügen die man zum einnehmen dieses feldes braucht
		else {

			// fange einmal mit jeder startfarbe an
			for (int i = 0; i < num_of_moves_per_startcolor.length; i++) {

				// kopie vom ursprünglichen board
				Field[][] current_board = copyBoard();

				// komponente von s1 im aktuellen board
				ArrayList<Field> component_s1 = findComponentS1(current_board);
				int current_color = i + 1;

				// solange die komponente das gesuchte feld noch nicht enthält
				while (!component_s1.contains(current_board[row][col])) {

					// mach einen move
					component_s1 = makeMoveS1(component_s1, current_color, current_board);

					num_of_moves++;

					// mach die moves zyklisch von 1-6
					if (current_color < 6) {
						current_color++;
					} else {
						current_color = 1;
					}
				}
				num_of_moves_per_startcolor[i] = num_of_moves;
				num_of_moves = 0;
			}

			// finde das minimum aus allen startfarben
			for (int i = 0; i < num_of_moves_per_startcolor.length; i++) {
				int current_min = num_of_moves_per_startcolor[i];
				if (current_min < min_moves) {
					min_moves = current_min;
				}
			}
		}
		return min_moves;
	}

	public int minMovesFull() {

		// zunächst so hoch setzen, das es auf jeden fall von der anzahl an zügen
		// abgelöst wird
		int min_moves = Integer.MAX_VALUE;
		int num_of_moves = 0;
		int[] num_of_moves_per_startcolor = new int[6];

		// für den test ob das board bereits einfarbig gefärbt ist
		Field[][] board = copyBoard();
		ArrayList<Field> comp_s1 = findComponentS1(board);
		int rws = board.length;
		int cls = board[0].length;

		// Fall 1: das spielbrett ist bereits einfarbig gefärbt -> gib 0 zurück
		if (comp_s1.size() == rws * cls) {
			return 0;
		}

		// Fall 2: das spielbrett ist nicht einfarbig gefärbt -> finde die minimale
		// anzahl an zügen die man zum färben braucht
		else {

			// fange einmal mit jeder startfarbe an
			for (int i = 0; i < num_of_moves_per_startcolor.length; i++) {

				// kopie vom ursprünglichen board
				Field[][] current_board = copyBoard();

				int rows = current_board.length;
				int cols = current_board[0].length;

				// komponente von s1 im aktuellen board
				ArrayList<Field> component_s1 = findComponentS1(current_board);
				int current_color = i + 1;

				// solange s1 noch nicht das ganze feld eingenommen hat mit der aktuellen farbe
				while (component_s1.size() != rows * cols) {

					// mach einen move
					component_s1 = makeMoveS1(component_s1, current_color, current_board);

					num_of_moves++;

					// mach die moves zyklisch von 1-6
					if (current_color < 6) {
						current_color++;
					} else {
						current_color = 1;
					}
				}
				num_of_moves_per_startcolor[i] = num_of_moves;

				num_of_moves = 0;
			}

			// finde das minimum aus allen startfarben
			for (int i = 0; i < num_of_moves_per_startcolor.length; i++) {
				int current_min = num_of_moves_per_startcolor[i];
				if (current_min < min_moves) {
					min_moves = current_min;
				}
			}
		}
		return min_moves;
	}

	public ArrayList<Field> makeMoveS1(ArrayList<Field> component, int selected_color, Field[][] currentBoard) {

		// kopie von der komponente, an der die änderungen vorgenommen werden
		ArrayList<Field> updatedComponent = new ArrayList<>(component);

		// gehe alle felder durch, die in der komponente enthalten sind
		for (int i = 0; i < updatedComponent.size(); i++) {
			Field field = updatedComponent.get(i);
			field.setColor(selected_color);

			// schau dir die nachbarn von dem aktuellen feld an
			List<Field> neighbours = getNeighbors(field, currentBoard);
			for (Field neighbour : neighbours) {

				// füge den nachbarn zur komponente hinzu, falls
				// - er nicht schon in der komponente drin ist
				// - er die ausgewählte farbe hat
				if (!updatedComponent.contains(currentBoard[neighbour.getRow()][neighbour.getCol()])
						&& currentBoard[neighbour.getRow()][neighbour.getCol()].getColor() == selected_color) {
					neighbour.setColor(selected_color);

					updatedComponent.add(currentBoard[neighbour.getRow()][neighbour.getCol()]);

				}
			}
		}
		return updatedComponent;
	}

	public ArrayList<Field> makeMoveS2(ArrayList<Field> component, int selected_color, Field[][] currentBoard) {

		// kopie von der komponente, an der die änderungen vorgenommen werden
		ArrayList<Field> updatedComponent = new ArrayList<>(component);

		// gehe alle felder durch, die in der komponente enthalten sind
		for (int i = 0; i < updatedComponent.size(); i++) {
			Field field = updatedComponent.get(i);
			field.setColor(selected_color);

			// schau dir die nachbarn von dem aktuellen feld an
			List<Field> neighbours = getNeighbors(field, currentBoard);
			for (Field neighbour : neighbours) {

				// füge den nachbarn zur komponente hinzu, falls
				// - er nicht schon in der komponente drin ist
				// - er die ausgewählte farbe hat
				if (!updatedComponent.contains(currentBoard[neighbour.getRow()][neighbour.getCol()])
						&& currentBoard[neighbour.getRow()][neighbour.getCol()].getColor() == selected_color) {
					neighbour.setColor(selected_color);

					updatedComponent.add(currentBoard[neighbour.getRow()][neighbour.getCol()]);

				}
			}
		}
		return updatedComponent;
	}

	public ArrayList<Field> findComponentS1(Field[][] spielbrett) {

		ArrayList<Field> componentS1 = new ArrayList<>();
		int colorS1;

		// startfeld ist feld unten links -> hinzufügen
		Field start_field = spielbrett[spielbrett.length - 1][0];
		componentS1.add(start_field);

		// kopie von der komponente, an der die änderungen vorgenommen werden
		ArrayList<Field> updatedComponent = new ArrayList<>(componentS1);

		// gehe alle felder durch, die in der komponente enthalten sind
		for (int i = 0; i < updatedComponent.size(); i++) {

			Field field = updatedComponent.get(i);
			colorS1 = field.getColor();

			// schau dir die nachbarn von dem aktuellen feld an
			List<Field> neighbours = getNeighbors(field, spielbrett);
			for (Field neighbour : neighbours) {
				// füge den nachbarn zur komponente hinzu, falls
				// - er nicht schon in der komponente drin ist
				// - er die ausgewählte farbe hat
				if (!updatedComponent.contains(neighbour) && neighbour.getColor() == colorS1) {
					updatedComponent.add(neighbour);
				}
			}
		}
		componentS1 = updatedComponent;
		return componentS1;
	}

	public ArrayList<Field> findComponentS2(Field[][] spielbrett) {

		ArrayList<Field> componentS2 = new ArrayList<>();
		int colorS2;

		// startfeld ist feld oben rechts -> hinzufügen
		Field start_field = spielbrett[0][spielbrett[0].length - 1];
		componentS2.add(start_field);

		// kopie von der komponente, an der die änderungen vorgenommen werden
		ArrayList<Field> updatedComponent = new ArrayList<>(componentS2);

		// gehe alle felder durch, die in der komponente enthalten sind
		for (int i = 0; i < updatedComponent.size(); i++) {

			Field field = updatedComponent.get(i);
			colorS2 = field.getColor();

			// schau dir die nachbarn von dem aktuellen feld an
			List<Field> neighbours = getNeighbors(field, spielbrett);
			for (Field neighbour : neighbours) {
				// füge den nachbarn zur komponente hinzu, falls
				// - er nicht schon in der komponente drin ist
				// - er die ausgewählte farbe hat
				if (!updatedComponent.contains(neighbour) && neighbour.getColor() == colorS2) {
					updatedComponent.add(neighbour);
				}
			}
		}
		componentS2 = updatedComponent;

		return componentS2;
	}

	// Hilfsmethode: holt die nachbarn eines feldes
	private ArrayList<Field> getNeighbors(Field field, Field[][] currentboard) {

		ArrayList<Field> neighbours = new ArrayList<>();
		int row = field.getRow();
		int col = field.getCol();

		// Überprüfen des oberen Nachbarn
		if (row - 1 >= 0 && row - 1 < currentboard.length && col >= 0 && col < currentboard[row - 1].length) {
			neighbours.add(currentboard[row - 1][col]);
		}

		// Überprüfen des unteren Nachbarn
		if (row + 1 >= 0 && row + 1 < currentboard.length && col >= 0 && col < currentboard[row + 1].length) {
			neighbours.add(currentboard[row + 1][col]);
		}

		// Überprüfen des linken Nachbarn
		if (row >= 0 && row < currentboard.length && col - 1 >= 0 && col - 1 < currentboard[row].length) {
			neighbours.add(currentboard[row][col - 1]);
		}

		// Überprüfen des rechten Nachbarn
		if (row >= 0 && row < currentboard.length && col + 1 >= 0 && col + 1 < currentboard[row].length) {
			neighbours.add(currentboard[row][col + 1]);
		}
		return neighbours;
	}

	// Hilfsmethode: holt alle nachbarn einer komponente
	private ArrayList<Field> getNeighboursOfComponent(ArrayList<Field> component, Field[][] currentboard) {

		// neue liste erstellen, in der die nachbarn der komponente gespeichert werden
		ArrayList<Field> neighbours_of_component = new ArrayList<>();

		// gehe alle felder in der komponente durch
		for (Field f : component) {
			// schau dir die nachbarn von dem aktuellen feld an
			List<Field> neighbours = getNeighbors(f, currentboard);

			// gehe alle diese nachbarn durch
			for (Field neighbour : neighbours) {
				// wenn
				// - der nachbar bereits in der neighbours_of_component liste enthalten ist
				// - oder der nachbar in der komponente enthalten ist
				// dann füge ihn nicht hinzu. ansonsten füge ihn hinzu
				if (!neighbours_of_component.contains(neighbour) && !component.contains(neighbour)) {
					neighbours_of_component.add(neighbour);
				}
			}
		}
		return neighbours_of_component;
	}

	// Hilfsmethode: findet raus, wie oft die nachbarn der komponente von s2 jeweils
	// vorkommen
	private int[] getNumOfOccurenceOfColorsS2(ArrayList<Field> component_s1, ArrayList<Field> component_s2,
			Field[][] currentboard) {

		// erstelle ein array was an der stelle i die anzahl der vorkommnisse von zahl i
		// enthält
		int[] num_of_color_occurence = new int[6];

		// hol dir alle nachbarn der komponente von s2
		ArrayList<Field> neighbours_of_component = getNeighboursOfComponent(component_s2, currentboard);

		// laufe alle nachbarn der komponente durch
		for (Field neighbour : neighbours_of_component) {

			// wenn nicht -> feld ist valide
			if (!component_s1.contains(neighbour) && !component_s2.contains(neighbour)) {
				// in testing klasse geändert, weil zahlen bei 1 anfangen
				num_of_color_occurence[neighbour.getColor() - 1]++;
			}
		}
		return num_of_color_occurence;
	}

	// Hilfsmethode: findet raus, wie oft die nachbarn der komponente von s1 jeweils
	// vorkommen
	private int[] getNumOfOccurenceOfColorsS1(ArrayList<Field> component_s1, ArrayList<Field> component_s2,
			Field[][] currentboard) {

		// erstelle ein array was an der stelle i die anzahl der vorkommnisse von zahl i
		// enthält
		int[] num_of_color_occurence = new int[6];

		// hol dir alle nachbarn der komponente von s2
		ArrayList<Field> neighbours_of_component = getNeighboursOfComponent(component_s1, currentboard);

		// laufe alle nachbarn der komponente durch
		for (Field neighbour : neighbours_of_component) {

			// wenn nicht -> feld ist valide
			if (!component_s1.contains(neighbour) && !component_s2.contains(neighbour)) {
				// in testing klasse geändert, weil zahlen bei 1 anfangen
				num_of_color_occurence[neighbour.getColor() - 1]++;
			}
		}
		return num_of_color_occurence;
	}

	// Methode zum Vergleichen der Farben von zwei Boards
	public boolean areBoardsEqual(Field[][] board, Field[][] anotherboard) {

		// Durch jedes Feld iterieren und die Farben vergleichen
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				// Wenn die Farben nicht übereinstimmen, gebe false zurück
				if (board[i][j].getColor() != anotherboard[i][j].getColor()) {
					return false;
				}
			}
		}

		// Wenn alle Farben übereinstimmen, gebe true zurück
		return true;
	}

	// Methode zum Vergleichen der Komponenten in den Boards
	public static boolean compareComponents(ArrayList<Field> component, ArrayList<Field> another_component) {

		for (Field f : component) {

			boolean belongs_to_another = false;
			int row_component = f.getRow();
			int col_component = f.getCol();

			for (Field f_another : another_component) {

				int row_another_component = f_another.getRow();
				int col_another_component = f_another.getCol();

				if (row_component == row_another_component && col_component == col_another_component) {
					belongs_to_another = true;
				}
			}
			if (!belongs_to_another) {
				return false;
			}
		}
		return true;
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

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

}