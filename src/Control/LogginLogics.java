package Control;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import Graphics.SignInWindow;
import Graphics.SignUpWindow;
import Network.TCPComm;
import Run.MasterMindRun;
import javafx.scene.control.Alert;

public class LogginLogics {

	private boolean isLog = false;
	private String userNick;
	
	private String name;
	private String surname;
	private String nickname;
	private String passwd;
	private String passwd2;

	private String serverAddres;
	private int serverPort;

	private SignUpWindow sUW;
	private SignInWindow sIW;
	private MasterMindRun mMR;
	private TCPComm comm;

	
	public LogginLogics(MasterMindRun mMR) {
		//serverAddres = new byte[4];
		this.mMR = mMR;
		
	
	}

	public boolean confirmDataInForm(String name, String surname, String nickname, String passwd, String passwd2) {

		if (nameConfirm(name) && surnameConfirm(surname) && nicknameConfirm(nickname)
				&& passwdsConfirm(passwd, passwd2)) {
			return true;
		}

		// Upravit
		return false;

	}

	public boolean confirmDataInServerForm(String addres, String port) {

		if (serverAddresConfirm(addres) && serverPortConfirm(port)) {
			return true;
		}
		// Upravit
		return false;

	}

	public String createRegMessage(){
		
		return "Registrace," + getNickname() + "," + getPasswd() + "\n" ; 
		
	}
	
	
	
	public String createLogMessage(String nick, String passwd){
		
		return "Log," + nick + "," + passwd + "\n" ; 
		
	}

	public boolean confirmDataInForm(String nickname, String passwd) {

		if (nicknameConfirm(nickname) && passwdConfirm(passwd)){
			return true;
		}
		
		return false;

	}

	public boolean serverAddresConfirm(String addres) {
		if (addres.length() == 0) {
			Alert alert = new Alert(javafx.scene.control.Alert.AlertType.WARNING);
			alert.setTitle("Sign error");
			alert.setHeaderText("No IP address!");
			alert.setContentText("You must fill IP address of server !");
			alert.showAndWait();

		} else {
			//convertIPAddress(addres.split("."));
			setServerAddres(addres);
			return true;
		}

		return false;
	}

	/*public void convertIPAddress(String[] ip) {

		for (int i = 0; i < ip.length; i++) {
			serverAddres[i] = Byte.parseByte(ip[i]);
		}

	}*/

	public boolean serverPortConfirm(String port) {
		if (port.equals("nickname") || port.length() == 0) {
			Alert alert = new Alert(javafx.scene.control.Alert.AlertType.WARNING);
			alert.setTitle("Sign error");
			alert.setHeaderText("No nickname!");
			alert.setContentText("You must fill nickname !");
			alert.showAndWait();

		} else {
			this.serverPort = Integer.parseInt(port);
			return true;
		}

		return false;
	}

	public boolean nicknameConfirm(String nickname) {
		if (nickname.equals("nickname") || nickname.length() == 0) {
			Alert alert = new Alert(javafx.scene.control.Alert.AlertType.WARNING);
			alert.setTitle("Sign error");
			alert.setHeaderText("No nickname!");
			alert.setContentText("You must fill nickname !");
			alert.showAndWait();

		} else {
			this.nickname = nickname;
			return true;
		}

		return false;
	}

	public boolean surnameConfirm(String surname) {

		if (surname.equals("surname") || surname.length() == 0) {
			Alert alert = new Alert(javafx.scene.control.Alert.AlertType.WARNING);
			alert.setTitle("Sign error");
			alert.setHeaderText("No surname!");
			alert.setContentText("You must fill surname !");
			alert.showAndWait();

		} else {
			this.surname = surname;
			return true;
		}
		return false;
	}

	public boolean nameConfirm(String name) {

		if (name.equals("name") || name.length() == 0) {
			Alert alert = new Alert(javafx.scene.control.Alert.AlertType.WARNING);
			alert.setTitle("Sign error");
			alert.setHeaderText("No name!");
			alert.setContentText("You must fill name !");
			alert.showAndWait();

		} else {
			this.name = name;
			return true;
		}

		return false;
	}

	public boolean passwdConfirm(String passwd) {
		if (passwd.equals(Constants.nullConstat)) {
			Alert alert = new Alert(javafx.scene.control.Alert.AlertType.WARNING);
			alert.setTitle("Sign error");
			alert.setHeaderText("No password!");
			alert.setContentText("You must fill password !");
			alert.showAndWait();

		} else {
			this.passwd = passwd;
			return true;
		}

		return false;
	}

	public boolean passwd2Confirm(String passwd2) {

		if (passwd2.equals(Constants.nullConstat)) {
			Alert alert = new Alert(javafx.scene.control.Alert.AlertType.WARNING);
			alert.setTitle("Sign error");
			alert.setHeaderText("No password!");
			alert.setContentText("You must fill confirm password !");
			alert.showAndWait();

		} else {
			this.passwd2 = passwd2;

			return true;
		}

		return false;
	}

	public boolean passwdsConfirm(String passwd, String passwd2) {

		if (passwdConfirm(passwd) && passwd2Confirm(passwd2)) {
			if (!passwd.equals(passwd2)) {

				Alert alert = new Alert(javafx.scene.control.Alert.AlertType.WARNING);
				alert.setTitle("Sign error");
				alert.setHeaderText("Bad passwords!");
				alert.setContentText("Passwords is not same!");
				alert.showAndWait();
				sUW.getPasswdTF().setText("");
				sUW.getPasswd2TF().setText("");

			} else {
				return true;
			}
		}

		return false;

	}

	public String hashPassword(String original) {

		StringBuffer sb = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");

			md.update(original.getBytes());

			byte[] digest = md.digest();
			for (byte b : digest) {
				sb.append(String.format("%02x", b & 0xff));
			}

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();

		}
	
		return sb.toString();

	}

	/*** Getrs and Setrs ***/

	public boolean isLog() {
		return isLog;
	}

	public void setLog(boolean isLog) {
		this.isLog = isLog;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getPasswd2() {
		return passwd2;
	}

	public void setPasswd2(String passwd2) {
		this.passwd2 = passwd2;
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

	public String getServerAddres() {
		return serverAddres;
	}

	public void setServerAddres(String serverAddres) {
		this.serverAddres = serverAddres;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getUserNick() {
		return userNick;
	}

	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}

	public TCPComm getComm() {
		return comm;
	}

	public void setComm(TCPComm comm) {
		this.comm = comm;
	}
	

}
