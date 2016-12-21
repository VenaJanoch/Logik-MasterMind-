package Run;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

import Control.LogginLogics;
import Control.NetworkLogics;
import Control.UiCommObserver;
import Graphics.FreePlayersListWindow;
import Graphics.MultiMode;
import Graphics.ServerWindow;
import Graphics.SignInWindow;
import Graphics.SignUpWindow;
import Graphics.SingleMode;
import Graphics.WellcomeWindow;
import Interfaces.ITCP;
import Network.TCPComm;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MasterMindRun extends Application {

	/**
	 * Metoda main Zavola launch a spusti program
	 * 
	 * @param args
	 */

	/** Atributy tridy **/
	private Stage primaryStage;
	private WellcomeWindow wellcome;
	private SingleMode sM;
	private MultiMode mM;
	private SignUpWindow signUpW;
	private SignInWindow signInW;
	private LogginLogics logLogics;
	private TCPComm comm;
	private ITCP tcp;
	private UiCommObserver m_commObserver;
	private ServerWindow serverWindow;
	private NetworkLogics netLog;
	private FreePlayersListWindow freePlayerL;
	private boolean isServer = true;

	public static void main(String[] args) {

		launch(args);
	}

	/**
	 * Metoda start Prepsana trida pro vytvoreni okna
	 */

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.logLogics = new LogginLogics(this);
		this.netLog = new NetworkLogics(this, logLogics);
		this.primaryStage = primaryStage;

		setServerWindow();

		this.primaryStage.show();
		
		 primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		       @Override
		       public void handle(WindowEvent e) {
		    	   if (logLogics.isLog()) {
		    		   netLog.signOutUser("LogOut,end\n");					
				}
		          Platform.exit();
		          System.exit(0);
		          
		       }
		    });
	}

	public void setStage(Stage stage) {

		this.primaryStage.setTitle(stage.getTitle());
		this.primaryStage.setScene(stage.getScene());

	}

	public void setFreePlayersListWindow(){
		
		this.freePlayerL = new FreePlayersListWindow(this, netLog);
		
		setStage(freePlayerL);
		m_commObserver.setFreePlayerL(freePlayerL);
		
		
	}
	public void setGameWindowSingleMode() {
		// stWindow = new StartWindow(this,true);
		sM = new SingleMode(this);

		setStage(sM);
	}

	public void setGameWindowMultiMode() {
		mM = new MultiMode(this,netLog,logLogics);
		mM.getLogics().setMultiMode(true);
		netLog.setMultiM(mM);
		m_commObserver.setmultiM(mM);
		m_commObserver.setLogics(mM.getLogics());
		setStage(mM);
		

	}

	public void setWellcomeWindow() {
		wellcome = new WellcomeWindow(this,logLogics, netLog);
		//netLog.createDatabase();
		this.setStage(wellcome);

	}

	public void setServerWindow() {

		serverWindow = new ServerWindow(this);
		this.setStage(serverWindow);
	}

	public void createConnect() {
		try {

			comm = new TCPComm(InetAddress.getByAddress(logLogics.getServerAddres()), logLogics.getServerPort(), this);
			
			m_commObserver = new UiCommObserver(this,logLogics,netLog);
			comm.registerObserver(m_commObserver);
		
			logLogics.setComm(comm);
			netLog.setComm(comm);
		} catch (NumberFormatException e) {
		
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Spatne");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		comm.start();
		
	}

	public void setSignUpWindow() {
		signUpW = new SignUpWindow(this);
		m_commObserver.setsUW(signUpW);
		this.setStage(signUpW);
		this.logLogics.setsUW(signUpW);

	}

	public void setSignInWindow() {
		signInW = new SignInWindow(this);
		m_commObserver.setsIW(signInW);
		this.setStage(signInW);
		this.logLogics.setsIW(signInW);
	}

	public void showPlayerMessage(String player){
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Message from player");
		alert.setHeaderText("Player " + player + "  invited you into their game" );
		alert.setContentText("Choose your option.");

		ButtonType submitButton = new ButtonType("Accept");
		ButtonType refuseButton = new ButtonType("Refuse");
		
		alert.getButtonTypes().setAll(submitButton, refuseButton);

		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.get() == submitButton){
			
			netLog.setChallenger(false);
			netLog.challengeAccepted(player);
			
		
		} else{
			netLog.challengeRefuse(player);
	
		}
		
		
	}
	
	public void showLogoutMessage(String player, int game) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Message from player");
		alert.setHeaderText("Player " + player + "  leave game" );
		alert.setContentText("Stay in game.");

		ButtonType submitButton = new ButtonType("Yes");
		ButtonType refuseButton = new ButtonType("Leave");
		
		alert.getButtonTypes().setAll(submitButton, refuseButton);

		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.get() == refuseButton){
			netLog.deleteGameLeave(game);
			setWellcomeWindow();
			
		} 	
	}

