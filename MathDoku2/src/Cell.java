import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
public class Cell extends StackPane{
	private int x;
	private int y;
	//Arraylist to store possible solutions
	private ArrayList<Integer> possible = new ArrayList<Integer>();
	private Label label;
	private boolean occupied = false;
	private boolean selected = false;
	private Cage cage;
	private Boolean correct;
	private Boolean highlighted = false;
	private Text number;
	private int numberOfText;
	public Cell(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		this.correct = true;
		this.getStylesheets().add("borders.css");
		this.getStyleClass().add("borders");
		this.setText(new Text(" "),true, 0);
		this.setNumber(0);
	}
	
	
	
	public void setLabel(String label) {
		this.label = new Label(label);
	    this.label.getStyleClass().add("small");
		this.getChildren().add(this.label);
		this.setAlignment(this.label, Pos.TOP_LEFT);
	}
	
	public Label getLabel() {
		return this.label;
	}
	
	//dont call this
	public void removeLabel(){
		this.getChildren().remove(this.label);
		this.label = null;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public Text getText() {
		return this.number;
	}
	
	
	public Action setText(Text newtext, boolean updateGrid, int actualvalue) {
		if(updateGrid == true) {
			if(this.getChildren().contains(this.getText())) {
				this.getChildren().remove(this.getText());
			}
			if(this.correct == null) {
				Action action = new Action(this, this.getText(), newtext);
				this.number = newtext;
				this.numberOfText = actualvalue;
				this.getChildren().add(this.number);
				return action;
			}
			else if(this.correct != null) {
				Action action = new Action(this, this.getText(), newtext, correct);
				this.number = newtext;
				this.numberOfText = actualvalue;
				this.getChildren().add(this.number);
				return action;
			}
			return null;
		}
		if(updateGrid == false) {
			this.number = newtext;
			this.numberOfText = actualvalue;
			
		}
		return null;
	}
	
	public String getStringText() {
		return this.getText().getText();
	}
	
	public void cellThickenBorder() {
		this.setStyle("-fx-border-width: 2.5 2.5 2.5 2.5");
	}
	
	public void setCage(Cage cage) {
		if(cage == null) {
			this.cage = null;
		}
		else {
			this.cage = cage;
			this.cage.addCell(this);	
		}
		
	}
	
	public Cage getCage() {
		return this.cage;
	}
	
	public boolean getSelected() {
		return this.selected;
	}
	
	public void setSelected(boolean i) {
		if (i == true && this.selected == false) {

			this.getStyleClass().remove("borders");
			this.getStyleClass().add("selected");
		}
		if (i == false) {
			this.getStyleClass().remove("selected");
			this.getStyleClass().add("borders");
			this.selected = false;
		}
	}
	
	//start with labels
	public void setFont(String size) {
		if(size.equals("small")) {
			if(this.label != null) {
				this.label.getStyleClass().clear();
			    this.label.getStyleClass().add("small");	
			}
			this.getStyleClass().clear();
			this.getStyleClass().add("borders");
			this.getStyleClass().add("small");
		    
		}
		if(size.equals("medium")) {
			if(this.label != null) {
				this.label.getStyleClass().clear();
			    this.label.getStyleClass().add("medium");	
			}
			this.getStyleClass().clear();
			this.getStyleClass().add("borders");
		    this.getStyleClass().add("medium");
		}
		if(size.equals("large")) {
			if(this.label != null) {
				this.label.getStyleClass().clear();
			    this.label.getStyleClass().add("large");	
			}
			this.getStyleClass().clear();
			this.getStyleClass().add("borders");
		    this.getStyleClass().add("large");
		}
	}
	
	public int getNumber() {
		return this.numberOfText;
	}
	
	public void increaseCell() {
		this.numberOfText = this.numberOfText + 1;
	}
	
	public Action changeCell(boolean increase, int limit) {
		if(increase== true && this.numberOfText < limit) {
			this.numberOfText++;
			Action action = this.setText(new Text(String.valueOf(this.numberOfText)), true, this.numberOfText);
			return action;
		}
		else if(increase == false && this.numberOfText > 1) {
			this.numberOfText--;
			Action action = this.setText(new Text(String.valueOf(this.numberOfText)), true, this.numberOfText);
			return action;
		}
		return null;
	}
	
	public void setNumber(int number) {
		this.numberOfText = number;
	}

	
	public void setCorrect(boolean input) {
		if(input == true) {
			this.getStyleClass().add("correct");
			this.correct = true;
		}
		if(input == false) {
			this.getStyleClass().add("wrong");
			this.correct = false;
		}
	}
	
	public Boolean getCorrect() {
		return this.correct;
	}
	
	public void resetStyle() {
		this.getStyleClass().clear();
		this.getStyleClass().add("borders");
	}
	
	public void refreshNumber() {
		//do this
	}
	
	public boolean getHighlighted() {
		return this.highlighted;
	}
	
	public void setHighlighted(boolean choice) {
		if(choice == true) {
			this.getStyleClass().add("highlight");	
			this.highlighted = true;
		}
		if(choice == false) {
			this.getStyleClass().remove("highlight");
		}
	}
	
	public void addPossible(int possible) {
		this.possible.add(possible);
	}
	
	public void removePossible(int possible) {
		this.possible.remove(possible);
	}
	
	public ArrayList<Integer> getPossible() {
		return this.possible;
	}
	
	public ArrayList<Integer> getDeadlyPattern(){
		return null;
	}
	
	public boolean getOccupied() {
		return this.occupied;
	}
	
	public void setOccupied(boolean input) {
		this.occupied = input;
	}
	
}

