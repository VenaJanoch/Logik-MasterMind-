package Graphics;

import Control.Constants;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.shape.Rectangle;


/**
 * Trida predstavujici objekt controlniho tlacitka
 * @author Václav Janoch
 *
 */
public class ControlKnob extends Button{

	
	
	private boolean black;
	private boolean white;
	
	public ControlKnob() {
	
		super();
		this.setBlack(false);
		this.setWhite(false);
		this.setShape(new Rectangle(1,1));
		this.setBackground(new Background(new BackgroundFill(Constants.backgroundColorDefaulKnobs,
						CornerRadii.EMPTY, Insets.EMPTY)));
	}

	
	/******* Getrs and Setrs ************/
	public boolean isBlack() {
		return black;
	}

	public void setBlack(boolean black) {
		this.black = black;
	}

	public boolean isWhite() {
		return white;
	}

	public void setWhite(boolean white) {
		this.white = white;
	}
	
	
	
}
