package Graphics;

import Control.Constants;
import Control.Logics;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ColorPalet extends VBox {

	private Button[] colorButtons;
	private KnobPanel kP;
	private int indexButton;
	private Logics logics;

	public ColorPalet(Logics logics) {

		super(4);
		this.logics = logics;
		createColorButton();
		createColorPickerPanel();
		this.setVisible(false);
		this.setWidth(300);
		this.setHeight(100);
		
	}

	private void createColorButton() {

		colorButtons = new Button[Control.Constants.countColorButton];

		for (int i = 0; i < colorButtons.length; i++) {

			colorButtons[i] = new Button();
			colorButtons[i].setId(String.valueOf(i));
			colorButtons[i].setShape(new Rectangle(5, 5));
			colorButtons[i].setBackground(
					new Background(new BackgroundFill(Constants.colors[i], CornerRadii.EMPTY, Insets.EMPTY)));
			colorButtons[i].setOnAction(event -> returnColor(event.getSource()));

		}
	}

	public void returnColor(Object button) {

		Button tmpButton = (Button) button;
		
		kP.getKnobs()[indexButton]
				.setBackground(new Background(new BackgroundFill(Constants.colors[Integer.parseInt(tmpButton.getId())],
						CornerRadii.EMPTY, Insets.EMPTY)));
		kP.getKnobs()[indexButton].setObarven(true);
		int identifikace = kP.getIdentifikace();
		
		
			if (logics.controlCountChoosedKnobs(kP)) {

				if (logics.evaluate(kP.getStWin(), identifikace)) {
					kP.getStWin().getKnobPanel()[identifikace].nothig();
					kP.getStWin().getKnobPanel()[identifikace + 1].setVisible(true);
					kP.getStWin().getControlKnobPanel()[identifikace + 1].setVisible(true);			
				}else if (logics.controlCountChoosedKnobs(kP) && identifikace == Constants.countKnobsPanels -1) {
					kP.getStWin().getResult().setVisible(true);
					kP.getStWin().getStatutL().setText("Sorry, try again");
				}else {
					kP.getStWin().getResult().setVisible(true);
					kP.getStWin().getStatutL().setText("You Win");
				}
			}
		
		
		this.setVisible(false);

	}

	public void createColorPickerPanel() {

		HBox firsLineColorBox = new HBox(4);
		HBox secondLineColorBox = new HBox(4);

		firsLineColorBox.getChildren().addAll(colorButtons[0], colorButtons[1], colorButtons[2]);

		firsLineColorBox.setAlignment(Pos.CENTER);
		secondLineColorBox.setAlignment(Pos.CENTER);

		secondLineColorBox.getChildren().addAll(colorButtons[3], colorButtons[4], colorButtons[5]);

		this.getChildren().addAll(firsLineColorBox, secondLineColorBox);
		this.setAlignment(Pos.CENTER_RIGHT);
		this.setBackground(new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY)));
		
	}

	public int getIndexButton() {
		return indexButton;
	}

	public void setIndexButton(int indexButton) {
		this.indexButton = indexButton;
	}

	public Button[] getColorButtons() {
		return colorButtons;
	}

	public void setColorButtons(Button[] colorButtons) {
		this.colorButtons = colorButtons;
	}

	public KnobPanel getkP() {
		return kP;
	}

	public void setkP(KnobPanel kP) {
		this.kP = kP;
	}
	

}
