package Control;

import Network.TCPComm;
import Run.MasterMindRun;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NetworkLogics {

	private TCPComm comm;
	private MasterMindRun mMR;
	private String playerName;
	private boolean challenger;
	
	
	public NetworkLogics(MasterMindRun mMR) {

		this.mMR = mMR;
		this.setChallenger(true);
		

	}

	public void getFreePlayerList() {

		comm.send("PlayerList,get\n");

	}

	public ObservableList<String> creatPlayersList(String mesagge) {
		
		String[] players = mesagge.split(";");
		
		
		ObservableList<String> data = FXCollections.observableArrayList();

		data.addAll(players);
		
		return data;
	}
	
	public void createGame(String playerName){
		
		comm.send("Challenge,invite," + playerName + "\n");
		
	}

	public void challengeAccepted(String player){

		comm.send("Challenge,accept," + player + "\n");
		mMR.setGameWindowMultiMode();

	}
	
	public void challengeRefuse(String player){

		comm.send("Challenge,refuse," + player + "\n");

	}
	
	public void createChallengeMesagge(String player){
		
		mMR.showPlayerMessage(player);
		
		
	}
	
	/***************************************** Getrs and Setrs *******************/

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

	
	
}
