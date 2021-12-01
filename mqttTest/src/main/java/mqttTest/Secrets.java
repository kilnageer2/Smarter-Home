package mqttTest;

public class Secrets {
	private final static String		broker = "tcp://192.168.3.6:1883";
	private final static String		clientId = "javaClient_100";
	private final static String		username = "tola";
	private final static String		password = "2016TolaPark!";
	
	public static String getBroker() { return broker ; };
	public static String getClientId() { return clientId ; };
	public static String getUsername() { return username ; };
	public static String getPassword() { return password ; };    
	
}