package Network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import Control.NetworkLogics;
import Graphics.ServerWindow;
import Interfaces.ICommObserver;
import Interfaces.ITCP;
import Run.MasterMindRun;
import javafx.application.Platform;

public class TCPComm implements ITCP, Runnable {

	private ICommObserver m_observer;
	OutputStream m_output;
	private InetAddress address;
	private int port;
	private boolean isServer = true;
	private MasterMindRun mMR;

	public TCPComm(InetAddress address, int port, MasterMindRun mMR) {
		this.address = address;
		this.port = port;
		this.mMR = mMR;

	}

	// ---------------------------------------------------------
	@Override
	public void send(String data) {
		assert (data != null);
		assert (m_output != null);
		System.out.println(data);
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
			Socket socket = new Socket(address, port);
			socket.setSoTimeout(10);
			m_output = socket.getOutputStream();
			InputStream input = socket.getInputStream();

			// TODO quit
			for (;;) {
				byte[] buffer = new byte[1024];
				int count = input.read(buffer);
				if (count > 0) {
					String data = new String(buffer, 0, count);
					if (m_observer != null) {
						m_observer.processData(data);
					}
				}
			}
		} catch (IOException e) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
			
				mMR.showNoServer();
				System.err.println("Caught IOException: " + e.getMessage());
				
				}				
				});
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

}
