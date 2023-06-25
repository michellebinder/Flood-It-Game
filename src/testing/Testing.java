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

	// in testing wird immer mit 6 farben getestet

	// neu hinzugefügt
	private boolean has_valid_neighbours;
	private boolean has_valid_num_of_unique_colors;
	private boolean has_valid_starting_fields;

	// liste die die komponente von einem spieler darstellt
	private ArrayList<Field> component_player_1;
	private ArrayList<Field> component_player_2;

	private int color_of_player_1;
	private int color_of_player_2;

	// array to store the num of occurence of each color for strategy
	private int[] num_of_color_occurence;

	private int num_of_colors = 6;

	// private Field[][] initial_board;

	public Testing(Field[][] initBoard) {

		// vorgegeben
		this.board = initBoard;
		// this.initial_board = initBoard;

		component_player_1 = new ArrayList<Field>();
		component_player_2 = new ArrayList<Field>();

		// stores the number of the players starting colors
		color_of_player_1 = board[board.length - 1][0].getColor();
		component_player_1.add(board[board.length - 1][0]);

		color_of_player_2 = board[0][board[0].length - 1].getColor();
		component_player_2.add(board[0][board[0].length - 1]);
	}

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
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				Field field = board[i][j];
				ArrayList<Field> neighbours = getNeighbors(field, board);

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
			List<Field> neighbours = getNeighbors(field, board);

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
			List<Field> neighbours = getNeighbors(field, board);

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
		for (int i = 0; i < this.board.length; i++) {
			for (int j = 0; j < this.board[i].length; j++) {
				Field field = board[i][j];
				if (!component_player_1.contains(field) && !component_player_2.contains(field)) {
					return false; // Wenn ein Feld weder zu s1 noch zu s2 gehört, ist es keine Endkonfiguration
				}
			}
		}
		return true; // Alle Felder gehören entweder zu s1 oder s2
	}

	// STAGNATION
	public int testStrategy01() {

		// finde raus, wie oft jede farbe vorkommt (gespeichert in
		// num_of_color_occurence array)
		getNumOfOccurenceOfColors(component_player_2, board);
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

	// GREEDY
	public int testStrategy02() {
		// finde raus, wie oft jede farbe vorkommt (gespeichert in
		// num_of_color_occurence array)
		getNumOfOccurenceOfColors(component_player_2, board);
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

	// BLOCKING
	public int testStrategy03() {

		// finde raus, wie oft jede farbe vorkommt (gespeichert in
		// num_of_color_occurence array)
		getNumOfOccurenceOfColors(component_player_1, board);
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

	public boolean toBoard(Field[][] anotherBoard, int moves) {

		return false;
	}

	public void step(Field[][] anotherBoard, int num_steps) {

		ArrayList<Field> component_player_1 = findComponentS1(board);
		ArrayList<Field> component_player_2 = findComponentS2(board);

		int color_of_player_1 = component_player_1.get(0).getColor();
		int color_of_player_2 = component_player_2.get(0).getColor();

		ArrayList<Field> compS1_a = findComponentS1(anotherBoard);
		ArrayList<Field> compS2_a = findComponentS2(anotherBoard);

		int color_of_player_1_a = compS1_a.get(0).getColor();
		int color_of_player_2_a = compS2_a.get(0).getColor();

		ArrayList<Field> neighbours_of_comp = getNeighboursOfComponent(component_player_1, board);

		if (!isSameBoard(anotherBoard)) {

			for (Field neighbour : neighbours_of_comp) {

				if (neighbour.getColor() != color_of_player_2
						&& compS1_a.contains(anotherBoard[neighbour.getRow()][neighbour.getCol()])) {
					System.out.println("steps: " + num_steps);
					System.out.println("Komponente vor dem zug: ");
					printComponent(component_player_1);
					makeMoveS1(component_player_1, neighbour.getColor());

					//step(anotherBoard, num_steps++);
					// System.out.println("Komponente nach dem zug: ");
					// printComponent(component_player_1);
				}
			}
		}
		// wenn mehrere comp_neighbours in frage kommen dann jeden einmal durchlaufen
		// und speichern wie viele züge man braucht

	}

	private boolean isSameBoard(Field[][] anotherBoard) {

		for (int i = 0; i < this.board.length; i++) {
			for (int j = 0; j < this.board[i].length; j++) {

				Field field = this.board[i][j];
				Field another_field = anotherBoard[i][j];

				if (field.getColor() != another_field.getColor()) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean isValidColor(int color) {
		if (color != getColor_of_player_1() && color != getColor_of_player_2()) {
			return true;
		} else {
			return false;
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

	public void makeMoveS1(ArrayList<Field> component, int selected_color) {

		// kopie von der komponente, an der die änderungen vorgenommen werden
		ArrayList<Field> updatedComponent = new ArrayList<>(component);

		// gehe alle felder durch, die in der komponente enthalten sind
		for (int i = 0; i < updatedComponent.size(); i++) {
			Field field = updatedComponent.get(i);
			field.setColor(selected_color);
			color_of_player_1 = selected_color;

			// schau dir die nachbarn von dem aktuellen feld an
			List<Field> neighbours = getNeighbors(field, board);
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
		component = updatedComponent;
	}

	// public void resetBoard() {
	// board = initial_board;
	// component_player_1.clear();
	// color_of_player_1 = board[board.length - 1][0].getColor();
	// component_player_1.add(board[board.length - 1][0]);

	// // Setze die Farbe von S1 auf die Farbe des Startfeldes
	// for (Field field : component_player_1) {
	// field.setColor(color_of_player_1);
	// }
	// }

	private boolean belongsToS1() {
		// prüfen ob es sich um eine endkonfiguration handelt
		for (int i = 0; i < this.board.length; i++) {
			for (int j = 0; j < this.board[i].length; j++) {
				Field field = board[i][j];
				if (!component_player_1.contains(field)) {
					return false; // Wenn ein Feld nicht zu s1 gehört
				}
			}
		}
		return true; // Alle Felder gehören zu s1
	}

	// Hilfsmethode: holt die nachbarn eines feldes
	// private ArrayList<Field> getNeighbors(Field field) {
	// ArrayList<Field> neighbours = new ArrayList<>();
	// int row = field.getRow();
	// int col = field.getCol();

	// // Überprüfen der Nachbarn oben, unten, links und rechts
	// // Überprüfen des oberen Nachbarn
	// if (row - 1 >= 0 && row - 1 < board.length && col >= 0 && col < board[row -
	// 1].length) {
	// neighbours.add(board[row - 1][col]);
	// }

	// // Überprüfen des unteren Nachbarn
	// if (row + 1 >= 0 && row + 1 < board.length && col >= 0 && col < board[row +
	// 1].length) {
	// neighbours.add(board[row + 1][col]);
	// }

	// // Überprüfen des linken Nachbarn
	// if (row >= 0 && row < board.length && col - 1 >= 0 && col - 1 <
	// board[row].length) {
	// neighbours.add(board[row][col - 1]);
	// }

	// // Überprüfen des rechten Nachbarn
	// if (row >= 0 && row < board.length && col + 1 >= 0 && col + 1 <
	// board[row].length) {
	// neighbours.add(board[row][col + 1]);
	// }

	// return neighbours;
	// }

	// Hilfsmethode: holt die nachbarn eines feldes
	private ArrayList<Field> getNeighbors(Field field, Field[][] spielbrett) {
		ArrayList<Field> neighbours = new ArrayList<>();
		int row = field.getRow();
		int col = field.getCol();

		// Überprüfen der Nachbarn oben, unten, links und rechts
		// Überprüfen des oberen Nachbarn
		if (row - 1 >= 0 && row - 1 < spielbrett.length && col >= 0 && col < spielbrett[row - 1].length) {
			neighbours.add(spielbrett[row - 1][col]);
		}

		// Überprüfen des unteren Nachbarn
		if (row + 1 >= 0 && row + 1 < spielbrett.length && col >= 0 && col < spielbrett[row + 1].length) {
			neighbours.add(spielbrett[row + 1][col]);
		}

		// Überprüfen des linken Nachbarn
		if (row >= 0 && row < spielbrett.length && col - 1 >= 0 && col - 1 < spielbrett[row].length) {
			neighbours.add(spielbrett[row][col - 1]);
		}

		// Überprüfen des rechten Nachbarn
		if (row >= 0 && row < spielbrett.length && col + 1 >= 0 && col + 1 < spielbrett[row].length) {
			neighbours.add(spielbrett[row][col + 1]);
		}

		return neighbours;
	}

	// Hilfsmethode: holt alle nachbarn einer komponente
	private ArrayList<Field> getNeighboursOfComponent(ArrayList<Field> component, Field[][] spielbrett) {

		// neue liste erstellen, in der die nachbarn der komponente gespeichert werden
		ArrayList<Field> neighbours_of_component = new ArrayList<>();

		// gehe alle felder in der komponente durch
		for (Field f : component) {
			// schau dir die nachbarn von dem aktuellen feld an
			List<Field> neighbours = getNeighbors(f, spielbrett);

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
	private void getNumOfOccurenceOfColors(ArrayList<Field> component, Field[][] spielbrett) {
		// erstelle ein array was an der stelle i die anzahl der vorkommnisse von zahl i
		// enthält
		num_of_color_occurence = new int[num_of_colors];

		// hol dir alle nachbarn der komponente
		ArrayList<Field> neighbours_of_component = getNeighboursOfComponent(component, spielbrett);

		// laufe alle nachbarn der komponente durch
		for (Field neighbour : neighbours_of_component) {

			// wenn nicht -> feld ist valide
			if (!component_player_1.contains(neighbour) && !component_player_2.contains(neighbour)) {
				// in testing klasse geändert, weil zahlen bei 1 anfangen
				num_of_color_occurence[neighbour.getColor() - 1]++;
			}
		}
	}

	// Hilfsmethode: findet raus, wie oft die nachbarn der komponente von s2 jeweils
	// vorkommen
	private void getNumOfOccurenceOfColorsForS1Solo(ArrayList<Field> component, Field[][] spielbrett) {
		// erstelle ein array was an der stelle i die anzahl der vorkommnisse von zahl i
		// enthält
		num_of_color_occurence = new int[num_of_colors];

		// hol dir alle nachbarn der komponente
		ArrayList<Field> neighbours_of_component = getNeighboursOfComponent(component, spielbrett);

		// laufe alle nachbarn der komponente durch
		for (Field neighbour : neighbours_of_component) {

			// wenn nicht -> feld ist valide
			if (!component_player_1.contains(neighbour)) {
				// in testing klasse geändert, weil zahlen bei 1 anfangen
				num_of_color_occurence[neighbour.getColor()]++;
			}
		}
	}

	public void printComponent(ArrayList<Field> comp) {
		for (int i = 0; i < comp.size(); i++) {
			int row = comp.get(i).getRow();
			int col = comp.get(i).getCol();
			int color = comp.get(i).getColor();
			System.out.println("comp : (" + row + "," + col + "), color: " + color);

		}
		System.out.println("-------------");
	}

	public int minMoves(int row, int col) {

		return 0;
	}

	public int minMovesFull() {

		// int min_moves;

		// int[] num_of_moves_per_startcolor = new int[6];

		// for (int i = 0; i < num_of_moves_per_startcolor.length; i++) {
		// resetBoard();
		// num_of_moves_per_startcolor[i] = minMovesFullHelper(i + 1);
		// }

		// min_moves = Integer.MAX_VALUE; // Ausgangswert auf maximalen Integer-Wert
		// setzen

		// for (int i = 0; i < num_of_moves_per_startcolor.length; i++) {
		// int current = num_of_moves_per_startcolor[i];
		// if (current < min_moves) {
		// min_moves = current;
		// }
		// }
		// System.out.println("die wenigsten züge sind: " + min_moves);
		return 0;
	}

	// TODO: get component methode
	public int minMovesFullHelper(int color) {

		// int num_of_moves = 0;

		// while (!belongsToS1()) {

		// makeMoveS1(component_player_1, color);

		// num_of_moves++;

		// if (color < 6) {
		// color++;
		// } else {
		// color = 1;
		// }
		// }
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

	public ArrayList<Field> getComponent_player_1() {
		return component_player_1;
	}

	public void setComponent_player_1(ArrayList<Field> component_player_1) {
		this.component_player_1 = component_player_1;
	}

	public ArrayList<Field> getComponent_player_2() {
		return component_player_2;
	}

	public void setComponent_player_2(ArrayList<Field> component_player_2) {
		this.component_player_2 = component_player_2;
	}

	public int getColor_of_player_1() {
		return color_of_player_1;
	}

	public void setColor_of_player_1(int color_of_player_1) {
		this.color_of_player_1 = color_of_player_1;
	}

	public int getColor_of_player_2() {
		return color_of_player_2;
	}

	public void setColor_of_player_2(int color_of_player_2) {
		this.color_of_player_2 = color_of_player_2;
	}

}
