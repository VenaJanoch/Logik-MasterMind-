package Graphics;

import Interfaces.IGameMode;
import Run.MasterMindRun;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MultiMode extends Desk implements IGameMode {

	public MultiMode(MasterMindRun mMR) {
		super(mMR);
		super.getHlavniPanel().setBottom(creatResultPanel());
		
	}

	@Override
	public HBox creatResultPanel() {
		HBox resultPanel = new HBox(5);
		getLogics().setMultiMode(true);
		Label resultLB = new Label("Color result: ");

		resultLB.setFont(Font.font("Verdana", FontWeight.BOLD, 13));

		setResult(new KnobPanel(100, this, getCp()));
		
		resultPanel.getChildren().add(resultLB);
		resultPanel.getChildren().add(getResult());


		return resultPanel;
	}

}
