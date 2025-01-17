import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Text;

public class Generator {
	//stuff to add -> merge single cells/for higher difficulty
	//ensure unique solution
	
	//special 2x2 case
	
	private Grid grid;
	public boolean validCell(Grid grid,Cell cell) {
		if(grid.columnDuplicates(cell.getX()) || grid.rowDuplicates(cell.getY()) || cell.getNumber() == 0) {
			return false;
		}
		return true;
	}
	
	
	
	public Grid generate(int dimension,int difficulty, boolean unique) throws InterruptedException {
		Grid created = new Grid(dimension);
		this.generateSodukoGrid(difficulty, created);
		grid = created;
		String[] config = new String[created.getAllCages().size()];
		for(int i = 0; i < config.length; i++) {
			config[i] = created.getAllCages().get(i).toString();
//			System.out.println(config[i]);
		}
		Grid temp = new Grid(dimension);
		for(String cage:config) {
			String[] split = cage.split(" ");
			String label = split[0];
			String[] argum = split[1].split(",");
			if(argum.length == 1) {
				temp.setCage(label, Integer.valueOf(split[1]));
			}
			else {
				int[] arguments = new int[argum.length];
				for(int i = 0;i < arguments.length;i++) {
					arguments[i] = Integer.valueOf(argum[i]);
				}
				temp.setCage(label, arguments);
			}
		}
		

		if(unique) {
			ThreadSolve solve = new ThreadSolve();
			solve.solve(temp,false);
			if(temp.getDimensions() == 2) {
				Grid makeunique = this.makeUnique(temp, 2, 1);
				return makeunique;
				
			}
			if(solve.getSolutions().size() == 1) {
				ArrayList<Integer[]> solutions = solve.getSolutions();
				for(int i = 0; i < solutions.get(0).length;i++) {
					temp.getCell(i+1).setNumber(solutions.get(0)[i]);
				}
				return temp;
			}
			if(solve.getSolutions().size() == 0) {
				this.generate(dimension, difficulty, unique);
			}
			else if(solve.getSolutions().size() > 1) {
				Grid uniquegrid = this.makeUnique(temp,dimension,difficulty);
				return uniquegrid;
			}
			return null;	
		}
		
		else {
			return temp;
		}
	}
	
	public Grid makeUnique(Grid grid, int dimension, int difficulty) {
		//Generate 6 grids if dim is below 7
		if(grid.getDimensions() == 2) {
			ThreadSolve solve = new ThreadSolve();
			solve.solve(grid,false);
			Integer[] sol = solve.getSolutions().get(0);
			boolean uniq = true;
			for(int i = 0; i < sol.length;i++) {
				grid.getCell(i+1).setNumber(sol[i]);
			}
			
			for(Cage cage:grid.getAllCages()) {
				if(cage.getCells().size() == 2) {
					uniq = false;
					this.addSingleCage(grid);
					return grid;
				}
			}
			
			return grid;
		}
		
		
		if(grid.getDimensions() < 7) {
			for(int i = 0; i < 5; i++) {
				Grid uniquegrid = this.tryUnique(dimension,difficulty);
				if(uniquegrid != null) {
					return uniquegrid;
				}
				
			}
		}
		
		
		//otherwise
		boolean unique = false;
		while(!unique) {
//				System.out.println("reaches1");
//				for(Cage cage:grid.getAllCages()) {
//					System.out.println(cage);
//				}
				this.addSingleCage(grid);
//				System.out.println("--------------------------------------");
//				for(Cage cage:grid.getAllCages()) {
//					System.out.println(cage);
//				}
				ThreadSolve solve = new ThreadSolve();
				solve.solve(grid,false);
				if(solve.getSolutions().size() == 1) {
					unique = true;
					ArrayList<Integer[]> solutions = solve.getSolutions();
					for(int i = 0; i < solutions.get(0).length;i++) {
						grid.getCell(i+1).setNumber(solutions.get(0)[i]);
					}
					return grid;
					
				}
		}
		
		
//		this.addSingleCage(grid);
		return null;
	}
	
	
	
