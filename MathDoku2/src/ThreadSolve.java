import java.util.ArrayList;

public class ThreadSolve {

	
	//Method to check if cell input is valid
	private ArrayList<Integer[]> solutionset;
	public boolean validCell(Grid grid, Cell cell) {
		if(grid.solvingGrid(cell) == false) {
			return false;
		}
		return true;
	}
	
	public ArrayList<Integer[]> getSolutions(){
		return this.solutionset;
	}
	
	public void solve(Grid grid,boolean showall) {
		solutionset = new ArrayList<Integer[]>();
		grid.clearCells();
		int index = 1;
		int dim = grid.getDimensions();
		//changed
		int lb = 1;
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
			
			if(index == 0 && this.solutionset.size() == 0) {
				solutionset = null;
				break;
			}
			else if(index == 0) {
				break;
			}
			
			if(index == dim * dim && validCell(grid, current) && current.getNumber() != 0) {
				//Add solution
				Integer[] solution = new Integer[dim*dim];
				for(int i = 1; i <= dim * dim; i++) {
					solution[i-1] = grid.getCell(i).getNumber();
				}
				solutionset.add(solution);
				if(!showall && this.solutionset.size() > 1) {
					return;
				}
				while(index > lb) {
					grid.getCell(index).setNumber(0);
					index--;
				}
				continue;
			}
			
			if (current.getNumber() == 0) {
				lb = index;
				index--;	
				//backtrack = true
			}
			else {
				index++;
			}
			
			
			
			
			
			
			
		}
		
		
		
		
		
	}
	

	
	
}
