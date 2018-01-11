package Run;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.Scanner;

import com.sun.javafx.font.LogicalFont;

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

public class MasterMindRun{

	/**
	 * Metoda main Zavola launch a spusti program
	 * 
	 * @param args
	 */

	/** Atributy tridy **/
	private LogginLogics logLogics;
	private TCPComm comm;
	private ITCP tcp;
	private UiCommObserver m_commObserver;
	private NetworkLogics netLog;
	private boolean isServer = true;
	private Scanner sc;
	public MasterMindRun() {
		
		this.logLogics = new LogginLogics(this);
		this.netLog = new NetworkLogics(this, logLogics);
		this.sc = new Scanner(System.in);
		prihlaseni();
		createConnect();
		
	}
	
	

	public void prihlaseni() {
		
		System.out.println("'Zadej adresu serveru");
		String addr = sc.nextLine();
		System.out.println("'Zadej port serveru");
		String port = sc.nextLine();
		logLogics.confirmDataInServerForm(addr, port);
		
	}
	
	
	
	public static void main(String[] args) {
		
		MasterMindRun run = new MasterMindRun();
		
	}	

	/**
	 * createConnect() Vytvori spojeni se serverem
	 * 
	 */
	public void createConnect() {
		try {

			comm = new TCPComm(logLogics.getServerAddres(), logLogics.getServerPort());

			//m_commObserver = new UiCommObserver(this, logLogics, netLog,serverWindow,mM);
			comm.registerObserver(m_commObserver);

			logLogics.setComm(comm);
			netLog.setComm(comm);
		} catch (NumberFormatException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Spatne");
		}
		comm.start();

	}

	
	/*** Setrs and Getrs ***/


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
	public boolean isServer() {
		return isServer;
	}

	public void setServer(boolean isServer) {
		this.isServer = isServer;
	}

}
