package Graphics;

import Control.Constants;
import Interfaces.ICommObserver;
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
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import jdk.nashorn.internal.runtime.NativeJavaPackage;

public class SignUpWindow extends Stage {

	private MasterMindRun mMR;

	private Scene newScena;

	private BorderPane hlavniPanel;
	private GridPane netPanel;

	private Label nameLB;
	private Label surnameLB;
	private Label nicknameLB;
	private Label passwdLB;
	private Label passwdLB2;

	private TextField nameTF;
	private TextField surnameTF;
	private TextField nicknameTF;
	private PasswordField passwdTF;
	private PasswordField passwd2TF;

	private Button confirmBT;
	private Button backBT;

	private Label regLB;
	
	
	private ITCP m_comm;
	private Control.UiCommObserver m_commObserver;


	public SignUpWindow(MasterMindRun mMR) {

		super();
		this.mMR = mMR;
		m_comm = mMR.getComm();
		this.setTitle("MasterMind-Sing up");
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

	private Node createNet() {

		netPanel = new GridPane();

		nameLB = new Label("Name");
		surnameLB = new Label("Surname");
		nicknameLB = new Label("Nickname");
		passwdLB = new Label("Password");
		passwdLB2 = new Label("Password again");

		nameTF = new TextField("name");
		surnameTF = new TextField("surname");
		nicknameTF = new TextField("nickname");
		passwdTF = new PasswordField();
		passwd2TF = new PasswordField();

		confirmBT = new Button("OK");

		backBT = new Button("Back");

		backBT.setOnAction(event -> mMR.setWellcomeWindow());

		backBT.setBackground(Constants.buttonBackground);

		backBT.setFont(Constants.buttonFontSmall);

		confirmBT.setOnAction(event -> confirmForm());

		confirmBT.setBackground(Constants.buttonBackground);

		confirmBT.setFont(Constants.buttonFontSmall);

		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(50);

		netPanel.getColumnConstraints().add(column1);

		netPanel.add(nameLB, 0, 0);
		netPanel.add(nameTF, 1, 0);
		netPanel.add(surnameLB, 0, 1);
		netPanel.add(surnameTF, 1, 1);
		netPanel.add(nicknameLB, 0, 2);
		netPanel.add(nicknameTF, 1, 2);
		netPanel.add(passwdLB, 0, 3);
		netPanel.add(passwdTF, 1, 3);
		netPanel.add(passwdLB2, 0, 4);
		netPanel.add(passwd2TF, 1, 4);

		netPanel.add(backBT, 0, 5);
		netPanel.add(confirmBT, 1, 5);

		netPanel.setHgap(10);
		netPanel.setVgap(10);

		netPanel.setBackground(Constants.menuBackground);
		return netPanel;
	}

	private void confirmForm() {

		if (mMR.getLogLogics().confirmDataInForm(nameTF.getText(), surnameTF.getText(), nicknameTF.getText(),
				mMR.getLogLogics().hashPassword(passwdTF.getText()),
				mMR.getLogLogics().hashPassword(passwd2TF.getText()))) {

			regLB = new Label();
			
			netPanel.add( regLB,0, 6);
			
			m_commObserver = new Control.UiCommObserver(regLB);
		    m_comm.registerObserver(m_commObserver);

		    
		    m_comm.send(mMR.getLogLogics().createMessage());
		    mMR.setWellcomeWindow();
	         
		}

	}
	
	

	/*** Getrs and Setrs **/
	public TextField getSurnameTF() {
		return surnameTF;
	}

	public void setSurnameTF(TextField surnameTF) {
		this.surnameTF = surnameTF;
	}

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

	public PasswordField getPasswd2TF() {
		return passwd2TF;
	}

	public void setPasswd2TF(PasswordField passwd2tf) {
		passwd2TF = passwd2tf;
	}

	public Label getRegLB() {
		return regLB;
	}

	public void setRegLB(Label regLB) {
		this.regLB = regLB;
	}

}
