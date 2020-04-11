//import javafx.animation.KeyFrame;
//import javafx.animation.Timeline;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.Pane;
//import javafx.scene.layout.StackPane;
//import javafx.scene.paint.Color;
//import javafx.scene.paint.Paint;
//import javafx.scene.shape.Circle;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontPosture;
//import javafx.scene.text.FontWeight;
//import javafx.scene.text.Text;
//import javafx.stage.Stage;
//
////Animation class for winning
//public class Winning {
//	//first animate the grid
//	//create circle --> make it expand -> make it say you win
//	//play again
//	//create new stage--> hide other--> -> play again option 
//	private Scene scene;
//	private boolean inScene = true;
//	private BorderPane grid;
//	private StackPane pane;
//	double height;
//	double width;
//	public Winning(BorderPane grid,double height,double width) {
//		pane = new StackPane();
//		this.height = height;
//		this.grid = grid;
//		this.width = width;
//		this.scene = new Scene(pane,width,height);
//	}
//	
//	
//	public Scene getAnimation() throws Exit{
//		Circle circle = new Circle(2);
//		circle.setFill(Color.AQUA);
//		Button more = new Button("Play more!");
//		Button back = new Button("Go back");
//		Button reset = new Button("reset");
//		this.pane.getChildren().add(grid);
//		this.pane.getChildren().add(circle);
//		this.pane.setAlignment(circle, Pos.CENTER);
//		KeyFrame frame = new KeyFrame(javafx.util.Duration.millis(75), new EventHandler<ActionEvent>() {
//			public void handle(ActionEvent arg0) {
//				circle.setRadius(circle.getRadius() * 1.25);
//			}
//			
//		});
//		Timeline timeline = new Timeline(frame);
//		timeline.setCycleCount(40);
//		timeline.setOnFinished(new EventHandler<ActionEvent>(){
//			public void handle(ActionEvent e) {
//				Font font = Font.font("Algerian", FontWeight.BOLD, FontPosture.ITALIC, 36);
//				Text text = new Text("You win");
//				text.setFont(font);
//				pane.getChildren().add(text);
//				text.relocate(height/2, width/2);
//					KeyFrame moveup = new KeyFrame(javafx.util.Duration.millis(500), new EventHandler<ActionEvent>() {
//						public void handle(ActionEvent arg0) {
//							text.setTranslateY(-25);
//						}
//						
//					});
//					KeyFrame movedown = new KeyFrame(javafx.util.Duration.millis(500), new EventHandler<ActionEvent>() {
//						public void handle(ActionEvent arg0) {
//							text.setTranslateY(25);
//						}
//					});
//					Timeline movingtext = new Timeline(moveup,movedown);
//					movingtext.setCycleCount(500);
//					movingtext.play();
//					movingtext.setOnFinished(new EventHandler<ActionEvent>(){
//						public void handle(ActionEvent arg0) {
//							inScene = false;
//						}
//						
//					});
//			}
//		});
//		//dont hardcode cycle count
//		//on finished
//		timeline.play();
//		
//		
//		
//		
//		
//		
//		
//		
//		return this.scene;
//	}
//	
//}

class Exit extends Exception{
	public Exit(String msg) {
		super(msg);
	}
}