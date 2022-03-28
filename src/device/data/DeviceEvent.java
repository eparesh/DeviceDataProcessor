package device.data;

public class DeviceEvent {
    Double timestamp;

    public Double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Double timestamp) {
        this.timestamp = timestamp;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getEvent_payload() {
        return event_payload;
    }

    public void setEvent_payload(String event_payload) {
        this.event_payload = event_payload;
    }

    String device_id;
    String event_type;
    String event_payload;
}
