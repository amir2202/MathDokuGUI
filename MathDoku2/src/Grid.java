import java.util.HashMap;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class Grid extends GridPane {
	private int dimensions;
	private Cell[][] cells;
	private Cell selectedCell;
	private HashMap<Integer,Integer[]> cords;
	public Grid(int dimensions) {
		super();
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
							int currentx = current.getX();
							int currenty = current.getY();
							int oldx = selectedCell.getX();
							int oldy = selectedCell.getY();
							if(selectedCell.getText() != null) {
								cells[oldx][oldy].getStyleClass().remove("selected");
								cells[oldx][oldy].getStyleClass().add("borders");
								cells[currentx][currenty].getStyleClass().add("selected");
								selectedCell = current;			

						}
						}
						else if(current.getSelected() == false) {
							int x = current.getX();
							int y = current.getY();
							cells[x][y].getStyleClass().remove("borders");
							cells[x][y].getStyleClass().add("selected");
							selectedCell = current;
							cells[x][y].setSelected(true);
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
				cells[i][j].setText(" ");
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
			Cage multiple = new Cage(label);
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
	
	//return an array, with pos 0 being x, pos 1 being 1
	public Integer[] getCords(int noobsystem){
		return this.cords.get(noobsystem);
	}
}
