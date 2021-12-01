package mqttTest;


import org.eclipse.paho.client.mqttv3.*;
import java.util.concurrent.*;

public class App {

	static String[] pollCommand = { "cmnd/tasmota_zbbridge_0576A9/status", "" };
	static Integer pollCounter = 0; // incremented every poll
	static String pollCounterTopic = "Tola/status/pollCounter";
	
	public static void incrementPollCounter() {
    	pollCounter = pollCounter + 1;
	}
	
    public static void main(String[] args) throws Exception {
        int qos             = 2;
        String broker       = Secrets.getBroker();
        String clientId     = Secrets.getBroker();
        String username		= Secrets.getUsername();
        String password		= Secrets.getPassword();        

        final MqttClient client = new MqttClient( broker, clientId, null );
    	//client.setCallback( new SimpleMqttCallBack() );
    	
        // MQTT connection options
    	// https://cumulocity.com/guides/device-sdk/mqtt-examples/#hello-mqtt-java
        final MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName( username );
        options.setPassword( password.toCharArray() );
        client.connect(options);
        System.out.println("The device has been registered successfully!");

    	System.out.println("pollCounter:"+pollCounter);
    	
        // listen for operations
    	client.subscribe("stat/tasmota_zbbridge_0576A9/STATUS", new IMqttMessageListener() {
            public void messageArrived (final String topic, final MqttMessage message) throws Exception {
                final String payload = new String(message.getPayload());

                System.out.println("Received operation " + payload);
                if (payload.startsWith("{")) {
                    // execute the operation in another thread to allow the MQTT client to
                    // finish processing this message and acknowledge receipt to the server
                	
                	// I think this thread should update a counter to let people know it is 
                	// alive and then publish every in an area of topics / data
                	//
                    Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                        public void run() {
                            System.out.println("Recieved topic:"+topic+", payload:"+payload);
                            System.out.println("...done");
                        }
                    });
                }
            }
        });
    	
    	
    	
        // listen for operations
        client.subscribe("tele/tasmota_zbbridge_0576A9/SENSOR", new IMqttMessageListener() {
            public void messageArrived (final String topic, final MqttMessage message) throws Exception {
                final String payload = new String(message.getPayload());

                //System.out.println("Received operation " + payload);
                if (payload.contains("0xEE12")) {
                    // execute the operation in another thread to allow the MQTT client to
                    // finish processing this message and acknowledge receipt to the server
                	
                	// I think this thread should update a counter to let people know it is 
                	// alive and then publish every in an area of topics / data
                	//
                    Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                        public void run() {
                            System.out.println("Recieved topic:"+topic+", payload:"+payload);
                            //System.out.println("...done");
                        }
                    });
                }
            }
        });
        
        
        

        // generate a random temperature (10º-20º) measurement and send it every 7 seconds
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(new Runnable() {
            public void run () {
                try {
                	// First update our poll counter to show we are alive :)
                	//incrementPollCounter();
                	pollCounter = pollCounter + 1;
            	
                	System.out.println("pollCounter:"+pollCounter);
                    client.publish(pollCounterTopic.toString(), App.pollCounter.toString().getBytes(), 2, false);
                    
                    System.out.println("Publishing polling commands..");
                    System.out.println("Tx topic:"+pollCommand[0]+", payload:"+ pollCommand[1]);
                    client.publish(pollCommand[0], pollCommand[1].getBytes(), 2, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }, 1, 600, TimeUnit.SECONDS);
    }


 
    /*
    public class SimpleMqttCallBack implements MqttCallback {

    	  public void connectionLost(Throwable throwable) {
    	    System.out.println("Connection to MQTT broker lost!");
    	  }

    	  public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
    	    System.out.println("Message received:\n\t"+ new String(mqttMessage.getPayload()) );
    	  }

    	  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    	    // not used in this example
    	  }
    	}
    */
}
        

