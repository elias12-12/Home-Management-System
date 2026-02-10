public class Light extends Device {
    // Attributes
    private boolean adjustable;
    private int level;

    // Constructors
    public Light(int id, String name, double maxPowerConsumption) {
        super(id, name, maxPowerConsumption);
        this.adjustable = false;
        this.level = 100;
    }

    public Light(int id, String name, double maxPowerConsumption, boolean adjustable) {
        super(id, name, maxPowerConsumption);
        this.adjustable = adjustable;
        this.level = 100;
    }

    public Light(int id, String name, double maxPowerConsumption, boolean critical, boolean adjustable) {
        super(id, name, maxPowerConsumption, critical);
        this.adjustable = adjustable;
        this.level = 100;
    }

    // Getters and setters
    public boolean isAdjustable() {
        return adjustable;
    }

    public void setAdjustable(boolean adjustable) {
        this.adjustable = adjustable;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        if (level >= 0 && level <= 100) {
            this.level = level;
        }
    }

    // Methods
    @Override
    public void turnOn() {
        super.turnOn();
        this.level = 100;
    }

    public void turnOn(int level) {
        if (level >= 0 && level <= 100) {
            super.turnOn();
            this.level = level;
        }
    }

    @Override
    public double getCurrentConsumption() {
        if (getStatus() == 1) { // If light is on
            return getMaxPowerConsumption() * level / 100.0;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Light{ " + super.toString() + ", " +
                (adjustable ? "adjustable" : "not adjustable") +
                ", level = " + level + " }";
    }
}
