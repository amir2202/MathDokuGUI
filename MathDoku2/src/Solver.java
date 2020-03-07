import javafx.scene.text.Text;

public class Solver {

	
	//Method to check if cell input is valid
	public static boolean validCell(Grid grid, Cell cell) {
		if(grid.validCellInput(cell, true) == false) {
			return false;
		}
		return true;
	}
	
	public static void solve(Grid grid) {
		int index = 1;
		int dim = grid.getDimensions();
		//outer loop for index
		while(index <= grid.getDimensions() * grid.getDimensions()) {
			
			Cell current = grid.getCell(index);
			current.increaseCell();
			
			if(current.getNumber() == grid.getDimensions() +1) {
				current.setNumber(0);
			}
			
			while(grid.getCell(index).getNumber() != 0 && (validCell(grid ,grid.getCell(index)) == false)) {
				grid.getCell(index).increaseCell();
			}
			
			if (grid.getCell(index).getNumber() == 0) {
				index--;					
				System.out.println("backtracking");
			}
			else {
				index++;
			}
		}
		
		for(int x = 0; x < grid.getDimensions(); x++) {
			for(int y = 0; y < grid.getDimensions(); y++) {
				int number = grid.getCell(x, y).getNumber();
				Text update = new Text(String.valueOf(number));
				grid.setText(x,y,update, true, number);	
		}
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
