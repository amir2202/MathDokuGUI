import javafx.scene.text.Text;

public class Solver {

	
	//Method to check if cell input is valid
	public boolean validCell(Grid grid, Cell cell) {
		if(grid.solvingGrid(cell) == false) {
			return false;
		}
		return true;
	}
	
	public void solve(Grid grid) {
		grid.clearCells();
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
		
//		for(int x = 0; x < grid.getDimensions(); x++) {
//			for(int y = 0; y < grid.getDimensions(); y++) {
//				int number = grid.getCell(x, y).getNumber();
//				Text update = new Text(String.valueOf(number));
//				grid.setText(x,y,update, true, number);	
//		}
//		
//		}
	}
	

	
	
}
