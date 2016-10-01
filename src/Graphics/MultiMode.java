package Graphics;

import Interfaces.IGameMode;
import Run.MasterMindRun;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MultiMode extends Desk implements IGameMode {

	public MultiMode(MasterMindRun mMR) {
		super(mMR);
		getHlavniPanel().setBottom(creatResultPanel());
		getHlavniPanel().setLeft(creatLegendPanel());
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
	
	@Override
	public void resetDesk(){

		getHlavniPanel().setCenter(creatGameDesk());
		getHlavniPanel().setLeft(creatLegendPanel());
		getHlavniPanel().setBottom(creatResultPanel());

	}

	@Override
	public Node creatLegendPanel() {

		VBox legendPanel = new VBox(4);
		HBox buttonPanel = new HBox(4);

		super.statutL = new Label("Find color combination");
		super.retryB = new Button("Retry");
		closeB = new Button("Exit");

		statutL.setFont(Font.font("Verdana", FontWeight.BOLD, 13));

		retryB.setOnAction(event -> resetDesk());
		closeB.setOnAction(event -> getmMR().getPrimaryStage().close());

		buttonPanel.getChildren().addAll(retryB, closeB);
		buttonPanel.setAlignment(Pos.CENTER);
		legendPanel.getChildren().addAll(getCp(), statutL, buttonPanel);

		legendPanel.setAlignment(Pos.CENTER);

		legendPanel.setMaxWidth(170);
		legendPanel.setMinWidth(170);

		return legendPanel;

	}

}
