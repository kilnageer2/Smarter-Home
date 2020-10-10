package mqttTest;


import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class App {

    public static void main(String[] args) {

        String topic        = "MQTT Examples";
        String content      = "Message from MqttPublishSample";
        int qos             = 2;
        //String broker       = "tcp://mqtt.eclipse.org:1883";
        String broker       = "tcp://192.168.1.150:1883";
        String clientId     = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
        	MqttClient client = new MqttClient("tcp://192.168.1.150:1883", MqttClient.generateClientId());
        	client.setCallback( new SimpleMqttCallBack() );
        	client.connect();
        	MqttMessage message = new MqttMessage();
        	message.setPayload("Hello world from Java".getBytes());
        	client.publish("iot_data", message);
        	client.disconnect();
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
            System.out.println("Disconnected");
            System.exit(0);
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }
 
    
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
    
}
        

