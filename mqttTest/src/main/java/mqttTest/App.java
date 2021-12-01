package mqttTest;


import org.eclipse.paho.client.mqttv3.*;
import java.util.concurrent.*;

public class App {

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
        
        
    	MqttMessage message = new MqttMessage();
    	message.setPayload("Hello world from Java".getBytes());
    	client.publish("iot_data", message);
    	//client.disconnect();
    	/*
        MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        System.out.println("Connecting to broker: "+broker);
        sampleClient.connect(connOpts);
        System.out.println("Connected");
        System.out.println("Publishing message: "+content);
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        sampleClient.publish(topic, message);
        System.out.println("Message published");
        sampleClient.disconnect();
        */
        //System.out.println("Disconnected");
        //System.exit(0);

        System.out.println("The device has been registered successfully!");

        // listen for operations
        client.subscribe("s/us", new IMqttMessageListener() {
            public void messageArrived (final String topic, final MqttMessage message) throws Exception {
                final String payload = new String(message.getPayload());

                System.out.println("Received operation " + payload);
                if (payload.startsWith("510")) {
                    // execute the operation in another thread to allow the MQTT client to
                    // finish processing this message and acknowledge receipt to the server
                    Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                        public void run() {
                            try {
                                System.out.println("Simulating device restart...");
                                client.publish("s/us", "501,c8y_Restart".getBytes(), 2, false);
                                System.out.println("...restarting...");
                                Thread.sleep(TimeUnit.SECONDS.toMillis(5));
                                client.publish("s/us", "503,c8y_Restart".getBytes(), 2, false);
                                System.out.println("...done...");
                            } catch (MqttException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

        // generate a random temperature (10º-20º) measurement and send it every 7 seconds
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(new Runnable() {
            public void run () {
                try {
                    int temp = (int) (Math.random() * 10 + 10);

                    System.out.println("Sending temperature measurement (" + temp + "º) ...");
                    client.publish("s/us", new MqttMessage(("211," + temp).getBytes()));
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }, 1, 7, TimeUnit.SECONDS);
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
        

