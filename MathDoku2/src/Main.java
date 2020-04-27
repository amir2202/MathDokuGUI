import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {
	private boolean checking = false;
	private Grid grid;
	public Stage stage;
	private ArrayList<String> numbers;
	public Scene scene;
	private ActionHandler handler;
	public static Button redo; 
	private BorderPane pane;
	public static Button undo;
	final KeyCombination undocmb = new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN);
	final KeyCombination redocmb = new KeyCodeCombination(KeyCode.Y, KeyCombination.SHORTCUT_DOWN);
	public static void main(String[] args) {
		launch(args);
	}


	
	
	public void setScene(Scene scene) {
		this.stage.setScene(scene);
	}
	
	public Scene getScene(String[] oldconfig, Integer[] nrs, int oldgriddim) {
		handler = new ActionHandler();
		pane = new BorderPane();
		if(oldconfig != null && nrs != null) {
			grid = new Grid(oldgriddim);
			for(String cage:oldconfig){
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
				else {
					grid.setCage(label, args);	
				}
		}
			for(int i = 0; i < nrs.length;i++) {
				grid.getCell(i+1).setNumber(nrs[i]);
				grid.getCell(i+1).updateText();
			}
		}
		else {
		grid = new Grid(8);	
		}
		pane.addEventHandler(ScrollEvent.ANY, new ScrollHandler());
		this.validInputNumber();
		pane.setCenter(grid);
		scene = new Scene(pane,600,600);
		VBox left = new VBox();
		left.setSpacing(10.0);
		VBox right = new VBox();
		right.setSpacing(10.0);
		Button hint = new Button("Hint");
		hint.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				hint.setDisable(true);
				if(grid.showHint()) {
					hint.setDisable(false);
				};
				
			}
			
		});
		Button allsolutions = new Button("All solutions");

		allsolutions.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				allsolutions.setDisable(true);
				String[] touse = grid.getConfig();
				int griddim = grid.getDimensions();
				AllSolutions task = new AllSolutions(touse,griddim);
				Thread thread = new Thread(task);
				task.setOnSucceeded(new EventHandler<WorkerStateEvent>(){

					@Override
					public void handle(WorkerStateEvent arg0) {
						allsolutions.setDisable(false);
						Scene get = (Scene) task.getValue();
						Stage created = new Stage();
						Integer solutionamount = (Integer) get.getUserData();
						created.setScene(get);
						created.setTitle("All Solutions: " + solutionamount);
						created.show();
					}
					
				});
				thread.start();
			}
			
			
		});
		
		

		
		
		
		right.setAlignment(Pos.CENTER_LEFT);
		pane.setRight(right);
		pane.setLeft(left);
		HBox bottom = new HBox();
		bottom.setAlignment(Pos.CENTER);
		pane.setBottom(bottom);
