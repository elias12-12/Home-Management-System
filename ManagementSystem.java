import java.util.ArrayList;
import java.util.Scanner;

public class ManagementSystem {
    // Power modes as static constants
    public static final double LOW_POWER = 1000;
    public static final double NORMAL_POWER = 4000;
    public static final double HIGH_POWER = 10000;

    // Attributes
    private String adminPassword;
    private String userPassword;
    private ArrayList<Room> rooms;
    private double maxAllowedPower;
    private boolean day;
    private ArrayList<Device> waitingListDay;
    private ArrayList<Device> waitingListPower;
    private Scanner scanner;

    // Constructor
    public ManagementSystem(String adminPassword, String userPassword) {
        setAdminPassword(adminPassword);
        setUserPassword(userPassword);
        this.rooms = new ArrayList<>();
        this.maxAllowedPower = NORMAL_POWER; // Default power mode
        this.day = true; // Default is day time
        this.waitingListDay = new ArrayList<>();
        this.waitingListPower = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    // Setters for passwords with validation
    public void setAdminPassword(String adminPassword) {
        if (isValidPassword(adminPassword)) {
            this.adminPassword = adminPassword;
        } else {
            System.out.println("Invalid admin password format. Password must have at least 8 characters with at least one uppercase letter, one lowercase letter, and one digit.");
            this.adminPassword = "Admin123"; // Default password
        }
    }

    public void setUserPassword(String userPassword) {
        if (isValidPassword(userPassword)) {
            this.userPassword = userPassword;
        } else {
            System.out.println("Invalid user password format. Password must have at least 8 characters with at least one uppercase letter, one lowercase letter, and one digit.");
            this.userPassword = "User1234"; // Default password
        }
    }

    // Password validation helper method
    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }
        
