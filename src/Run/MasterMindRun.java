package Run;

import java.net.InetAddress;
import java.net.UnknownHostException;

import Control.LogginLogics;
import Graphics.MultiMode;
import Graphics.ServerWindow;
import Graphics.SignInWindow;
import Graphics.SignUpWindow;
import Graphics.SingleMode;
import Graphics.StartWindow;
import Graphics.WellcomeWindow;
import Interfaces.ITCP;
import Network.TCPComm;
import javafx.application.Application;
import javafx.stage.Stage;

public class MasterMindRun extends Application{
	
	
	/**
	 * Metoda main
	 * Zavola launch a spusti program 
	 * @param args
	 */
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/** Atributy tridy **/
	private Stage primaryStage;
	private StartWindow stWindow;
	private WellcomeWindow wellcome;
	private SingleMode sM;
	private MultiMode mM;
	private SignUpWindow signUpW;
	private SignInWindow signInW;
	private LogginLogics logLogics;
	private TCPComm comm;
	private ITCP tcp;
	private ServerWindow serverWindow;
	
	
	
	/**
	 * Metoda start
	 * Prepsana trida pro vytvoreni okna
	 */
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.logLogics = new LogginLogics();
		this.primaryStage = primaryStage;
		setServerWindow();
	
		   
		this.primaryStage.show();
	}


	public void setStage(Stage stage){
		
		this.primaryStage.setTitle(stage.getTitle());
		this.primaryStage.setScene(stage.getScene());

	}

	public void setGameWindowSingleMode(){
		//stWindow = new StartWindow(this,true);
		sM= new SingleMode(this);
		
		setStage(sM);
	}
	
	public void setGameWindowMultiMode(){
		mM = new MultiMode(this);
		setStage(mM);
	}
	
	
	public void setWellcomeWindow(){
		wellcome = new WellcomeWindow(this);
		
		try {
			comm = new TCPComm(InetAddress.getByAddress(logLogics.getServerAddres()),logLogics.getServerPort());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Spatne");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    comm.start();
		
		this.setStage(wellcome);
		
	}
	
	public void setServerWindow(){
		serverWindow = new ServerWindow(this);
		this.setStage(serverWindow);
	}
	
	public void setSignUpWindow(){
		signUpW = new SignUpWindow(this);
		this.setStage(signUpW);
		this.logLogics.setsUW(signUpW);

	}

	public void setSignInWindow(){
		signInW = new SignInWindow(this);
		this.setStage(signInW);
		this.logLogics.setsIW(signInW);
	}
	/*** Setrs and Getrs ***/
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}



	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}


	public SignUpWindow getSignUpW() {
		return signUpW;
	}


	public void setSignUpW(SignUpWindow signUpW) {
		this.signUpW = signUpW;
	}


	public SignInWindow getSignInW() {
		return signInW;
	}


	public void setSignInW(SignInWindow signInW) {
		this.signInW = signInW;
	}


	public LogginLogics getLogLogics() {
		return logLogics;
	}


	public void setLogLogics(LogginLogics logLogics) {
		this.logLogics = logLogics;
	}


	public ITCP getTcp() {
		return tcp;
	}


	public void setTcp(ITCP tcp) {
		this.tcp = tcp;
	}



}
