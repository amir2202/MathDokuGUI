import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {
	
	private Grid grid;
	private ArrayList<String> numbers;
	private ActionHandler handler;
	final KeyCombination undo = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
	final KeyCombination redo = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);
	public static void main(String[] args) {
		launch(args);
	
	}

	@Override
	public void start(Stage stage) throws Exception {
		handler = new ActionHandler();
		VBox main = new VBox();
		Scene scene = new Scene(main, 500,500);
		//Get grid
		grid = new Grid(5);
		this.validInput();
		main.getChildren().add(grid);
		main.setOnKeyPressed(new KeyHandler());
		HBox options = new HBox();
		main.setVgrow(grid, Priority.ALWAYS);
		Button undo = new Button("Undo");
		undo.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				if(handler.undoEmpty() == false) {
					handler.undo();
				}
			}
		});
//		undo.
		Button redo = new Button("Redo");
		redo.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if(handler.redoEmpty() == false) {
					handler.redo();
				}			
			}
			
		});
		Button clear = new Button("Clear");
		clear.setOnAction(new ClearHandler());
		Button load = new Button("Load game");
		Button mistakes = new Button("Show mistakes");
		
		options.getChildren().addAll(clear,undo,redo,load,mistakes);
		options.setHgrow(undo, Priority.ALWAYS);
		options.setHgrow(redo, Priority.ALWAYS);
		options.setHgrow(load, Priority.ALWAYS);
		options.setHgrow(mistakes, Priority.ALWAYS);
		main.getChildren().add(options);
		stage.setScene(scene);
		stage.show();
		
		
		load.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				FileChooser choseFiles = new FileChooser();
				choseFiles.setTitle("Open a premade MathDoku game");
				File game = null;
				game = choseFiles.showOpenDialog(stage);
				if(game != null && game.canRead() == true && game.exists() == true) {
					try {
						ArrayList<Integer> allarguments = new ArrayList<Integer>();
						ArrayList<String> allcages = new ArrayList<String>();
						BufferedReader reader = new BufferedReader(new FileReader(game));
						while(reader.ready() == true) {
							String line = reader.readLine();
							String[] split = line.split(" ");
							String[] cages = split[1].split(",");
							String[] cageargs = new String[split.length];
							//for every argument of the
							for(int i = 0; i < cages.length;i++) {
								allarguments.add(Integer.valueOf(cages[i]));
							}
							allcages.add(line);
						}
						Collections.sort(allarguments);
					
						int dimensions = (int) Math.sqrt(allarguments.get(allarguments.size() -1));
						Grid newgrid = new Grid(dimensions);
						
						main.getChildren().remove(grid);
						main.getChildren().remove(options);
						grid = newgrid;
						for(String cage:allcages){
							String[] splitline = cage.split(" ");
							String label = splitline[0];
							String[] arguments = splitline[1].split(",");
							int[] args = new int[arguments.length];
							for(int i = 0; i < args.length; i++) {
								args[i] = Integer.valueOf(arguments[i]);
							}
							grid.setCage(label, args);
							
						}			
						main.setVgrow(grid, Priority.ALWAYS);
						main.getChildren().add(grid);
						main.getChildren().add(options);
					} catch(IOException e2) {
						e2.printStackTrace();
					}
					
				}
			}
		});
	}
	
	public void validInput() {
		numbers = new ArrayList<String>();
		for(int i = 1; i <= grid.getDimensions();i++) {
			numbers.add(String.valueOf(i));
		}
	}
	
	
	class ClearHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent arg0) {
			
			Alert alert = new Alert(AlertType.CONFIRMATION,
					"Are you sure you want to clear the board?, any changes will be lost");
			
			alert.setTitle("Clearing the board confirmation");
			alert.setHeaderText("Clearing the board");
			
			Optional<ButtonType> result = alert.showAndWait();
			
			
			if (result.isPresent() && result.get() == ButtonType.OK) {	
				grid.clearCells();	
			}
		
		}
		
	}

	class KeyHandler implements EventHandler<KeyEvent>{
		public void handle(KeyEvent arg0) {
			if(undo.match(arg0)){
				handler.undo();
			}
			else if(redo.match(arg0)) {
				handler.redo();
			}
			else if(grid.getSelected() != null && numbers.contains(arg0.getText())) {
				Action action = grid.getSelected().setText(new Text(arg0.getText()));
				handler.notify(action);
			}
		}
	}
}
