/**
 * 
 */
package mqttTest;

import java.time.*;

enum House {
	KILNAGEER,
	TOLA
}

enum Room {
	BACK_GARDEN,
	BACKYARD,
	SIDE_ALLEY,
	ATTIC,
	KITCHEN,
	FRONT_ROOM,
	HALLWAY,
	NORTH_BEDROOM,
	SOUTH_BEDROOM,
	OFFICE,
	BATHROOM,
}


enum SensorType {
	SNZB_02,  // Temperature and humidity
	TOLA
}

enum Sensor {
	TEMPERATURE,  // In Celcius
	HUMIDITY,
	PM2_5,
	PM10,
}
/**
 * @author Tony Walsh
 *
 */
public class DatabaseManager {

	static final int MAX_HOUSES = House.values().length;
	static final int MAX_ROOMS = Room.values().length;
	static final int MAX_SENSOR_TYPES = SensorType.values().length;
	static final int MAX_SENSORS = Sensor.values().length;	

	SensorReading sensorData[][][][] = new SensorReading[MAX_HOUSES][MAX_ROOMS][MAX_SENSOR_TYPES][MAX_SENSORS];
	
	public void setSensorData( int house, int room, int type, int sensor, SensorReading data) {
		sensorData[house][room][type][sensor] = data;
	}
	
	public SensorReading getSensorData( int house, int room, int type, int sensor ) {
		return sensorData[house][room][type][sensor];
	}	
}



