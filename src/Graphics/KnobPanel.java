package Graphics;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class KnobPanel extends HBox{

	
	private Button[] knobs;
	private int identifikace;
	
	public KnobPanel(int identifikace) {
		super(5);
		
		this.setIdentifikace(identifikace);
		
		createKnobs();
		
		
	}
	
	private void createKnobs(){
		
		knobs = new Button[Control.Constants.countKnobs];
		
		for (int i = 0; i < Control.Constants.countKnobs; i++) {
			
			knobs[i] = new Button();
			
			knobs[i].setId(String.valueOf(i));
			
			knobs[i].setShape(new Circle(3,Color.GRAY));
			knobs[i].setPadding(new Insets(8));
			knobs[i].setOnAction(event -> setColor(event.getSource()));
			this.getChildren().add(knobs[i]);
		}
		
	}

	private void setColor(Object button) {
		Button pomButton = (Button) button;
		
		ColorPalet cp = new ColorPalet(this,Integer.parseInt(pomButton.getId()));
		
		
	}

	
	
	/************** Getrs and Setrs *******************/
	public int getIdentifikace() {
		return identifikace;
	}

	public void setIdentifikace(int identifikace) {
		this.identifikace = identifikace;
	}

	public Button[] getKnobs() {
		return knobs;
	}

	public void setKnobs(Button[] knobs) {
		this.knobs = knobs;
	}

	
	

}
