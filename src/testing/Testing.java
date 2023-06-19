package testing;
/*
 * Siehe Hinweise auf dem Aufgabenblatt. 
 */

import logic.Field;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import logic.Board;

public class Testing {

	private Field[][] board;

	// in testing wird immer mit 6 farben getestet

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

		// neu hinzugefügt
		// rows = board.length;
		// cols = board[0].length;

		component_player_1 = new ArrayList<Field>();
		component_player_2 = new ArrayList<Field>();

		// stores the number of the players starting colors
		color_of_player_1 = board[board.length - 1][0].getColor();
		component_player_1.add(board[board.length - 1][0]);

		color_of_player_2 = board[0][board[0].length - 1].getColor();
		component_player_2.add(board[0][board[0].length - 1]);
	}

	// (1) Für jedes Feld aus dem Spielbrett gilt, dass seine Nachbarn eine andere
	// Farbe haben als das Feld selbst.
	// (2) Wenn es t viele Farben im Spiel gibt, gibt es auch t viele Farben im
	// Spielbrett.
	// (3) Das Feld in der Ecke links unten hat nicht die gleiche Farbe wie das Feld
	// in der Ecke rechts oben.
	public boolean isStartklar() {

		// Anforderung 2: Wenn es t viele Farben im Spiel gibt, gibt es auch t viele
		// Farben im Spielbrett.
		Set<Integer> uniqueColors = new HashSet<>();

		// Anforderung 3: Das Feld in der Ecke links unten hat nicht die gleiche Farbe
		// wie das Feld in der Ecke rechts oben.
		Field upper_right_field = board[0][board[0].length - 1];
		Field lower_left_field = board[board.length - 1][0];

		// wenn das feld oben rechts und unten links ne andere farbe haben
		if (upper_right_field.getColor() != lower_left_field.getColor()) {
			has_valid_starting_fields = true;
		}

		// Anforderung 1: Für jedes Feld aus dem Spielbrett gilt, dass seine Nachbarn
		// eine andere Farbe haben als das Feld selbst.
		// TODO: fragen ob das so richtig ist?
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				Field field = board[i][j];
				ArrayList<Field> neighbours = getNeighbors(field);

				if (neighbours.stream().anyMatch(neighbour -> field.getColor() == neighbour.getColor())) {
					has_valid_neighbours = false; // Das Feld hat einen Nachbarn mit derselben Farbe, also ist es
													// ungültig
				}
				if (!neighbours.stream().anyMatch(neighbour -> field.getColor() != neighbour.getColor())) {
					has_valid_neighbours = true; // Das Feld hat gültige Nachbarn
				}

				// Anforderung 2: checkt, wie viele farben tatsächlich auf dem feld sind
				if (!uniqueColors.contains(board[i][j].getColor())) {
					uniqueColors.add(board[i][j].getColor());
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
	// es ist möglich, innerhalb von höchstens moves vielen Zügen board so zu ändern, dass die Farbverteilung identisch zu der von anotherBoard ist.
	public boolean toBoard(Field[][] anotherBoard, int moves) {

		return false;
	}

	public int minMoves(int row, int col) {

		return 0;
	}

	public int minMovesFull() {

		return 0;
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

	private void printComponent1() {
		for (int i = 0; i < component_player_1.size(); i++) {
			int row = component_player_1.get(i).getRow();
			int col = component_player_1.get(i).getCol();
			int color = component_player_1.get(i).getColor();
			System.out.println("comp p1: (" + row + "," + col + "), color: " + color);
			System.out.println();
		}
	}

	private void printComponent2() {
		for (Field f : component_player_2) {
			int row = f.getRow();
			int col = f.getCol();
			int color = f.getColor();
			System.out.println("comp p2: (" + row + "," + col + "), color: " + color);
			System.out.println("end");
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
