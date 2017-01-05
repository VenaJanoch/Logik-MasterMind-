package Graphics;

import javafx.scene.control.Button;

/**
 * Trida predstavuje instatnci predstavujici jednotlive tlacitko
 * @author Václav Janoch
 *
 */
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
