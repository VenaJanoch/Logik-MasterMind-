package Graphics;

import Interfaces.IGameMode;
import Run.MasterMindRun;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SingleMode extends Desk implements IGameMode{

	public SingleMode(MasterMindRun mMR) {
		super(mMR);
		super.getHlavniPanel().setBottom(creatResultPanel());
	}
	
	@Override
	public HBox creatResultPanel() {
		HBox resultPanel = new HBox(5);
		getLogics().setMultiMode(false);
		Label resultLB = new Label("Color result: ");

		resultLB.setFont(Font.font("Verdana", FontWeight.BOLD, 13));

		
		setResult(new KnobPanel(100, this, super.getCp()));
		super.getResult().setVisible(false);
		super.getResult().setResultColor(super.getLogics().creatResultColors());

		resultPanel.getChildren().add(resultLB);
		resultPanel.getChildren().add(super.getResult());

		return resultPanel;
	}
	
	

}
