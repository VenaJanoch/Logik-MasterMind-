package Control;


import Interfaces.ICommObserver;
import javafx.scene.control.Label;

public class UiCommObserver implements ICommObserver{

	private Label m_label;

	  public UiCommObserver(Label label)
	  {
	    m_label = label;
	  }

	  public void processData(String data)
	  {
	    // TODO sync with GUI thread
	   System.out.println(data);
	  }

}
