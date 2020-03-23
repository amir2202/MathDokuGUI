import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {
	private boolean checking = false;
	private Grid grid;
	private ArrayList<String> numbers;
	private ActionHandler handler;
	private Button redo; 
	private Stage stage;
	private Button undo;
	final KeyCombination undocmb = new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN);
	final KeyCombination redocmb = new KeyCodeCombination(KeyCode.Y, KeyCombination.SHORTCUT_DOWN);
	public static void main(String[] args) {
		launch(args);
	
	}

	@Override
	public void start(Stage stage) throws Exception {
		BorderPane layout = new BorderPane();
		this.stage = stage;
		handler = new ActionHandler();
		VBox main = new VBox();
		Scene scene = new Scene(main, 600,600);
		stage.setTitle("MathDoku");
		//Get grid
		grid = new Grid(4);
		grid.requestFocus();
		this.validInputNumber();
		ChoiceBox font = new ChoiceBox();
		font.getItems().add("Small");
		font.getItems().add("Medium");
		font.getItems().add("Big");
		main.getChildren().add(grid);
		main.setOnKeyPressed(new KeyHandler());
		main.addEventHandler(ScrollEvent.ANY, new ScrollHandler());
		HBox options = new HBox();
		font.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				if (font.getValue() == "Small") {
					grid.setFont("small");
				}
				if (font.getValue() == "Medium") {
					grid.setFont("medium");
				}
				if (font.getValue() == "Big") {
					grid.setFont("large");
				}
			}
			
		});
		main.setVgrow(grid, Priority.ALWAYS);
		Button solve = new Button("Solve");
		Button generate = new Button("Generate");
		generate.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				Generator gen = new Generator();
				grid.clearCells();
				gen.generateSodukoGrid(2, grid);
				generate.setDisable(true);
			}
			
		});
		solve.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
//				Task<Void> task = new Task<Void>() {
//					protected Void call() {
//						solve.setDisable(true);
//						Solver.solve(grid);
//						return null;
//					}
//				};
//				Thread solving = new Thread(task);
//				solving.start();
				Solver.solve(grid);
			}
		});
		
		
		undo = new Button("Undo");
		undo.setDisable(true);
		undo.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				if(handler.undoEmpty() == false) {
					handler.undo();
				}
			}
		});
//		undo.
		redo = new Button("Redo");
		redo.setDisable(true);
		redo.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if(handler.redoEmpty() == false) {
					handler.redo();
				}			
			}
			
		});
		
		Button clear = new Button("Clear");
		clear.setOnAction(new ClearHandler());
//		clear.setOnAction(new EventHandler<ActionEvent>(){
//			public void handle(ActionEvent e) {
//				System.out.print(grid.rowDuplicates(0));
////				Cell[] cols = grid.getColumncells(0);
////				for(int i = 0; i <cols.length;i++ ) {
////					System.out.print(cols[i].getNumber());
////				}
//			}
//		});
		
		Button load = new Button("Load game");
		CheckBox mistakes = new CheckBox("Show mistakes");
		mistakes.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if(mistakes.isSelected() == true) {
					checking = true;
				}
				else if (mistakes.isSelected() == false) {
					checking = false;
				}
				
			}
			
		});
		
		options.getChildren().addAll(clear,undo,redo,load,font ,solve,mistakes,generate);
		options.setHgrow(undo, Priority.ALWAYS);
		options.setSpacing(2);
		options.setAlignment(Pos.BASELINE_LEFT);
		options.setHgrow(redo, Priority.ALWAYS);
		options.setHgrow(load, Priority.ALWAYS);
		options.setHgrow(mistakes, Priority.ALWAYS);
		main.getChildren().add(options);
		stage.setScene(scene);
		stage.show();
		
		Dialog<String> dialog = new Dialog<>();
