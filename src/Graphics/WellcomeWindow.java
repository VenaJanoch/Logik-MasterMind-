package Graphics;

import Control.Constants;
import Run.MasterMindRun;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class WellcomeWindow extends Stage{

	
	private MasterMindRun mMR;
	private Scene newScena;
	private BorderPane hlavniPanel;
	private HBox logBarPanel;
	private Button singlePlayer;
	private Button multiPlayer;
	private Button signInB;
	private Button signUpB;
	private Button signOutB;
	
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
		
		singlePlayer.setMinSize(205, 150);
		multiPlayer.setMinSize(205, 150);
		
		singlePlayer.setBackground(Constants.buttonBackground);
		
		multiPlayer.setBackground(Constants.buttonBackground);
		
		singlePlayer.setFont(Font.font("Verdana", FontWeight.BOLD, 23));
		multiPlayer.setFont(Font.font("Verdana", FontWeight.BOLD, 23));
		
		singlePlayer.setOnAction(event -> mMR.setGameWindowSingleMode());
		multiPlayer.setOnAction(event -> multiModeControl());
		
		VBox menu = new VBox(10);
		menu.getChildren().addAll(singlePlayer,multiPlayer);
		
		
		hlavniPanel.setBackground(Constants.menuBackground);
		
		hlavniPanel.setCenter(menu);
		BorderPane.setAlignment(menu, Pos.CENTER);
		hlavniPanel.setPadding(new Insets(55));
		
		hlavniPanel.setTop(createLogBar());
		return hlavniPanel;
	}

	private void multiModeControl(){
		
		if (!mMR.getLogLogics().isLog()) {
			
			Alert alert = new Alert(javafx.scene.control.Alert.AlertType.WARNING);
			alert.setTitle("Sign error");
			alert.setHeaderText("No body sign!");
			alert.setContentText("If you would play multi mod, you must be sign player !");
			alert.showAndWait();
			
	 
		}else {
			
			mMR.setGameWindowMultiMode();
		}
		
	}
	private Node createLogBar() {
		
		logBarPanel = new HBox(5);
		
		signInB = new Button("Sign in");
		signUpB = new Button("Sign up");
		signOutB = new Button("Sign out");
		
		signInB.setOnAction(event -> mMR.setSignInWindow());
		signUpB.setOnAction(event -> mMR.setSignUpWindow());
		
		
		signInB.setMinSize(100, 50);
		signUpB.setMinSize(100, 50);
		signOutB.setMinSize(100, 50);
		
		signInB.setBackground(new Background(new BackgroundFill(Constants.menuButtonC,
				CornerRadii.EMPTY, Insets.EMPTY)));
		
		signUpB.setBackground(new Background(new BackgroundFill(Constants.menuButtonC,
				CornerRadii.EMPTY, Insets.EMPTY)));
		
		signOutB.setBackground(new Background(new BackgroundFill(Constants.menuButtonC,
				CornerRadii.EMPTY, Insets.EMPTY)));
		
		
		logBarPanel.setPadding(new Insets(0, 0, 10, 0));
		
		signInB.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
		signUpB.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

		logBarPanel.getChildren().addAll(signInB,signUpB);
		
		return logBarPanel;
	}
	
	
	
}
