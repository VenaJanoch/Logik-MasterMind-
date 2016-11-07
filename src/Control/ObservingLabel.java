package Control;
import java.util.Observable;
import java.util.Observer;

import javafx.scene.control.Label;


public class ObservingLabel extends Label implements Observer {

	@Override
	public void update(Observable o, Object arg) {
		// when observed value is changed, text in the label is changed as well
		setText(arg + "");		
	}

}