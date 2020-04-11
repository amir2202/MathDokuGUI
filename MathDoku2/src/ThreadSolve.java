import javafx.scene.text.Text;

public class ThreadSolve {

	
	//Method to check if cell input is valid
	public boolean validCell(Grid grid, Cell cell) {
		if(grid.solvingGrid(cell) == false) {
			return false;
		}
		return true;
	}
	
	public void solve(Grid grid) {
		int index = 1;
		int dim = grid.getDimensions();
		//outer loop for index
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
		
	}
	

	
	
}
