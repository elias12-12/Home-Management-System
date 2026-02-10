import java.util.ArrayList;

public class Room {
    // Attributes
    private String code;
    private String description;
    private ArrayList<Device> devicesList;

    // Constructor
    public Room(String code, String description) {
        this.code = code;
        this.description = description;
        this.devicesList = new ArrayList<>();
    }

    // Getters and setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Device> getDevicesList() {
        return devicesList;
    }

    public void setDevicesList(ArrayList<Device> devicesList) {
        this.devicesList = devicesList;
    }

    // Methods
    public int getNbLights() {
        int count = 0;
        for (Device device : devicesList) {
            if (device instanceof Light) {
                count++;
            }
        }
        return count;
    }

    public int getNbAppliances() {
        int count = 0;
        for (Device device : devicesList) {
            if (device instanceof Appliance) {
                count++;
            }
        }
        return count;
    }

    public double getCurrentComsuption() {
        double totalConsumption = 0;
        for (Device device : devicesList) {
            totalConsumption += device.getCurrentConsumption();
        }
        return totalConsumption;
    }

    public void addDevice(Device d) {
        if (d != null) {
            devicesList.add(d);
        }
    }

    public void removeDevice(Device d) {
        devicesList.remove(d);
    }

    public Device searchDeviceById(int id) {
        for (Device device : devicesList) {
            if (device.getId() == id) {
                return device;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Room: ").append(code).append(" - ").append(description).append("\n");
        sb.append("Number of devices: ").append(devicesList.size()).append("\n");
        sb.append("Devices:\n");
        
        for (Device device : devicesList) {
            sb.append("- ").append(device.toString()).append("\n");
        }
        
        sb.append("Total current consumption: ").append(getCurrentComsuption()).append(" watts\n");
        
        return sb.toString();
    }

    public String toBreifString() {
        return "Room: " + code + " - " + description +
                ", Total devices: " + devicesList.size() +
                ", Lights: " + getNbLights() +
                ", Appliances: " + getNbAppliances();
    }
}
