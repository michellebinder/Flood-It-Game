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

	// in testing ist die maximale anzahl an farben immer 6

	private int rows;
	private int cols;

	public Testing(Field[][] initBoard) {

		// vorgegeben
		this.board = initBoard;

		// neu hinzugefügt
		rows = board.length;
		cols = board[0].length;
	}

	// Hilfsmethode die eine Kopie von dem übergebenen Board erstellt, damit wir
	// nicht auf dem "Original" arbeiten müssen
	private Field[][] copyBoard() {

		Field[][] copied_board = new Field[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Field originalField = board[i][j];
				Field copiedField = new Field(i, j, originalField.getColor());
				copied_board[i][j] = copiedField;
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

	// s1 fängt an
	// es ist möglich, innerhalb von höchstens moves vielen Zügen board so zu
	// ändern, dass die Farbverteilung identisch zu der von anotherBoard ist.
	// wenn der Wert 0 oder 1 oder 2 oder 3 gespeichert ist, false zurück gegeben
	// wird.
	// wenn der Wert 4 oder größer gespeichert ist, true zurück gegeben wird.
	public boolean toBoard(Field[][] anotherBoard, int moves) {

		return false;
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
				int current_color = i;

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

		// zunächst so hoch setzen, das es auf jeden fall von den anzahl zügen abgelöst
		// wird
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
				int current_color = i;

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

	// Hilfsmethode die ein board auf der konsole ausgibt
	public void printBoard(Field[][] currentboard) {
		for (int i = 0; i < currentboard.length; i++) {
			for (int j = 0; j < currentboard[0].length; j++) {
				System.out.print(currentboard[i][j].getColor() + " ");
			}
			System.out.println();
		}
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

	private ArrayList<Field> findComponentS2(Field[][] spielbrett) {

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

		// for (int i = 0; i < num_of_color_occurence.length; i++) {
		// System.out.println("color: " + i + " num of occ: " +
		// num_of_color_occurence[i]);
		// }
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