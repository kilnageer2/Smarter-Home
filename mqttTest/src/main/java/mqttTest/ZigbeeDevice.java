package mqttTest;

public class ZigbeeDevice {
	String			address;
	String 			description;
	SensorType		type;
	House			house;
	Room			room;
	static 	int index = 0;
	static final int MAX_ZIGBEE_DEVICES = 50;
	static  ZigbeeDevice[]	devices = new ZigbeeDevice[MAX_ZIGBEE_DEVICES];

	public ZigbeeDevice( 	String			address,
							String 			description,
							SensorType		type,
							House			house,
							Room			room ) throws Exception {
		this.address = address;
		this.description = description;
		this.type = type;
		this.house = house;
		this.room = room;
		devices[ index ] = this;
		if (index < MAX_ZIGBEE_DEVICES) {
			index++;
		} else {
			throw new Exception();
		}
		
	}
}