//		bottom
		Button solvebutton = new Button("Solve");
		Button generate = new Button("Generate");
		generate.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				VBox generating = new VBox();
				boolean unique = true;
				generating.setAlignment(Pos.CENTER);
				Label dimension = new Label("Dimension");
				Label difficulty = new Label("Difficulty");
				Button generate = new Button("Generate");
				Button back = new Button("Go back");
				back.setOnAction(e-> {pane.setLeft(left);});
				ChoiceBox dim = new ChoiceBox();
				dim.getItems().add("2x2");
				dim.getItems().add("3x3");
				dim.getItems().add("4x4");
				dim.getItems().add("5x5");
				dim.getItems().add("6x6");
				dim.getItems().add("7x7");
				dim.getItems().add("8x8");
				ChoiceBox dif = new ChoiceBox();
				dif.getItems().add("Easy");
				dif.getItems().add("Intermediate");
				dif.getItems().add("Hard");
				dif.getItems().add("Guru");
				CheckBox checkbox = new CheckBox("Unique");
				
				dif.getSelectionModel().selectFirst();
				dim.getSelectionModel().selectFirst();
				generate.setOnAction(e -> {
					grid.clearCells();
					grid.eraseCages();
					String dimensionstring = (String)dim.getValue();
					dimensionstring = dimensionstring.substring(0, 1);
					int chosendim = Integer.valueOf(dimensionstring);
					Grid newgrid = new Grid(chosendim);
					pane.getChildren().remove(grid);
					Main.this.grid = newgrid;
//					grid = newgrid;
					validInputNumber();
					int chosendifficulty = 0;
					//default
					switch((String) dif.getValue()) {
					case "Easy":
						chosendifficulty = 0;
					case "Intermediate":
						chosendifficulty = 1;
					case "Hard":
						chosendifficulty = 2;
					case "Guru":
						chosendifficulty = 3;
					}
					GeneratingTask genTask = new GeneratingTask(chosendim,chosendifficulty,checkbox.isSelected());
					genTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
						public void handle(WorkerStateEvent arg0) {
							
							grid = null;
							Grid created = (Grid) genTask.getValue();
							Main.this.grid = created;
							Main.this.grid.clearCells();
							pane.setCenter(Main.this.grid);
							pane.setLeft(left);
							
						}
						
					});
					Thread genThread = new Thread(genTask);
					genThread.start();
					
				});
				generating.setSpacing(20);
				generating.getChildren().addAll(dimension,dim,difficulty,dif,checkbox,generate, back);
				pane.setLeft(generating);
				grid.clearCells();

			}
			
		});
		solvebutton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				if(grid.alreadySolved() == true) {
					ArrayList<Integer[]> allvalues = grid.getSolution();
					Integer[] values = allvalues.get(0);
					for(int i = 0; i < grid.getDimensions() * grid.getDimensions(); i++) {
						grid.getCell(i +1).setNumber(values[i]);
					}
					grid.updateGrid();
					return;
				}
				else {
					solvebutton.setDisable(true);
				String[] config = new String[grid.getAllCages().size()];
				for(int i = 0; i < config.length; i++) {
					config[i] = grid.getAllCages().get(i).toString();
				}
				SolvingTask solve = new SolvingTask(config, grid.getDimensions(),false);
				solve.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
					public void handle(WorkerStateEvent arg0) {
						if(solve.getValue() == null) {
							Alert alert = new Alert(AlertType.WARNING,"No solution found!");
							alert.show();
							alert.setTitle("No solution");
							return;
						}
						ArrayList<Integer[]> allvalues = (ArrayList<Integer[]>) solve.getValue();
						Integer[] values = allvalues.get(0);
						for(int i = 0; i < grid.getDimensions() * grid.getDimensions();i++) {
							grid.getCell(i +1).setNumber(values[i]);
						}
						grid.updateGrid();
						grid.setSolutions(allvalues);
						grid.solved(true);
						solvebutton.setDisable(false);
					}
					
				});
				Thread thread = new Thread(solve);
				thread.setDaemon(true);
				thread.start();
			}
			}
			
		});
		
		
		undo = new Button("Undo");
		undo.setDisable(true);
		undo.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				if(handler.undoEmpty() == false) {
					Cell cell = handler.undo();
					if(checking == true) {
					grid.mistakeAnalysis(cell.getX(), cell.getY());
					}
				}
			}
		});