public void showAcceptMessage(String player){
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Message from player");
		alert.setHeaderText("Player " + player + " accept invited into your game" );

		ButtonType submitButton = new ButtonType("OK");
		
		alert.getButtonTypes().setAll(submitButton);

		alert.showAndWait();
		
	}

public void showRefusetMessage(String player){
	
	Alert alert = new Alert(AlertType.CONFIRMATION);
	alert.setTitle("Message from player");
	alert.setHeaderText("Player " + player + " refuse invited into your game, choose another player" );


	ButtonType submitButton = new ButtonType("OK");
	
	alert.getButtonTypes().setAll(submitButton);

	Optional<ButtonType> result = alert.showAndWait();
	
	if (result.get() == submitButton){
		
	netLog.getFreePlayerList();
	
	} 	
	
}
public void showNoServer(){
	Alert alert = new Alert(AlertType.CONFIRMATION);
	alert.setTitle("Message from Server");
	alert.setHeaderText("Server is inaccessible" );

	ButtonType submitButton = new ButtonType("OK");
	
	alert.getButtonTypes().setAll(submitButton);

	Optional<ButtonType> result = alert.showAndWait();
		
}
public void showLeaveMessage(String player){
	
	Alert alert = new Alert(AlertType.CONFIRMATION);
	alert.setTitle("Message from player");
	alert.setHeaderText("Player " + player + " leave game" );

	ButtonType submitButton = new ButtonType("OK");
	
	alert.getButtonTypes().setAll(submitButton);

	Optional<ButtonType> result = alert.showAndWait();
	
	if (result.get() == submitButton){
		
		setWellcomeWindow();
	
	} 	
	
}

public void showReloadGameMesage(int game, String player, boolean challenger) {
	System.out.println(player);
	Alert alert = new Alert(AlertType.CONFIRMATION);
	alert.setTitle("Message from player");
	alert.setHeaderText("You have game with " + player);

	ButtonType submitButton = new ButtonType("Back");
	ButtonType refuseButton = new ButtonType("Cancel");
	
	alert.getButtonTypes().setAll(submitButton, refuseButton);

	Optional<ButtonType> result = alert.showAndWait();
	
	if (result.get() == submitButton){
		
		netLog.setChallenger(challenger);
		setGameWindowMultiMode();
		netLog.checkGame(game);
		
	
	}else{
		netLog.deleteGame(game);
	}
	
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

	public TCPComm getComm() {
		return comm;
	}

	public void setComm(TCPComm comm) {
		this.comm = comm;
	}

	public UiCommObserver getM_commObserver() {
		return m_commObserver;
	}

	public void setM_commObserver(UiCommObserver m_commObserver) {
		this.m_commObserver = m_commObserver;
	}

	public NetworkLogics getNetLog() {
		return netLog;
	}

	public void setNetLog(NetworkLogics netLog) {
		this.netLog = netLog;
	}

	public FreePlayersListWindow getFreePlayerL() {
		return freePlayerL;
	}

	public void setFreePlayerL(FreePlayersListWindow freePlayerL) {
		this.freePlayerL = freePlayerL;
	}

	public boolean isServer() {
		return isServer;
	}

	public void setServer(boolean isServer) {
		this.isServer = isServer;
	}

	

	

}
