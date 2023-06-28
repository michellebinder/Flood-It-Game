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

	// neu hinzugefügt
	private boolean has_valid_neighbours;
	private boolean has_valid_num_of_unique_colors;
	private boolean has_valid_starting_fields;

	private int rows;
	private int cols;

	// liste die die komponente von einem spieler darstellt
	private ArrayList<Field> component_player_1;
	private ArrayList<Field> component_player_2;

	private int color_of_player_1;
	private int color_of_player_2;

	// array to store the num of occurence of each color for strategy
	private int[] num_of_color_occurence;

	private int num_of_colors = 6;

	public Testing(Field[][] initBoard) {

		// vorgegeben
		this.board = initBoard;
		// this.initial_board = initBoard;

		// neu hinzugefügt
		rows = board.length;
		cols = board[0].length;

		component_player_1 = new ArrayList<Field>();
		component_player_2 = new ArrayList<Field>();

		// stores the number of the players starting colors
		color_of_player_1 = board[board.length - 1][0].getColor();
		component_player_1.add(board[board.length - 1][0]);

		color_of_player_2 = board[0][board[0].length - 1].getColor();
		component_player_2.add(board[0][board[0].length - 1]);

	}

	private Field[][] copyBoard() {
		// Tiefe Kopie von initBoard erstellen und in initial_board speichern
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

	// (1) Für jedes Feld aus dem Spielbrett gilt, dass seine Nachbarn eine andere
	// Farbe haben als das Feld selbst.
	// (2) Wenn es t viele Farben im Spiel gibt, gibt es auch t viele Farben im
	// Spielbrett.
	// (3) Das Feld in der Ecke links unten hat nicht die gleiche Farbe wie das Feld
	// in der Ecke rechts oben.
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

		// Anforderung 1: Für jedes Feld aus dem Spielbrett gilt, dass seine Nachbarn
		// eine andere Farbe haben als das Feld selbst.
		for (int i = 0; i < currentBoard.length; i++) {
			for (int j = 0; j < currentBoard[i].length; j++) {
				Field field = currentBoard[i][j];
				ArrayList<Field> neighbours = getNeighbors(field, currentBoard);

				for (Field neighbour : neighbours) {
					if (neighbour.getColor() != field.getColor()) {
						has_valid_neighbours = true;
					} else {
						has_valid_neighbours = false;
					}
				}

				// Anforderung 2: checkt, wie viele farben tatsächlich auf dem feld sind
				if (!uniqueColors.contains(currentBoard[i][j].getColor())) {
					uniqueColors.add(currentBoard[i][j].getColor());
				}
			}
		}

		// wenn die tatsächlichen farben auf dem brett 6 entspricht
		if (uniqueColors.size() == num_of_colors) {
			has_valid_num_of_unique_colors = true;
		}

		if (has_valid_neighbours && has_valid_num_of_unique_colors && has_valid_starting_fields) {
			return true;
		} else {
			return false;
		}
	}

	// Das Spielbrett befindet sich in einer Endkonfiguration, wenn alle vorhandenen
	// Felder entweder zur Komponente von S1 oder zur Komponente von S2 gehören. Das
	// heißt folglich, dass keine der jeweiligen Komponenten mehr vergrößert werden
	// kann.
	public boolean isEndConfig() {

		// die beiden startfelder zu den komponenten hinzufügen
		Field upper_right_field = board[0][board[0].length - 1];
		Field lower_left_field = board[board.length - 1][0];
		component_player_1.add(lower_left_field);
		component_player_2.add(upper_right_field);

		color_of_player_1 = lower_left_field.getColor();
		color_of_player_2 = upper_right_field.getColor();

		// kopie von der komponente, an der die änderungen vorgenommen werden
		ArrayList<Field> updatedComponent_player_1 = new ArrayList<>(component_player_1);
		ArrayList<Field> updatedComponent_player_2 = new ArrayList<>(component_player_2);

		// befüllung der komponente von s1
		// gehe alle felder durch, die in der komponente enthalten sind
		for (int i = 0; i < updatedComponent_player_1.size(); i++) {
			Field field = updatedComponent_player_1.get(i);

			// schau dir die nachbarn von dem aktuellen feld an
			List<Field> neighbours = getNeighbors(field);

			for (Field neighbour : neighbours) {
				// füge den nachbarn zur komponente hinzu, falls
				// - er nicht schon in der komponente drin ist
				// - er die ausgewählte farbe hat
				if (!updatedComponent_player_1.contains(neighbour) && neighbour.getColor() == color_of_player_1) {
					updatedComponent_player_1.add(neighbour);
				}
			}

		}
		component_player_1 = updatedComponent_player_1;

		// befüllung der komponente von s2
		// gehe alle felder durch, die in der komponente enthalten sind
		for (int i = 0; i < updatedComponent_player_2.size(); i++) {
			Field field = updatedComponent_player_2.get(i);

			// schau dir die nachbarn von dem aktuellen feld an
			List<Field> neighbours = getNeighbors(field);

			for (Field neighbour : neighbours) {
				// füge den nachbarn zur komponente hinzu, falls
				// - er nicht schon in der komponente drin ist
				// - er die ausgewählte farbe hat
				if (!updatedComponent_player_2.contains(neighbour) && neighbour.getColor() == color_of_player_2) {
					updatedComponent_player_2.add(neighbour);
				}
			}

		}
		component_player_2 = updatedComponent_player_2;

		// prüfen ob es sich um eine endkonfiguration handelt
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Field field = board[i][j];
				if (!component_player_1.contains(field) && !component_player_2.contains(field)) {
					return false; // Wenn ein Feld weder zu s1 noch zu s2 gehört, ist es keine Endkonfiguration
				}
			}
		}
		return true; // Alle Felder gehören entweder zu s1 oder s2
	}

	// Diese Methode gibt eine Zahl zurück, die für die Farbe steht, die S2 als
	// nächstes wählt
	// STAGNATION
	public int testStrategy01() {

		// finde raus, wie oft jede farbe vorkommt (gespeichert in
		// num_of_color_occurence array)
		getNumOfOccurenceOfColors(component_player_2);
		// -1 weil die bei testing bei 1 anfangen statt bei 0
		num_of_color_occurence[color_of_player_1 - 1] = 2000;
		num_of_color_occurence[color_of_player_2 - 1] = 2000;

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

	// Diese Methode gibt eine Zahl zurück, die für die Farbe steht, die S2 als
	// nächstes wählt
	// GREEDY
	public int testStrategy02() {
		// finde raus, wie oft jede farbe vorkommt (gespeichert in
		// num_of_color_occurence array)
		getNumOfOccurenceOfColors(component_player_2);
		num_of_color_occurence[color_of_player_1] = -2000;
		num_of_color_occurence[color_of_player_2] = -2000;

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

	// Diese Methode gibt eine Zahl zurück, die für die Farbe steht, die S2 als
	// nächstes wählt
	// BLOCKING
	public int testStrategy03() {

		// finde raus, wie oft jede farbe vorkommt (gespeichert in
		// num_of_color_occurence array)
		getNumOfOccurenceOfColors(component_player_1);
		num_of_color_occurence[color_of_player_1] = -2000;
		num_of_color_occurence[color_of_player_2] = -2000;

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

	// Hier spielt S1 das Spiel alleine, das heißt, nach jedem Zug von S1 ist S1
	// wieder dran und darf auch die Farbe wählen, die das Feld oben rechts hat.
	// Diese berechnet die Anzahl an Zügen, die S1 mindestens braucht, um das
	// Field-Objekt mit Zeilenindex x und Spaltenindex y einzunehmen (also zum Teil
	// der eigenen Komponente zu machen), ausgehend von dem in board gespeicherten
	// Spielbrett. Ist das entsprechende Field- Objekt bereits eingenommen, so wird
	// der Wert 0 zurück gegeben. Bei der Wahl der Farben muss S1 aufsteigend und
	// zyklisch vorgehen
	public int minMoves(int row, int col) {

		// TODO: wenn board bereits einfarbig gefärbt ist -> 0 returnen

		int min_moves = Integer.MAX_VALUE; // Ausgangswert auf maximalen Integer-Wert setzen
		int num_of_moves = 0;

		int[] num_of_moves_per_startcolor = new int[6];

		// fange einmal mit jeder startfarbe an
		for (int i = 0; i < num_of_moves_per_startcolor.length; i++) {

			// kopie vom ursprünglichen board
			Field[][] current_board = copyBoard();

			// komponente von s1 im aktuellen board
			ArrayList<Field> component_s1 = findComponentS1(current_board);
			int current_color = i;

			// solange s1 noch nicht das ganze feld eingenommen hat mit der aktuellen farbe
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

		for (int i = 0; i < num_of_moves_per_startcolor.length; i++) {
			int current_min = num_of_moves_per_startcolor[i];
			if (current_min < min_moves) {
				min_moves = current_min;
			}
		}
		return min_moves;
	}

	// Hier spielt S1 das Spiel alleine, das heißt, nach jedem Zug von S1 ist S1
	// wieder dran und darf auch die Farbe wählen, die das Feld oben rechts hat.
	// Implementieren Sie die folgende Methode.
	// Diese berechnet die Anzahl an Zügen, die S1 mindestens braucht, um das
	// gesamte Spielbrett in einer Farbe zu färben. Dabei darf S1 die Farben nur
	// zyklisch und aufsteigend sortiert wählen, wobei allerdings nicht festgelegt
	// ist, mit welcher Farbe S1 beginnt: S1 wählt z.B. als erste Farbe die Farbe 1,
	// muss aber dann 2, dann 3, bis zur 6 wählen, und beginnt dann wieder von vorne
	// bei 1, dann 2, und so weiter. Die erste Farbe von S1 könnte aber z.B. auch
	// die Farbe 3 sein. Die nächste muss dann 4, 5, 6 und anschließend wieder
	// 1,2,3,4,5,6 und wieder 1, 2, und so weiter sein, bis das Spielbrett eine
	// Farbe hat. Unter diesen zylkischen Reihenfolgen ermittelt die Methode also
	// die kleinste Anzahl an Zügen, die notwendig ist, um das Spielbrett in einer
	// Farbe zu färben.
	// Ist das Spielbrett bereits einfarbig gefärbt, so wird der Wert 0 zurück
	// gegeben.
	public int minMovesFull() {

		// TODO: wenn board bereits einfarbig gefärbt ist -> 0 returnen

		int min_moves = Integer.MAX_VALUE; // Ausgangswert auf maximalen Integer-Wert setzen
		int num_of_moves = 0;

		int[] num_of_moves_per_startcolor = new int[6];

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

		for (int i = 0; i < num_of_moves_per_startcolor.length; i++) {
			int current_min = num_of_moves_per_startcolor[i];
			if (current_min < min_moves) {
				min_moves = current_min;
			}
		}
		return min_moves;
	}

	public void makeMoveS1(ArrayList<Field> component, int selected_color) {

		// kopie von der komponente, an der die änderungen vorgenommen werden
		ArrayList<Field> updatedComponent = new ArrayList<>(component);

		// gehe alle felder durch, die in der komponente enthalten sind
		for (int i = 0; i < updatedComponent.size(); i++) {
			Field field = updatedComponent.get(i);
			field.setColor(selected_color);
			color_of_player_1 = selected_color;

			// schau dir die nachbarn von dem aktuellen feld an
			List<Field> neighbours = getNeighbors(field);
			for (Field neighbour : neighbours) {
				// füge den nachbarn zur komponente hinzu, falls
				// - er nicht schon in der komponente drin ist
				// - er die ausgewählte farbe hat
				if (!updatedComponent.contains(neighbour) && neighbour.getColor() == selected_color) {
					neighbour.setColor(selected_color);
					color_of_player_1 = selected_color;
					updatedComponent.add(neighbour);
				}
			}
		}
		component_player_1 = updatedComponent;
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

		// startfeld ist feld unten links
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

	private ArrayList<Field> setColorOfWholeComponent(ArrayList<Field> current_component, int newColor) {

		for (Field f : current_component) {
			f.setColor(newColor);
		}

		return current_component;
	}

	private ArrayList<Field> findComponentS2(Field[][] spielbrett) {

		ArrayList<Field> componentS2 = new ArrayList<>();
		int colorS2;

		// startfeld ist feld obenrechts
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

	private boolean belongsToS1(Field[][] currentboard, int color_s1) {
		// prüfen ob es sich um eine endkonfiguration handelt
		for (int i = 0; i < currentboard.length; i++) {
			for (int j = 0; j < currentboard[0].length; j++) {
				Field field = currentboard[i][j];
				if (field.getColor() != color_s1) {
					return false; // Wenn ein Feld nicht zur komponente von s1 gehört
				}
			}
		}
		return true; // Alle Felder gehören zu s1
	}

	// Hilfsmethode: holt die nachbarn eines feldes
	private ArrayList<Field> getNeighbors(Field field) {
		ArrayList<Field> neighbours = new ArrayList<>();
		int row = field.getRow();
		int col = field.getCol();

		// Überprüfen der Nachbarn oben, unten, links und rechts
		// Überprüfen des oberen Nachbarn
		if (row - 1 >= 0 && row - 1 < board.length && col >= 0 && col < board[row - 1].length) {
			neighbours.add(board[row - 1][col]);
		}

		// Überprüfen des unteren Nachbarn
		if (row + 1 >= 0 && row + 1 < board.length && col >= 0 && col < board[row + 1].length) {
			neighbours.add(board[row + 1][col]);
		}

		// Überprüfen des linken Nachbarn
		if (row >= 0 && row < board.length && col - 1 >= 0 && col - 1 < board[row].length) {
			neighbours.add(board[row][col - 1]);
		}

		// Überprüfen des rechten Nachbarn
		if (row >= 0 && row < board.length && col + 1 >= 0 && col + 1 < board[row].length) {
			neighbours.add(board[row][col + 1]);
		}

		return neighbours;
	}

	// Hilfsmethode: holt die nachbarn eines feldes
	private ArrayList<Field> getNeighbors(Field field, Field[][] currentboard) {
		ArrayList<Field> neighbours = new ArrayList<>();
		int row = field.getRow();
		int col = field.getCol();

		// Überprüfen der Nachbarn oben, unten, links und rechts
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
	private ArrayList<Field> getNeighboursOfComponent(ArrayList<Field> component) {

		// neue liste erstellen, in der die nachbarn der komponente gespeichert werden
		ArrayList<Field> neighbours_of_component = new ArrayList<>();

		// gehe alle felder in der komponente durch
		for (Field f : component) {
			// schau dir die nachbarn von dem aktuellen feld an
			List<Field> neighbours = getNeighbors(f);

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
	private void getNumOfOccurenceOfColors(ArrayList<Field> component) {
		// erstelle ein array was an der stelle i die anzahl der vorkommnisse von zahl i
		// enthält
		num_of_color_occurence = new int[num_of_colors];

		// hol dir alle nachbarn der komponente
		ArrayList<Field> neighbours_of_component = getNeighboursOfComponent(component);

		// laufe alle nachbarn der komponente durch
		for (Field neighbour : neighbours_of_component) {

			// wenn nicht -> feld ist valide
			if (!component_player_1.contains(neighbour) && !component_player_2.contains(neighbour)) {
				// in testing klasse geändert, weil zahlen bei 1 anfangen
				num_of_color_occurence[neighbour.getColor() - 1]++;
			}
		}

		// for (int i = 0; i < num_of_color_occurence.length; i++) {
		// System.out.println("color: " + i + " num of occ: " +
		// num_of_color_occurence[i]);
		// }
	}

	// Hilfsmethode: findet raus, wie oft die nachbarn der komponente von s2 jeweils
	// vorkommen
	private void getNumOfOccurenceOfColorsForS1Solo(ArrayList<Field> component) {
		// erstelle ein array was an der stelle i die anzahl der vorkommnisse von zahl i
		// enthält
		num_of_color_occurence = new int[num_of_colors];

		// hol dir alle nachbarn der komponente
		ArrayList<Field> neighbours_of_component = getNeighboursOfComponent(component);

		// laufe alle nachbarn der komponente durch
		for (Field neighbour : neighbours_of_component) {

			// wenn nicht -> feld ist valide
			if (!component_player_1.contains(neighbour)) {
				// in testing klasse geändert, weil zahlen bei 1 anfangen
				num_of_color_occurence[neighbour.getColor()]++;
			}
		}

		// for (int i = 1; i < num_of_color_occurence.length; i++) {
		// System.out.println("color: " + i + " num of occ: " +
		// num_of_color_occurence[i]);
		// }

	}

	private void printComponent1(Field[][] currentboard) {
		for (int i = 0; i < component_player_1.size(); i++) {
			int row = component_player_1.get(i).getRow();
			int col = component_player_1.get(i).getCol();
			int color = component_player_1.get(i).getColor();
			System.out.println("comp p1: (" + row + "," + col + "), color: " + color);
			System.out.println();
		}
	}

	private void printComponent(Field[][] currentboard) {
		var currentcomponent = findComponentS1(currentboard);
		for (int i = 0; i < currentcomponent.size(); i++) {
			int row = currentcomponent.get(i).getRow();
			int col = currentcomponent.get(i).getCol();
			int color = currentcomponent.get(i).getColor();
			System.out.println("comp p1: (" + row + "," + col + "), color: " + color);
			System.out.println();
		}
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