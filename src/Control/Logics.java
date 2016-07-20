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
	private int greatColors = 0;
	private int catchColors = 0;
	
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
	
		
		for (int i = 0; i < Constants.countKnobs; i++) {
			
			colors[i] = Constants.colors[r.nextInt(Constants.countColorButton)];
			
		}
		
		
		return colors;

	}

	public void evaluate(StartWindow stWin, int identifikace) {
		
			greatColors = 0;
			catchColors = 0;
		
		findGreatColors(stWin.getKnobPanel()[identifikace], stWin);
		findGoodColors(stWin.getKnobPanel()[identifikace], stWin);
			
			
		for (int j = 0; j < catchColors; j++) {
			if (j<greatColors) {
				
				stWin.getControlKnobPanel()[identifikace].getControlKnob()[j].setBackground(new Background(new BackgroundFill(Constants.greatChoose,
						CornerRadii.EMPTY, Insets.EMPTY)));
				
			}else {
				stWin.getControlKnobPanel()[identifikace].getControlKnob()[j].setBackground(new Background(new BackgroundFill(Constants.goodChoose,
						CornerRadii.EMPTY, Insets.EMPTY)));
				
			}
		}
		
		
	}

	private void findGoodColors(KnobPanel kP,StartWindow stWin) {
		
		int goodColors = 0;
		
		for (int i = 0; i < Constants.countKnobs; i++) {
			for (int j = 0; j < colors.length; j++) {
				
				if (kP.getKnobs()[i].getBackground().equals(new Background(new BackgroundFill(stWin.getLogics().colors[j],
						CornerRadii.EMPTY, Insets.EMPTY)))) {
					goodColors++;
				}
			}
		}
		
		catchColors = goodColors - greatColors;
	}

	private void findGreatColors(KnobPanel kP,StartWindow stWin) {
		
		for (int i = 0; i < Constants.countKnobs; i++) {

			if (kP.getKnobs()[i].getBackground().equals(new Background(new BackgroundFill(stWin.getLogics().colors[i],
					CornerRadii.EMPTY, Insets.EMPTY)))){
				greatColors++;
			}
		}
		
		
	}

}
