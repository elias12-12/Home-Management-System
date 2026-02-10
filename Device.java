public class Device {
    // Attributes
    private int id;
    private String name;
    private int status; // 0: off, 1: on, 2: standby
    private double maxPowerConsumption;
    private boolean critical;

    // Constructors
    public Device(int id, String name, double maxPowerConsumption) {
        this(id, name, maxPowerConsumption, false);
    }

    public Device(int id, String name, double maxPowerConsumption, boolean critical) {
        setId(id);
        this.name = name;
        this.status = 0; // Default is off
        this.maxPowerConsumption = maxPowerConsumption;
        this.critical = critical;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id >= 100 && id <= 999) {
            this.id = id;
        } else {
            this.id = 0; // Invalid ID
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        if (status >= 0 && status <= 2) {
            this.status = status;
        }
    }

    public double getMaxPowerConsumption() {
        return maxPowerConsumption;
    }

    public void setMaxPowerConsumption(double maxPowerConsumption) {
        if (maxPowerConsumption > 0) {
            this.maxPowerConsumption = maxPowerConsumption;
        } else {
            this.maxPowerConsumption = 50; // Default value
        }
    }

    public boolean isCritical() {
        return critical;
    }

    public void setCritical(boolean critical) {
        this.critical = critical;
    }

    // Methods
    public void turnOn() {
        this.status = 1; // On
    }

    public void turnOff() {
        this.status = 0; // Off
    }

    public double getCurrentConsumption() {
        return (status == 1) ? maxPowerConsumption : 0;
    }

    @Override
    public String toString() {
        String statusStr;
        switch (status) {
            case 0:
                statusStr = "Off";
                break;
            case 1:
                statusStr = "On";
                break;
            case 2:
                statusStr = "Standby";
                break;
            default:
                statusStr = "Unknown";
        }

        return "id=" + id + ", name=" + name + ", status: " + statusStr +
                ", maximum power consumption = " + maxPowerConsumption +
                ", " + (critical ? "critical" : "not critical");
    }
}
