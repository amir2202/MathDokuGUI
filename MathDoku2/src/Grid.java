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
				cells[i][j].setText(new Text(" "));
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
	
	
	
	//return an array, with pos 0 being x, pos 1 being 1
	public Integer[] getCords(int noobsystem){
		return this.cords.get(noobsystem);
	}
	
	public boolean validInput(Cell cell) {
		boolean valid = true;
		int currentx = cell.getX();
		int currenty = cell.getY();
		//work on this later.
//		for(int x = 0; x < this.getDimensions(); x++) {
//			for(int y = 0; y < this.getDimensions(); y++) {
//				if(cells[x][y].getCorrect() == null) {
////					validInput(cells[x][y]);
//					System.out.println(cells[x][y].getX() + cells[x][y].getY());
//				}
//			}
//		}

		//Check columns and rows for duplicates first
		//proof by contradictino method --> assume valid
		
		//Check column
		for(int x = 0; x < this.getDimensions();x++) {
			if(x != currentx && cells[x][currenty].getStringText().equals(cell.getStringText())) {
				valid = false;
				//columnerror = true
			}
		}
		//Check row
		for(int y = 0; y < this.getDimensions();y++) {
			if(y != currenty && cells[currentx][y].getStringText().equals(cell.getStringText())) {
				valid = false;
				//rowerror = true
				//
			}
		}
		Cage cage = cell.getCage();
		String label = cage.getLabel();
		char operator = label.charAt(label.length() -1);
		
		//Algorithm for plus label 
		//cage error
		if(operator == '+') {
			//if input 1 plus dimension - 1 < 
			boolean cagecomplete = true;
			int expected = Integer.valueOf(label.substring(0, label.length() -1));
			ArrayList<Integer> cagenumbers = new ArrayList<Integer>();
			for(int	coordinate: cage.getCords()) {
				Integer[] proper = this.getCords(coordinate);
				int x = proper[0];
				int y = proper[1];
				int number = cells[x][y].getNumber();
				if(cells[x][y].getNumber() == 0) {
					cagecomplete = false;
					break;
				}
				int possible = number + ((cage.getCords().length -1)* this.getDimensions());
				if(possible < expected) {
					valid = false;
				}
				
				cagenumbers.add(cells[x][y].getNumber());
			}
			int result = 0;
			for(Integer value: cagenumbers) {
				result+= value;
			}

			if(result != expected && cagecomplete == true) {
				valid = false;
			}
		}
		
		
		
		
		
//		else if(label[label.length-1] == 'x') {
//			
//		}
		
		else if(operator == '-') {
			boolean cagecomplete = true;
			int expected = Integer.valueOf(label.substring(0, label.length() -1)); 
			ArrayList<Integer> cagenumbers = new ArrayList<Integer>();
			for(int	coordinate: cage.getCords()) {
				Integer[] proper = this.getCords(coordinate);
				int x = proper[0];
				int y = proper[1];
				if(cells[x][y].getNumber() == 0) {
					cagecomplete = false;
					break;
				}
				cagenumbers.add(cells[x][y].getNumber());
			}
			Collections.sort(cagenumbers);
			Collections.reverse(cagenumbers);
			int result = cagenumbers.get(0);
			for(int element = 1; element < cagenumbers.size(); element++){
				result -= cagenumbers.get(element);
			}
			
			if(result != expected && cagecomplete == true) {
				valid = false;
			}
//			System.out.print(result);
			//some sort of recursive method
			
		}
		// work on this 
		else if(operator == '÷') {
			boolean cagecomplete = true;
			int expected = Integer.valueOf(label.substring(0, label.length() -1)); 
			ArrayList<Integer> cagenumbers = new ArrayList<Integer>();
			for(int	coordinate: cage.getCords()) {
				Integer[] proper = this.getCords(coordinate);
				int x = proper[0];
				int y = proper[1];
				if(cells[x][y].getNumber() == 0) {
					cagecomplete = false;
					break;
				}
				cagenumbers.add(cells[x][y].getNumber());
			}
			Collections.sort(cagenumbers);
			Collections.reverse(cagenumbers);
			int result = cagenumbers.get(0);
			for(int element = 1; element < cagenumbers.size(); element++){
				result /= cagenumbers.get(element);
			}
			System.out.print(result);
			if(result != expected && cagecomplete == true) {
				valid = false;
			}
//			System.out.print(result);
			//some sort of recursive method
			
		}
		
		//row and column time complexity 2n together, with n being dimensions (not an issue for the size of this grid)
		
		//for - and plus there is a N! options 
		
		
		return valid;
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
		for(int i = 0; i < cols.length; i++) {
			if(i + 1 < cols.length) {
				if (cols[i].getNumber() == cols[i+1].getNumber()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean rowDuplicates(int rowindex) {
		Cell[] rows = this.getRowCells(rowindex);
		for(int i = 0; i < rows.length; i++) {
			if(i + 1 < rows.length) {
				if (rows[i].getNumber() == rows[i+1].getNumber()) {
					return true;
				}
			}
		}
		return false;
	}

	
	
	
	
	
}
