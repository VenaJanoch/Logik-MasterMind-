package Control;

import Graphics.Desk;
import Graphics.FreePlayersListWindow;
import Graphics.MultiMode;
import Graphics.SignInWindow;
import Graphics.SignUpWindow;
import Interfaces.ICommObserver;
import Run.MasterMindRun;
import javafx.application.Platform;

public class UiCommObserver implements ICommObserver {

	/** Globani promenne tridy **/
	private SignUpWindow sUW;
	private SignInWindow sIW;
	private MultiMode multiM;
	private Desk desk;
	private MasterMindRun mMR;
	private LogginLogics lLog;
	private FreePlayersListWindow freePlayerL;
	private NetworkLogics netLog;
	private Logics logics;

	/**
	 * Inicializace objektu mMR, lLog, netLog
	 * 
	 * @param mMR
	 * @param lLog
	 * @param netLog
	 */
	public UiCommObserver(MasterMindRun mMR, LogginLogics lLog, NetworkLogics netLog) {
		this.mMR = mMR;
		this.lLog = lLog;
		this.netLog = netLog;
	}

	/**
	 * Metoda pro zpracovani prijatych zprav ze serveru
	 */
	public void processData(String data) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				System.out.println("Data " + data + " " + netLog.getName());
				String[] pomData = data.split(",");
				switch (pomData[0]) {

				case "NoServer":
					mMR.showNoServer();
					break;

				case "CheckConnect":
					netLog.checkConnect();
					break;
				case "Connect":
					mMR.setWellcomeWindow();
					break;

				case "Reload":
					boolean challenger;
					if (pomData[3].equals("0")) {
						challenger = false;
					} else {
						challenger = true;
					}
					mMR.showReloadGameMesage(Integer.parseInt(pomData[1]), pomData[2], challenger);
					break;
				case "Registrace":
					receiveRegistrace(pomData);
					break;
				case "Log":

					if (pomData[1].contains("yes")) {
						lLog.setLog(true);
						netLog.setName(sIW.getNicknameTF().getText());
						mMR.setWellcomeWindow();

					} else if (pomData[1].contains("no") && pomData[2].contains("badLog")) {
						sIW.getObserText().inc("This nickname is not using");
					} else {
						sIW.getObserText().inc("Bad password");

					}
					break;
				case "PlayerList":
					if (pomData.length > 1) {
						freePlayerL.getListLV().setItems(netLog.creatPlayersList(pomData[1]));
					} else {
						freePlayerL.getListLV().setItems(netLog.creatPlayersList(""));
					}

					break;
				case "Logout":

					mMR.showLogoutMessage(netLog.getPlayerName(), Integer.parseInt(pomData[1]));

					break;
				case "Challenge":
					receiveChallenge(pomData);
					
					break;
				case "Game":
					receive_game(pomData);

					break;
				default:
					System.out.println("Invalid input");
					break;
				}
			}

		});
	}
	/**
	 * Pomocna metoda pro zpracovani zprav o registraci
	 * @param pomData
	 */
	private void receiveRegistrace(String[] pomData) {
		if (pomData[1].contains("bad1")) {
			sUW.getObserText().inc("This nickname is using");

		} else if (pomData[1].contains("bad2")) {

			sUW.getObserText().inc("This nickname is long, length must be less 30");

		} else {

			mMR.setWellcomeWindow();
		}
		
	}

	/**
	 * Pomocna metoda pro zpracovani zprav o hre
	 * @param pomData
	 */
	private void receive_game(String[] pomData) {
		if (pomData[1].contains("leave")) {
			mMR.showLeaveMessage(pomData[2], Integer.parseInt(pomData[3]));

		} else if (pomData[1].contains("colorResult")) {
		
			if (pomData[2].contains("R") && netLog.isChallenger()) {
				netLog.setResultR(pomData[3]);
				multiM.getObserText().inc("Find color combination");
				
			} else if(pomData[2].contains("R") && !netLog.isChallenger()) {
			
				netLog.setResultR(pomData[3]);
				multiM.getObserText().inc("Challenger find combination");
			
			} else {
				netLog.setResult(pomData[2]);
				multiM.getObserText().inc("Find color combination");
			}

		} else if (pomData[1].contains("goodColors")) {

			netLog.setGoodColor(Integer.parseInt(pomData[3]), Integer.parseInt(pomData[2]));

		} else if (pomData[1].contains("greatColors")) {

			netLog.setGreatColor(Integer.parseInt(pomData[3]), Integer.parseInt(pomData[2]));

		} else if (pomData[1].contains("knobPanel")) {

			netLog.setKnobPanel(Integer.parseInt(pomData[2]), pomData[3]);

		} else if (pomData[1].contains("gameOver")) {

			multiM.getObserText().inc("Challenger had failed");

		} else if (pomData[1].contains("gameDone")) {

			multiM.getObserText().inc("Challenger had succeeded");

		} else if (pomData[1].contains("player")) {

			netLog.setPlayerName(pomData[3]);

		}

	}

	/**
	 * Pomocna metoda pro zpracovani zprava o vyzve
	 * @param pomData
	 */
	private void receiveChallenge(String[] pomData) {
		if (pomData[2].contains("invite")) {

			netLog.createChallengeMesagge(pomData[1]);

		} else if (pomData[2].contains("refuse")) {

			mMR.showRefusetMessage(pomData[1]);

		} else {

			mMR.showAcceptMessage(pomData[1]);
			netLog.setChallenger(true);

			mMR.setGameWindowMultiMode();
			multiM.getObserText().inc("Wait for color combination");

		}
		
	}

	/*************** Getrs and Setrs ******************/
	public SignUpWindow getsUW() {
		return sUW;
	}

	public void setsUW(SignUpWindow sUW) {
		this.sUW = sUW;
	}

	public SignInWindow getsIW() {
		return sIW;
	}

	public void setsIW(SignInWindow sIW) {
		this.sIW = sIW;
	}

	public MultiMode getmultiM() {
		return multiM;
	}

	public void setmultiM(MultiMode multiM) {
		this.multiM = multiM;
	}

	public FreePlayersListWindow getFreePlayerL() {
		return freePlayerL;
	}

	public void setFreePlayerL(FreePlayersListWindow freePlayerL) {
		this.freePlayerL = freePlayerL;
	}

	public Desk getDesk() {
		return desk;
	}

	public void setDesk(Desk desk) {
		this.desk = desk;
	}

	public Logics getLogics() {
		return logics;
	}

	public void setLogics(Logics logics) {
		this.logics = logics;
	}

}
