import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import javafx.scene.text.Text;

public class Generator {

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
		ArrayList<Integer> pals = new ArrayList<Integer>();
		for(int position = 1; position <= grid.getDimensions() * grid.getDimensions();position++) {
			//if a cell is not occupied
			if(grid.getCell(position).getOccupied() == false) {
				cageCells.add(position);
				grid.getCell(position).setOccupied(true);
				
				//adding neighbours to pals array
				pals.addAll(grid.adjacentCells(position));
				//how many neighbours will be chosen
				int howmany = random.nextInt(pals.size());
				while(howmany == 0) {
					howmany = random.nextInt(pals.size());
				}
//				System.out.println(howmany);
				//for each neighbour 
				for(int j = 0; j < howmany;j++) {
					if(grid.getCell(pals.get(j)).getOccupied() == false) {
						cageCells.add(pals.get(j));	
						grid.getCell(pals.get(j)).setOccupied(true);
						pals.remove(pals.get(j));
					}
				}
				int more = random.nextInt(pals.size());
				for(int l = more; l>=0; l--) {
					if(grid.getCell(pals.get(l)).getOccupied() == false) {
					cageCells.add(pals.get(l));
					grid.getCell(pals.get(l)).setOccupied(true);
					pals.remove(pals.get(l));
					}
				}
				
			}
			pals.clear();
			int[] args = new int[cageCells.size()];
			for(int m = 0; m < cageCells.size();m++) {
				args[m] = cageCells.get(m);			
			}
			setupCage(grid, args,random);

			cageCells.clear();
		}
	
				
	}
	public void setupCage(Grid grid, int[] args, Random random) {
		int operator = random.nextInt(2);

		if(args.length == 1) {
			grid.setCage(String.valueOf(grid.getCell(args[0]).getNumber()), args[0]);
			operator = -1;
		}
		//plus
		if(operator == 0 && args.length != 0) {
		int result = 0;
		for(int cagearg: args) {
			result += grid.getCell(cagearg).getNumber();
		}	
		grid.setCage(result+"+", args);
		}
		
		//multipliation
		
		else if(operator == 1 && args.length != 0){
		int result = 1;
		for(int cagearg:args) {
			result *= grid.getCell(cagearg).getNumber();
		}
		grid.setCage(result+"x", args);
		}
		
	}
}