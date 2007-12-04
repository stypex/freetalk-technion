/**
 * 
 */
package client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * Will be the wrap for all the properties loaded from client.ini
 * file
 * @author lenka
 *
 */
public class Globals {

	static Properties prop = new Properties();
	
	public static String ClientName;
	
	public static void load() {
		try {
			FileInputStream s = new FileInputStream("client.ini");
			prop.load(s);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static InetAddress getServerIP() {
		String ip = prop.getProperty("ServerIP");
		
		try {
			if (ip == null)
				return InetAddress.getLocalHost();
			
			return InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static int getUDPPort() {
		String p = prop.getProperty("UDPPort");
		
		if (p == null)
			return 5000;
		
		return Integer.parseInt(p);
	}
	
	public static int getTCPPort() {
		String p = prop.getProperty("TCPPort");
		
		if (p == null)
			return 5000;
		
		return Integer.parseInt(p);
	}

	public static String getClientName() {
		return ClientName;
	}

	public static void setClientName(String clientName) {
		ClientName = clientName;
	}
}
