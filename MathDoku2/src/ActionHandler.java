import java.util.Stack;

//Dealing with actions/undo redo
public class ActionHandler {
	private Stack<Action> undo;
	private Stack<Action> redo;
	public ActionHandler() {
		undo = new Stack<Action>();
		redo = new Stack<Action>();	
	}
	
	public void notify(Action action) {
		this.addUndo(action);
	}
	public void addUndo(Action action) {
		undo.add(action);
	}
	
	public void addRedo(Action action) {
		redo.add(action);
	}
	public void undo() {
		Action tofix = this.undo.pop();
		tofix.getCell().setText(tofix.getOldText());
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
		tofix.getCell().setText(tofix.getnewText());
	}
	
	public void reset() {
		this.undo.clear();
		this.redo.clear();
	}
}