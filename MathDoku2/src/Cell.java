import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
public class Cell extends StackPane{
	private int x;
	private int y;
	private Label label;
	private boolean selected;
	private Cage cage;
	private Text number;
	public Cell(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		this.getStylesheets().add("borders.css");
		this.getStyleClass().add("borders");
	}
	
	
	public void setText(String text) {
		this.number = new Text(text);
		this.getChildren().add(number);
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
	
	public void setText(Text text) {
		this.getChildren().remove(this.getText());
		this.number = text;
		this.getChildren().add(this.number);
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
		this.selected = i;
	}
}

