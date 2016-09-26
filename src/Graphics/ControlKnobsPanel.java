package Graphics;

import Control.Constants;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ControlKnobsPanel extends HBox{

	private VBox[] firstLine;
	
	private ControlKnob[] controlKnob;
	private int identifikace;
	private StartWindow stWin;
	
	public ControlKnobsPanel(int identifikace, StartWindow stWin) {
		
		super(5);
		setControlKnob(new ControlKnob[Constants.countControlKnobs]);
		firstLine = new VBox[Constants.countControlKnobsLine];
		this.identifikace = identifikace;
		this.stWin = stWin;
		createLines();
		createControlKnobs();
		
	
	}


	public void createLines(){
		
		for (int i = 0; i < firstLine.length; i++) {
			firstLine[i] = new VBox();
		}
		
		
	}
	
	private void createControlKnobs(){
		
		int indexPanel = -1;
		
		for (int i = 0; i < controlKnob.length; i++) {
			controlKnob[i] = new ControlKnob();
			controlKnob[i].setBackground(new Background(
					new BackgroundFill(Constants.backgroundColorDefaulKnobs, CornerRadii.EMPTY, Insets.EMPTY)));
			
			if (i%2 == 0) {
				indexPanel++;
				this.getChildren().add(firstLine[indexPanel]);
			}
			
			controlKnob[i].setId(String.valueOf(indexPanel-1)+ String.valueOf(i));
			firstLine[indexPanel].getChildren().add(controlKnob[i]);
			
		}
		
		
	}
	
	
	/******** Getrs and Setrs *******/
	
	public ControlKnob[] getControlKnob() {
		return controlKnob;
	}


	public void setControlKnob(ControlKnob[] controlKnob) {
		this.controlKnob = controlKnob;
	}


	public VBox[] getFirstLine() {
		return firstLine;
	}


	public void setFirstLine(VBox[] firstLine) {
		this.firstLine = firstLine;
	}


	public int getIdentifikace() {
		return identifikace;
	}


	public void setIdentifikace(int identifikace) {
		this.identifikace = identifikace;
	}


	public StartWindow getStWin() {
		return stWin;
	}


	public void setStWin(StartWindow stWin) {
		this.stWin = stWin;
	}

	

	
	
	
	
	
}
