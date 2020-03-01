import java.util.Stack;

import javafx.scene.text.Text;

/**
 * Change class, whenever a change/or action is made this is initialised
 * @author amirghafghazi
 *
 */
public class Action {
	private Cell oldCell;
	private Text oldText;
	private Text newText;
	private Boolean correct;
	
	public Action(Cell cell,Text oldText, Text newText) {
		this.oldCell = cell;
		this.oldText = oldText;
		this.newText = newText;
	}
	
	public Action(Cell cell,Text oldText, Text newText, Boolean correct) {
		this.oldCell = cell;
		this.oldText = oldText;
		this.newText = newText;
		this.correct = correct;
	}
	
	public Cell getCell() {
		return this.oldCell;
	}
	
	public Text getOldText() {
		return this.oldText;
	}
	
	public Text getnewText() {
		return this.newText;
	}
	
	public Boolean getCorrect() {
		return this.correct;
	}
	
	public void invert() {
		Text temp = this.oldText;
		this.oldText = this.newText;
		this.newText = temp;
	}
}