	private Grid tryUnique(int dimension, int difficulty) {
		Grid generate = new Grid(dimension);
		this.generateSodukoGrid(difficulty, generate);
		String[] config = new String[generate.getAllCages().size()];
		for(int i = 0; i < config.length; i++) {
			config[i] = generate.getAllCages().get(i).toString();
		}
		
		Grid fresh = new Grid(dimension);
		for(String cage:config) {
			String[] split = cage.split(" ");
			String label = split[0];
			String[] argum = split[1].split(",");
			if(argum.length == 1) {
				fresh.setCage(label, Integer.valueOf(split[1]));
			}
			else {
				int[] arguments = new int[argum.length];
				for(int i = 0;i < arguments.length;i++) {
					arguments[i] = Integer.valueOf(argum[i]);
				}
				fresh.setCage(label, arguments);
			}
		}
		
		ThreadSolve solve = new ThreadSolve();
		solve.solve(fresh,true);
		ArrayList<Integer[]> solutions = solve.getSolutions();
		if(solutions.size() == 1) {
			return fresh;
		}
		else {
			return null;	
		}
		
	}
	
	
	
	public boolean addSingleCage(Grid grid) {
//		for(Cage cage: grid.getAllCages()) {
//			System.out.println("CAGE EDGECELLS " + cage.getEdgeOfCage());
//		}
//		return false;
		Random random = new Random();
//		for(Cage stuff2: grid.getAllCages()) {
//			System.out.println(stuff2);
//		}
		//infintie loop deal with later
		Cage cage = null;
		for(int i = 0; i < grid.getAllCages().size();i++) {
			cage = grid.getAllCages().get(i);
					if(cage.getEdgeOfCage().size() > 0 && cage.getCords().length > 1) {
						break;
					}
					else {
						cage = null;
					}
		
		}

		if(cage == null) {
			return false;
		}
		ArrayList<Cell> replace = cage.getEdgeOfCage();
		Cell delete = cage.getEdgeOfCage().get(cage.getEdgeOfCage().size() -1);
		int pos = grid.getPosition(delete.getX(), delete.getY());
		int[] args = new int[1];
		args[0] = pos;
		cage.removeCell(delete);
		int[] args2 = cage.getCords();
		grid.deleteCage(cage);
		this.setupCage(grid, args, new Random(), 1);
		this.setupCage(grid, args2, new Random(), 1);
		
//		for(Cage stuff: grid.getAllCages()) {
//			System.out.println(stuff);
//		}
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
	}
	
	
	public void generateSodukoGridSpecial(int difficulty, Grid grid) {
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
		
		
		
		
		
		
		
		
		
		//leave in temporarily 
	} 
	
