package Graphics;

import Control.Constants;
import Run.MasterMindRun;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ServerWindow extends Stage {

	private MasterMindRun mMR;

	private Scene newScena;

	private BorderPane hlavniPanel;
	private GridPane netPanel;

	private Label serverAddresLB;
	private Label serverPortLB;

	private TextField serverAddresTF;
	private TextField serverPortTF;

	private Button confirmBT;
	
	private boolean isServer = true;

	/**
	 * Incializace objektu mMR
	 * @param mMR
	 */
	public ServerWindow(MasterMindRun mMR) {

		super();
		this.mMR = mMR;

		this.setTitle("MasterMind-Set server");
		hlavniPanel = new BorderPane();

		this.setScene(creatScene());

	}

	private Scene creatScene() {
		newScena = new Scene(creatPanel(), 310, 600);
		return newScena;
	}

	private Parent creatPanel() {

		hlavniPanel.setCenter(createNet());

		hlavniPanel.setBackground(
				new Background(new BackgroundFill(Constants.menuBackgroundC, CornerRadii.EMPTY, Insets.EMPTY)));

		BorderPane.setAlignment(netPanel, Pos.CENTER);
		hlavniPanel.setPadding(new Insets(55));

		return hlavniPanel;

	}

	/**
	 * createNet()
	 * Vytvoreni prihlasovaciho formulare
	 * @return
	 */
	private Node createNet() {

		netPanel = new GridPane();

		serverAddresLB = new Label("Server IP");
		serverPortLB = new Label("Port");

		serverAddresTF = new TextField("localhost");
		serverPortTF = new TextField("22434");

		confirmBT = new Button("OK");

		confirmBT.setOnAction(event -> confirmForm());

		confirmBT.setBackground(Constants.buttonBackground);

		confirmBT.setFont(Constants.buttonFontSmall);

		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(40);

		netPanel.getColumnConstraints().add(column1);

		netPanel.add(serverAddresLB, 0, 0);
		netPanel.add(serverAddresTF, 1, 0);
		netPanel.add(serverPortLB, 0, 1);
		netPanel.add(serverPortTF, 1, 1);

		netPanel.add(confirmBT, 0, 5);

		netPanel.setHgap(10);
		netPanel.setVgap(10);

		netPanel.setBackground(Constants.menuBackground);
		return netPanel;

	}

	/**
	 * confirmForm()
	 * Overeni vstupnich poli
	 */
	private void confirmForm() {
		if (mMR.getLogLogics().confirmDataInServerForm(serverAddresTF.getText(), serverPortTF.getText())) {
			mMR.createConnect(); 
		}

	}
	
	/** Getrs and Setrs **/
	public boolean isServer() {
		return isServer;
	}

	public void setServer(boolean isServer) {
		this.isServer = isServer;
	}

	

}
