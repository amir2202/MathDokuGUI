import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

public class Grid extends GridPane {
	private int dimensions;
	private Cell[][] cells;
	private Cell selectedCell;
	private ArrayList<String> numbers;
	private HashMap<Integer,Integer[]> cords;
	public Grid(int dimensions) {
		super();
		this.numbers = new ArrayList<String>();
		for(int i = 1; i <= this.getDimensions();i++) {
			numbers.add(String.valueOf(i));
		}
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(100/dimensions);
        for (int i = 0; i < dimensions; i++)
            super.getColumnConstraints().add(column);
		cells = new Cell[dimensions][dimensions];
		this.dimensions = dimensions;
		cords = new HashMap<Integer,Integer[]>();
		
		
		for(int i= 0; i< this.dimensions;i++) {
			for(int j = 0; j<this.dimensions;j++) {
				cells[i][j] = new Cell(i,j);
				cells[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent arg0) {
						Cell current = (Cell) arg0.getSource();
						if(current.getSelected() == true) {
							current.setSelected(false);
							selectedCell = null;
							
						}
						else if(current.getSelected() == false) {
							if(selectedCell == null) {
								selectedCell = current;
							}
							selectedCell.setSelected(false);
							current.setSelected(true);
							selectedCell = current;
						}
						for(int i = 1; i <= dimensions*dimensions; i++) {
							System.out.println("Neighbouring Cells for position " + i);
							for(int j: adjacentCells(i)) {
								System.out.println(j);
							}
						}
				}
					
				});
				this.setVgrow(cells[i][j], Priority.ALWAYS);
				this.setHgrow(cells[i][j], Priority.ALWAYS);
				this.add(cells[i][j], i, j);
			}
		}
		this.intCords();
	}
	
	

	
	public int getDimensions() {
		return this.dimensions;
	}
	
	public void clearCells() {
		for(int i = 0; i < cells.length; i++) {
			for(int j = 0;j<cells.length;j++) {
				cells[i][j].setText(new Text(" "),true, 0);
				cells[i][j].resetStyle();
			}
		}
	}
	
	public void setCage(String label, int... args) {
		if (args.length == 1) {
			Cage single = new Cage(label, args);
			Integer[] coordinates = this.getCords(args[0]);
			int x = coordinates[0];
			int y = coordinates[1];
			cells[x][y].setLabel(label);
			this.cells[x][y].setCage(single);
			cells[x][y].cellThickenBorder();
		}

		//now for args 
		else if(args.length > 1) {
			Cage multiple = new Cage(label,args);
			for(int i = 0; i < args.length;i++) {
				if(i == 0) {
					Integer[] cords = this.getCords(args[i]);
					int x = cords[0];
					int y = cords[1];
					cells[x][y].setLabel(label);
				    cells[x][y].setCage(multiple);
				    multiple.addCords(cords);
				}
				else {
					Integer[] other = this.getCords(args[i]);
					int x = other[0];
					int y = other[1];
					this.cells[x][y].setCage(multiple);
					multiple.addCords(other);
				
			}
			}
			
			this.setUpBorders();
		}
	}
	
	public void intCords() {
		//initial coordinate
		if(cords.isEmpty() != true) {
			cords.clear();
		}
		int coordinate = 1;
		for(int row = 0; row < this.getDimensions(); row++) {
			for(int column = 0; column <this.getDimensions(); column++) {
				Integer[] temp = new Integer[2];
				temp[0] = column;
				temp[1] = row;
				cords.put(coordinate, temp);
				coordinate++;
			}
		}
		
	}
	
	public Cell getSelected() {
		return this.selectedCell;
	}
	
	
	public void setUpBorders() {
		for(int i = 0; i < this.getDimensions();i++) {
			for(int j = 0; j< this.getDimensions(); j++) {
				String right = "0.5";
				String left = "0.5";
				String up= "0.5";
				String bottom = "0.5";
				//check right
				//if right is maximum --> add border to right
				if(i==this.getDimensions() -1) {
					right = "0.5";
				}
				else if(this.cells[i][j].getCage() != this.cells[i+1][j].getCage()) {
					right = "2.50";
				}
				//check left
				if(i == 0) {
					left = "0.5";
				}
				else if(this.cells[i][j].getCage() != this.cells[i-1][j].getCage()) {
					left = "2.50";
				}	
					
				//check top 
				if(j== 0) {
					up = "0.5";
				}
				else if (this.cells[i][j].getCage() != this.cells[i][j-1].getCage()) {
					up = "2.50";
				}
					
				//check bottom
				if(j == this.getDimensions()-1) {
					bottom = "0.5";
				}
				else if(this.cells[i][j].getCage() != this.cells[i][j+1].getCage()) {
					bottom = "2.50";
				}
				
				
				this.cells[i][j].setStyle("-fx-border-width: " +up + " " + right + " " + bottom+ " " + left);
			}
		}
	}
	
	//public void setUpBorders(Cage cage, boolean mistakes)
	//Indicate whether it used for solving purpose
	public boolean validCellInput(Cell cell, boolean solver) {
		//Row column logic 
		if((columnDuplicates(cell.getX()) || rowDuplicates(cell.getY())) && (solver == true)) {
			return false;
		}
		if(solver == false) {
			if(!cell.getCage().isCageFull()|| !cell.getCage().isCageCorrect()) {
				cell.getCage().setCorrect(false);
			}
			if(cell.getCage().isCageFull() == true && cell.getCage().isCageCorrect() == true) {
				cell.getCage().setCorrect(true);
			}
			if(columnDuplicates(cell.getX()) == true) {
				for(int y = 0; y < this.dimensions;y++) {
					if(this.cells[cell.getX()][y].getCorrect() != false) {
						this.cells[cell.getX()][y].setHighlighted(true);	
					}
					if(!this.cells[cell.getX()][y].getCorrect() && this.cells[cell.getX()][y].getHighlighted()) {
						this.cells[cell.getX()][y].resetStyle();
						this.cells[cell.getX()][y].setCorrect(false);
					}
				}
			}
			if(rowDuplicates(cell.getY()) == true) {
				for(int x = 0; x < this.dimensions;x++) {
					if(this.cells[x][cell.getY()].getCorrect() != false) {
						this.cells[x][cell.getY()].setHighlighted(true);	
					}
					if(!this.cells[x][cell.getY()].getCorrect() && this.cells[x][cell.getY()].getHighlighted()) {
						this.cells[x][cell.getY()].resetStyle();
						this.cells[x][cell.getY()].setCorrect(false);
					}
					
				}
			}
			
			return false;
		}
		
		
		
		//Cage logic
		if(cell.getCage().isCageFull() == false) {
			if(solver == true) {
//				cell.getCage().setCorrect(false);
				return true;
			}
			if (solver == false) {
				return false;
			}
		}
		else if(cell.getCage().isCageFull() == true) {
			if(!cell.getCage().isCageCorrect() ) {
				return false;
			}
		}
		
		return true;

	}

	
	
	
	//return an array, with pos 0 being x, pos 1 being 1
	public Integer[] getCords(int noobsystem){
		return this.cords.get(noobsystem);
	}
	
	
	
	
	public ArrayList<String> getNumbers(){
		return this.numbers;
	}
	
	
	public Cell[] getRowCells(int rowindex) {
		Cell[] rows = new Cell[this.getDimensions()];
		for(int i = 0; i < rows.length;i++) {
			rows[i] = cells[i][rowindex];
		}
		return rows;
	}
	
	public Cell[] getColumncells(int colindex) {
		Cell[] cols = new Cell[this.getDimensions()];
		for(int i = 0; i < cols.length;i++) {
			cols[i] = cells[colindex][i];
		}
		return cols;
	}
	
	
	public boolean columnDuplicates(int columnindex) {
		Cell[] cols = this.getColumncells(columnindex);
				//sort the array if there are pairs then return true
				//should work-->
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		//Should run in like 2n, rather than n^2
		for(Cell cell: cols) {
			int number = cell.getNumber();
			numbers.add(number);
		}
		Collections.sort(numbers);
		for(int i = 0; i < numbers.size() -1; i++) {
			if(numbers.get(i) == numbers.get(i+1) && numbers.get(i) != 0 && numbers.get(i+1) != 0) {
				return true;
			}
		}
		return false;
	}
	
	
	public boolean rowDuplicates(int rowindex) {
		Cell[] rows = this.getRowCells(rowindex);
		//sort the array if there are pairs then return true
		//should work-->
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		//Should run in like 2n, rather than n^2
		for(Cell cell: rows) {
			int number = cell.getNumber();
			numbers.add(number);
		}
		Collections.sort(numbers);
		for(int i = 0; i < numbers.size() -1; i++) {
			if(numbers.get(i) == numbers.get(i+1) && numbers.get(i) != 0 && numbers.get(i+1) != 0) {
				return true;
			}
		}
		return false;
	}
	

	public Cell getCell(int position) {
		Integer[] proper = this.getCords(position);
		int x = proper[0];
		int y = proper[1];
		return cells[x][y];
	}

	public Cell getCell(int x, int y) {
		return this.cells[x][y];
	}
	
	public void setText(int x, int y, Text text, boolean update, int value) {
		cells[x][y].setText(text, update, value);
	}
	
	public void setSelected(int x, int y) {
		if(this.selectedCell == null) {
			this.selectedCell = this.cells[x][y];
			this.selectedCell.setSelected(true);
		}
		else {
			this.selectedCell.setSelected(false);
			this.selectedCell = this.cells[x][y];
			this.selectedCell.setSelected(true);
		}
	}
	
	public void setFont(String input) {
		for(int x = 0; x < this.dimensions; x++) {
			for(int y = 0; y < this.dimensions;y++) {
				this.cells[x][y].setFont(input);
			}
		}
	}
	
	public boolean isFilled() {
		for(int x = 0; x < this.dimensions; x++) {
			for(int y = 0; y < this.dimensions;y++) {
				if (this.cells[x][y].getNumber() == 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean solved() {
		if(this.isFilled() != true) {
			return false;
		}
		else {
			for(int x = 0; x < this.getDimensions(); x++) {
				for(int y = 0; y < this.getDimensions(); y++) {
					if (validCellInput(cells[x][y], true) != true) {
						return false;
					}
				}
			}
			return true;			
		}
	}
	
	public void shuffleRow(int initial, int end) {
		for(int x= 0; x< this.dimensions; x++) {
			Cell first = this.cells[x][initial];
			Cell second = this.cells[x][end];
			this.getChildren().remove(this.cells[x][initial]);
			this.getChildren().remove(this.cells[x][end]);
			this.cells[x][initial] = second;
			this.add(this.cells[x][initial],x, initial);
			this.cells[x][end] = first; 
			this.add(this.cells[x][end], x, end);
		}
	}
	
	public void shuffleColumn(int initial, int end) {
		for(int y= 0; y< this.dimensions; y++) {
			Cell first = this.cells[initial][y];
			Cell second = this.cells[end][y];
			this.getChildren().remove(this.cells[initial][y]);
			this.getChildren().remove(this.cells[end][y]);
			this.cells[initial][y] = second;
			this.add(this.cells[initial][y], initial,y);
			this.cells[end][y] = first;
			this.add(this.cells[end][y], end, y);
		}
	}
	
	public void win(boolean input) throws InterruptedException {
		if(input == true) {
			Cage temp = this.cells[0][0].getCage();
			temp.setCorrect(true);
		for(int x = 1; x <this.getDimensions(); x++) {
			for(int y= 0; y < this.getDimensions();y++) {
				if(temp != cells[x][y].getCage()) {
					cells[x][y].getCage().setCorrect(true);
				}
			}
		}
		
		//Winning animation 
		
		}
	}
	
	public ArrayList<Integer> adjacentCells(int position) {
		ArrayList<Integer> adjacentCells = new ArrayList<Integer>();
		//starting corner
		if(position == 1 && this.dimensions != 1) {
			adjacentCells.add(2);
			adjacentCells.add((1 +this.dimensions));
			return adjacentCells;
		}
		//corner 1
		if(position == this.dimensions) {
			adjacentCells.add(this.dimensions -1);
			adjacentCells.add(this.dimensions + this.dimensions);
			return adjacentCells;
		}
		
		//quadrant 3 corner
		if(position == ((1+ (this.dimensions * (this.dimensions-1))))){
			adjacentCells.add(position +1);
			adjacentCells.add(position - this.getDimensions());
			return adjacentCells;
		}
		
		//end corner
		if(position == this.dimensions * this.dimensions) {
			adjacentCells.add((this.dimensions * this.dimensions) -1);
			adjacentCells.add((this.dimensions * this.dimensions) -this.dimensions);
			return adjacentCells;
		}
		
		//top border
		for(int n = 0; n < this.dimensions; n++) {
			if(position <= this.dimensions) {
				adjacentCells.add(position -1);
				adjacentCells.add(position +1);
				adjacentCells.add(position + this.getDimensions());
				return adjacentCells;
			}
		}
		
		//right border 
		for(int n = 1; n < this.dimensions; n++) {
			if(position == this.dimensions + (n*this.dimensions)) {
				adjacentCells.add((this.dimensions + (n*this.dimensions)) -1);
				adjacentCells.add((n*this.dimensions)); 
				adjacentCells.add(2*this.dimensions + (n*this.dimensions));
				return adjacentCells;
			}
		}
		
		//left border
		for(int n = 1; n< this.dimensions; n++) {
			if(position == (1+(this.dimensions*n))) {
				adjacentCells.add(position - this.dimensions);
				adjacentCells.add(position + this.dimensions);
				adjacentCells.add(position +1);
				return adjacentCells;
			}
		}
		
		//bottom border
		for(int n = 0; n < this.dimensions;n++) {
			int d = this.dimensions;
			if((position < d * d) && position >= (d*(d-1)+1)) {
				adjacentCells.add(position -1);
				adjacentCells.add(position+1);
				adjacentCells.add(position-d);
				return adjacentCells;
			}
		}
		
		//otherwise
		adjacentCells.add(position - this.dimensions);
		adjacentCells.add(position +1);
		adjacentCells.add(position -1);
		adjacentCells.add(position + this.dimensions);
		
		return adjacentCells;
		
 	}
	
	public void updateGrid() {
		for(int x = 0; x < this.getDimensions(); x++) {
		for(int y = 0; y < this.getDimensions(); y++) {
			int number = this.getCell(x, y).getNumber();
			Text update = new Text(String.valueOf(number));
			this.setText(x,y,update, true, number);	
	}
	}
	}
	
}
