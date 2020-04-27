import java.util.ArrayList;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AllSolutions extends Task {
	
	private Grid[] grids;
	private String[] config; 
	private HBox menubar;
	private VBox vbox;
	private int current = 1;
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
//		for(int i = 0; i < this.solutions.size();i++) {
//			System.out.println("SOLUTION");
//			for(int x: this.solutions.get(i)) {
//				System.out.println(x);
//			}
//		}
		grids = new Grid[solutions.size()];
		for(int i = 0; i < solutions.size();i++) {
			Grid solution = new Grid(this.dimension);
			solution.initialiseConfig(config);
			solution.initialiseSolution(solutions.get(i));
			grids[i] = solution;
			solution.setUpBorders();
		}
		
		vbox = new VBox();
		Scene scene = new Scene(vbox,400,400);
		menubar = new HBox();
		Button next = new Button("Next solution/permutation");
		Button prev = new Button("Previous solution");
		Label currentsolution = new Label("Solution " + current + "/" + grids.length);
		prev.setDisable(true);
		next.setOnAction(e->{
			current++;
			currentsolution.setText("Solution " + current + "/" + grids.length);
			if(current == grids.length) {
				next.setDisable(true);
			}
			if(current == 2) {
				prev.setDisable(false);
			}
				this.vbox.getChildren().clear();
				this.vbox.getChildren().addAll(grids[current-1], menubar);
				vbox.setVgrow(grids[current-1],Priority.ALWAYS);
		});
		
		prev.setOnAction(e->{
			current--;
			currentsolution.setText("Solution " + current + "/" + grids.length);
			if(current ==1) {
				prev.setDisable(true);
			}
			if(current == grids.length -1) {
				next.setDisable(false);
			}
			this.vbox.getChildren().clear();
			this.vbox.getChildren().addAll(grids[current-1], menubar);
			vbox.setVgrow(grids[current-1],Priority.ALWAYS);
		});
		menubar.setSpacing(5);
		menubar.getChildren().addAll(prev,next,currentsolution);
		if(grids.length == 1) {
			next.setDisable(true);
			prev.setDisable(true);
		}
		vbox.getChildren().addAll(grids[0],menubar);
		vbox.setVgrow(grids[0], Priority.ALWAYS);
		scene.setUserData(this.grids.length);
		return scene;
	}

	
}

