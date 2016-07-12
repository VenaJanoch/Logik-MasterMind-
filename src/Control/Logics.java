package Control;

import Graphics.KnobPanel;
import Graphics.StartWindow;
import javafx.scene.paint.Color;

public class Logics {

	private StartWindow stWin;
	
	public Logics() {
	
	}
	
	
	public boolean controlCountChoosedKnobs(KnobPanel kp){
		
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
	
	
	
	
	
}
