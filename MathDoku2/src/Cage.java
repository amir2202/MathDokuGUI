import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import javafx.scene.control.Label;

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
	
	public ArrayList<Cell> getCells() {
		return this.cagecells;
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
	
	public void removeCell(Cell cell) {
		//if cell has label reshift the label(to highest position)
		if(this.cagecells.contains(cell)) {
			String operator = "";
			this.cagecells.remove(cell);
			int pos = cell.getGrid().getPosition(cell.getX(), cell.getY());
			this.removeCordinate(pos);
			if(cell.getLabel() != null) {
				cell.removeLabel();
			}
		}
		else {
		}
	}
	
	public void removeCordinate(int pos) {
		ArrayList<Integer> position = new ArrayList<Integer>();
		for(int x: this.cordinates) {
			if(x != pos) {
			position.add(x);
			}
		}
		int[] removed = new int[position.size()];
		for(int i = 0; i < removed.length; i++) {
			removed[i] = position.get(i);
		}
		this.cordinates = removed;
	}
	
	public Cell getFirstCell() {
		
		int[][] cagecords = new int[this.cagecells.size()][2]; 
		for(int i = 0; i < cagecells.size();i++) {
			Cell temp = cagecells.get(i);
			cagecords[i][0] = temp.getX();
			cagecords[i][1] = temp.getY();
		}
		Arrays.sort(cagecords, Comparator.comparingInt(o -> o[0]));
		int x = cagecords[0][0];
		int y = cagecords[0][1];
		for(int i = 0; i < cagecells.size();i++) {
			Cell temp = cagecells.get(i);
			if(temp.getX() == x && temp.getY() == y) {
				return temp;
			}
		}
		return null;
	}
	
	public ArrayList<Cell> getEdgeOfCage() {
		ArrayList<Cell> edgecells = new ArrayList<Cell>();
		for(Cell cell: this.cagecells) {
			int x = cell.getX();
			Grid grid = cell.getGrid();
			int y = cell.getY();
			if(!this.cagecells.contains(grid.getCell(x+1,y)) || !this.cagecells.contains(grid.getCell(x-1,y)) || !this.cagecells.contains(grid.getCell(x,y-1)) || !this.cagecells.contains(grid.getCell(x-1,y-1)))
			{
				edgecells.add(cell);
			}
		}
		return edgecells;
	}
	
	public static Cage join(Cage ...cage) {
		HashMap<String,Integer> cageparam = new HashMap<String,Integer>();
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		ArrayList<Integer> coordinates = new ArrayList<Integer>();
		for(Cage cages: cage) {
			  for(int x: cages.getCords()) {
				  coordinates.add(x);
				  System.out.println("adding coordinate " + x);
			  }
			  for(Cell cell: cages.getCells()) {
				  numbers.add(cell.getNumber());
			  }
		}
		
		int[] cords = new int[coordinates.size()];
		int[] nrs = new int[coordinates.size()];
		for(int i = 0; i < coordinates.size(); i++) {
			cords[i] = coordinates.get(i);
			nrs[i] = numbers.get(i);
		}
		int div = Generator.divPossible(nrs);
		int sub = Generator.subtractionPossible(nrs);
		if(sub != 0) {
			Cage created = new Cage(String.valueOf(sub)+"-", cords);
			return created;
		}
		//chose new operator here easiest way
		if(div != 0) {
			Cage created = new Cage(String.valueOf(div)+ "÷", cords);
			return created;
		}
		
		else {
			Cage created = new Cage(String.valueOf(Generator.multiply(nrs)) + "x", cords);
			return created;
			
			//random between plus or mult
			
		}
		
	}
	
	
}