	private void generateCages(int difficulty, Grid grid) {
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
//				System.out.println("Setting cage at");
//				System.out.println(availableCells.get(0));
				setupCage(grid, oneel, random,difficulty);					
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
				
				
//				System.out.println("Setting cage at");
				if(cageCells.size() == 1) {
					//do nothing if difficulty is 0(easy)
					//intermediate (merge half of the cages)
					if(difficulty == 1) {
						int fifty = random.nextInt(2);
						if(fifty == 1) {
							neighbours.clear();
							neighbours.addAll(grid.adjacentCells(cageCells.get(0)));
							for(int neighbour: neighbours) {
								if(grid.getCell(neighbour).getOccupied() == false) {
									cageCells.add(neighbour);
									grid.getCell(neighbour).setOccupied(true);
									availableCells.remove(Integer.valueOf(neighbour));
								}
							}
						}
					}
					
					//hard (merge all possible single cells)
					else if(difficulty >=2) {
						neighbours.clear();
						neighbours.addAll(grid.adjacentCells(cageCells.get(0)));
						for(int neighbour: neighbours) {
							if(grid.getCell(neighbour).getOccupied() == false) {
								cageCells.add(neighbour);
								grid.getCell(neighbour).setOccupied(true);
								availableCells.remove(Integer.valueOf(neighbour));
							}
						}
					}
				}
				
				
				int[] pass = new int[cageCells.size()];
 				for(int i = 0; i < pass.length;i++) {
 					pass[i] = cageCells.get(i);
// 					System.out.println(cageCells.get(i));
 				}
 				Arrays.sort(pass);
				setupCage(grid, pass, random,difficulty);
 				cageCells.clear();
 				neighbours.clear();

				
			}
		}
		
		//if guru (make bigger cages)
		
		for(int position = 1; position <= grid.getDimensions() * grid.getDimensions(); position++) {
			int corr = grid.getCell(position).getNumber();
			grid.getCell(position).setCorrectValue(corr);
		}
		grid.clearCells();

	}
	
	
	
	private void setupCage(Grid grid, int[] args, Random random, int difficulty) {
		//rework --> check if division/subtraction possible IF YES CHOSE IT(apart from easy mode)
//		int operator = random.nextInt(4);
		
		
		int operator = random.nextInt(4);
		
		
		
		
		
		//rework later 
		///maybe add stuff in depending for difficulties
//		boolean subtract = this.subtractionPossible(grid, args);
//		boolean division = this.divisionPossible(grid, args);
//		if(subtract && division) {
//			int subtraction = random.nextInt(2);
//			if(subtraction == 1) {
////				operator = 4;
//				operator = 3;
//			}
//			else if(subtraction == 0) {
//				operator = 3;
//			}
//		}
		
		if(args.length == 0) {return;}
		if(args.length == 1) {
			grid.setCage(String.valueOf(grid.getCell(args[0]).getNumber()), args[0]);
			return;
		}
		
		//adding
		if(difficulty > 1) {
			int[] nrs = new int[args.length];
			for(int i = 0; i < args.length;i++) {
				nrs[i] = grid.getCell(args[i]).getNumber();
			}
			if(this.subtractionPossible(nrs) != 0 || this.divPossible(nrs) != 0) {
				int sub = random.nextInt(2);
				if(sub == 0) {
					operator = 2;
				}
				else {
					operator = 3;
				}
			}
		}
		
		
		
		
		
		
		
		
		//
		
		//division/subtraction possible
		
		
		
		
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
			int result = numbers[numbers.length -1];
			for(int j = numbers.length -2; j >= 0; j--) {
				result -= numbers[j];
			}
			if(result == 0 || result < 0) {
				setupCage(grid, args,random,difficulty);
			}
			else {
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
				setupCage(grid, args,random,difficulty);
			}
			else {
				int real = (int) result;
				grid.setCage(real+"÷", args);
			}
			return;
		}
		
		
		
		
	}
	
	public static int subtractionPossible(int[] numbers) {
		Arrays.sort(numbers);
		int result = numbers[numbers.length -1];
		for(int i = numbers.length -2; i >= 0;i--) {
			result -= numbers[i];
		}
		if(result > 0) {
			return result;
		}
		else {
		return 0;
		}
	}
	
	public static int divPossible(int[] numbers) {
		Arrays.sort(numbers);
		int initial = numbers[numbers.length-1];
		for(int i = numbers.length -2; i >= 0; i--) {
			if(initial % numbers[i] != 0) {
				return 0;
			}
			else {
				initial /= numbers[i];
			}
		}
		return initial;
	}
	
	public static int multiply(int[] numbers) {
		int result = numbers[0];
		for(int i = 1; i < numbers.length; i++) {
			result *= numbers[i];
		}
		return result;
	}
	

	
	
	//find deadly pattern given a grid
//	private boolean multipleSolution(Grid grid) {
//		int dim = grid.getDimensions();
//		boolean multiple = false;
//		
//		for(int col = 0; col < dim; col++) {
//			int[][][] pairs = this.getColPairs(col, grid);
//			for(int col2 = 0; col2 < dim;col2++) {
//				if(col == col2) {
//					continue;
//				}
//				int[][][] pairs2 = this.getColPairs(col2, grid);
//				if(this.validInversePairExist(pairs, pairs2,grid,false)) {
//					multiple = true;
//				}
//				
//			}
//		}
//		
//		//now for row
//		for(int row = 0; row < dim; row++) {
//			int[][][] pairs = this.getRowPairs(row, grid);
//			for(int row2 = 0; row2 < dim;row2++) {
//				if(row == row2) {
//					continue;
//				}
//				int[][][] pairs2 = this.getRowPairs(row2, grid);
//				if(this.validInversePairExist(pairs, pairs2,grid,false)) {
//					multiple = true;
//				}
//				
//			}
//		}
//		return multiple;
//	}
	
