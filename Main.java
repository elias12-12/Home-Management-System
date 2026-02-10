import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to Smart Home Management System");
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Initial Setup");
        System.out.print("Enter admin password (must have at least 8 characters with at least one uppercase letter, one lowercase letter, and one digit): ");
        String adminPassword = scanner.nextLine();
        
        System.out.print("Enter user password (must have at least 8 characters with at least one uppercase letter, one lowercase letter, and one digit): ");
        String userPassword = scanner.nextLine();
        
        ManagementSystem system = new ManagementSystem(adminPassword, userPassword);
        
        // Add some sample data for testing
        addSampleData(system);
        
        // Run the system
        system.run();
        
        scanner.close();
    }
    
    private static void addSampleData(ManagementSystem system) {
        // Add rooms
        system.addRoom("LR1", "Living Room - 1st Floor");
        system.addRoom("K1F", "Kitchen - 1st Floor");
        system.addRoom("BR1", "Bedroom - 1st Floor");
        system.addRoom("BR2", "Bedroom - 2nd Floor");
        
        // Add devices to Living Room
        Light mainLight = new Light(101, "MainLight-LivingRoom", 100, false, true);
        Light cornerLight = new Light(102, "CornerLight-LivingRoom", 60, false, true);
        Appliance tv = new Appliance(103, "TV-LivingRoom", 200, true, new int[]{50, 75, 100}, false);
        
        system.addDevice("LR1", mainLight);
        system.addDevice("LR1", cornerLight);
        system.addDevice("LR1", tv);
        
        // Add devices to Kitchen
        Light kitchenLight = new Light(201, "MainLight-Kitchen", 120, false, false);
        Appliance refrigerator = new Appliance(202, "Refrigerator-Kitchen", 500, true, new int[]{100}, false);
        Appliance microwave = new Appliance(203, "Microwave-Kitchen", 1200, false, new int[]{50, 100}, true);
        
        system.addDevice("K1F", kitchenLight);
        system.addDevice("K1F", refrigerator);
        system.addDevice("K1F", microwave);
        
        // Add devices to Bedroom 1
        Light bedroomLight = new Light(301, "MainLight-Bedroom1", 80, false, true);
        Appliance airConditioner = new Appliance(302, "AC-Bedroom1", 1500, false, new int[]{33, 66, 100}, true);
        
        system.addDevice("BR1", bedroomLight);
        system.addDevice("BR1", airConditioner);
        
        // Add devices to Bedroom 2
        Light bedroom2Light = new Light(401, "MainLight-Bedroom2", 80, false, true);
        Appliance fan = new Appliance(402, "Fan-Bedroom2", 100, false, new int[]{25, 50, 75, 100}, false);
        
        system.addDevice("BR2", bedroom2Light);
        system.addDevice("BR2", fan);
        
        System.out.println("Sample data added successfully!");
    }
}
