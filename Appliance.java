import java.util.Arrays;

public class Appliance extends Device {
    // Attributes
    private int[] powerLevels;
    private int currentLevel;
    private boolean noisy;

    // Constructors
    public Appliance(int id, String name, double maxPowerConsumption, int[] powerLevels, boolean noisy) {
        super(id, name, maxPowerConsumption);
        setPowerLevels(powerLevels);
        this.currentLevel = 0;
        this.noisy = noisy;
    }

    public Appliance(int id, String name, double maxPowerConsumption, boolean critical, int[] powerLevels, boolean noisy) {
        super(id, name, maxPowerConsumption, critical);
        setPowerLevels(powerLevels);
        this.currentLevel = 0;
        this.noisy = noisy;
    }

    // Getters and setters
    public int[] getPowerLevels() {
        return powerLevels;
    }

    public void setPowerLevels(int[] powerLevels) {
        if (powerLevels != null && powerLevels.length > 0) {
            // Sort the array in ascending order
            Arrays.sort(powerLevels);
            this.powerLevels = powerLevels;
        } else {
            this.powerLevels = new int[]{100}; // Default: 100% power
        }
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        if (currentLevel >= 0 && currentLevel < powerLevels.length) {
            this.currentLevel = currentLevel;
        }
    }

    public boolean isNoisy() {
        return noisy;
    }

    public void setNoisy(boolean noisy) {
        this.noisy = noisy;
    }

    // Methods
    @Override
    public void turnOn() {
        super.turnOn();
        this.currentLevel = 0;
    }

    public void turnOn(int level) {
        if (level >= 0 && level < powerLevels.length) {
            super.turnOn();
            this.currentLevel = level;
        }
    }

    @Override
    public double getCurrentConsumption() {
        if (getStatus() == 1) { // If appliance is on
            return getMaxPowerConsumption() * powerLevels[currentLevel] / 100.0;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Appliance{ " + super.toString() +
                ", power Levels = " + Arrays.toString(powerLevels) +
                ", level = " + currentLevel +
                ", " + (noisy ? "noisy" : "not noisy") + " }";
    }
}
