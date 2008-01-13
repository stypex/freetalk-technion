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
import util.Consts;
import util.Consts.ConnectionMethod;
import util.Consts.ResponseCode;

public class ClientData {

	private String name;
	private InetAddress ip;
	private int port1;
	private int port2;
	private ResponseCode port1open; 
	private ResponseCode port2open;
	private ResponseCode port80open; 

	private Socket tcp80;	
	private List<HandlerThread> threads;

	public Socket callMeSocket;
	public Object callMeLock = new Object();

	private long lastProbed;
	private boolean probeFailed;

	private boolean connected = false;

	public ClientData(String name, InetAddress ip, int port1, 
			int port2, ResponseCode port1open, ResponseCode port2open, Socket tcp80) {
		super();
		this.name = name;
		this.ip = ip;
		this.port1 = port1;
		this.port2 = port2;
		this.port1open = port1open;
		this.port2open = port2open;
		this.port80open = tcp80 != null ? 
				ResponseCode.OK : ResponseCode.BAD;
		this.tcp80 = tcp80;
		this.threads = new LinkedList<HandlerThread>();
		this.lastProbed = 0;
		this.probeFailed = false;
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
		return !port1open.equals(ResponseCode.BAD);
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
		return !port2open.equals(ResponseCode.BAD);
	}


	public void setPort2open(ResponseCode port2open) {
		synchronized (this) {
			this.port2open = port2open;
		}
	}


	public boolean isPort80open() {
		return !port80open.equals(ResponseCode.BAD);
	}


	public void setPort80open(ResponseCode port80open) {
		synchronized (this) {
			this.port80open = port80open;
		}
	}


	public Socket getTcp80() {
		return tcp80;
	}


	public void setTcp80(Socket tcp80) {
		synchronized (this) {
			this.tcp80 = tcp80;

			if (tcp80 == null)
				setPort80open(ResponseCode.BAD);
			else
				setPort80open(ResponseCode.OK);
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
		if (isPort80open())
			return ConnectionMethod.Indirect;

		return ConnectionMethod.None;
	}

	/**
	 * Create an outgoing interface to this client
	 * @param cId
	 * @return
	 * @throws IOException
	 */
	public OutgoingInterface createOutInterface(ConnectionId cId, boolean oneWay) throws IOException {

		if (getConnectionMethod().equals(ConnectionMethod.UDPDirect))
			return new UDPOutgoingInterface(ip, Consts.SERVER_PORT, port1, cId);
		if (getConnectionMethod().equals(ConnectionMethod.TCPDirect))
			return new TCPOutgoingInterface(ip, port2);
		if (oneWay && tcp80 != null)
			return new TCPOutgoingInterface(tcp80);

		return getTCP80interface(cId);
	}


	private TCPOutgoingInterface getTCP80interface(ConnectionId cId) {

		try {

			Socket tcp80 = getTcp80();
			if (tcp80 == null)
				return null;

			CallMeMessage cmm = new CallMeMessage("Server", getName(), cId);

			synchronized (callMeLock) {
				callMeSocket = null;
				new TCPOutgoingInterface(tcp80).send(cmm);

				if (callMeSocket == null) {
					callMeLock.wait();
				}
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

	public ResponseCode getPort80open() {
		return port80open;
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

	/**
	 * Register successful probe
	 */
	public void setProbed() {
		synchronized (this) {
			probeFailed = false;
			lastProbed = System.currentTimeMillis();
			System.out.println("Probed " + getName() + " at " + lastProbed);
		}
	}

	/**
	 * Call when a probe can't reach the client
	 * @return true if it's time to remove this client
	 */
	public boolean setCantProbe() {
		synchronized (this) {
			long currTime = System.currentTimeMillis();

			if (currTime - lastProbed < Consts.PROBE_WAIT)
				return false;

			lastProbed = currTime;

			if (probeFailed == false)
				probeFailed = true;
			else
				return true;
		}
		return false;
	}


	public boolean isConnected() {
		return connected;
	}


	public void setConnected(boolean connected) {
		this.connected = connected;
	}


	public long getLastProbed() {
		return lastProbed;
	}
}