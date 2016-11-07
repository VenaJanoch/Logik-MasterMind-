package Control;

import Graphics.MultiMode;
import Graphics.SignInWindow;
import Graphics.SignUpWindow;
import Interfaces.ICommObserver;
import Run.MasterMindRun;
import javafx.application.Platform;

public class UiCommObserver implements ICommObserver {

	private SignUpWindow sUW;
	private SignInWindow sIW;
	private MultiMode mM;
	private MasterMindRun mMR;
	private LogginLogics lLog;

	public UiCommObserver(MasterMindRun mMR, LogginLogics lLog) {
		this.mMR = mMR;
		this.lLog = lLog;

	}

	public void processData(String data) {

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
					
					if(pomData[1].contains("yes")){
						lLog.setLog(true);
						mMR.setWellcomeWindow();
						
					}else if(pomData[1].contains("no") && pomData[2].contains("badLog")){
							sIW.getObserText().inc("This nickname is not using");
					}else{
						sIW.getObserText().inc("Bad password");
						
					}
					break;
				default:
					break;
				}
			}
		});
	}

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

	public MultiMode getmM() {
		return mM;
	}

	public void setmM(MultiMode mM) {
		this.mM = mM;
	}

}
