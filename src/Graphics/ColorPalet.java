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
			colorButtons[i].setOnAction(event -> logics.returnColor(event.getSource()));

		}
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

	public Logics getLogics() {
		return logics;
	}

	public void setLogics(Logics logics) {
		this.logics = logics;
	}

	
	
	

}
