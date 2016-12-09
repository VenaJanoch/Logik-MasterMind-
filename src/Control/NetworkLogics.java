package Control;

import java.util.Random;

import Graphics.Desk;
import Graphics.Knob;
import Graphics.KnobPanel;
import Graphics.MultiMode;
import Network.TCPComm;
import Run.MasterMindRun;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class NetworkLogics {

	private TCPComm comm;
	private MasterMindRun mMR;
	private MultiMode multiM;
	private String playerName;
	private String name;
	private boolean challenger;

	private Color[] colors;
	private int[] colorsIndex;
	private Color[] checkColors;
	private int checkIndex;
	private int greatColors = 0;
	private int catchColors = 0;
	private int goodColors = 0;
	private KnobPanel kP;
	private int indexButton;
	private LogginLogics lLog;

	public NetworkLogics(MasterMindRun mMR, LogginLogics lLog) {

		this.mMR = mMR;
		this.setChallenger(true);
		this.setlLog(lLog);
		colors = new Color[Constants.countKnobs];
		colorsIndex = new int[Constants.countKnobs];

	}

	public void getFreePlayerList() {

		comm.send("PlayerList,get\n");

	}

	public void signOutUser(String message) {

		lLog.setLog(false);
		comm.send(message);
		mMR.setWellcomeWindow();
	}

	public ObservableList<String> creatPlayersList(String mesagge) {

		String[] players = mesagge.split(";");

		ObservableList<String> data = FXCollections.observableArrayList();

		data.addAll(players);

		return data;
	}

	public void createGame(String playerName) {

		comm.send("Challenge,invite," + playerName + "\n");

	}

	public void challengeAccepted(String player) {

		comm.send("Challenge,accept," + player + "\n");
		
		mMR.setGameWindowMultiMode();
		multiM.getLogics().setMultiMode(true);

	}

	public void challengeRefuse(String player) {

		comm.send("Challenge,refuse," + player + "\n");

	}

	public void createChallengeMesagge(String player) {

		mMR.showPlayerMessage(player);

	}

	public void leaveGame() {

		comm.send("Game,leave," + name + "\n");

	}

	public void returnColor(Object button) {

		Button tmpButton = (Button) button;

		kP.getKnobs()[indexButton]
				.setBackground(new Background(new BackgroundFill(Constants.colors[Integer.parseInt(tmpButton.getId())],
						CornerRadii.EMPTY, Insets.EMPTY)));

		kP.getKnobs()[indexButton].setObarven(true);

		int identifikace = kP.getIdentifikace();

		if (controlCountChoosedKnobs(kP)) {

			if (evaluate(kP.getDesk(), identifikace)) {
				multiM.getKnobPanel()[identifikace].nothig();
				multiM.getKnobPanel()[identifikace + 1].setVisible(true);
				multiM.getControlKnobPanel()[identifikace + 1].setVisible(true);

				sendKnobs(multiM.getKnobPanel()[identifikace]);

			} else if (controlCountChoosedKnobs(kP) && identifikace == Constants.countKnobsPanels - 1) {

				sendKnobs(multiM.getKnobPanel()[identifikace]);
				sendGameOver();
				multiM.getResult().setVisible(true);
				multiM.getObserText().inc("Sorry, you lost");

			} else {

				sendKnobs(multiM.getKnobPanel()[identifikace]);
				sendGameDone();
				multiM.getResult().setVisible(true);
				multiM.getObserText().inc("You win");
			}
		}

		multiM.getCp().setVisible(false);

	}

	public void sendGameOver() {

		comm.send("Game,gameOver");

	}

	public void sendGameDone() {

		comm.send("Game,gameDone");
	}

	public int findColorIndex(Knob knob) {

		for (int i = 0; i < Constants.countColorButton; i++) {

			if (knob.getBackground()
					.equals(new Background(new BackgroundFill(Constants.colors[i], CornerRadii.EMPTY, Insets.EMPTY)))) {
				return i;
			}

		}

		return -1;
	}

	private void sendKnobs(KnobPanel knobPanel) {
		String message = "Game,knobPanel," + knobPanel.getIdentifikace() + ",";

		for (int i = 0; i < knobPanel.getKnobs().length; i++) {

			message = message + findColorIndex(knobPanel.getKnobs()[i]) + ";";

		}

		comm.send(message + "\n");
	
		comm.send("Game,goodColors," + knobPanel.getIdentifikace() + "," + goodColors + ",\n");
		comm.send("Game,greatColors," + knobPanel.getIdentifikace() + "," + greatColors + ",\n");
		
		

	}
	
	public void sendAnswer(){
		comm.send("Answer,\n");
	}

	public void setKnobPanel(int identifikace, String message) {

		System.out.println("knobs " + message);
		String[] pomString = message.split(";");

		for (int i = 0; i < Constants.countKnobs; i++) {

			multiM.getKnobPanel()[identifikace].getKnobs()[i]
					.setBackground(new Background(new BackgroundFill(Constants.colors[Integer.parseInt(pomString[i])],
							CornerRadii.EMPTY, Insets.EMPTY)));

		}
		if (identifikace <= Constants.countControlKnobsLine) {
			multiM.getKnobPanel()[identifikace + 1].setVisible(true);
			multiM.getControlKnobPanel()[identifikace + 1].setVisible(true);
		}

	}

	public void setGreatColor(int greatColor1, int identifikace) {

		for (int i = 0; i < greatColor1; i++) {

			multiM.getControlKnobPanel()[identifikace].getControlKnob()[i].setBackground(
					new Background(new BackgroundFill(Constants.greatChoose, CornerRadii.EMPTY, Insets.EMPTY)));

		}
	}

	public void setGoodColor(int goodColor1, int identifikace) {

		for (int i = 0; i < goodColor1; i++) {

			multiM.getControlKnobPanel()[identifikace].getControlKnob()[i].setBackground(
					new Background(new BackgroundFill(Constants.goodChoose, CornerRadii.EMPTY, Insets.EMPTY)));

		}
	}

	public void setResult(String message) {

		String[] pomString = message.split(";");
		for (int i = 0; i < Constants.countKnobs; i++) {

			multiM.getResult().getKnobs()[i]
					.setBackground(new Background(new BackgroundFill(Constants.colors[Integer.parseInt(pomString[i])],
							CornerRadii.EMPTY, Insets.EMPTY)));
			colors[i] = Constants.colors[Integer.parseInt(pomString[i])];
		}

		kP = multiM.getKnobPanel()[0];

	}

	public boolean controlCountChoosedKnobs(KnobPanel kp) {

		int count = 0;
		for (int i = 0; i < Constants.countKnobs; i++) {

			if (kp.getKnobs()[i].isObarven()) {
				count++;
			}

		}

		if (count == Constants.countKnobs) {

			if (kp.getIdentifikace() == 100) {

				loadColorResult();
				return false;

			}

			return true;

		}

		return false;
	}

	private void loadColorResult() {

		String result = "Game,colorResult,";

		for (int i = 0; i < Constants.countKnobs; i++) {
			for (int j = 0; j < Constants.colors.length; j++) {

				if (kP.getKnobs()[i].getBackground().equals(
						new Background(new BackgroundFill(Constants.colors[j], CornerRadii.EMPTY, Insets.EMPTY)))) {

					colors[i] = Constants.colors[j];
					result = result + j + ";";
					System.out.println(j + " net");
				}
			}
		}

		System.out.println(result);
		multiM.getObserText().inc("Chellanger findig combination");
		comm.send(result + "\n");
		

	}

	public boolean evaluate(Desk desk, int identifikace) {

		greatColors = 0;
		catchColors = 0;
		goodColors = 0;

		checkColors = new Color[Constants.countKnobs];
		checkIndex = 0;

		findGreatColors(desk.getKnobPanel()[identifikace], desk);
		findGoodColors(desk.getKnobPanel()[identifikace], desk);

		for (int i = 0; i < goodColors; i++) {
			desk.getControlKnobPanel()[identifikace].getControlKnob()[i].setBackground(
					new Background(new BackgroundFill(Constants.goodChoose, CornerRadii.EMPTY, Insets.EMPTY)));
		}

		for (int i = 0; i < greatColors; i++) {

			desk.getControlKnobPanel()[identifikace].getControlKnob()[i].setBackground(
					new Background(new BackgroundFill(Constants.greatChoose, CornerRadii.EMPTY, Insets.EMPTY)));

		}

		if (greatColors == 4) {
			return false;
		} else if (identifikace == Constants.countKnobsPanels - 1) {
			return false;
		}

		return true;

	}

	public void findGoodColors(KnobPanel kP, Desk desk) {

		for (int i = 0; i < Constants.countKnobs; i++) {
			for (int j = 0; j < colors.length; j++) {

				if (kP.getKnobs()[i].getBackground()
						.equals(new Background(
								new BackgroundFill(desk.getNetLog().getColors()[j], CornerRadii.EMPTY, Insets.EMPTY)))
						&& checkColor(desk.getNetLog().getColors()[j])) {

					checkColors[checkIndex] = desk.getNetLog().getColors()[j];
					checkIndex++;
					goodColors++;

				}
			}
		}

	}

	public boolean checkColor(Color color) {

		for (int i = 0; i < checkColors.length; i++) {

			if (checkColors[i] == color) {
				return false;
			}
		}

		return true;
	}

	public void findGreatColors(KnobPanel kP, Desk desk) {

		for (int i = 0; i < Constants.countKnobs; i++) {

			if (kP.getKnobs()[i].getBackground().equals(new Background(
					new BackgroundFill(desk.getNetLog().getColors()[i], CornerRadii.EMPTY, Insets.EMPTY)))) {
				greatColors++;
			}
		}
	}
	
	public void checkGame(int game){
		
		comm.send("CheckGame,"+ game + "\n");
		
	}
	
	public void deleteGame(int game) {
		comm.send("DeleteGame,"+game+"\n");
		
	}
	
	public void createDatabase(){
		
		comm.send("Challenge,invite,ja\n");
		comm.send("Challenge,accept,test\n");
		comm.send("Game,knobPanel,0,1;2;3;4;\n");
		comm.send("Game,goodColors,0,2,\n");
		comm.send("Game,greatColors,0,1,\n");
		
		
		
	}

	/*****************************************
	 * Getrs and Setrs
	 *******************/

	public TCPComm getComm() {
		return comm;
	}

	public void setComm(TCPComm comm) {
		this.comm = comm;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public boolean isChallenger() {
		return challenger;
	}

	public void setChallenger(boolean challenger) {
		this.challenger = challenger;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MultiMode getMultiM() {
		return multiM;
	}

	public void setMultiM(MultiMode multiM) {
		this.multiM = multiM;
	}

	public KnobPanel getkP() {
		return kP;
	}

	public void setKp(KnobPanel kp) {
		kP = kp;
	}

	public int getIndexButton() {
		return indexButton;
	}

	public void setIndexButton(int indexButton) {
		this.indexButton = indexButton;
	}

	public Color[] getColors() {
		return colors;
	}

	public void setColors(Color[] colors) {
		this.colors = colors;
	}

	public LogginLogics getlLog() {
		return lLog;
	}

	public void setlLog(LogginLogics lLog) {
		this.lLog = lLog;
	}

	

}
