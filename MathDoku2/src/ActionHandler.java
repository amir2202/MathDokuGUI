import java.util.Stack;

//Dealing with actions/undo redo
public class ActionHandler {
	private Stack<Action> undo;
	private Stack<Action> redo;
	public ActionHandler() {
		undo = new Stack<Action>();
		redo = new Stack<Action>();	
	}
	
	public boolean notify(Action action) {
		if(redo.isEmpty() == false) {
			redo.clear();
			return true;
		}
		this.addUndo(action);
		return false;
	}
	public void addUndo(Action action) {
		undo.add(action);
	}
	
	public boolean isRedoEmpty() {
		return this.redo.isEmpty();
	}
	
	public boolean isUndoEmpty() {
		return this.undo.isEmpty();
	}
	
	public void addRedo(Action action) {
		redo.add(action);
	}
	public void undo() {
		Action tofix = this.undo.pop();
		if(tofix.getOldText().getText() == " " || tofix.getOldText().getText() == "") {
			tofix.getCell().setText(tofix.getOldText(),true, 0);
		}
		else {
			tofix.getCell().setText(tofix.getOldText(),true, Integer.valueOf(tofix.getOldText().getText()));
		}
		tofix.getCell().resetStyle();
		this.addRedo(tofix);
	}
	
	public boolean redoEmpty() {
		if(this.redo.isEmpty()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean undoEmpty() {
		if(this.undo.isEmpty()) {
			return true;
		}
		else {
			return false;
		}
	}

	public void redo() {
		Action tofix = this.redo.pop();
		this.addUndo(tofix);
		Cell celltofix = tofix.getCell();
		int temp;
		if(celltofix.getText().getText() == "" || celltofix.getText().getText() == " ") {
			temp = Integer.valueOf(tofix.getnewText().getText());
		}
		else {
			temp = 0;
		}
		
		if(celltofix.getCorrect() == null) {
			celltofix.setText(tofix.getnewText(),true, temp);
		}
		else if(celltofix.getCorrect() != null) {
			celltofix.setText(tofix.getnewText(),true, temp);
//			celltofix.setCorrect(!celltofix.getCorrect());
		}
	}
	
	public void reset() {
		this.undo.clear();
		this.redo.clear();
	}
}