        return hasUpperCase && hasLowerCase && hasDigit;
    }

    // Password change methods
    public void changeAdminPassword(String currentPassword, String newPassword) {
        if (currentPassword.equals(adminPassword)) {
            setAdminPassword(newPassword);
            System.out.println("Admin password changed successfully.");
        } else {
            System.out.println("Current password is incorrect.");
        }
    }

    public void changeUserPassword(String currentPassword, String newPassword) {
        if (currentPassword.equals(userPassword)) {
            setUserPassword(newPassword);
            System.out.println("User password changed successfully.");
        } else {
            System.out.println("Current password is incorrect.");
        }
    }

    // Room management methods
    public void addRoom(String code, String description) {
        Room room = searchRoomByCode(code);
        if (room == null) {
            rooms.add(new Room(code, description));
            System.out.println("Room added successfully.");
        } else {
            System.out.println("Room with code " + code + " already exists.");
        }
    }

    public void removeRoom(String code) {
        Room room = searchRoomByCode(code);
        if (room != null) {
            rooms.remove(room);
            System.out.println("Room removed successfully.");
        } else {
            System.out.println("Room with code " + code + " not found.");
        }
    }

    public Room searchRoomByCode(String code) {
        for (Room room : rooms) {
            if (room.getCode().equals(code)) {
                return room;
            }
        }
        return null;
    }

    // Device management methods
    public void addDevice(String roomCode, Device device) {
        Room room = searchRoomByCode(roomCode);
        if (room != null) {
            if (searchDeviceById(device.getId()) == null) {
                room.addDevice(device);
                System.out.println("Device added successfully to room " + roomCode);
            } else {
                System.out.println("Device with ID " + device.getId() + " already exists in the system.");
            }
        } else {
            System.out.println("Room with code " + roomCode + " not found.");
        }
    }

    public void removeDevice(int deviceId) {
        for (Room room : rooms) {
            Device device = room.searchDeviceById(deviceId);
            if (device != null) {
                room.removeDevice(device);
                System.out.println("Device removed successfully.");
                return;
            }
        }
        System.out.println("Device with ID " + deviceId + " not found.");
    }

    public Device searchDeviceById(int id) {
        for (Room room : rooms) {
            Device device = room.searchDeviceById(id);
            if (device != null) {
                return device;
            }
        }
        return null;
    }

    // Time mode methods
    public void setDayTime() {
        if (!day) {
            day = true;
            System.out.println("Day time mode activated.");
            
            // Ask if user wants to turn off all lights
            System.out.print("Do you want to turn off all lights in the house? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("y")) {
                turnOffAllLights();
            }
            
            // Process devices in day waiting list
            ArrayList<Device> devicesToRemove = new ArrayList<>();
            for (Device device : waitingListDay) {
                if (canTurnOnDevice(device)) {
                    device.turnOn();
                    System.out.println("Device " + device.getId() + " turned on from day waiting list.");
                    devicesToRemove.add(device);
                } else {
                    // Move to power waiting list
                    waitingListPower.add(device);
                    devicesToRemove.add(device);
                    System.out.println("Device " + device.getId() + " moved from day waiting list to power waiting list.");
                }
            }
            waitingListDay.removeAll(devicesToRemove);
        } else {
            System.out.println("System is already in day time mode.");
        }
    }

    public void setNightTime() {
        if (day) {
            day = false;
            System.out.println("Night time mode activated.");
            
            // Check for running noisy devices
            for (Room room : rooms) {
                for (Device device : room.getDevicesList()) {
                    if (device instanceof Appliance && ((Appliance) device).isNoisy() && device.getStatus() == 1) {
                        System.out.println("Noisy device detected: " + device.getId() + " - " + device.getName());
                        System.out.print("What would you like to do? (1: Turn off, 2: Standby until day, 3: Keep on): ");
                        int choice = Integer.parseInt(scanner.nextLine().trim());
                        
                        switch (choice) {
                            case 1:
                                device.turnOff();
                                System.out.println("Device turned off.");
                                break;
                            case 2:
                                device.setStatus(2); // Standby
                                waitingListDay.add(device);
                                System.out.println("Device set to standby and added to day waiting list.");
                                break;
                            case 3:
                                System.out.println("Device kept on.");
                                break;
                            default:
                                System.out.println("Invalid choice. Device kept on.");
                        }
                    }
                }
            }
        } else {
            System.out.println("System is already in night time mode.");
        }
    }

    // Power mode methods
    public void setPowerMode(String mode) {
        switch (mode.toUpperCase()) {
            case "LOW":
                maxAllowedPower = LOW_POWER;
                System.out.println("Power mode set to LOW. Maximum allowed power: " + LOW_POWER + " watts.");
                break;
            case "NORMAL":
                maxAllowedPower = NORMAL_POWER;
                System.out.println("Power mode set to NORMAL. Maximum allowed power: " + NORMAL_POWER + " watts.");
                break;
            case "HIGH":
                maxAllowedPower = HIGH_POWER;
                System.out.println("Power mode set to HIGH. Maximum allowed power: " + HIGH_POWER + " watts.");
                break;
            default:
                System.out.println("Invalid power mode. Valid modes are LOW, NORMAL, and HIGH.");
        }
        
        // Check if we can turn on devices from power waiting list
        checkPowerWaitingList();
    }

    // Device control methods
    public void turnOnDevice(int deviceId, int level) {
        Device device = searchDeviceById(deviceId);
        if (device != null) {
            // Check if device is noisy and it's night time
            if (device instanceof Appliance && ((Appliance) device).isNoisy() && !day) {
                System.out.println("Warning: This is a noisy device and it's night time.");
                System.out.print("What would you like to do? (1: Turn on anyway, 2: Standby until day, 3: Cancel): ");
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1:
                        if (canTurnOnDevice(device)) {
                            if (device instanceof Light) {
                                ((Light) device).turnOn(level);
                            } else if (device instanceof Appliance) {
                                ((Appliance) device).turnOn(level);
                            } else {
                                device.turnOn();
                            }
                            System.out.println("Device turned on.");
                        } else {
                            handlePowerConstraint(device);
                        }
                        break;
                    case 2:
                        device.setStatus(2); // Standby
                        waitingListDay.add(device);
                        System.out.println("Device set to standby and added to day waiting list.");
                        break;
                    case 3:
                        System.out.println("Operation cancelled.");
                        break;
                    default:
                        System.out.println("Invalid choice. Operation cancelled.");
                }
            } else {
                // Normal device or day time
                if (canTurnOnDevice(device)) {
                    if (device instanceof Light) {
                        ((Light) device).turnOn(level);
                    } else if (device instanceof Appliance) {
                        ((Appliance) device).turnOn(level);
                    } else {
                        device.turnOn();
                    }
                    System.out.println("Device turned on.");
                } else {
                    handlePowerConstraint(device);
                }
            }
        } else {
            System.out.println("Device with ID " + deviceId + " not found.");
        }
    }

    public void turnOffDevice(int deviceId) {
        Device device = searchDeviceById(deviceId);
        if (device != null) {
            if (device.isCritical()) {
                System.out.print("This is a critical device. Please enter admin password to confirm: ");
                String password = scanner.nextLine();
                if (!password.equals(adminPassword)) {
                    System.out.println("Incorrect password. Operation cancelled.");
                    return;
                }
            }
            
            double previousConsumption = device.getCurrentConsumption();
            device.turnOff();
            System.out.println("Device turned off.");
            
            // Check if we can turn on devices from power waiting list
            if (previousConsumption > 0) {
                checkPowerWaitingList();
            }
        } else {
            System.out.println("Device with ID " + deviceId + " not found.");
        }
    }

    public void shutDownOneRoom(String roomCode) {
        Room room = searchRoomByCode(roomCode);
        if (room != null) {
            for (Device device : new ArrayList<>(room.getDevicesList())) {
                if (device.getStatus() == 1) { // If device is on
                    if (device.isCritical()) {
                        System.out.print("Device " + device.getId() + " is critical. Please enter admin password to confirm: ");
                        String password = scanner.nextLine();
                        if (!password.equals(adminPassword)) {
                            System.out.println("Incorrect password. Device " + device.getId() + " not turned off.");
                            continue;
                        }
                    }
                    device.turnOff();
                    System.out.println("Device " + device.getId() + " turned off.");
                }
            }
            System.out.println("All devices in room " + roomCode + " have been shut down.");
            
            // Check if we can turn on devices from power waiting list
            checkPowerWaitingList();
        } else {
            System.out.println("Room with code " + roomCode + " not found.");
        }
    }

    public void shutDownAllDevices() {
        for (Room room : rooms) {
            for (Device device : new ArrayList<>(room.getDevicesList())) {
                if (device.getStatus() == 1) { // If device is on
                    if (device.isCritical()) {
                        System.out.print("Device " + device.getId() + " is critical. Please enter admin password to confirm: ");
                        String password = scanner.nextLine();
                        if (!password.equals(adminPassword)) {
                            System.out.println("Incorrect password. Device " + device.getId() + " not turned off.");
                            continue;
                        }
                    }
                    device.turnOff();
                    System.out.println("Device " + device.getId() + " turned off.");
                }
            }
        }
        System.out.println("All devices have been shut down.");
        
        // Clear power waiting list since all devices are off
        waitingListPower.clear();
        System.out.println("Power waiting list cleared.");
    }

    // Helper methods
    private boolean canTurnOnDevice(Device device) {
        double currentTotalConsumption = getCurrentTotalConsumption();
        double deviceConsumption = device.getMaxPowerConsumption();
        
        if (device instanceof Light) {
            Light light = (Light) device;
            deviceConsumption = light.getMaxPowerConsumption() * light.getLevel() / 100.0;
        } else if (device instanceof Appliance) {
            Appliance appliance = (Appliance) device;
            deviceConsumption = appliance.getMaxPowerConsumption() * appliance.getPowerLevels()[appliance.getCurrentLevel()] / 100.0;
        }
        
        return (currentTotalConsumption + deviceConsumption) <= maxAllowedPower;
    }

    private void handlePowerConstraint(Device device) {
        System.out.println("Warning: Turning on this device would exceed the maximum allowed power consumption.");
        System.out.print("Would you like to add it to the power waiting list? (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        if (response.equals("y")) {
            device.setStatus(2); // Standby
            waitingListPower.add(device);
            System.out.println("Device added to power waiting list.");
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    private void checkPowerWaitingList() {
        ArrayList<Device> devicesToRemove = new ArrayList<>();
        for (Device device : waitingListPower) {
            if (canTurnOnDevice(device)) {
                device.turnOn();
                System.out.println("Device " + device.getId() + " turned on from power waiting list.");
                devicesToRemove.add(device);
            }
        }
        waitingListPower.removeAll(devicesToRemove);
    }

    private double getCurrentTotalConsumption() {
        double totalConsumption = 0;
        for (Room room : rooms) {
            totalConsumption += room.getCurrentComsuption();
        }
        return totalConsumption;
    }

    private void turnOffAllLights() {
        for (Room room : rooms) {
            for (Device device : room.getDevicesList()) {
                if (device instanceof Light && device.getStatus() == 1) {
                    device.turnOff();
                    System.out.println("Light " + device.getId() + " turned off.");
                }
            }
        }
        System.out.println("All lights have been turned off.");
        
        // Check if we can turn on devices from power waiting list
        checkPowerWaitingList();
    }

    // Display methods
    public void displaySummaryAllRooms() {
        System.out.println("\n===== ROOMS SUMMARY =====");
        if (rooms.isEmpty()) {
            System.out.println("No rooms available.");
        } else {
            for (Room room : rooms) {
                System.out.println(room.toBreifString());
            }
        }
        System.out.println("Total power consumption: " + getCurrentTotalConsumption() + " watts");
        System.out.println("Maximum allowed power: " + maxAllowedPower + " watts");
        System.out.println("Time mode: " + (day ? "Day" : "Night"));
    }

    public void displayDetailsOneRoom(String roomCode) {
        Room room = searchRoomByCode(roomCode);
        if (room != null) {
            System.out.println("\n===== ROOM DETAILS =====");
            System.out.println(room.toString());
        } else {
            System.out.println("Room with code " + roomCode + " not found.");
        }
    }

    public void displayAllDevices() {
        System.out.println("\n===== ALL DEVICES =====");
        boolean found = false;
        for (Room room : rooms) {
            for (Device device : room.getDevicesList()) {
                System.out.println("Room: " + room.getCode() + " - Device: " + device.toString());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No devices available.");
        }
    }

    public void displayRunningDevices() {
        System.out.println("\n===== RUNNING DEVICES =====");
        boolean found = false;
        for (Room room : rooms) {
            for (Device device : room.getDevicesList()) {
                if (device.getStatus() == 1) { // If device is on
                    System.out.println("Room: " + room.getCode() + " - Device: " + device.toString());
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("No running devices.");
        }
    }

    public void displayDayWaitingList() {
        System.out.println("\n===== DAY WAITING LIST =====");
        if (waitingListDay.isEmpty()) {
            System.out.println("No devices in day waiting list.");
        } else {
            for (Device device : waitingListDay) {
                System.out.println("Device: " + device.toString());
            }
        }
    }

    public void displayPowerWaitingList() {
        System.out.println("\n===== POWER WAITING LIST =====");
        if (waitingListPower.isEmpty()) {
            System  POWER WAITING LIST =====");
        if (waitingListPower.isEmpty()) {
            System.out.println("No devices in power waiting list.");
        } else {
            for (Device device : waitingListPower) {
                System.out.println("Device: " + device.toString());
            }
        }
    }

    public void displayInfo() {
        System.out.println("\n===== SYSTEM INFORMATION =====");
        System.out.println("Number of rooms: " + rooms.size());
        int totalDevices = 0;
        int totalLights = 0;
        int totalAppliances = 0;
        
        for (Room room : rooms) {
            totalDevices += room.getDevicesList().size();
            totalLights += room.getNbLights();
            totalAppliances += room.getNbAppliances();
        }
        
        System.out.println("Total devices: " + totalDevices);
        System.out.println("Total lights: " + totalLights);
        System.out.println("Total appliances: " + totalAppliances);
        System.out.println("Current power consumption: " + getCurrentTotalConsumption() + " watts");
        System.out.println("Maximum allowed power: " + maxAllowedPower + " watts");
        System.out.println("Time mode: " + (day ? "Day" : "Night"));
        System.out.println("Devices in day waiting list: " + waitingListDay.size());
        System.out.println("Devices in power waiting list: " + waitingListPower.size());
    }

    // Menu methods
    public void run() {
        boolean exit = false;
        
        while (!exit) {
            displayMainMenu();
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine().trim());
            
            switch (choice) {
                case 1:
                    if (authenticateAdmin()) {
                        runAdminMode();
                    }
                    break;
                case 2:
                    if (authenticateUser()) {
                        runUserMode();
                    }
                    break;
                case 3:
                    exit = true;
                    System.out.println("Exiting system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n===== SMART HOME MANAGEMENT SYSTEM =====");
        System.out.println("1. Admin Mode");
        System.out.println("2. User Mode");
        System.out.println("3. Exit");
    }

    private boolean authenticateAdmin() {
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();
        if (password.equals(adminPassword)) {
            System.out.println("Admin authentication successful.");
            return true;
        } else {
            System.out.println("Incorrect password.");
            return false;
        }
    }

    private boolean authenticateUser() {
        System.out.print("Enter user password: ");
        String password = scanner.nextLine();
        if (password.equals(userPassword)) {
            System.out.println("User authentication successful.");
            return true;
        } else {
            System.out.println("Incorrect password.");
            return false;
        }
    }

    private void runAdminMode() {
        boolean exit = false;
        
        while (!exit) {
            displayAdminMenu();
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine().trim());
            
            switch (choice) {
                case 1:
                    changePasswords();
                    break;
                case 2:
                    changePowerMode();
                    break;
                case 3:
                    changeTimeMode();
                    break;
                case 4:
                    manageRooms();
                    break;
                case 5:
                    manageDevices();
                    break;
                case 6:
                    exit = true;
                    System.out.println("Exiting admin mode.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayAdminMenu() {
        System.out.println("\n===== ADMIN MENU =====");
        System.out.println("1. Change Passwords");
        System.out.println("2. Change Power Mode");
        System.out.println("3. Set Day/Night Mode");
        System.out.println("4. Manage Rooms");
        System.out.println("5. Manage Devices");
        System.out.println("6. Exit Admin Mode");
    }

    private void changePasswords() {
        System.out.println("\n===== CHANGE PASSWORDS =====");
        System.out.println("1. Change Admin Password");
        System.out.println("2. Change User Password");
        System.out.println("3. Back");
        
        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(scanner.nextLine().trim());
        
        switch (choice) {
            case 1:
                System.out.print("Enter current admin password: ");
                String currentAdminPassword = scanner.nextLine();
                System.out.print("Enter new admin password: ");
                String newAdminPassword = scanner.nextLine();
                changeAdminPassword(currentAdminPassword, newAdminPassword);
                break;
            case 2:
                System.out.print("Enter current user password: ");
                String currentUserPassword = scanner.nextLine();
                System.out.print("Enter new user password: ");
                String newUserPassword = scanner.nextLine();
                changeUserPassword(currentUserPassword, newUserPassword);
                break;
            case 3:
                System.out.println("Returning to admin menu.");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void changePowerMode() {
        System.out.println("\n===== CHANGE POWER MODE =====");
        System.out.println("Current power mode: " + (maxAllowedPower == LOW_POWER ? "LOW" : 
                                                   maxAllowedPower == NORMAL_POWER ? "NORMAL" : "HIGH"));
        System.out.println("1. LOW (" + LOW_POWER + " watts)");
        System.out.println("2. NORMAL (" + NORMAL_POWER + " watts)");
        System.out.println("3. HIGH (" + HIGH_POWER + " watts)");
        System.out.println("4. Back");
        
        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(scanner.nextLine().trim());
        
        switch (choice) {
            case 1:
                setPowerMode("LOW");
                break;
            case 2:
                setPowerMode("NORMAL");
                break;
            case 3:
                setPowerMode("HIGH");
                break;
            case 4:
                System.out.println("Returning to admin menu.");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void changeTimeMode() {
        System.out.println("\n===== CHANGE TIME MODE =====");
        System.out.println("Current time mode: " + (day ? "Day" : "Night"));
        System.out.println("1. Set Day Time");
        System.out.println("2. Set Night Time");
        System.out.println("3. Back");
        
        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(scanner.nextLine().trim());
        
        switch (choice) {
            case 1:
                setDayTime();
                break;
            case 2:
                setNightTime();
                break;
            case 3:
                System.out.println("Returning to admin menu.");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void manageRooms() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n===== MANAGE ROOMS =====");
            System.out.println("1. Add Room");
            System.out.println("2. Remove Room");
            System.out.println("3. Search Room");
            System.out.println("4. Display All Rooms");
            System.out.println("5. Back");
            
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine().trim());
            
            switch (choice) {
                case 1:
                    System.out.print("Enter room code: ");
                    String code = scanner.nextLine();
                    System.out.print("Enter room description: ");
                    String description = scanner.nextLine();
                    addRoom(code, description);
                    break;
                case 2:
                    System.out.print("Enter room code to remove: ");
                    String roomCode = scanner.nextLine();
                    removeRoom(roomCode);
                    break;
                case 3:
                    System.out.print("Enter room code to search: ");
                    String searchCode = scanner.nextLine();
                    Room room = searchRoomByCode(searchCode);
                    if (room != null) {
                        System.out.println("Room found: " + room.toBreifString());
                    } else {
                        System.out.println("Room not found.");
                    }
                    break;
                case 4:
                    displaySummaryAllRooms();
                    break;
                case 5:
                    back = true;
                    System.out.println("Returning to admin menu.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void manageDevices() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n===== MANAGE DEVICES =====");
            System.out.println("1. Add Device");
            System.out.println("2. Remove Device");
            System.out.println("3. Search Device");
            System.out.println("4. Display All Devices");
            System.out.println("5. Back");
            
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine().trim());
            
            switch (choice) {
                case 1:
                    addDeviceMenu();
                    break;
                case 2:
                    System.out.print("Enter device ID to remove: ");
                    int deviceId = Integer.parseInt(scanner.nextLine().trim());
                    removeDevice(deviceId);
                    break;
                case 3:
                    System.out.print("Enter device ID to search: ");
                    int searchId = Integer.parseInt(scanner.nextLine().trim());
                    Device device = searchDeviceById(searchId);
                    if (device != null) {
                        System.out.println("Device found: " + device.toString());
                    } else {
                        System.out.println("Device not found.");
                    }
                    break;
                case 4:
                    displayAllDevices();
                    break;
                case 5:
                    back = true;
                    System.out.println("Returning to admin menu.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void addDeviceMenu() {
        System.out.println("\n===== ADD DEVICE =====");
        System.out.print("Enter room code: ");
        String roomCode = scanner.nextLine();
        
        Room room = searchRoomByCode(roomCode);
        if (room == null) {
            System.out.println("Room not found.");
            return;
        }
        
        System.out.println("Device Type:");
        System.out.println("1. Light");
        System.out.println("2. Appliance");
        System.out.print("Enter your choice: ");
        int deviceType = Integer.parseInt(scanner.nextLine().trim());
        
        System.out.print("Enter device ID (100-999): ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        
        System.out.print("Enter device name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter maximum power consumption (watts): ");
        double maxPower = Double.parseDouble(scanner.nextLine().trim());
        
        System.out.print("Is this a critical device? (y/n): ");
        boolean critical = scanner.nextLine().trim().toLowerCase().equals("y");
        
        Device device = null;
        
        if (deviceType == 1) { // Light
            System.out.print("Is this an adjustable light? (y/n): ");
            boolean adjustable = scanner.nextLine().trim().toLowerCase().equals("y");
            
            device = new Light(id, name, maxPower, critical, adjustable);
            
        } else if (deviceType == 2) { // Appliance
            System.out.print("Is this a noisy appliance? (y/n): ");
            boolean noisy = scanner.nextLine().trim().toLowerCase().equals("y");
            
            System.out.print("Enter number of power levels: ");
            int numLevels = Integer.parseInt(scanner.nextLine().trim());
            
            int[] powerLevels = new int[numLevels];
            for (int i = 0; i < numLevels; i++) {
                System.out.print("Enter power level " + i + " percentage (0-100): ");
                powerLevels[i] = Integer.parseInt(scanner.nextLine().trim());
            }
            
            device = new Appliance(id, name, maxPower, critical, powerLevels, noisy);
            
        } else {
            System.out.println("Invalid device type.");
            return;
        }
        
        addDevice(roomCode, device);
    }

    private void runUserMode() {
        boolean exit = false;
        
        while (!exit) {
            displayUserMenu();
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine().trim());
            
            switch (choice) {
                case 1:
                    displaySummaryAllRooms();
                    break;
                case 2:
                    displayAllDevices();
                    break;
                case 3:
                    displayRunningDevices();
                    break;
                case 4:
                    displayDayWaitingList();
                    break;
                case 5:
                    displayPowerWaitingList();
                    break;
                case 6:
                    System.out.print("Enter room code: ");
                    String roomCode = scanner.nextLine();
                    displayDetailsOneRoom(roomCode);
                    break;
                case 7:
                    System.out.print("Enter device ID: ");
                    int deviceId = Integer.parseInt(scanner.nextLine().trim());
                    Device device = searchDeviceById(deviceId);
                    if (device != null) {
                        System.out.println("Device found: " + device.toString());
                    } else {
                        System.out.println("Device not found.");
                    }
                    break;
                case 8:
                    controlDevice();
                    break;
                case 9:
                    System.out.print("Enter room code: ");
                    String shutdownRoomCode = scanner.nextLine();
                    shutDownOneRoom(shutdownRoomCode);
                    break;
                case 10:
                    shutDownAllDevices();
                    break;
                case 11:
                    System.out.println("Current power consumption: " + getCurrentTotalConsumption() + " watts");
                    System.out.println("Maximum allowed power: " + maxAllowedPower + " watts");
                    break;
                case 12:
                    changeTimeMode();
                    break;
                case 13:
                    exit = true;
                    System.out.println("Exiting user mode.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayUserMenu() {
        System.out.println("\n===== USER MENU =====");
        System.out.println("1. Check All Rooms Info");
        System.out.println("2. Check All Devices Info");
        System.out.println("3. Check All Running Devices");
        System.out.println("4. Check Day Waiting List");
        System.out.println("5. Check Power Waiting List");
        System.out.println("6. Search Room");
        System.out.println("7. Search Device");
        System.out.println("8. Control Device (Turn On/Off)");
        System.out.println("9. Turn Off All Devices in a Room");
        System.out.println("10. Turn Off All Devices");
        System.out.println("11. Check Current Power Consumption");
        System.out.println("12. Set Day/Night Mode");
        System.out.println("13. Exit User Mode");
    }

    private void controlDevice() {
        System.out.println("\n===== CONTROL DEVICE =====");
        System.out.print("Enter device ID: ");
        int deviceId = Integer.parseInt(scanner.nextLine().trim());
        
        Device device = searchDeviceById(deviceId);
        if (device == null) {
            System.out.println("Device not found.");
            return;
        }
        
        System.out.println("Device found: " + device.toString());
        System.out.println("1. Turn On");
        System.out.println("2. Turn Off");
        System.out.println("3. Back");
        
        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(scanner.nextLine().trim());
        
        switch (choice) {
            case 1:
                int level = 0;
                if (device instanceof Light && ((Light) device).isAdjustable()) {
                    System.out.print("Enter light intensity level (0-100): ");
                    level = Integer.parseInt(scanner.nextLine().trim());
                } else if (device instanceof Appliance) {
                    Appliance appliance = (Appliance) device;
                    System.out.println("Available power levels: " + Arrays.toString(appliance.getPowerLevels()));
                    System.out.print("Enter power level index (0-" + (appliance.getPowerLevels().length - 1) + "): ");
                    level = Integer.parseInt(scanner.nextLine().trim());
                }
                turnOnDevice(deviceId, level);
                break;
            case 2:
                turnOffDevice(deviceId);
                break;
            case 3:
                System.out.println("Returning to user menu.");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
}
