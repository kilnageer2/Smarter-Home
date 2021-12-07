package mqttTest;

public class Secrets {
	private final static String		broker = "tcp://hostanme_or_i[p:1883"; // replace this!! 
	private final static String		clientId = "uniqueclientname";  // replace this!! 
	private final static String		username = "username"; // replace this!!
	private final static String		password = "password!"; // replace this!!
	
	public static String getBroker() { return broker ; };
	public static String getClientId() { return clientId ; };
	public static String getUsername() { return username ; };
	public static String getPassword() { return password ; };    
	
}