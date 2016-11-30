package Network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import Interfaces.ICommObserver;
import Interfaces.ITCP;


public class TCPComm implements ITCP, Runnable {
	
	private ICommObserver m_observer;
	OutputStream m_output;
	private InetAddress address;
	private int port;

	
	public TCPComm(InetAddress address, int port) {
		this.address = address;
		this.port = port;
		
		
	
	
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
			Socket socket = new Socket(address, port);
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
			System.err.println("Caught IOException: " + e.getMessage());
		}
	}

	// ---------------------------------------------------------

	public void start() {
		
		(new Thread(this)).start();
	}

	
}
