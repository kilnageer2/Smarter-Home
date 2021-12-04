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
	SNZB_02,  		// Temperature and humidity
	TUYA_TRV,		// Can turn on/off and set temperature, reports valve and temperature
	RGB_LED_STRIP, 	// Can control RGB colour
}

enum Sensor {
	TEMPERATURE,  // In Celcius
	HUMIDITY,
	PM2_5,
	PM10,
	TUYA_TARGET_TEMPERATURE,
	TUYA_PRESET_VALUE,
	TUYA_FORCE_MODE,
	TUYA_VALVE_POSITION,
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

	static SensorReading sensorData[][][][] = new SensorReading[MAX_HOUSES][MAX_ROOMS][MAX_SENSOR_TYPES][MAX_SENSORS];
	
	public static void setSensorData( House house, Room room, SensorType type, Sensor sensor, SensorReading data) {
		sensorData[house.ordinal()][room.ordinal()][type.ordinal()][sensor.ordinal()] = data;
		System.out.println("Set database entry to: "+data.toString());	        			
	}
	
	public static SensorReading getSensorData( House house, Room room, SensorType type, Sensor sensor ) {
		return sensorData[house.ordinal()][room.ordinal()][type.ordinal()][sensor.ordinal()];
	}	
}



