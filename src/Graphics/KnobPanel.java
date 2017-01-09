package Graphics;

import javax.swing.text.StyledEditorKit.ForegroundAction;

import Control.Constants;
import Control.Logics;
import Control.NetworkLogics;
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
	/** Globalni promenne tridy **/
	private Knob[] knobs;
	private int identifikace;

	private StartWindow stWin;
	private Desk desk;
	private ColorPalet cp;
	Logics logics;
	NetworkLogics netLog;

	public KnobPanel(int identifikace, Desk desk, ColorPalet cp, Logics logics, NetworkLogics netLog) {
		super(5);
		this.cp = cp;
		this.logics = logics;
		this.netLog = netLog;
		this.setMaxHeight(50);
		this.setMinHeight(50);
		this.setDesk(desk);
		this.setIdentifikace(identifikace);
		createKnobs();
	}
	
	public KnobPanel(int identifikace, Desk desk, ColorPalet cp, Logics logics) {
		super(5);
		this.cp = cp;
		this.logics = logics;
		this.setMaxHeight(50);
		this.setMinHeight(50);
		this.setDesk(desk);
		this.setIdentifikace(identifikace);
		createKnobs();
	}
	/**
	 *createKnobs()
	 *Vytvori panel 
	 *
	 */
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
	/**
	 * setColor(Object button)
	 * Nastavi barvu tlacitku
	 * @param button
	 */
	private void setColor(Object button) {
		Button pomButton = (Button) button;
		if (logics.isMultiMode()) {

			netLog.setKp(this);
			netLog.setIndexButton(Integer.parseInt(pomButton.getId()));
			cp.setVisible(true);
			
		} else {
			logics.setKp(this);
			logics.setIndexButton(Integer.parseInt(pomButton.getId()));
			cp.setVisible(true);
		}
	}

	/**
	 * setResultColor(Color[] resultColor
	 * nastavi barvy vysledku
	 * @param resultColor
	 */
	public void setResultColor(Color[] resultColor) {

		for (int i = 0; i < Control.Constants.countKnobs; i++) {
			knobs[i].setBackground(new Background(new BackgroundFill(resultColor[i], CornerRadii.EMPTY, Insets.EMPTY)));
			nothig();
		}

	}

	/**
	 * Vypne funkce tlacitka
	 */
	public void nothig() {

		for (int i = 0; i < knobs.length; i++) {

			knobs[i].setOnAction(event -> nothig());

		}

	}
	
	public void getFunction(){
		for (int i = 0; i < knobs.length; i++) {

			knobs[i].setOnAction(event -> setColor(event.getSource()));

		}
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

	public Desk getDesk() {
		return desk;
	}

	public void setDesk(Desk desk) {
		this.desk = desk;
	}

}
