package Graphics;

import Control.Constants;
import Control.ObservableText;
import Control.ObservingLabel;
import Interfaces.ITCP;
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

public class SignInWindow extends Stage {

	/** Globalni promenne tridy**/
	private MasterMindRun mMR;

	private Scene newScena;

	private BorderPane hlavniPanel;
	private GridPane netPanel;

	private Label nicknameLB;
	private Label passwdLB;

	private TextField nicknameTF;
	private PasswordField passwdTF;

	private Button confirmBT;
	private Button backBT;

	private ObservingLabel logLB;

	private ObservableText obserText = new ObservableText("");

	private ITCP m_comm;

	/**
	 * Inicializace objektu mMR
	 * @param mMR
	 */
	public SignInWindow(MasterMindRun mMR) {

		super();
		this.mMR = mMR;

		this.setTitle("MasterMind-Sing in");
		hlavniPanel = new BorderPane();
		m_comm = mMR.getComm();
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
		logLB = new ObservingLabel();
		hlavniPanel.setBottom(logLB);
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

		nicknameLB = new Label("Nickname");
		passwdLB = new Label("Password");

		nicknameTF = new TextField("nickname");
		passwdTF = new PasswordField();

		confirmBT = new Button("OK");

		backBT = new Button("Back");

		backBT.setOnAction(event -> mMR.setWellcomeWindow());

		backBT.setBackground(Constants.buttonBackground);

		backBT.setFont(Constants.buttonFontSmall);

		confirmBT.setOnAction(event -> confirmForm());

		confirmBT.setBackground(Constants.buttonBackground);

		confirmBT.setFont(Constants.buttonFontSmall);

		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(40);

		netPanel.getColumnConstraints().add(column1);

		netPanel.add(nicknameLB, 0, 0);
		netPanel.add(nicknameTF, 1, 0);
		netPanel.add(passwdLB, 0, 1);
		netPanel.add(passwdTF, 1, 1);

		netPanel.add(backBT, 0, 5);
		netPanel.add(confirmBT, 1, 5);
		Button server = new Button("Server");
				server.setOnAction(event -> mMR.setServerWindow());
				
		netPanel.add(server, 2,5);		
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

		if (mMR.getLogLogics().confirmDataInForm(nicknameTF.getText(),
				mMR.getLogLogics().hashPassword(passwdTF.getText()))) {

			obserText.addObserver(logLB);
			m_comm.send(mMR.getLogLogics().createLogMessage(nicknameTF.getText(),
					mMR.getLogLogics().hashPassword(passwdTF.getText())));

		}
	}

	/*** Getrs and Setrs **/

	public TextField getNicknameTF() {
		return nicknameTF;
	}

	public void setNicknameTF(TextField nicknameTF) {
		this.nicknameTF = nicknameTF;
	}

	public PasswordField getPasswdTF() {
		return passwdTF;
	}

	public void setPasswdTF(PasswordField passwdTF) {
		this.passwdTF = passwdTF;
	}

	public ObservableText getObserText() {
		return obserText;
	}

	public void setObserText(ObservableText obserText) {
		this.obserText = obserText;
	}

}
