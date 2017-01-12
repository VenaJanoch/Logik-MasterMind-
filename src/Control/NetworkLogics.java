package Control;

import java.util.Random;

import Graphics.Desk;
import Graphics.FreePlayersListWindow;
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

	/** Globalni promenne tridy**/
	private TCPComm comm;
	private MasterMindRun mMR;
	private MultiMode multiM;
	private FreePlayersListWindow fPLW;
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
	private int countBackPanel = 0;

	/**
	 * Inicializace objektu mMR a lLog
	 * @param mMR
	 * @param lLog
	 */
	public NetworkLogics(MasterMindRun mMR, LogginLogics lLog) {

		this.mMR = mMR;
		this.setChallenger(true);
		this.setlLog(lLog);

		colors = new Color[Constants.countKnobs];
		colorsIndex = new int[Constants.countKnobs];

	}

	/**
	 * Odesle zadost o ziskani uzivatelu pro dalsi hru
	 */
	public void getFreePlayerList() {

		comm.send("PlayerList,get,\n");
		
	}

	/**
	 * Odesle zpravu s informaci o odhlaseni uzivatel
	 * @param message 
	 */
	public void signOutUser(String message) {

		lLog.setLog(false);
		comm.send(message);
		mMR.setWellcomeWindow();
	}

	/**
	 * Vytvori seznam prihlasenych uzivatelu se kterymi je mozne hrat
	 * @param mesagge
	 * @return
	 */
	public ObservableList<String> creatPlayersList(String mesagge) {

		String[] players = mesagge.split(";");

		ObservableList<String> data = FXCollections.observableArrayList();

		data.addAll(players);

		return data;
	}

	/**
	 * Odesle zpravu s informaci o zadost pro pripojeni daneho hrace do hry
	 * @param playerName
	 */
	public void createGame(String playerName) {
		comm.send("Challenge,invite," + playerName + ",\n");
		fPLW.freezButton();
	}

	/**
	 * Zprava o prijeti vyzvy
	 * @param player
	 */
	public void challengeAccepted(String player) {

		comm.send("Challenge,accept," + player + ",\n");
		mMR.setGameWindowMultiMode();
		multiM.getLogics().setMultiMode(true);

	}

	/**
	 * Zprava o odmituti vyzvy
	 * @param player
	 */
	public void challengeRefuse(String player) {

		comm.send("Challenge,refuse," + player + ",\n");

	}

	/**
	 * Zavola metodu mMR pro messagebox o pozvani do hry
	 * @param player
	 */
	public void createChallengeMesagge(String player) {

		mMR.showPlayerMessage(player);

	}

	/**
	 * Zprava o opusteni hry
	 */
	public void leaveGame() {

		comm.send("Game,leave," + name + ",\n");

	}

	/**
	 * Metoda vyhodnocujici pocet obarvenych tlacitek 
	 * Urcuje zda bylo ve hre dosazeno uspechu ci ne
	 * @param button
	 */
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

			} else if (greatColors != Constants.countKnobs && identifikace == Constants.countKnobsPanels - 1) {

				sendKnobs(multiM.getKnobPanel()[identifikace]);
				sendGameOver();
				setResult();
				multiM.getResult().setVisible(true);
				multiM.getObserText().inc("Sorry, you lost");
				sendGameOver();

			} else {

				sendKnobs(multiM.getKnobPanel()[identifikace]);
				sendGameDone();
				setResult();
				multiM.getResult().setVisible(true);
				multiM.getObserText().inc("You win");
				sendGameDone();
			}
		}

		multiM.getCp().setVisible(false);

	}

	/**
	 * Zprava o prohre vyzivatele 
	 */
	public void sendGameOver() {

		comm.send("Game,gameOver,\n");

	}

	/**
	 * Zprava o vyhre vyzivatele
	 */
	public void sendGameDone() {

		
		comm.send("Game,gameDone,\n");
	}

	/**
	 * Nastaveni zadaneho vysledku od porotihrace
	 */
	public void setResult(){
		for (int i = 0; i < colors.length; i++) {
			
			
			multiM.getResult().getKnobs()[i]
					.setBackground(new Background(new BackgroundFill(colors[i],CornerRadii.EMPTY, Insets.EMPTY)));
			
		}
		multiM.getResult().nothig();
	}
	
	/**
	 * Najde index 
	 * @param knob
	 * @return
	 */
	public int findColorIndex(Knob knob) {

		for (int i = 0; i < Constants.countColorButton; i++) {

			if (knob.getBackground()
					.equals(new Background(new BackgroundFill(Constants.colors[i], CornerRadii.EMPTY, Insets.EMPTY)))) {
				return i;
			}

		}

		return -1;
	}

	/**
	 * Posle zvolenou barevnou kombinaci a jeji vyhodnoceni
	 * @param knobPanel
	 */
	private void sendKnobs(KnobPanel knobPanel) {
		String message = "Game,knobPanel," + knobPanel.getIdentifikace() + ",";

		for (int i = 0; i < knobPanel.getKnobs().length; i++) {

			message = message + findColorIndex(knobPanel.getKnobs()[i]) + ";";

		}

		comm.send(message + "\n");
	
		comm.send("Game,goodColors," + knobPanel.getIdentifikace() + "," + goodColors + "\n");
		comm.send("Game,greatColors," + knobPanel.getIdentifikace() + "," + greatColors + "\n");
		
		multiM.freezDesk();

	}
	

	/**
	 * Nastaveni prijatych barev v tahu
	 * @param identifikace
	 * @param message
	 */
	public void setKnobPanel(int identifikace, String message) {
		multiM.unFreezDesk();
		String[] pomString = message.split(";");

		for (int i = 0; i < Constants.countKnobs; i++) {

			multiM.getKnobPanel()[identifikace].getKnobs()[i]
					.setBackground(new Background(new BackgroundFill(Constants.colors[Integer.parseInt(pomString[i])],
							CornerRadii.EMPTY, Insets.EMPTY)));
		}
		
		multiM.getKnobPanel()[identifikace].nothig();
		
		if (identifikace < Constants.countKnobsPanels-1 ) {
			multiM.getKnobPanel()[identifikace + 1].setVisible(true);
			multiM.getControlKnobPanel()[identifikace + 1].setVisible(true);
			if (!challenger) {
				multiM.getKnobPanel()[identifikace + 1].nothig();				
			}else if(identifikace == 1 && isChallenger()){
				multiM.getKnobPanel()[identifikace - 1].getFunction();;
			}
		}

	}
	/**
	 * Nastaveni prijateho vyhodnoceni poctu pozic barev
	 * @param greatColor1
	 * @param identifikace
	 */
	public void setGreatColor(int greatColor1, int identifikace) {

		for (int i = 0; i < greatColor1; i++) {

			multiM.getControlKnobPanel()[identifikace].getControlKnob()[i].setBackground(
					new Background(new BackgroundFill(Constants.greatChoose, CornerRadii.EMPTY, Insets.EMPTY)));

		}
	}

	/**
	 * Nastaveni prijateho vyhodnoceni poctu barev
	 * @param goodColor1
	 * @param identifikace
	 */
	public void setGoodColor(int goodColor1, int identifikace) {

		for (int i = 0; i < goodColor1; i++) {

			multiM.getControlKnobPanel()[identifikace].getControlKnob()[i].setBackground(
					new Background(new BackgroundFill(Constants.goodChoose, CornerRadii.EMPTY, Insets.EMPTY)));

		}
	}
	/**
	 * Nastaveni prijateho vysledku 2
	 * @param message
	 */
	public void setResult(String message) {

		String[] pomString = message.split(";");
		for (int i = 0; i < Constants.countKnobs; i++) {

			multiM.getResult().getKnobs()[i]
					.setBackground(new Background(new BackgroundFill(Constants.colors[Integer.parseInt(pomString[i])],
							CornerRadii.EMPTY, Insets.EMPTY)));
			colors[i] = Constants.colors[Integer.parseInt(pomString[i])];
			multiM.getResult().nothig();
		}

		kP = multiM.getKnobPanel()[0];
		kP.getFunction();

	}
	/**
	 * Nastaveni prijateho vysledku
	 * @param message
	 */
	public void setResultR(String message) {

		String[] pomString = message.split(";");
		for (int i = 0; i < Constants.countKnobs; i++) {

		colors[i] = Constants.colors[Integer.parseInt(pomString[i])];
		multiM.getKnobPanel()[0].getFunction();
		}
		
		
	}

	/**
	 * Kontrola poctu obarvenych tlacitek
	 * @param kp
	 * @return
	 */
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

	/**
	 * Nacteni vysledku
	 */
	private void loadColorResult() {

		String result = "Game,colorResult,";

		for (int i = 0; i < Constants.countKnobs; i++) {
			for (int j = 0; j < Constants.colors.length; j++) {

				if (kP.getKnobs()[i].getBackground().equals(
						new Background(new BackgroundFill(Constants.colors[j], CornerRadii.EMPTY, Insets.EMPTY)))) {

					colors[i] = Constants.colors[j];
					result = result + j + ";";
				}
			}
		}
		kP.nothig();
	
		multiM.freezDesk();
		multiM.getObserText().inc("Chellanger findig combination");
		comm.send(result + "\n");

	}

	/**
	 * Vyhodnoceni zadanych barve v aktualnim tahu
	 * @param desk
	 * @param identifikace
	 * @return
	 */
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

	/**
	 * Vyhodnoceni nalezenych barev v tahu
	 * @param kP
	 * @param desk
	 */
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

	/**
	 * 
	 * @param color
	 * @return
	 */
	public boolean checkColor(Color color) {

		for (int i = 0; i < checkColors.length; i++) {

			if (checkColors[i] == color) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Vyhodnoceni pozic barev
	 * @param kP
	 * @param desk
	 */
	public void findGreatColors(KnobPanel kP, Desk desk) {

		for (int i = 0; i < Constants.countKnobs; i++) {

			if (kP.getKnobs()[i].getBackground().equals(new Background(
					new BackgroundFill(getColors()[i], CornerRadii.EMPTY, Insets.EMPTY)))) {
				greatColors++;
			}
		}
	}
	
	/**
	 * Odesle dotaz pro kontrolu, zda nema uzivatel rozehranou nejakou hru
	 * @param game
	 */
	public void checkGame(int game){
		
		comm.send("CheckGame,"+ game + ",\n");
		
	}
	
	public void messageAccepted() {
		
		multiM.unFreezDesk();
		
	}
	
	public void invateMessageAccept() {
		fPLW.unFreezButton();
		
	}

	
	/**
	 * Odesle zpravu s informaci o smazani hry ze serveru
	 * @param game
	 */
	public void deleteGame(int game) {
		comm.send("DeleteGame,"+game+",\n");
		
	}
	
	/**
	 * Odesle zpravu s informaci o smazani hry ze serveru
	 * @param game
	 */
	public void deleteGameLeave(int game) {
		comm.send("DeleteGame,"+game+",both\n");
		
	}
	
	
	public void sendSingForm(String nickname, String passwd) {
		comm.send(lLog.createLogMessage(nickname,mMR.getLogLogics().hashPassword(passwd)));
				
			}
	public void sendRegForm() {
		comm.send(lLog.createRegMessage());
	}
	
	/**
	 * Vynulovani pocitadla pro timeouty
	 */
	public void checkConnect() {
		
	comm.setCounterTimeOUt(0);
		
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

	public FreePlayersListWindow getfPLW() {
		return fPLW;
	}

	public void setfPLW(FreePlayersListWindow fPLW) {
		this.fPLW = fPLW;
	}

	
	
	

	

	

	

	

}
