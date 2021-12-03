package mqttTest;

import org.eclipse.paho.client.mqttv3.*;

public class SimpleMqttClient implements MqttCallback {

	MqttClient myClient;
	MqttConnectOptions connOpt;

	//static final String BROKER_URL = "tcp://q.m2m.io:1883";
	//static final String M2MIO_DOMAIN = "<Insert m2m.io domain here>";
	//static final String M2MIO_STUFF = "things";
	//static final String M2MIO_THING = "<Unique device ID>";
	//static final String M2MIO_USERNAME = "<m2m.io username>";
	//static final String M2MIO_PASSWORD_MD5 = "<m2m.io password (MD5 sum of password)>";

	// the following two flags control whether this example is a publisher, a subscriber or both
	static final Boolean subscriber = true;
	static final Boolean publisher = true;

	/**
	 * 
	 * connectionLost
	 * This callback is invoked upon losing the MQTT connection.
	 * 
	 */
	public void connectionLost(Throwable t) {
		System.out.println("Connection lost!");
		// code to reconnect to the broker would go here if desired
	}

	/**
	 * 
	 * deliveryComplete
	 * This callback is invoked when a message published by this client
	 * is successfully received by the broker.
	 * 
	 */
	public void deliveryComplete(IMqttDeliveryToken token) {
		try {
			MqttMessage message = token.getMessage();
			// message is null on success
			System.out.println("Pub complete");			
		} catch (MqttException e) {
			System.out.println("Exception #3 is "+e);
		}

	}

	/**
	 * 
	 * messageArrived
	 * This callback is invoked when a message is received on a subscribed topic.
	 * 
	 */
	
	//@@ TODO LOt's of work to parse messages into an internal array which is then used to publish
	//@@ updates to the MQTT broker at house/room/sensor/reading { value, units, timestampp }
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		String msg = new String(message.getPayload());
		System.out.println("-------------------------------------------------");
		System.out.println("| Topic:" + topic);
		System.out.println("| Message: " + msg);
		System.out.println("-------------------------------------------------");
	}

	/**
	 * 
	 * MAIN
	 * 
	 */
	//public static void main(String[] args) {
	//	App smc = new App();
	//	smc.runClient();
	//}
	
	/**
	 * 
	 * runClient
	 * The main functionality of this simple example.
	 * Create a MQTT client, connect to broker, pub/sub, disconnect.
	 * 
	 */
	public void runClient() {
		// setup MQTT Client
        int qos             = 2;
        String broker       = Secrets.getBroker();
        String clientId     = Secrets.getBroker();
        String username		= Secrets.getUsername();
        String password		= Secrets.getPassword();        

        //final MqttClient client = new MqttClient( broker, clientId, null );
        
		//String clientID = M2MIO_THING;
		//connOpt = new MqttConnectOptions();
		connOpt = new MqttConnectOptions();
		connOpt.setUserName( username );
		connOpt.setPassword( password.toCharArray() );
        //client.setCallback(this);
        //client.connect(options);
        
		//connOpt.setCleanSession(true);
		//connOpt.setKeepAliveInterval(30);
		//connOpt.setUserName(M2MIO_USERNAME);
		//connOpt.setPassword(M2MIO_PASSWORD_MD5.toCharArray());
		
		// Connect to Broker
		try {
			//myClient = new MqttClient(BROKER_URL, clientID);
			myClient = new MqttClient( broker, clientId, null );
			myClient.setCallback(this);
			myClient.connect(connOpt);
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		System.out.println("Connected to " + broker);

		// @@TODO Let's get these topics to subscribe to from an external array
		
		// setup topic
		// topics on m2m.io are in the form <domain>/<stuff>/<thing>
		String myTopics[] = { 
				"stat/tasmota_zbbridge_0576A9/STATUS",
				"tele/tasmota_zbbridge_0576A9/SENSOR" };
		

		// subscribe to topic if subscriber
		for (String myTopic: myTopics) {
			try {
				int subQoS = 0;
				MqttTopic topic = myClient.getTopic(myTopic);
				myClient.subscribe(myTopic, subQoS);
				System.out.println("Subscribed to " + myTopic);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// @@TODO Let's get these topics to publish to from an external array
		
		// publish messages if publisher
		if (publisher) {
			for (int i=1; i<=1; i++) {
		   		String pubMsg = "";
		   		int pubQoS = 0;
				MqttMessage message = new MqttMessage(pubMsg.getBytes());
		    	message.setQos(pubQoS);
		    	message.setRetained(false);
		    	String topic2 = "cmnd/tasmota_zbbridge_0576A9/status";
		    	MqttTopic pubTopic = myClient.getTopic(topic2);
		    	

		    	// Publish the message
		    	System.out.println("Publishing to topic \"" + topic2 + "\" qos " + pubQoS);
		    	MqttDeliveryToken token = null;
		    	try {
		    		// publish message to broker
					token = pubTopic.publish(message);
			    	// Wait until the message has been delivered to the broker
					token.waitForCompletion();
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}			
		}
		
		// @@TODO Should we ever leave? Why not just poll static arrays for work?
		
		// disconnect
		try {
			// wait to ensure subscribed messages are delivered
			if (subscriber) {
				Thread.sleep(20 * 1000); // Let code run for 20 seconds
			}
			myClient.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


