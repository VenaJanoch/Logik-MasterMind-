package Control;

import java.util.Random;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import Graphics.Desk;
import Graphics.KnobPanel;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class Logics {

	private Desk desk;
	private Random r;
	private Color[] colors;
	private Color[] checkColors;
	private int checkIndex;
	private int greatColors = 0;
	private int catchColors = 0;
	private int goodColors = 0;
	private KnobPanel kP;
	private int indexButton;
	private boolean multiMode;

	public Logics(Desk desk, boolean multiMode) {
		// this.stWin = stWin;

		this.desk = desk;
		this.multiMode = multiMode;
		r = new Random();
		colors = new Color[Constants.countKnobs];

	}

	public void returnColor(Object button) {

		Button tmpButton = (Button) button;

		kP.getKnobs()[indexButton]
				.setBackground(new Background(new BackgroundFill(Constants.colors[Integer.parseInt(tmpButton.getId())],
						CornerRadii.EMPTY, Insets.EMPTY)));
		kP.getKnobs()[indexButton].setObarven(true);
		int identifikace = kP.getIdentifikace();

		if (controlCountChoosedKnobs(kP) && resultControl()) {

			if (evaluate(kP.getDesk(), identifikace)) {
				desk.getKnobPanel()[identifikace].nothig();
				desk.getKnobPanel()[identifikace + 1].setVisible(true);
				desk.getControlKnobPanel()[identifikace + 1].setVisible(true);
			} else if (controlCountChoosedKnobs(kP) && identifikace == Constants.countKnobsPanels - 1) {
				desk.getResult().setVisible(true);
				desk.getStatutL().setText("Sorry, try again");
			} else {
				desk.getResult().setVisible(true);
				desk.getStatutL().setText("You Win");
			}
		}

		desk.getCp().setVisible(false);

	}

	private boolean resultControl() {

		if (multiMode) {

			kP.setVisible(false);
			loadColorResult();
			this.multiMode = false;
			return false;
		}

		return true;
	}

	private void loadColorResult() {

		for (int i = 0; i < Constants.countKnobs; i++) {
			for (int j = 0; j < Constants.colors.length; j++) {

				if (kP.getKnobs()[i].getBackground().equals(new Background(
						new BackgroundFill(Constants.colors[j], CornerRadii.EMPTY, Insets.EMPTY)))) {

					colors[i] = Constants.colors[j];
					System.out.println(j);
				}
			}
		}
	}

	public boolean controlCountChoosedKnobs(KnobPanel kp) {

		int count = 0;
		for (int i = 0; i < Constants.countKnobs; i++) {

			if (kp.getKnobs()[i].isObarven()) {
				count++;
			}

		}

		if (count == Constants.countKnobs) {
			return true;
		}

		return false;
	}

	public Color[] creatResultColors() {

		int randomc = -1;

		for (int i = 0; i < Constants.countKnobs; i++) {

			randomc = r.nextInt(Constants.countColorButton);
			colors[i] = Constants.colors[randomc];

			System.out.println(randomc + " logic");
		}

		return colors;
	}

	public boolean evaluate(Desk desk, int identifikace) {

		greatColors = 0;
		catchColors = 0;
		goodColors = 0;

		checkColors = new Color[Constants.countKnobs];
		checkIndex = 0;

		findGreatColors(desk.getKnobPanel()[identifikace], desk);
		findGoodColors(desk.getKnobPanel()[identifikace], desk);

		for (int i = 0; i < goodColors; i++) {

			desk.getControlKnobPanel()[identifikace].getControlKnob()[i].setBackground(
					new Background(new BackgroundFill(Constants.goodChoose, CornerRadii.EMPTY, Insets.EMPTY)));
		}

		for (int i = 0; i < greatColors; i++) {

			desk.getControlKnobPanel()[identifikace].getControlKnob()[i].setBackground(
					new Background(new BackgroundFill(Constants.greatChoose, CornerRadii.EMPTY, Insets.EMPTY)));

		}

		if (greatColors == 4) {
			return false;
		} else if (identifikace == Constants.countKnobsPanels - 1) {
			return false;
		}

		return true;

	}

	private void findGoodColors(KnobPanel kP, Desk desk) {

		for (int i = 0; i < Constants.countKnobs; i++) {
			for (int j = 0; j < colors.length; j++) {

				if (kP.getKnobs()[i].getBackground()
						.equals(new Background(
								new BackgroundFill(desk.getLogics().colors[j], CornerRadii.EMPTY, Insets.EMPTY)))
						&& checkColor(desk.getLogics().colors[j])) {

					checkColors[checkIndex] = desk.getLogics().colors[j];
					checkIndex++;
					goodColors++;

				}
			}
		}

	}

	private boolean checkColor(Color color) {

		for (int i = 0; i < checkColors.length; i++) {

			if (checkColors[i] == color) {
				return false;
			}
		}

		return true;
	}

	private void findGreatColors(KnobPanel kP, Desk desk) {

		for (int i = 0; i < Constants.countKnobs; i++) {

			if (kP.getKnobs()[i].getBackground().equals(
					new Background(new BackgroundFill(desk.getLogics().colors[i], CornerRadii.EMPTY, Insets.EMPTY)))) {
				greatColors++;
			}
		}

	}

	/********** Getrs and setrs ********/
	
	public KnobPanel getkP() {
		return kP;
	}

	public void setKp(KnobPanel kp) {
		kP = kp;
	}

	public int getIndexButton() {
		return indexButton;
	}

	public void setIndexButton(int indexButton) {
		this.indexButton = indexButton;
	}

	public boolean isMultiMode() {
		return multiMode;
	}

	public void setMultiMode(boolean multiMode) {
		this.multiMode = multiMode;
	}

	public Color[] getColors() {
		return colors;
	}

	public void setColors(Color[] colors) {
		this.colors = colors;
	}
	
	

}