//		load.setOnAction(new EventHandler<ActionEvent>() {
//			public void handle(ActionEvent arg0) {
//				dialog.isShowing();
//				// TODO Auto-generated method stub
//				
//			}
//			
//		});
		
		load.setOnAction(new EventHandler<ActionEvent>(){
		public void handle(ActionEvent e){
			Stage custom = new Stage();
			custom.setTitle("Load your custom MathDoku game");
			HBox choices = new HBox();
			Button config = new Button("Load from text");
			Button loadfiles = new Button("Chose file");	
			VBox area = new VBox();
			Scene second = new Scene(area);
			TextArea txt = new TextArea();
			area.getChildren().addAll(txt,choices);
			area.setVgrow(txt, Priority.ALWAYS);
			choices.getChildren().addAll(config,loadfiles);
			custom.setScene(second);
			custom.show();
			config.setOnAction(new EventHandler<ActionEvent>(){
				public void handle(ActionEvent arg0) {
					String[] configlines = txt.getText().split("\n");
					FileHandler manual = new FileHandler();
						try {
							int line = 1;
							for(String cage:configlines) {
							if(manual.parseLine(cage) == false) {return;}
							}
							int realdim = manual.getDimension();
							Grid newgrid = new Grid(realdim);
							main.getChildren().remove(grid);
							main.getChildren().remove(options);
							grid = newgrid;
							validInputNumber();
							for(String cage:configlines) {
								String[] split = cage.split(" ");
								String label = split[0];
								String[] argum = split[1].split(",");
								if(argum.length == 1) {
									grid.setCage(label, Integer.valueOf(split[1]));
								}
								int[] arguments = new int[argum.length];
								for(int i = 0;i < arguments.length;i++) {
									arguments[i] = Integer.valueOf(argum[i]);
									System.out.println(arguments[i]);
								}
								///change this argument shit
								if(manual.checkCage(arguments, realdim) == true) {
									grid.setCage(label, arguments);
								}
								else {
									throw new ConfigurationError("Your cages are not adjacent check line, " + line);
								}
								line++;
							}
							main.setVgrow(grid, Priority.ALWAYS);
							main.getChildren().add(grid);
							grid.requestFocus();
							main.getChildren().add(options);
							
							
						} catch(ConfigurationError e) {
							e.printStackTrace();
						}
					

				}
				
			});
			
			
			
			loadfiles.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent z) {
					FileChooser choseFiles = new FileChooser();
					choseFiles.setTitle("Open a premade MathDoku game");
					File game = choseFiles.showOpenDialog(stage);
					if(game != null && game.canRead() == true && game.exists() == true) {
						try {
							int line = 1;
							FileHandler handler = new FileHandler(game);
							boolean temp = handler.readFile();
							int dimensions = handler.getDimension();
							Grid newgrid = new Grid(dimensions);
							main.getChildren().remove(grid);
							main.getChildren().remove(options);
							grid = newgrid;
							validInputNumber();
							for(String cage:handler.getLines()){
								if(handler.parseLine(cage) == true) {
									String[] splitline = cage.split(" ");
									String label = splitline[0];
									String[] arguments;
									arguments = splitline[1].split(",");
									int[] args = new int[arguments.length];
									for(int i = 0; i < args.length; i++) {
										args[i] = Integer.valueOf(arguments[i]);
									}
									if(arguments.length == 0) {
										grid.setCage(label, Integer.valueOf(splitline[1]));
									}
									if(handler.checkCage(args, dimensions) == true) {
										grid.setCage(label, args);	
									}
								}
								line++;
							}			
							main.setVgrow(grid, Priority.ALWAYS);
							main.getChildren().add(grid);
							grid.requestFocus();
							main.getChildren().add(options);
						} catch(Exception e2) {
							e2.printStackTrace();
						}
						
					}
				}
			});


		}
	});
	}
	
	public void validInputNumber() {
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
			handler.reset();
			undo.setDisable(true);
			redo.setDisable(true);
			if (result.isPresent() && result.get() == ButtonType.OK) {	
				grid.clearCells();	
			}
		
		}
		
	}

	class KeyHandler implements EventHandler<KeyEvent>{
		public void handle(KeyEvent arg0) {	
			if(arg0.getCode() == KeyCode.A || arg0.getCode() == KeyCode.W || arg0.getCode() == KeyCode.S || arg0.getCode() == KeyCode.D /*|| arg0.getCode() == KeyCode.UP || arg0.getCode() == KeyCode.DOWN || arg0.getCode() == KeyCode.LEFT || arg0.getCode() == KeyCode.RIGHT */) {
				if(grid.getSelected() == null) {
					grid.setSelected(0, 0);
				}
				
				else if(arg0.getCode() == KeyCode.A || arg0.getCode() == KeyCode.LEFT){
					int x = grid.getSelected().getX();
					int y = grid.getSelected().getY();
					if(x != 0) {
						grid.setSelected(x -1, y);
					}
				}
				//right
				else if(arg0.getCode() == KeyCode.D || arg0.getCode() == KeyCode.RIGHT){
					int x = grid.getSelected().getX();
					int y = grid.getSelected().getY();
					if(x != grid.getDimensions()-1) {
						grid.setSelected(x +1, y);
					}
				}
				
				//up
				else if(arg0.getCode() == KeyCode.W || arg0.getCode() == KeyCode.UP){
					int x = grid.getSelected().getX();
					int y = grid.getSelected().getY();
					if(y != 0) {
						grid.setSelected(x, y-1);
					}
				}
				
				else if(arg0.getCode() == KeyCode.S || arg0.getCode() == KeyCode.DOWN){
					int x = grid.getSelected().getX();
					int y = grid.getSelected().getY();
					if(y != grid.getDimensions()-1) {
						grid.setSelected(x, y+1);
					}
				}
			}
			
			if(arg0.getCode() == KeyCode.BACK_SPACE){
				if(grid.getSelected() != null) {
					Action action = grid.getSelected().setText(new Text(""),true, 0);
					grid.getSelected().setNumber(0);
					boolean temp = handler.notify(action);
					if(temp == true) {
						redo.setDisable(true);
						handler.addUndo(action);
					}
					else if(temp == false) {
						redo.setDisable(false);
						undo.setDisable(false);
					}
				}		
			} 
			if(undocmb.match(arg0)){
				handler.undo();
			}
			else if(redocmb.match(arg0)) {
				handler.redo();
			}
			else if(grid.getSelected() != null && numbers.contains(arg0.getText())) {
				if(checking == false) {
					Action action = grid.getSelected().setText(new Text(arg0.getText()),true, Integer.valueOf(arg0.getText()));
					boolean temp = handler.notify(action);	
					if(temp == true) {
						redo.setDisable(true);
						handler.addUndo(action);
					}
					else if(temp == false) {
						redo.setDisable(false);
						undo.setDisable(false);
					}
				}
				else if(checking == true) {
					Action action = grid.getSelected().setText(new Text(arg0.getText()),true, Integer.valueOf(arg0.getText()));
					boolean temp = handler.notify(action);	
					if(temp == true) {
						redo.setDisable(true);
						handler.addUndo(action);
					}
					else if(temp == false) {
						redo.setDisable(false);
						undo.setDisable(false);
					}
					if(grid.validCellInput(grid.getSelected(),false) == true)
					{
						grid.getSelected().setCorrect(true);
					}
					if(grid.validCellInput(grid.getSelected(),false) == false)
					{
						grid.getSelected().setCorrect(false);
					}
				}
			}
			
			
			if(grid.isFilled()) {
				try {
					grid.win(grid.solved());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
		class ScrollHandler implements EventHandler<ScrollEvent>{
		@Override
		public void handle(ScrollEvent arg0) {
			if(grid.isFilled()) {
				try {
					grid.win(grid.solved());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			double deltaY = arg0.getDeltaY();
			//decrease
			if(deltaY < 0) {
				if(grid.getSelected() != null && grid.getSelected().getNumber() != 0) {
					Action action = grid.getSelected().changeCell(false, grid.getDimensions());
					handler.notify(action);
					undo.setDisable(false);
					redo.setDisable(false);
				}
			}
			else if(deltaY > 0) {
				if(grid.getSelected() != null) {
					Action action = grid.getSelected().changeCell(true, grid.getDimensions());
					handler.notify(action);
					undo.setDisable(false);
					redo.setDisable(false);
				}
				
			}
			
		}
		
	}
}

