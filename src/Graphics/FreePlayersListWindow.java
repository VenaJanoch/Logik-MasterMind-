package Graphics;

import Control.NetworkLogics;
import Run.MasterMindRun;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FreePlayersListWindow extends Stage {

	/** Globalni promenne tridy**/
	
	private MasterMindRun mMR;
	private Scene newScena;
	private BorderPane hlavniPanel;
	private NetworkLogics netLog;

	private ListView<String> listLV;

	public FreePlayersListWindow(MasterMindRun mMR, NetworkLogics netLog) {

		super();

		this.mMR = mMR;
		this.netLog = netLog;
		this.setTitle("MasterMind-PlayersList");

		this.setScene(creatScene());

	}

	public Scene creatScene() {

		newScena = new Scene(creatPanel(), 310, 600);
		return newScena;

	}

	/**
	 * Metoda pro vytvoreni korenoveho panelu okna
	 * 
	 * @return BorderPane
	 */
	public Parent creatPanel() {

		hlavniPanel = new BorderPane();
		hlavniPanel.setCenter(creatList());
		hlavniPanel.setBottom(createMenuBar());

		hlavniPanel.setBackground(new Background(new BackgroundFill(Color.BURLYWOOD, CornerRadii.EMPTY, Insets.EMPTY)));
		hlavniPanel.setPadding(new Insets(8));
		return hlavniPanel;
	}

	/**
	 * Metoda pro vytvoreni menu panelu
	 * 
	 */
	private Node createMenuBar() {

		FlowPane controlPane = new FlowPane();

		// creating buttons
		Button readBTN = new Button("Refresh");
		Button selectBTN = new Button("Select");
		Button back	= new Button("Back");
		readBTN.setOnAction(event -> netLog.getFreePlayerList());
		selectBTN.setOnAction(event -> processSelection());
		back.setOnAction(event -> mMR.setWellcomeWindow());
		controlPane.getChildren().addAll(readBTN, selectBTN, back);

		controlPane.getChildren().forEach(node -> FlowPane.setMargin(node, new Insets(3)));
		controlPane.getChildren().forEach(node -> {
			((Button) node).setPrefWidth(80);
		});

		controlPane.setAlignment(Pos.CENTER);
		controlPane.setPadding(new Insets(5));

		return controlPane;

	}

	/**
	 * processSelection()
	 * Naplni seznam prijatymi hraci 
	 */
	private void processSelection() {

		ObservableList<String> selection = FXCollections
				.observableArrayList(listLV.getSelectionModel().getSelectedItems());

		if (selection.size() == 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setContentText("Please select some lines before processing them!");
			alert.setTitle("Selection error");
			alert.setHeaderText("Nothing is selected!");
			alert.showAndWait();
			// when at least one item is selected, all selected items are
			// displayed in the dialog
		} else {
			netLog.setPlayerName(selection.get(0));
			netLog.createGame(selection.get(0));
			
		}

	}

	private Node creatList() {
		
		listLV = new ListView<String>();
		
		listLV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		listLV.setEditable(false);
		BorderPane.setMargin(listLV, new Insets(5));
		
		netLog.getFreePlayerList();
		
		return listLV;
	}
	private ObservableList<String> getFreePlayers() {
		// TODO Auto-generated method stub
		return null;
	}


	/********************** Getrs and Setrs ******/

	public ListView<String> getListLV() {
		return listLV;
	}

	public void setListLV(ListView<String> listLV) {
		this.listLV = listLV;
	}

}
