import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import javafx.scene.text.Text;

public class Generator {
	//stuff to add -> merge single cells/for higher difficulty
	//ensure unique solution

	public Generator() {
		
	}
	public boolean validCell(Grid grid,Cell cell) {
		if(grid.columnDuplicates(cell.getX()) || grid.rowDuplicates(cell.getY()) || cell.getNumber() == 0) {
			return false;
		}
		return true;
	}
	
	
	public void generateSodukoGrid(int difficulty, Grid grid) {
		grid.clearCells();
		int index = 1;
		while(index <= grid.getDimensions() * grid.getDimensions()) {
			Cell current = grid.getCell(index);
			current.increaseCell();
			if(current.getNumber() == grid.getDimensions() +1) {
				current.setNumber(0);
			}
			
			while(current.getNumber() != 0 && (validCell(grid ,current) == false)) {
				current.increaseCell();
				if(current.getNumber() == grid.getDimensions() +1) {
					current.setNumber(0);
				}
			}
			
			if (current.getNumber() == 0) {
				index--;					
			}
			else {
				index++;
			}
			
			
		}
		Random random = new Random();
		//so now random algorithm
		for(int shuffle = 0;shuffle < 20; shuffle++) {
			int one = random.nextInt(grid.getDimensions() -1);
			int two = random.nextInt(grid.getDimensions() -1);
			if(one != two) {
				grid.shuffleColumn(one, two);
			}
			int three = random.nextInt(grid.getDimensions() -1);
			int four = random.nextInt(grid.getDimensions() -1);
			if(three != four) {
				grid.shuffleRow(three, four);
			}
		}
		
		
		
		
		
		
		
		
		
		
		generateCages(difficulty, grid);
		//leave in temporarily 
//		grid.updateGrid();
	}
	public void generateCages(int difficulty, Grid grid) {
		//Need a list of initial positions
		//+, -, x or ÷
		//0 = +
		// 1 = -
		// 2 = x
		// 3 = ÷
		Random random = new Random();
		ArrayList<Integer> cageCells = new ArrayList<Integer>();
		ArrayList<Integer> availableCells = new ArrayList<Integer>();
		for(int position = 1; position <= grid.getDimensions() * grid.getDimensions();position++) {
			availableCells.add(position);
		}
		//rewrite this algorithm
		while(availableCells.size() != 0) {
			//chosing a random position
			int index;
			if(availableCells.size() == 1) {
				int[] oneel = new int[1];
				oneel[0] = availableCells.get(0);
				System.out.println("Setting cage at");
				System.out.println(availableCells.get(0));
				setupCage(grid, oneel, random);
				break;
			}
			else {
				index = random.nextInt(availableCells.size()-1);
			}
			
			//if cell is not occupied
			ArrayList<Integer> neighbours = new ArrayList<Integer>();
			if(grid.getCell(availableCells.get(index)).getOccupied() == false) {
				cageCells.add(availableCells.get(index));
				grid.getCell(availableCells.get(index)).setOccupied(true);
				neighbours.addAll(grid.adjacentCells(availableCells.get(index)));
				availableCells.remove(Integer.valueOf(availableCells.get(index)));
				int numberofneighbours = random.nextInt(neighbours.size());
				//
				
				for(int i = 0; i<numberofneighbours;i++) {
					int neighbourposition = neighbours.get(i);
					//chose at max 2 neighbours of neighbours
					if(grid.getCell(neighbourposition).getOccupied() == false) {
						cageCells.add(neighbourposition);
						grid.getCell(neighbourposition).setOccupied(true);
						//brea
						//only for difficult maybe do this??
						
						ArrayList<Integer> more = new ArrayList<Integer>();
						more.addAll(grid.adjacentCells(neighbourposition));
						int morestuff = random.nextInt(more.size());
						for(int j = 0; j < morestuff; j++) {
							if(grid.getCell(more.get(j)).getOccupied() == false) {
								cageCells.add(more.get(j));
								grid.getCell(more.get(j)).setOccupied(true);
								availableCells.remove(Integer.valueOf(more.get(j)));
							}
						}
						more.clear();
						
						
						availableCells.remove(Integer.valueOf(neighbourposition));
					}
				}
				
				
				System.out.println("Setting cage at");
				int[] pass = new int[cageCells.size()];
 				for(int i = 0; i < pass.length;i++) {
 					pass[i] = cageCells.get(i);
 					System.out.println(cageCells.get(i));
 				}
 				Arrays.sort(pass);
				setupCage(grid, pass, random);
 				cageCells.clear();
				//loop through neighbours
				//
				//get the cell neighbours
				
			}
		}

	}
	
	
	
	public void setupCage(Grid grid, int[] args, Random random) {
		int operator = random.nextInt(4);
		if(args.length == 0) {return;}
		if(args.length == 1) {
			grid.setCage(String.valueOf(grid.getCell(args[0]).getNumber()), args[0]);
			return;
		}
		//plus
		if(operator == 0) {
		int result = 0;
		for(int cagearg: args) {
			result += grid.getCell(cagearg).getNumber();
		}	
		grid.setCage(result+"+", args);
		return;
		}
		
		//multipliation
		
		else if(operator == 1){
		int result = 1;
		for(int cagearg:args) {
			result *= grid.getCell(cagearg).getNumber();
		}
		grid.setCage(result+"x", args);
		return;
		}
		
		else if(operator == 2) {
			int numbers[] = new int[args.length];
			for(int i = 0; i<args.length;i++) {
				numbers[i] = grid.getCell(args[i]).getNumber();
			}
			Arrays.sort(numbers);
			int result = numbers[numbers.length -2];
			for(int j = numbers.length -2; j >= 0; j--) {
				result -= numbers[j];
			}
			if(result == 0 || result < 0) {
				setupCage(grid, args,random);
			}
			else {
				for(int x:args) {System.out.println(x);}
				grid.setCage(result+"-", args);
			}
			return;
 		}
		
		else if(operator == 3) {
			int numbers[] = new int[args.length];
			for(int i = 0; i<args.length;i++) {
				numbers[i] = grid.getCell(args[i]).getNumber();
			}
			Arrays.sort(numbers);
			double result = numbers[numbers.length -1];
			for(int j = numbers.length -2; j >= 0; j--) {
				result /= numbers[j];
			}
			if(result == 0 || result < 0 || (result % 1 != 0)) {
				setupCage(grid, args,random);
			}
			else {
				int real = (int) result;
				grid.setCage(real+"÷", args);
			}
			return;
		}
		
		
		
		
	}
}