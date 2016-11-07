package Control;

import Network.TCPComm;
import Run.MasterMindRun;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NetworkLogics {

	TCPComm comm;
	MasterMindRun mMR;

	public NetworkLogics(MasterMindRun mMR) {

		this.mMR = mMR;
		

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
		
		comm.send("ChoosePlayer," + playerName + "\n");
		
	}

	/***************************************** Getrs and Setrs *******************/

	public TCPComm getComm() {
		return comm;
	}

	public void setComm(TCPComm comm) {
		this.comm = comm;
	}

	
	
}
