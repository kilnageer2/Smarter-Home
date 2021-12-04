package mqttTest;

import java.time.LocalDateTime;

public class SensorReading {
		String 				value; 		// For example, "19.3"
		String				units; 		// For example, "C"
		LocalDateTime		timestamp; 	// For example, 2021-11-04T16:01:00
		
		public SensorReading( String value, String units ) {
			this.value = value;
			this.units = units;
			this.timestamp = LocalDateTime.now();
		}
		
		public String toString() {
			String conversion = "Value: "+this.value+", Units: "+this.units+ ", Time: "+this.timestamp;
			return conversion;
		}		
}
