package Graphics;

import javafx.scene.control.RadioButton;

public class ControlKnob extends RadioButton{

	private boolean black;
	private boolean white;
	
	public ControlKnob() {
	
		super();
		this.setBlack(false);
		this.setWhite(false);
	
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
