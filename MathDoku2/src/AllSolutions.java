import java.util.ArrayList;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AllSolutions extends Task {
	
	private Grid[] grids;
	private String[] config; 
	private HBox menubar;
	private Scene scene;
	private int dimension;
	private ArrayList<Integer[]> solutions;
	public AllSolutions(String[] config, int dim) {
		this.config = config;
		this.dimension = dim;
	}
	

	@Override
	protected Object call() throws Exception {
		
		//IF WE DONT HAVE ALL SOLUTIONS YET 
		ThreadSolve solver = new ThreadSolve();
		Grid temp = new Grid(this.dimension);
		for(String cage:this.config) {
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
		solver.solve(temp,true);
		this.solutions = solver.getSolutions();
		
		grids = new Grid[solutions.size()];
		for(int i = 0; i < solutions.size();i++) {
			Grid solution = new Grid(this.dimension);
			solution.initialiseConfig(config);
			solution.initialiseSolution(solutions.get(i));
			grids[i] = solution;
			solution.setUpBorders();
		}
		
		VBox vbox = new VBox();
		Scene scene = new Scene(vbox);
		menubar = new HBox();
		Button next = new Button("Next solution/permutation");
		next.setOnAction(e-> {
			vbox.getChildren().clear();
//			vbox.getChildren().addAll(grids[])
		});
		Button prev = new Button("Previous solution");
		menubar.getChildren().addAll(prev,next);
		vbox.getChildren().addAll(grids[0],menubar);
		return scene;
	}
}


//allsolutions.setOnAction(new EventHandler<ActionEvent>() {
//public void handle(ActionEvent e) {
//	SolvingTask solve = new SolvingTask(grid.getConfig(),grid.getDimensions(),false);
//	Thread thread = new Thread(solve);
//	solve.setOnSucceeded(new EventHandler<WorkerStateEvent>(){
//
//		@Override
//		public void handle(WorkerStateEvent arg0) {
//			ArrayList<Integer[]> sol =(ArrayList<Integer[]>) solve.getValue();
//			System.out.println(sol.size());
//			for(Integer[] solu: sol) {
//				System.out.println("SOLUTION");
//				for(int x = 0; x < solu.length; x++) {
//					System.out.print(solu[x] + " ");
//					if((x +1) % (grid.getDimensions()) == 0) {
//						System.out.println();
//					}
//				}
//			}
//		}
//		
//	});
//	thread.start();
//
//}
//});