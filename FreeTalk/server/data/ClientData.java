/**
 * 
 */
package server.data;

import interfaces.OutgoingInterface;
import interfaces.TCPOutgoingInterface;
import interfaces.UDPOutgoingInterface;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import messages.CallMeMessage;
import messages.ConnectionId;
import server.handler.HandlerThread;
import util.Consts.ConnectionMethod;
import util.Consts.ResponseCode;

public class ClientData {

	private String name;
	private InetAddress ip;
	private int port1;
	private int port2;
	private ResponseCode port1open; 
	private ResponseCode port2open; 

	private Socket tcp80;	
	private List<HandlerThread> threads;

	public Socket callMeSocket;

	public ClientData(String name, InetAddress ip, int port1, int port2, ResponseCode port1open, ResponseCode port2open, Socket tcp80) {
		super();
		this.name = name;
		this.ip = ip;
		this.port1 = port1;
		this.port2 = port2;
		this.port1open = port1open;
		this.port2open = port2open;
		this.tcp80 = tcp80;
		this.threads = new LinkedList<HandlerThread>();
	}


	public InetAddress getIp() {
		return ip;
	}


	public void setIp(InetAddress ip) {
		synchronized (this) {
			this.ip = ip;
		}
	}


	public int getPort1() {
		return port1;
	}


	public void setPort1(int port1) {
		synchronized (this) {
			this.port1 = port1;
		}
	}


	public boolean isPort1open() {
		return port1open != ResponseCode.BAD;
	}


	public void setPort1open(ResponseCode port1open) {
		synchronized (this) {
			this.port1open = port1open;
		}
	}


	public int getPort2() {
		return port2;
	}


	public void setPort2(int port2) {
		synchronized (this) {
			this.port2 = port2;
		}
	}


	public boolean isPort2open() {
		return port2open != ResponseCode.BAD;
	}


	public void setPort2open(ResponseCode port2open) {
		synchronized (this) {
			this.port2open = port2open;
		}
	}


	public Socket getTcp80() {
		return tcp80;
	}


	public void setTcp80(Socket tcp80) {
		synchronized (this) {
			this.tcp80 = tcp80;
		}
	}


	public List<HandlerThread> getThreads() {
		return threads;
	}


	public void setThreads(List<HandlerThread> threads) {
		synchronized (this) {
			this.threads = threads;
		}
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		synchronized (this) {
			this.name = name;
		}
	}


	/**
	 * @return the preferred connection method for this client
	 */
	public ConnectionMethod getConnectionMethod() {
		if (isPort1open())
			return ConnectionMethod.UDPDirect;
		if (isPort2open())
			return ConnectionMethod.TCPDirect;

		return ConnectionMethod.Indirect;
	}

	/**
	 * Create an outgoing interface to this client
	 * @param cId
	 * @return
	 * @throws IOException
	 */
	public OutgoingInterface createOutInterface(ConnectionId cId, boolean oneWay) throws IOException {

		if (getConnectionMethod() == ConnectionMethod.UDPDirect)
			return new UDPOutgoingInterface(ip, 80, port1, cId);
		if (getConnectionMethod() == ConnectionMethod.TCPDirect)
			return new TCPOutgoingInterface(ip, port2);
		if (oneWay)
			return new TCPOutgoingInterface(tcp80);

		return getTCP80interface(cId);
	}


	private TCPOutgoingInterface getTCP80interface(ConnectionId cId) {

		try {

			Socket tcp80 = getTcp80();
			if (tcp80 == null)
				return null;

			CallMeMessage cmm = new CallMeMessage("Server", getName(), cId);
			callMeSocket = null;
			new TCPOutgoingInterface(tcp80).send(cmm);


			while (true) {
				synchronized (this) {
					if (callMeSocket != null)
						break;
				}
				Thread.sleep(100);
			}

			return new TCPOutgoingInterface(callMeSocket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return null;
	}

	public ResponseCode getPort1open() {
		return port1open;
	}


	public ResponseCode getPort2open() {
		return port2open;
	}

	public void addThread(HandlerThread t) {
		synchronized (this) {
			threads.add(t);
		}
	}

	public void removeThread(HandlerThread t) {
		synchronized (this) {
			threads.remove(t);
		}
	}
}