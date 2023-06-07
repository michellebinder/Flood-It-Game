package logic;

/*
 * Siehe Hinweise zu Umgang mit dem Repository auf Aufgabenblatt.  
 */

public class Field {

	private int color;
	private int row;
	private int col;
	private int x_coordinate;
	private int y_coordinate;

	public Field(int row, int col, int color) {
		this.row = row;
		this.col = col;
		this.color = color;

	}

	/*
	 * Getter und Setter
	 */
	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getX_coordinate() {
		return x_coordinate;
	}

	public void setX_coordinate(int x_coordinate) {
		this.x_coordinate = x_coordinate;
	}

	public int getY_coordinate() {
		return y_coordinate;
	}

	public void setY_coordinate(int y_coordinate) {
		this.y_coordinate = y_coordinate;
	}

}
