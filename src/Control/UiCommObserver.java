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

		System.out.println(data);
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				String[] pomData = data.split(",");
				
				switch (pomData[0]) {
				case "Registrace":

					if (pomData[1].contains("bad")) {
						sUW.getObserText().inc("This nickname is using");

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
					freePlayerL.getListLV().setItems(netLog.creatPlayersList(pomData[1]));

					break;
				case "Challenge":

					if (pomData[2].contains("invite")) {

						netLog.createChallengeMesagge(pomData[1]);
					
					}else if (pomData[2].contains("refuse")) {

						mMR.showRefusetMessage(pomData[1]);

					} else {
						
						mMR.showAcceptMessage(pomData[1]);
						netLog.setChallenger(true);
						
						mMR.setGameWindowMultiMode();
						multiM.getObserText().inc("Wait for color combination");
						
					}
					break;
				case "Game":
					if(pomData[2].contains("leave")){
						
						mMR.showLeaveMessage(pomData[1]);
						
					}else if(pomData[1].contains("colorResult")){
						
						netLog.setResult(pomData[2]);
						multiM.getObserText().inc("Find color combination");
						
					}else if(pomData[1].contains("goodColors")){
						
						netLog.setGoodColor(Integer.parseInt(pomData[3]), Integer.parseInt(pomData[2]));
						
					}else if(pomData[1].contains("greatColors")){
						

						netLog.setGreatColor(Integer.parseInt(pomData[3]), Integer.parseInt(pomData[2]));
						netLog.checkGame();
					}else if(pomData[1].contains("knobPanel")){
						
						netLog.setKnobPanel(Integer.parseInt(pomData[2]), pomData[3]);
						
					}else if(pomData[1].contains("gameOver")){
						
						multiM.getObserText().inc("Challenger had failed");
						
					}else if(pomData[1].contains("gameDone")){
						
						multiM.getObserText().inc("Challenger had succeeded");
						
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
