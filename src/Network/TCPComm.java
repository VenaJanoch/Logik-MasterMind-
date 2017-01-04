package Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

import Control.NetworkLogics;
import Graphics.ServerWindow;
import Interfaces.ICommObserver;
import Interfaces.ITCP;
import Run.MasterMindRun;
import javafx.application.Platform;

public class TCPComm implements ITCP, Runnable {

	private ICommObserver m_observer;
	OutputStream m_output;
	BufferedReader input;
	private String address;
	private int port;
	private boolean isServer = true;
	private MasterMindRun mMR;
	private int counterTimeOUt = 0;
	private boolean connect = true;

	public TCPComm(String address, int port, MasterMindRun mMR) {

		this.address = address;
		this.port = port;
		this.mMR = mMR;

	}

	// ---------------------------------------------------------
	@Override
	public void send(String data) {
		assert (data != null);
		assert (m_output != null);
		try {
			m_output.write(data.getBytes());
		} catch (IOException e) {
			System.err.println("Caught IOException: " + e.getMessage());
		}
	}

	// ---------------------------------------------------------

	@Override
	public void registerObserver(ICommObserver observer) {
		m_observer = observer;
	}

	// ---------------------------------------------------------

	public void run() {

				try {
					Socket socket = new Socket();
					socket.connect(new InetSocketAddress(address, port));
					socket.setSoTimeout(30000);
					m_output = socket.getOutputStream();
					input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					
					for (;;) {
						try {

							String data = new String();
							if ((data = input.readLine()) != null) {
								if (m_observer != null) {
									m_observer.processData(data);
									counterTimeOUt = 0;
								}
							}else{
								m_observer.processData("NoServer");
								break;
							}

						} catch (SocketTimeoutException e) {

							send("CheckConnect,\n");
							System.err.println("Caught TimeoutException: " + counterTimeOUt + " " + e.getMessage());

							if (counterTimeOUt > 0) {
								m_observer.processData("NoServer");
								socket.close();
								m_output.close();
								input.close();
							}

							counterTimeOUt++;
						}

					}

				} catch (IOException e) {
					m_observer.processData("NoServer");
					System.err.println("Caught IOException: " + e.getMessage());

				}

			}
		
	
	// ---------------------------------------------------------

	public void start() {

		(new Thread(this)).start();

	}

	public boolean isServer() {
		return isServer;
	}

	public void setServer(boolean isServer) {
		this.isServer = isServer;
	}

	public int getCounterTimeOUt() {
		return counterTimeOUt;
	}

	public void setCounterTimeOUt(int counterTimeOUt) {
		this.counterTimeOUt = counterTimeOUt;
	}

}
