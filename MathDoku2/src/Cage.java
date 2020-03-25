import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Cage {
	private String label;
	private int[] cordinates;
	private int[] converted;
	private int target;
	private char operator;
	private int expected;
	private boolean checked = false;
	private ArrayList<Cell> cagecells;
	public Cage(String label, int... cordinates) {
		cagecells = new ArrayList<Cell>();
		this.label = label;
		this.cordinates = cordinates;
		this.operator = label.charAt(label.length() -1);
		if(this.label.charAt(this.label.length() -1 ) == '+' || this.label.charAt(this.label.length()-1) == '-' || this.label.charAt(this.label.length()-1) == 'x' || this.label.charAt(this.label.length()-1) == '÷'|| this.label.charAt(this.label.length()-1) == '/' || this.label.charAt(this.label.length()-1) == '*' ) {
			this.expected = Integer.valueOf(label.substring(0, label.length() -1));
		}
		else {
			this.expected = Integer.valueOf(label);
		}
	}
	
	public void addCords(Integer[] cords) {
		
	}
	
	public void setCorrect(boolean input) {
		for(Cell cell: this.cagecells) {
			cell.setCorrect(input);
		}
	}
	
	//Adding cell to cage 
	public void addCell(Cell cell) {
		cagecells.add(cell);
	}
	public int[] getCords() {
		return this.cordinates;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public boolean isCageFull() {
		//get all cagecells
		for(int i = 0; i < this.cagecells.size(); i++) {
			if(this.cagecells.get(i).getNumber() == 0) {
				return false;
			}
		}
		return true;
	}

	//unneeded method
//	
//	public boolean cageDuplicates() {
//		HashSet real = new HashSet();
//		int size = this.cagecells.size();
//		for(Cell cell:this.cagecells) {
//			if(cell.getNumber() == 0) {
//				size--;
//			}
//			else {
//				real.add(cell.getNumber());
//			}
//		}
//		if(size == real.size()) {
//			return false;
//		}
//		else {
//			return true;
//		}
//	}
	
	public boolean isCageCorrect() {
		//
		//if there is no operator?
		if(this.cagecells.size() == 1) {
			int result = Integer.valueOf(this.label);
			if (this.cagecells.get(0).getNumber()!= result) {
				return false;
			}
		}
		
		if(operator == '+') {
			int result = 0;
			for(int i = 0; i < this.cagecells.size(); i++) {
				result += this.cagecells.get(i).getNumber();
			}
			if(result != this.expected) {
				return false;
			}
		}
		
		else if(operator =='x' || operator == '*') {
			int result = 1;
			for(int i = 0; i < this.cagecells.size(); i++) {
				result *= this.cagecells.get(i).getNumber();
			}
			if(result != this.expected) {
				return false;
			}
		}
		
		else if(operator == '-') {
			ArrayList<Integer> cagenumbers = new ArrayList<Integer>();
			for(int i = 0; i < this.cagecells.size();i++) {
				cagenumbers.add(this.cagecells.get(i).getNumber());
			}
			Collections.sort(cagenumbers);
			Collections.reverse(cagenumbers);
			int result = cagenumbers.get(0);
			for(int i = 1; i < cagenumbers.size(); i++) {
				result -= cagenumbers.get(i);
			}
			
			if(result != expected) {
				return false;
			}
		}
	
		else if(operator =='÷' || operator == '/') {
			ArrayList<Integer> cagenumbers = new ArrayList<Integer>();
			for(int i = 0; i < this.cagecells.size(); i++) {
				cagenumbers.add(this.cagecells.get(i).getNumber());
			}
			Collections.sort(cagenumbers);
			Collections.reverse(cagenumbers);
			double result = cagenumbers.get(0);
			for(int i = 1; i < cagenumbers.size(); i++) {
				result /= cagenumbers.get(i); 
			}
			if(result != expected ) {
				return false;
			}
		}
		
		return true; 
		
	}	
	
	public boolean getChecked() {
		return this.checked;
	}
	
	public void setChecked(boolean checked) {
		this.checked = checked; 
	}
	
	public String toString() {
		String numbers = "";
		if(this.cordinates.length == 1) {
			numbers = String.valueOf(this.cordinates[0]);
		}
		else {
			for(int i = 0; i < this.cordinates.length; i++) {
				numbers += String.valueOf(this.cordinates[i]);
				if(i != this.cordinates.length -1) {
					numbers += ",";
				}
			}
		}
		return this.label + " " + numbers; 
	}
	
}
