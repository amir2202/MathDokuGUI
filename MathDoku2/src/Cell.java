import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
public class Cell extends StackPane{
	private int x;
	private int y;
	private Label label;
	private boolean selected = false;;
	private Cage cage;
	private Text number;
	public Cell(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		this.getStylesheets().add("borders.css");
		this.getStyleClass().add("borders");
		this.setText(new Text(" "));
	}
	
	
	
	public void setLabel(String label) {
		this.label = new Label(label);
	    this.label.setStyle("-fx-font: 14 arial;");
		this.getChildren().add(this.label);
		this.setAlignment(this.label, Pos.TOP_LEFT);
	}
	
	public void removeLabel(){
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
	
	
//	public Action setText(String text) {
//		Text newtext = new Text(text);
//		this.getChildren().remove(this.getText());
//		Action action = new Action(this, this.getText(), newtext);
//		this.number = newtext;
//		this.getChildren().add(this.number);
//		return action;
//	}
	
	public Action setText(Text newtext) {
		if(this.getChildren().contains(this.getText())) {
			this.getChildren().remove(this.getText());
		}
		Action action = new Action(this, this.getText(), newtext);
		this.number = newtext;
		this.getChildren().add(this.number);
		return action;
	}
	
	public void cellThickenBorder() {
		this.setStyle("-fx-border-width: 2.5 2.5 2.5 2.5");
	}
	
	public void setCage(Cage cage) {
		this.cage = cage;
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
	
	public void setFont(String size) {
		if(size.equals("small")) {
			
		}
		if(size.equals("medium")) {
			
		}
		if(size.equals("large")) {
			
		}
	}
	
}
