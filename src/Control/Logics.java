package Control;

import java.util.Random;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import Graphics.KnobPanel;
import Graphics.StartWindow;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class Logics {

	private StartWindow stWin;
	private Random r;
	private Color[] colors;
	private Color[] checkColors;
	private int checkIndex;
	private int greatColors = 0;
	private int catchColors = 0;
	private int goodColors = 0;

	public Logics() {

		r = new Random();
		colors = new Color[Constants.countKnobs];

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

			System.out.println(randomc);
		}

		return colors;

	}

	public boolean evaluate(StartWindow stWin, int identifikace) {

		greatColors = 0;
		catchColors = 0;
		goodColors = 0;
		
		checkColors = new Color[4];
		checkIndex = 0;
		
		findGreatColors(stWin.getKnobPanel()[identifikace], stWin);
		findGoodColors(stWin.getKnobPanel()[identifikace], stWin);

		for (int i = 0; i < goodColors; i++) {

			stWin.getControlKnobPanel()[identifikace].getControlKnob()[i].setBackground(
					new Background(new BackgroundFill(Constants.goodChoose, CornerRadii.EMPTY, Insets.EMPTY)));
		}

		for (int i = 0; i < greatColors; i++) {

			stWin.getControlKnobPanel()[identifikace].getControlKnob()[i].setBackground(
					new Background(new BackgroundFill(Constants.greatChoose, CornerRadii.EMPTY, Insets.EMPTY)));

		}

		if (greatColors == 4) {
			return false;
		}

		return true;

	}

	private void findGoodColors(KnobPanel kP, StartWindow stWin) {

		for (int i = 0; i < Constants.countKnobs; i++) {
			for (int j = 0; j < colors.length; j++) {
				
					if (kP.getKnobs()[i].getBackground().equals(new Background(
							new BackgroundFill(stWin.getLogics().colors[j], CornerRadii.EMPTY, Insets.EMPTY))) && checkColor(stWin.getLogics().colors[j])) {
						
						checkColors[checkIndex] = stWin.getLogics().colors[j];
						goodColors++;
						
				}
			}
		}

	}
	
	private boolean checkColor(Color color){
		
		for (int i = 0; i < checkColors.length; i++) {
			
			if (checkColors[i] == color) {
				return false;
			}
		}
		
		return true;
	}

	private void findGreatColors(KnobPanel kP, StartWindow stWin) {

		for (int i = 0; i < Constants.countKnobs; i++) {

			if (kP.getKnobs()[i].getBackground().equals(
					new Background(new BackgroundFill(stWin.getLogics().colors[i], CornerRadii.EMPTY, Insets.EMPTY)))) {
				greatColors++;
			}
		}

	}

}