//		undo.
		redo = new Button("Redo");
		redo.setDisable(true);
		redo.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if(handler.redoEmpty() == false) {
					Cell cell = handler.redo();
					if(checking == true) {
					grid.mistakeAnalysis(cell.getX(), cell.getY());
					}
				}			
			}
			
		});
		Button clear = new Button("Clear");
		clear.setOnAction(new ClearHandler());
		Button config = new Button("Save config");
		config.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				String configfinal = "";
				String[] conf = grid.getConfig();
				for(int i = 0; i < conf.length;i++) {
					configfinal += conf[i] + "\n";
				}
				TextArea textArea = new TextArea(configfinal);
				textArea.setEditable(false);
				textArea.setWrapText(true);
				GridPane gridPane = new GridPane();
				gridPane.setMaxWidth(Double.MAX_VALUE);
				gridPane.add(textArea, 0, 0);

				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Retrieve your configuration file");
				alert.getDialogPane().setContent(gridPane);
				alert.showAndWait();

				
			}
			
		});
		bottom.getChildren().addAll(undo,redo,clear,solvebutton,config,hint);
		
		CheckBox mistakes = new CheckBox("Show mistakes");
		mistakes.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if(mistakes.isSelected() == true) {
					checking = true;
					int dimension = grid.getDimensions();
					for(int i = 1; i <= dimension* dimension; i++) {
						if(grid.getCell(i).getNumber() != 0 && !grid.getCell(i).getChecked()) {
							grid.validCellInput(grid.getCell(i));
						}
					}
				}
				else if (mistakes.isSelected() == false) {
					int dimension = grid.getDimensions();
					for(int i = 1; i <= dimension*dimension; i++) {
						grid.getCell(i).resetStyle();
					}
					checking = false;
				}
				
			}
			
		});
		right.getChildren().add(mistakes);
		
		
		ChoiceBox font = new ChoiceBox();
		font.getItems().add("Small");
		font.getItems().add("Medium");
		font.getItems().add("Big");
		font.getSelectionModel().selectFirst();
		pane.setOnKeyPressed(new KeyHandler());
		pane.addEventHandler(ScrollEvent.ANY, new ScrollHandler());
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
		
		left.getChildren().addAll(new Label("Font Size"),font,generate);
		left.setAlignment(Pos.CENTER);
		Button load = new Button("Load games");
		right.getChildren().addAll(load,allsolutions);
		
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
							if(manual.parseLine(cage) == false) {
								Alert alert = new Alert(AlertType.WARNING);
								alert.setTitle("Configuration Error");
								alert.setHeaderText("You have errors in the configuration structure, try again");
								alert.show();
								}
							}
							int realdim = manual.getDimension();
							Grid newgrid = new Grid(realdim);
							pane.getChildren().remove(grid);
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
							pane.setCenter(grid);
							
							
						} catch(ConfigurationError e) {
							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("Configuration Error");
							alert.setHeaderText("You have errors in the configuration");
							alert.setContentText(e.getLocalizedMessage());
							alert.show();
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
							handler.readFile();
							int dimensions = handler.getDimension();
							Grid newgrid = new Grid(dimensions);
							pane.getChildren().remove(grid);
							grid = newgrid;
							validInputNumber();
							for(String cage:handler.getLines()){
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
								line++;
							}			
							pane.setCenter(grid);
						} catch(Exception e2) {
							e2.printStackTrace();
						}
						
					}
				}
			});
			


		}
	});
		return scene;
	}
	
	@Override
	public void start(Stage stage) {
		Scene get = this.getScene(null,null,0);
		this.stage = stage;
		stage.setScene(get);
		stage.show();
		stage.setTitle("MathDoku");
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
//					if(grid.getSelected().getCage().isCageFull() == false) {
//						grid.getSelected().getCage().setCorrect(true);
//					}
//					if(grid.rowDuplicates(grid.getSelected().getY()) == false){
//						for(Cell rowcell: grid.getRowCells(grid.getSelected().getY())) {
//							rowcell.unhighlight();
//						}
//					}
//					if(grid.columnDuplicates(grid.getSelected().getX()) == false) {
//						for(Cell colcell: grid.getColumncells(grid.getSelected().getX())) {
//							colcell.unhighlight();
//						}
//					}
					grid.mistakeAnalysis(grid.getSelected().getX(), grid.getSelected().getY());
					boolean temp = handler.notify(action);
					if(temp == true) {
						redo.setDisable(true);
						handler.addUndo(action);
					}
					else if(temp == false) {
//						redo.setDisable(false);
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
//						redo.setDisable(false);
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
//						redo.setDisable(false);
						undo.setDisable(false);
					}
					grid.mistakeAnalysis(grid.getSelected().getX(), grid.getSelected().getY());
				}
			}
			
			
			//rework this
			if(grid.isFilled()) {
				try {
					if(grid.win(grid.solved())){
						Winning win = new Winning(pane,scene.getHeight(), scene.getWidth(),grid); 
						
						stage.setScene(win.getAnimation());
					}
							
					//edit here
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exit e) {
					// TODO Auto-generated catch block
//					this.stage.setScene(scene);
				}
			}
		}
	}
	
		class ScrollHandler implements EventHandler<ScrollEvent>{
		@Override
		public void handle(ScrollEvent arg0) {
			double deltaY = arg0.getDeltaY();
			//decrease
			if(deltaY < 0) {
				if(grid.getSelected() != null && grid.getSelected().getNumber() != 0) {
					if(checking == false) {
						Action action = grid.getSelected().changeCell(false, grid.getDimensions());
						handler.notify(action);
						undo.setDisable(false);
//						redo.setDisable(false);						
					}
					else if(checking == true) {
						Action action = grid.getSelected().changeCell(false, grid.getDimensions());
						handler.notify(action);
						undo.setDisable(false);
//						redo.setDisable(false);
						grid.mistakeAnalysis(grid.getSelected().getX(), grid.getSelected().getY());
					}

				}
			}
			else if(deltaY > 0) {
				if(grid.getSelected() != null) {
					if(checking == false) {
						Action action = grid.getSelected().changeCell(true, grid.getDimensions());
						handler.notify(action);
						undo.setDisable(false);
//						redo.setDisable(false);	
					}
					else if(checking == true) {
						Action action = grid.getSelected().changeCell(true, grid.getDimensions());
						handler.notify(action);
						undo.setDisable(false);
//						redo.setDisable(false);	
						grid.mistakeAnalysis(grid.getSelected().getX(), grid.getSelected().getY());
					}
				}
				
			}
			
		}
		
	}	
		
		
		
		
		class Winning {
			//first animate the grid
			//create circle --> make it expand -> make it say you win
			//play again
			//create new stage--> hide other--> -> play again option 
			private Scene sceneanother;
			private boolean inScene = true;
			private BorderPane grid;
			private String config[];
			private int olddim;
			private Integer[] numbers;
			private StackPane paneanimation;
			double height;
			double width;
			public Winning(BorderPane grid,double height,double width, Grid oldgrid) {
				this.paneanimation = new StackPane();
				this.height = height;
				this.grid = grid;
				this.width = width;
				this.olddim = oldgrid.getDimensions();
				config = new String[oldgrid.getAllCages().size()];
				for(int i = 0; i < oldgrid.getAllCages().size();i++) {
					config[i] = oldgrid.getAllCages().get(i).toString();
				}
				numbers = new Integer[oldgrid.getDimensions() * oldgrid.getDimensions()];
				for(int i = 0; i < numbers.length; i ++) {
					numbers[i] = oldgrid.getCell(i+1).getNumber();
				}
				this.sceneanother = new Scene(paneanimation,width,height);
			}
			
			
			
			public Scene getAnimation() throws Exit{
				Circle circle = new Circle(2);
				circle.setFill(Color.AQUA);
				Button more = new Button("Play more!");
				Button back = new Button("Go back");
				back.setOnAction(e->{
					Scene saved = Main.this.getScene(config, numbers, olddim);
					Main.this.stage.setScene(saved);
				});
				more.setOnAction(e->{
					Scene newest = Main.this.getScene(null,null,0);
					Main.this.stage.setScene(newest);
				});
				this.paneanimation.getChildren().add(grid);
				this.paneanimation.getChildren().add(circle);
				KeyFrame frame = new KeyFrame(javafx.util.Duration.millis(75), new EventHandler<ActionEvent>() {
					public void handle(ActionEvent arg0) {
						circle.setRadius(circle.getRadius() * 1.25);
					}
					
				});
				Timeline timeline = new Timeline(frame);
				timeline.setCycleCount(40);
				timeline.setOnFinished(new EventHandler<ActionEvent>(){
					public void handle(ActionEvent e) {
						Font font = Font.font("Algerian", FontWeight.BOLD, FontPosture.ITALIC, 36);
						Text text = new Text("You win");
						text.setFont(font);
						paneanimation.getChildren().add(text);
						back.relocate((height/4) * 3, width/3);
						more.relocate((height/4) * 3, 2* width/3);
						paneanimation.getChildren().add(back);
						paneanimation.setAlignment(back, Pos.CENTER_LEFT);
						paneanimation.setAlignment(more, Pos.CENTER_RIGHT);
						paneanimation.getChildren().add(more);

						
						
					}
				});
				//dont hardcode cycle count
				//on finished
				timeline.play();
				
				
				
				
				
				
				
				
				return this.sceneanother;
			}
			
		}
		
		
		
		
		
		
		
		
		
		
}

class Exit extends Exception{
public Exit(String msg) {
	super(msg);
}
}

