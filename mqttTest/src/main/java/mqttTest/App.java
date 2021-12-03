package mqttTest;

import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import org.eclipse.paho.client.mqttv3.*;
import java.util.concurrent.*;




/*
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
*/


public class App {
	
	public static void main(String[] args) throws Exception {
		SimpleMqttClient smc = new SimpleMqttClient();
		smc.runClient();
		
		System.out.println("Program finished..");
	}
}






/*

package mqttTest;

import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import org.eclipse.paho.client.mqttv3.*;
import java.util.concurrent.*;

public class App implements MqttCallback {

	static String[] pollCommand = { "cmnd/tasmota_zbbridge_0576A9/status", "" };
	static Integer pollCounter = 0; // incremented every poll
	static String pollCounterTopic = "Tola/status/pollCounter";
	
	public void messageArrived (final String topic, final MqttMessage message) throws Exception {
	    final String payload = new String(message.getPayload());

	    System.out.println("Received operation " + payload);
	    if (payload.contains("Tasmota_ZbBridge")) {
	        // execute the operation in another thread to allow the MQTT client to
	        // finish processing this message and acknowledge receipt to the server
	    	
	    	
	    	// {"Status":
	    	//   {"Module":75,
	    	//    "DeviceName":"Tasmota_ZbBridge",
	    	//    "FriendlyName":["Tasmota_ZbBridge"],
	    	//    "Topic":"tasmota_zbbridge_0576A9",
	    	//    "ButtonTopic":"0",
	    	//    "Power":0,
	    	//    "PowerOnState":3,
	    	//    "LedState":1,
	    	//    "LedMask":"FFFF",
	    	//    "SaveData":1,
	    	//    "SaveState":1,
	    	//    "SwitchTopic":"0",
	    	//    "SwitchMode":[0,0,0,0,0,0,0,0],
	    	//    "ButtonRetain":0,
	    	//    "SwitchRetain":0,
	    	//    "SensorRetain":0,
	    	//    "PowerRetain":0,
	    	//    "InfoRetain":0,
	    	//    "StateRetain":0}
	    	// }

	        Object obj = new JSONParser().parse( payload );
	          
	        // typecasting obj to JSONObject
	        JSONObject jo = (JSONObject) obj;
	       
	        
	        try {
	        	JSONObject jos = (JSONObject)jo.get("Status");
	        	String DeviceName = (String) jos.get("DeviceName");
	        	System.out.println("DeviceName is "+DeviceName);
	        }
	        catch (Exception e ) {
	        	System.out.println("Exception #1 is "+e);
	        }
	    }
	    
	    if (payload.contains("ZbReceived")) {
	    	// {"ZbReceived":
	    	//   {"0xEE12":
	    	//      {"Device":"0xEE12",
	    	//       "Name":"\"TRV_Front_Bedroom\"",
	    	//       "LocalTemperature":3,
	    	//       "Endpoint":1,
	    	//       "LinkQuality":47}
	    	//   }
	    	// }
	    	// parsing file "JSONExample.json"
	        Object obj = new JSONParser().parse( payload );
	          
	        // typecasting obj to JSONObject
	        JSONObject jo = (JSONObject) obj;
	            
	        // getting fields of interest
	        try {
	        	JSONObject joz = (JSONObject)jo.get("ZbReceived");
	        	JSONObject jod = (JSONObject)joz.get("0xEE12");
	        	String localTemperature = (String) joz.get("LocalTemperature");
	        	System.out.println("localTemperature for TRV 0xEE12 is "+localTemperature);
	        }
	        catch (Exception e ) {
	        	System.out.println("Exception #2 is "+e);
	        }
	    }
	    	
	    // I think this thread should update a counter to let people know it is 
		// alive and then publish every in an area of topics / data
		//
	    
	}
	    	
	public static void incrementPollCounter() {
    	pollCounter = pollCounter + 1;
	}
	
    public static void main(String[] args) throws Exception {
    		SimpleMqttClient smc = new App();
    		smc.runClient();
    	}
    	
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
        client.setCallback(this);
        client.connect(options);
        System.out.println("The device has been registered successfully!");

    	System.out.println("pollCounter:"+pollCounter);
    	
    	
        // listen for operations
    	int subQoS = 0;
    	client.subscribe("stat/tasmota_zbbridge_0576A9/STATUS", subQoS);
        
        

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
        
*/

