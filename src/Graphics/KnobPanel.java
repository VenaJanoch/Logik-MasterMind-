package Graphics;

import com.sun.swing.internal.plaf.basic.resources.basic;

import Control.Constants;
import Control.Logics;
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

public class KnobPanel extends HBox {

	private Knob[] knobs;
	private int identifikace;

	private Logics logics;
	private StartWindow stWin;

	public KnobPanel(int identifikace, StartWindow stWin) {
		super(5);

		this.setStWin(stWin);
		this.setIdentifikace(identifikace);
		logics = new Logics();
		createKnobs();

	}

	private void createKnobs() {

		knobs = new Knob[Control.Constants.countKnobs];

		for (int i = 0; i < Control.Constants.countKnobs; i++) {

			knobs[i] = new Knob();

			knobs[i].setId(String.valueOf(i));

			knobs[i].setShape(new Circle(3));
			knobs[i].setPadding(new Insets(8));
			knobs[i].setOnAction(event -> setColor(event.getSource()));
			knobs[i].setBackground(new Background(
					new BackgroundFill(Constants.backgroundColorDefaulKnobs, CornerRadii.EMPTY, Insets.EMPTY)));
			this.getChildren().add(knobs[i]);
		}

	}

	private void setColor(Object button) {
		Button pomButton = (Button) button;
		
		ColorPalet cp = new ColorPalet(this, Integer.parseInt(pomButton.getId()));
	
	}

	/************** Getrs and Setrs *******************/
	public int getIdentifikace() {
		return identifikace;
	}

	public void setIdentifikace(int identifikace) {
		this.identifikace = identifikace;
	}

	public Knob[] getKnobs() {
		return knobs;
	}

	public void setKnobs(Knob[] knobs) {
		this.knobs = knobs;
	}

	public StartWindow getStWin() {
		return stWin;
	}

	public void setStWin(StartWindow stWin) {
		this.stWin = stWin;
	}

}
