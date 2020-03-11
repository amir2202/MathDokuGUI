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
	
	public void generate(int difficulty, Grid grid) {
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
			
			for(int x = 0; x < grid.getDimensions(); x++) {
				for(int y = 0; y < grid.getDimensions(); y++) {
					int number = grid.getCell(x, y).getNumber();
					Text update = new Text(String.valueOf(number));
					grid.setText(x,y,update, true, number);	
			}
			}
			
		}
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
		
		for(int x = 0; x < grid.getDimensions(); x++) {
			for(int y = 0; y < grid.getDimensions(); y++) {
				int number = grid.getCell(x, y).getNumber();
				Text update = new Text(String.valueOf(number));
				grid.setText(x,y,update, true, number);	
		}
		}
	}	


	//Now cell constructing
	
	
}
