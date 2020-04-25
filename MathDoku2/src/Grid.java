import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
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
	public int hints = 3; 
	private ArrayList<Integer> givenhints; 
	private HashMap<String, Integer> positions;
	private int solutions = 0;
	private Cell selectedCell;
	private boolean solved = false;
//	private HashMap<Integer,Integer> solution;
	private ArrayList<Integer[]> solution;
	private ArrayList<String> numbers;
	private ArrayList<ArrayList<Cell>> swappablecells;
	private ArrayList<Cage> allcages; 
	private HashMap<Integer,Integer[]> cords;
	private String fontSize = "small";
	public Grid(int dimensions) {
		super();
		this.allcages = new ArrayList<Cage>();
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
				cells[i][j].setGrid(this);
				cells[i][j].addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>() {
					public void handle(MouseEvent arg0) {
						if(arg0.getEventType() == MouseEvent.MOUSE_CLICKED) {
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
				this.cells[i][j].setText(new Text(" "),true, 0);
				this.cells[i][j].resetStyle();
//				this.cells[i][j].setCage(null);
			}
		}
	}
	
	public void setCage(String label, int... args) {
		if (args.length == 1) {
			Cage single = new Cage(label, args);
			this.allcages.add(single);
			Integer[] coordinates = this.getCords(args[0]);
			int x = coordinates[0];
			int y = coordinates[1];
			cells[x][y].setLabel(label,true);
			this.cells[x][y].setCage(single);
			cells[x][y].cellThickenBorder();
		}

		//now for args 
		else if(args.length > 1) {
			Cage multiple = new Cage(label,args);
			this.allcages.add(multiple);
			for(int i = 0; i < args.length;i++) {
				if(i == 0) {
					Integer[] cords = this.getCords(args[i]);
					int x = cords[0];
					int y = cords[1];
					cells[x][y].setLabel(label,true);
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
	
	
	public void setNonGuiCage(String label, int... args) {
		if (args.length == 1) {
			Cage single = new Cage(label, args);
			this.allcages.add(single);
			Integer[] coordinates = this.getCords(args[0]);
			int x = coordinates[0];
			int y = coordinates[1];
//			cells[x][y].setLabel(label,false);
			this.cells[x][y].setCage(single);
		}

		//now for args 
		else if(args.length > 1) {
			Cage multiple = new Cage(label,args);
			this.allcages.add(multiple);
			for(int i = 0; i < args.length;i++) {
				if(i == 0) {
					Integer[] cords = this.getCords(args[i]);
					int x = cords[0];
					int y = cords[1];
//					cells[x][y].setLabel(label,false);
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
	
	//called only for purpose of unique solution
	public boolean validCellSwaps(int x, int y, int x2, int y2, int x3, int y3, int x4, int y4) {
		int cell1oldvalue = this.getCell(x,y).getNumber();
		int cell2oldvalue = this.getCell(x2, y2).getNumber();
		int cell3oldvalue = this.getCell(x3,y3).getNumber();
		int cell4oldvalue = this.getCell(x4,y4).getNumber();
		this.getCell(x, y).setNumber(cell2oldvalue);
		this.getCell(x2, y2).setNumber(cell1oldvalue);
		this.getCell(x3, y3).setNumber(cell4oldvalue);
		this.getCell(x4,y4).setNumber(cell3oldvalue);
		boolean cell1 = this.solvingGrid(this.getCell(x, y));
		boolean cell2 = this.solvingGrid(this.getCell(x2, y2));
		boolean cell3 = this.solvingGrid(this.getCell(x3, y3));
		boolean cell4 = this.solvingGrid(this.getCell(x4, y4));
		this.getCell(x, y).setNumber(cell1oldvalue);
		this.getCell(x2,y2).setNumber(cell2oldvalue);
		this.getCell(x3,y3).setNumber(cell3oldvalue);
		this.getCell(x4, y4).setNumber(cell4oldvalue);
		if(cell1 && cell2 && cell3 && cell4) {
			if(this.swappablecells == null) {
				this.swappablecells = new ArrayList<ArrayList<Cell>>();
			}
			else {
				ArrayList<Cell> pair = new ArrayList<Cell>();
				pair.add(this.getCell(x,y));
				pair.add(this.getCell(x2,y2));
				pair.add(this.getCell(x3,y3));
				pair.add(this.getCell(x4,y4));
				this.swappablecells.add(pair);
			}
		}
		return cell1 && cell2 && cell3 && cell4;
	}
	
	//public void setUpBorders(Cage cage, boolean mistakes)
	//Indicate whether it used for solving purpose
	public boolean validCellInput(Cell cell) {
		//Row column logic 
		boolean valid = true;
		if(cell.getCage().isCageFull() == true) {
			if(!cell.getCage().isCageCorrect()) {
				cell.getCage().setCorrect(false);
				valid = false;
			}
			else {
				cell.getCage().setCorrect(true);
			}
		}
		
		if(columnDuplicates(cell.getX()) == true) {
				for(int y = 0; y < this.dimensions;y++) {
					if(this.cells[cell.getX()][y].getCorrect() != false) {
						this.cells[cell.getX()][y].setHighlighted(true);	
					}
					if(!this.cells[cell.getX()][y].getCorrect() && this.cells[cell.getX()][y].getHighlighted()) {
						this.cells[cell.getX()][y].resetStyle();
						this.cells[cell.getX()][y].setCorrect(false,false);
					}
				}
				valid = false;
		}
		if(rowDuplicates(cell.getY()) == true) {
			valid = false;
				for(int x = 0; x < this.dimensions;x++) {
					if(this.cells[x][cell.getY()].getCorrect() != false) {
						this.cells[x][cell.getY()].setHighlighted(true);	
					}
					if(!this.cells[x][cell.getY()].getCorrect() && this.cells[x][cell.getY()].getHighlighted()) {
						this.cells[x][cell.getY()].resetStyle();
						this.cells[x][cell.getY()].setCorrect(false,false);
					}
					
				}
				valid = false;
			}
			
		return valid;
	}
	
	
	public boolean solvingGrid(Cell cell){
				if(columnDuplicates(cell.getX()) || rowDuplicates(cell.getY())) {
					return false;
				}
				
				//Cage logic
				if(cell.getCage().isCageFull() == false) {
						return true;

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
		int size = cols.length;
		HashSet actual = new HashSet();
		for(int i = 0; i < cols.length;i++) {
			if(cols[i].getNumber() == 0 ) {
				size--;
			}
			else {
				actual.add(cols[i].getNumber());
			}
		}
		if(actual.size() == size) {
			return false;		}
		else {
			return true;
		}
	}
	
	
	public boolean rowDuplicates(int rowindex) {
		Cell[] rows = this.getRowCells(rowindex);
		int size = rows.length;
		HashSet actual = new HashSet();
		for(int i = 0; i < rows.length;i++) {
			if(rows[i].getNumber() == 0) {
				size--;
			}
			else {
				actual.add(rows[i].getNumber());
			}
		}
		if(actual.size() == size) {
			return false;		}
		else {
			return true;
		}
	}
	

	public Cell getCell(int position) {
		if(position > this.getDimensions() * this.getDimensions() || position < 1) {
			System.out.println("Trying to access invalid index at " + position);
			return cells[0][0];
		}
		Integer[] proper = this.getCords(position);
		int x = proper[0];
		int y = proper[1];
		return cells[x][y];
	}

	public Cell getCell(int x, int y) {
		if(x >= this.getDimensions() || y >= this.getDimensions() || x < 0 || y < 0 ){
			return null;
		}
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
		this.fontSize = input;
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
					if (solvingGrid(cells[x][y]) != true) {
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
	
	public boolean win(boolean input) throws InterruptedException {
		if(input == true) {
			return this.animateWin();
		}
		else {
			return false;
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
	
	public static ArrayList<Integer> adjacentCells(int position, int dimension) {
		ArrayList<Integer> adjacentCells = new ArrayList<Integer>();
		//starting corner
		if(position == 1 && dimension != 1) {
			adjacentCells.add(2);
			adjacentCells.add((1 +dimension));
			return adjacentCells;
		}
		//corner 1
		if(position == dimension) {
			adjacentCells.add(dimension -1);
			adjacentCells.add(dimension + dimension);
			return adjacentCells;
		}
		
		//quadrant 3 corner
		if(position == ((1+ (dimension * (dimension))))){
			adjacentCells.add(position +1);
			adjacentCells.add(position - dimension);
			return adjacentCells;
		}
		
		//end corner
		if(position == dimension * dimension) {
			adjacentCells.add((dimension * dimension) -1);
			adjacentCells.add((dimension * dimension) -dimension);
			return adjacentCells;
		}
		
		//top border
		for(int n = 0; n < dimension; n++) {
			if(position <= dimension) {
				adjacentCells.add(position -1);
				adjacentCells.add(position +1);
				adjacentCells.add(position + dimension);
				return adjacentCells;
			}
		}
		
		//right border 
		for(int n = 1; n < dimension; n++) {
			if(position == dimension + (n*dimension)) {
				adjacentCells.add((dimension + (n*dimension)) -1);
				adjacentCells.add((n*dimension)); 
				adjacentCells.add(2*dimension + (n*dimension));
				return adjacentCells;
			}
		}
		
		//left border
		for(int n = 1; n< dimension; n++) {
			if(position == (1+(dimension*n))) {
				adjacentCells.add(position - dimension);
				adjacentCells.add(position + dimension);
				adjacentCells.add(position +1);
				return adjacentCells;
			}
		}
		
		//bottom border
		for(int n = 0; n < dimension;n++) {
			int d = dimension;
			if((position < d * d) && position >= (d*(d-1)+1)) {
				adjacentCells.add(position -1);
				adjacentCells.add(position+1);
				adjacentCells.add(position-d);
				return adjacentCells;
			}
		}
		
		//otherwise
		adjacentCells.add(position - dimension);
		adjacentCells.add(position +1);
		adjacentCells.add(position -1);
		adjacentCells.add(position + dimension);
		
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
	
	public boolean animateWin() {
		KeyFrame frame = new KeyFrame(javafx.util.Duration.millis(250), new EventHandler<ActionEvent>() {
			private int index = 0;
			public void handle(ActionEvent e) {
			allcages.get(index).setCorrect(true);
			index++;
			}
		});
		Timeline timeline = new Timeline(frame);
		timeline.setCycleCount(this.allcages.size());
		timeline.setOnFinished(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent arg0) {
			
			}
			
		});
		timeline.play();
		return true;
		
	}
	
	public ArrayList<Cage> getAllCages(){
		return this.allcages;
	}
	
	public void eraseCages() {
		this.allcages.clear();
	}
	
	public boolean alreadySolved() {
		return this.solved;
	}
	
	public void solved(boolean solved) {
		this.solved = solved;
	}
	
	public void setSolutions(ArrayList<Integer[]> solution) {
		this.solution = solution;
		Integer[] first = this.solution.get(0);
		for(int i = 0; i < this.dimensions * this.dimensions; i++) {
			this.getCell(i+1).setCorrectValue(first[i]);
		}
	}
	
	public void setamountSolutions(int am) {
		this.solutions = am;
	}
	
	public ArrayList<Integer[]> getSolution(){
		return this.solution;
	}
	
	public void increaseSolution() {
		this.solutions++;
	}
	
	public int solutions() {
		return this.solutions;
	}
	

	public void deleteCage(int position) {
		Cell main = this.getCell(position);
		Cage todelete = main.getCage();
		this.allcages.remove(todelete);
		ArrayList<Cell> cells = todelete.getCells();
		cells.get(0).removeLabel();
		for(Cell cell: cells) {
			cell.setCage(null);
		}
	}
	
	public void deleteCage(Cage cage) {
		this.allcages.remove(cage);
		ArrayList<Cell> cells = cage.getCells();
		cells.get(0).removeLabel();
		for(Cell cell: cells) {
			cell.setCage(null);
		}
	}
	
	public ArrayList<ArrayList<Cell>> getSwappableCells(){
		return this.swappablecells;
	}
	
	public void intPosition() {
		int position = 0;
		//FIX THIS PIDERAS CYKA IDI NAHUI
		for(int row = 0; row < this.dimensions; row++) {
			for(int col= 0; col< this.dimensions;col++) {
				position++;
				String cord = String.valueOf(col) + String.valueOf(row);
				this.positions.put(cord, position);
			}
		}
		
	}
	public int getPosition(int x, int y) {
		if(this.positions == null) {
			this.positions = new HashMap<String, Integer>();
			this.intPosition();
		}
		String temp = String.valueOf(x) + String.valueOf(y);
		return this.positions.get(temp);
		
	}
	
	public boolean showHint() {
		int hint = this.generateHint();
		Cell tochange = this.getCell(hint);
		tochange.setNumber(tochange.getCorrectValue());
		tochange.updateText();
		tochange.animateSolution(tochange.getCorrectValue());
		return true;
//		System.out.println("ALREADY SOLVED" + this.solved);

//		
//		if(this.givenhints == null) {
//			Random random = new Random();
//			this.givenhints = new ArrayList<Integer>();
//			int rand = random.nextInt(this.dimensions * this.dimensions);
//			boolean v = false;
//			while(v == false) {
//				if(rand > 0 ) {
//					break;
//				}
//				else {
//					random.nextInt(this.dimensions * this.dimensions);
//				}
//			}
//			
//			this.givenhints.add(rand);
//			Cell cell = this.getCell(rand);
//			cell.setNumber(cell.getCorrectValue());
//			cell.updateText();					
//			return true;
//		}
//		if(this.hints == 0) {
//			return false;
//		}
//		
//		else {
//			System.out.println("executes");
//			Random random = new Random();
//			boolean valid = false;
//			int rand = random.nextInt(this.dimensions * this.dimensions);
//			while(valid == false) {
//				if(rand > 0 ) {
//					break;
//				}
//				else {
//					rand = random.nextInt(this.dimensions * this.dimensions);
//				}
//			}
//			
//			boolean correct = false;
//			while(correct = false) {
//				if(rand == 0) {
//					rand = random.nextInt(this.dimensions * this.dimensions);
//					continue;
//				}
//				else {
//					int chosen = random.nextInt(this.dimensions*this.dimensions);
//						if(this.givenhints.contains(chosen)) {
//							continue;
//						}
//						else {
//							correct = true;
//							Cell cell = this.getCell(rand);
//							cell.setNumber(cell.getCorrectValue());
//							cell.updateText();
//							this.hints--;
//						}
//					}
//				}
//			return true;
//		}
	}
	
	public int generateHint() {
		if(this.solved ==false) {
		ThreadSolve  solve = new ThreadSolve();
		solve.solve(this, false);
		Integer[] sols = solve.getSolutions().get(0);
		for(int i = 0; i < sols.length;i++) {
			this.getCell(i+1).setCorrectValue((sols[i]));
		}
		//maybe do a animation for 3s
		this.solved = true;
	}
		Random random = new Random();
		int rand = random.nextInt((this.dimensions*this.dimensions)+1);
		boolean valid = false;
		while(!valid) {
			if(rand > 0) {
				break;
			}
			else {
				rand = random.nextInt((this.dimensions*this.dimensions)+1);
			}
		}
		
		if (this.givenhints == null) {
			this.givenhints = new ArrayList<Integer>();
		}
		this.givenhints.add(rand);
		
		return rand;
		
		
	}
	
	public String[] getConfig() {
		String config[] = new String[this.allcages.size()];
		for(int i = 0; i < config.length;i++) {
			config[i] = this.allcages.get(i).toString();
		}
		return config;
	}
	
}