//	public boolean multipleSolution(Grid grid,boolean gui) {
//		int dim = grid.getDimensions();
//		boolean multiple = false;
//		
//		for(int col = 0; col < dim; col++) {
//			int[][][] pairs = this.getColPairs(col, grid);
//			for(int col2 = 0; col2 < dim;col2++) {
//				if(col == col2) {
//					continue;
//				}
//				int[][][] pairs2 = this.getColPairs(col2, grid);
//				if(this.validInversePairExist(pairs, pairs2,grid,gui)) {
//					multiple = true;
//				}
//				
//			}
//		}
//		
//		//now for row
//		for(int row = 0; row < dim; row++) {
//			int[][][] pairs = this.getRowPairs(row, grid);
//			for(int row2 = 0; row2 < dim;row2++) {
//				if(row == row2) {
//					continue;
//				}
//				int[][][] pairs2 = this.getRowPairs(row2, grid);
//				if(this.validInversePairExist(pairs, pairs2,grid,gui)) {
//					multiple = true;
//				}
//				
//			}
//		}
//		return multiple;
//	}
	

	
//	private int[][][] getColPairs(int x, Grid grid) {
//		int dim = grid.getDimensions();
////		Cell collcells[] = grid.getColumncells(index);
//		int[][][] pairs = new int[dim * (dim -1)][2][3];
//		int pair = 0;
//		for(int rowindex = 0; rowindex < dim; rowindex++) {
//			for(int row2 = 0; row2 < dim; row2++) {
//				if(rowindex == row2) {
//					continue;
//				}
//				pairs[pair][0][0] = grid.getCell(x, rowindex).getCorrectValue();
//				pairs[pair][0][1] = x;
//				pairs[pair][0][2] = rowindex;
//				pairs[pair][1][0] = grid.getCell(x, row2).getCorrectValue();
//				pairs[pair][1][1] = x;
//				pairs[pair][1][2] = row2;
//				//pairs are added in same order so, can compare in same
//				//eg if pair 0 --> then check if 1 is equal to the 0 of the other
//				pair++;
//			}
//		}
//		
//		return pairs;
//	}
	
