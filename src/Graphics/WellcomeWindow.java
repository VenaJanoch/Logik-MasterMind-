package Graphics;

import Control.Constants;
import Run.MasterMindRun;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import sun.net.www.content.audio.basic;

public class WellcomeWindow extends Stage{

	
	private MasterMindRun mMR;
	private Scene newScena;
	private BorderPane hlavniPanel;
	private Button singlePlayer;
	private Button multiPlayer;

	public WellcomeWindow(MasterMindRun mMR) {
		
		super();
		this.mMR = mMR;
		
		this.setTitle("MasterMind-Menu");
		hlavniPanel = new BorderPane();
		singlePlayer = new Button("Single mode");
		multiPlayer = new Button("Multi mode");
		this.setScene(creatScene());
		
	}

	private Scene creatScene() {
		
		newScena = new Scene(creatPanel(), 310, 600);
		return newScena;
	}

	private Parent creatPanel() {
		
		singlePlayer.setMinSize(200, 150);
		multiPlayer.setMinSize(200, 150);
		
		singlePlayer.setBackground(new Background(new BackgroundFill(Constants.menuButtonC,
				CornerRadii.EMPTY, Insets.EMPTY)));
		
		multiPlayer.setBackground(new Background(new BackgroundFill(Constants.menuButtonC,
				CornerRadii.EMPTY, Insets.EMPTY)));
		
		hlavniPanel.setBackground(new Background(new BackgroundFill(Constants.menuBackgroundC,
				CornerRadii.EMPTY, Insets.EMPTY)));
		
		singlePlayer.setFont(Font.font("Verdana", FontWeight.BOLD, 23));
		multiPlayer.setFont(Font.font("Verdana", FontWeight.BOLD, 23));
		
		singlePlayer.setOnAction(event -> mMR.setGameWindowSingleMode());
		multiPlayer.setOnAction(event -> mMR.setGameWindowMultiMode());
		
		VBox menu = new VBox(10);
		menu.getChildren().addAll(singlePlayer,multiPlayer);
		
		
		hlavniPanel.setCenter(menu);
		BorderPane.setAlignment(menu, Pos.CENTER);
		hlavniPanel.setPadding(new Insets(55));
		return hlavniPanel;
	}
	
	
	
}
