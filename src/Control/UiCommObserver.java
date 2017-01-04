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

	private SignUpWindow sUW;
	private SignInWindow sIW;
	private MultiMode multiM;
	private Desk desk;
	private MasterMindRun mMR;
	private LogginLogics lLog;
	private FreePlayersListWindow freePlayerL;
	private NetworkLogics netLog;
	private Logics logics;

	public UiCommObserver(MasterMindRun mMR, LogginLogics lLog, NetworkLogics netLog) {
		this.mMR = mMR;
		this.lLog = lLog;
		this.netLog = netLog;
	}

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
					if (pomData[3].equals("1")) {
						challenger = false;
					} else {
						challenger = true;
					}
					mMR.showReloadGameMesage(Integer.parseInt(pomData[1]), pomData[2], challenger);
					break;
				case "Registrace":

					if (pomData[1].contains("bad1")) {
						sUW.getObserText().inc("This nickname is using");

					} else if (pomData[1].contains("bad2")) {
						
						sUW.getObserText().inc("This nickname is long, length must be less 30");
					
					} else {

						mMR.setWellcomeWindow();
					}

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
					if(pomData.length >0){
						freePlayerL.getListLV().setItems(netLog.creatPlayersList(pomData[1]));						
					}else {
						freePlayerL.getListLV().setItems(netLog.creatPlayersList(""));							
					}

					break;
				case "Logout":
					
				mMR.showLogoutMessage(netLog.getPlayerName(), Integer.parseInt(pomData[1]));
					
					break;
				case "Challenge":

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
					break;
				case "Game":
					if (pomData[1].contains("leave")) {
						mMR.showLeaveMessage(pomData[2],Integer.parseInt(pomData[3]));

					} else if (pomData[1].contains("colorResult")) {
						if(pomData[2].contains("R") && netLog.isChallenger()){
							netLog.setResultR(pomData[3]);
							multiM.getObserText().inc("Find color combination");							
						}else{							
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

					break;
				default:
					break;
				}
			}
		});
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