//	
//	public static int[][][] getRowPairs(int index, Grid grid) {
//		int dim = grid.getDimensions();
//		int[][][] pairs = new int[dim * (dim -1)][2][3];
//		int pair = 0;
//		for(int rowelement = 0; rowelement < dim; rowelement++) {
//			for(int rowelement2 = 0; rowelement2 < dim; rowelement2++) {
//				if(rowelement == rowelement2) {
//					continue;
//				}
//				
//				pairs[pair][0][0] = grid.getCell(index,rowelement).getCorrectValue();
//				pairs[pair][0][1] = rowelement;
//				pairs[pair][0][2] = index;
//				pairs[pair][1][0] = grid.getCell(index,rowelement2).getCorrectValue();
//				pairs[pair][1][1] = rowelement2;
//				pairs[pair][1][2] = index;
//				pair++;
//			}
//		}
//		
//		return pairs;
//	}
	
	
//	private boolean validInversePairExist(int[][][] pair1, int[][][] pair2, Grid grid,boolean gui) {
//		boolean multiplesolution = false;
//		for(int i = 0; i < pair1.length; i++) {
//			int p1element1 = pair1[i][0][0];
//			int p1el1cordx = pair1[i][0][1];
//			int p1el1cordy = pair1[i][0][2];
//			int p1element2 = pair1[i][1][0];
//			int p1el2cordx = pair1[i][1][1];
//			int p1el2cordy = pair1[i][1][2];
//			
//			int p2element1 = pair2[i][0][0];
//			int p2el1cordx = pair2[i][0][1];
//			int p2el1cordy = pair2[i][0][2];
//			int p2element2 = pair2[i][1][0];
//			int p2el2cordx = pair2[i][1][1];
//			int p2el2cordy = pair2[i][1][2];
//
//			if((p1element2 == p2element1) &&(p1el2cordy == p2el2cordy) && (p1el1cordy == p2el1cordy)){
//				// uneed to update the other cell value somehow and revert that too
//				boolean swap = grid.validCellSwaps(p1el1cordx, p1el1cordy, p2el1cordx, p2el1cordy,p1el2cordx, p1el2cordy, p2el2cordx, p2el2cordy);
//				if(swap) {
//					Random rand = new Random();
//					if(gui) {
//					String color1 = "-fx-background-color: rgba(" + rand.nextInt(256) + "," + rand.nextInt(256) + "," + rand.nextInt(256) + ",1)";
//					grid.getCell(p1el1cordx, p1el1cordy).appendStyle(color1);
//					grid.getCell(p2el1cordx, p2el1cordy).appendStyle(color1);
//					grid.getCell(p1el2cordx, p1el2cordy).appendStyle(color1);
//					grid.getCell(p2el2cordx, p2el2cordy).appendStyle(color1);
//					}
//					grid.increaseSolution();
//					multiplesolution = true;
//					//get cage 
//					//remove cage method
//					//remove cell from cage?
//				}
//				
//			}
//			
//
//		}
//		return multiplesolution;
//	}
	
	
//	public void makeUnique() {
//		ArrayList<ArrayList<Cell>> swappable = grid.getSwappableCells();
//		for(ArrayList<Cell> lists: swappable) {
//			if(this.makeUnswappable(lists)) {
//			};
//		}
//}
//	private boolean makeUnswappable(ArrayList<Cell> toswap) {
//		
//		
//		for(Cell cell: toswap) {
//			ArrayList<Cell> edgecells = cell.getCage().getEdgeOfCage();
//			if(edgecells.contains(cell)) {
//				int[] pos = cell.getCage().getCords();
//				int[] pos2 = new int[pos.length -1];
//				for(int i = 0; i < pos2.length;i++) {
//					pos2[i] = pos[i];
//				}
//				int[] pos3 = new int[1];
//				pos3[0] = pos[pos.length-1];
//				grid.deleteCage(cell.getCage());
//				this.setupCage(grid, pos3, new Random(), 1);
//				this.setupCage(grid, pos2, new Random(), 1);
//				ArrayList<Cage> tt = grid.getAllCages();
//				
//				
//				
//				
//				
//				
//				
//				return true;
//			}
//		}
//		//
//		
//		//check top,right,left,bottom --> needs to be at edge of cage 
//		
//		//Set cell to single cell --> 
//		
//		return false;
//	}
	
	
	public Grid specialCase(int dimension, int difficulty,boolean unique) {
		Grid newest = new Grid(dimension);
		this.generateSodukoGridSpecial(difficulty, newest);
		
		ArrayList<Integer> usedcells = new ArrayList<Integer>();
		while(usedcells.size() < dimension*dimension) {
			for(int i = 1; i < dimension* dimension;i++) {
				if(!usedcells.contains(i)) {
					this.specialCage(i, difficulty, newest,usedcells);
				}
			}

		}
		
		return newest;
		
	}
	
	public void specialCage(int startingcell,int difficulty,Grid grid,ArrayList<Integer> usedshit){
		ArrayList<Integer> positions = new ArrayList<Integer>();
		ArrayList<Integer> posvalues = new ArrayList<Integer>();
		Random rand = new Random();
		usedshit.add(startingcell);
		for(int i = 0; i < rand.nextInt((difficulty+1) *3);i++) {
			int previouscell = startingcell;
			startingcell += rand.nextInt(3) -1;
			if (startingcell == previouscell){ 
				if(rand.nextInt(2) == 1)
				startingcell+= grid.getDimensions();
			}
			if(startingcell < 1) {
				continue;
			}
			if(!usedshit.contains(i)) {
				positions.add(startingcell);
				posvalues.add(grid.getCell(startingcell).getNumber());
				usedshit.add(startingcell);
			}
			else {
				startingcell = previouscell;
				continue;
			}
		}
		int[] args = new int[positions.size()];
		for(int i = 0; i < positions.size();i++) {
			args[i] = positions.get(i);
		}
		this.setupCage(grid, args, new Random(), difficulty);
	}
	
}	
