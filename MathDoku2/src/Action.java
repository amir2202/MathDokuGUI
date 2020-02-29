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
	
	public Action(Cell cell,Text oldText, Text newText) {
		this.oldCell = cell;
		this.oldText = oldText;
		this.newText = newText;
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
	
	public void invert() {
		Text temp = this.oldText;
		this.oldText = this.newText;
		this.newText = temp;
	}
}
