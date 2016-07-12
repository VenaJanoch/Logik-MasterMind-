package Graphics;

import javafx.scene.control.Button;

public class Knob extends Button{
	
	private boolean obarven;
	
	public Knob() {

		super();
		
		this.setObarven(false);
		
	}

	/******* Getrs and Setrs ****/
	
	public boolean isObarven() {
		return obarven;
	}

	public void setObarven(boolean obarven) {
		this.obarven = obarven;
	}

}
