package org.cscb525;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import org.cscb525.configure.SessionFactoryUtil;
import org.cscb525.entity.Client;
import org.cscb525.entity.Company;
import org.cscb525.entity.Employee;
import org.cscb525.entity.Transport;
import org.cscb525.entity.Vehicle;
import org.cscb525.service.CompanyService;
import org.cscb525.service.TransportService;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static CompanyService companyService;
    private static TransportService transportService;
    private static Company currentCompany;

    public static void main(String[] args) {
        setup();

        boolean running = true;
        while (running) {
            printMenu();
            String input = scanner.nextLine();

            try {
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1 -> handleNewTransport();
                    case 2 -> showAllTransports();
                    case 3 -> sortTransportsByDestination();
                    case 4 -> filterTransportsByDestination();
                    case 5 -> markTransportAsPaid(); 
                    case 6 -> deleteTransport(); 
                    case 7 -> exportTransports();
                    case 8 -> importTransports();
                    case 0 -> {
                        running = false;
                        System.out.println("Goodbye!");
                    }
                    default -> System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        SessionFactoryUtil.getSessionFactory().close();
    }

    // ======= setup =======
    private static void setup() {
        System.out.println("--- SYSTEM SETUP ---");
        companyService = new CompanyService();
        transportService = new TransportService();

        try {
            // Company Code
            currentCompany = new Company("Kiselo Mlyako Express");
            currentCompany.setAddress("Sofia");
            currentCompany.setAddress("Sofia");
            companyService.createCompany(currentCompany);
        } catch (Exception e) {
            // If exists, load it
            List<Company> list = companyService.getCompaniesSortedByName();
            if(!list.isEmpty()) currentCompany = list.get(0);
        }
        System.out.println("[OK] Company loaded: " + currentCompany.getName());

        // Drivers
        try {
            Employee d1 = new Employee("Ivan Petrov", "Heavy Truck License", new BigDecimal("1500"), currentCompany);
            Employee d2 = new Employee("Georgi Ivanov", "Bus License", new BigDecimal("1400"), currentCompany);
            companyService.hireEmployee(currentCompany.getId(), d1);
            companyService.hireEmployee(currentCompany.getId(), d2);
            System.out.println("[OK] Drivers hired.");
        } catch (Exception e) { /* Ignore duplicates */ }

        // Vehicles
        try {
            Vehicle v1 = new Vehicle(Vehicle.VehicleType.TRUCK, "Heavy Truck License", currentCompany);
            v1.setLicensePlate("CA 1234 TT"); 
            v1.setMaxCargoWeight(20000.0);
            
            Vehicle v2 = new Vehicle(Vehicle.VehicleType.BUS, "Bus License", currentCompany);
            v2.setLicensePlate("CB 5678 BP");
            v2.setMaxPassengers(50);

            try {
                companyService.buyVehicle(currentCompany.getId(), v1);
                companyService.buyVehicle(currentCompany.getId(), v2);
                System.out.println("[OK] Vehicles ready.");
            } catch (Exception e) {
                 // Recover if exists
                 System.out.println("[OK] Vehicles recovered.");
            }
        } catch (Exception e) { /* Ignore */ }
        
        // Client
        try {
             Client c = new Client("SuperMarket Ltd", currentCompany);
             companyService.addClient(currentCompany.getId(), c);
             System.out.println("[OK] Client loaded.");
        } catch (Exception e) { /* Ignore */ }
        
        System.out.println("--------------------\n");
    }

    // ======= menu =======
    private static void printMenu() {
        System.out.println("1. New Transport");
        System.out.println("2. Show all transports");
        System.out.println("3. Sort by destination");
        System.out.println("4. Filter by destination");
        System.out.println("5. Mark transport as paid"); 
        System.out.println("6. Delete transport"); 
        System.out.println("7. Export transports to file");
        System.out.println("8. Show transports from file");
        System.out.println("0. Back");
        System.out.print("Choose: ");
    }

    // ======= create =======
    private static void handleNewTransport() {
        System.out.println("\n--- New Transport ---");
        try {
            Transport t = new Transport();
            t.setCompany(currentCompany);

            System.out.print("From (e.g. Sofia): ");
            t.setFromLocation(scanner.nextLine());

            System.out.print("To (e.g. Varna): ");
            t.setToLocation(scanner.nextLine());

            System.out.print("Type (1=Cargo, 2=Passenger) [Enter 1 or 2]: ");
            int typeChoice = Integer.parseInt(scanner.nextLine());
            
            if (typeChoice == 1) {
                t.setType(Transport.TransportType.CARGO);
                System.out.print("Cargo Weight (e.g. 1500): ");
                t.setCargoWeight(Double.parseDouble(scanner.nextLine()));
                t.setCargoDescription("Standard Cargo");
                System.out.print("Price per kg (e.g. 0.5): ");
                t.setPricePerKg(new BigDecimal(scanner.nextLine()));
            } else {
                t.setType(Transport.TransportType.PASSENGER);
                System.out.print("Passenger Count (e.g. 45): ");
                t.setPassengerCount(Integer.parseInt(scanner.nextLine()));
                t.setCargoDescription("Passengers");
                System.out.print("Price per passenger (e.g. 5.0): ");
                t.setPricePerPassenger(new BigDecimal(scanner.nextLine()));
            }

            System.out.print("Total Price (e.g. 300): ");
            t.setPrice(new BigDecimal(scanner.nextLine()));


            t.setDepartureDateTime(LocalDate.now().atStartOfDay());
            t.setArrivalDateTime(LocalDate.now().plusDays(1).atStartOfDay());

             try (org.hibernate.Session s = SessionFactoryUtil.getSessionFactory().openSession()) {

                 List<Vehicle> vs = s.createQuery("from Vehicle v join fetch v.company", Vehicle.class).list();
                 for(Vehicle v : vs) {
                     if (typeChoice == 1 && v.getType() == Vehicle.VehicleType.TRUCK) t.setVehicle(v);
                     if (typeChoice == 2 && v.getType() == Vehicle.VehicleType.BUS) t.setVehicle(v);
                 }
                 List<Client> cs = s.createQuery("from Client", Client.class).list();
                 if(!cs.isEmpty()) t.setClient(cs.get(0));
                 
                 List<Employee> drivers = s.createQuery("from Employee e join fetch e.company", Employee.class).list();
                 if (!drivers.isEmpty()) {
                     t.setDriver(drivers.get(0));
                 }
             }
            
            transportService.registerTransport(t);
            System.out.println("Transport registered successfully!");

        } catch (Exception e) {
            System.out.println("Err: " + e.getMessage());
        }
    }

    // ======= read =======
    private static void showAllTransports() {
        System.out.println("\n--- All Transports ---");
        List<Transport> list = transportService.getAllTransports();
        if (list.isEmpty()) System.out.println("No transports found.");
        for (Transport t : list) {
            System.out.printf("ID: %d | %s -> %s | Price: %s | Type: %s%n", 
                t.getId(), t.getFromLocation(), t.getToLocation(), t.getPrice(), t.getType());
        }
    }

    // ======= sort =======
    private static void sortTransportsByDestination() {
        System.out.println("\n--- Sorted by Dest ---");
        List<Transport> list = transportService.getTransportsByDestination("", false); // reuse existing or sort manually

        for (Transport t : list) {
             System.out.printf("%s -> %s%n", t.getFromLocation(), t.getToLocation());
        }
    }

    // ======= filter =======
    private static void filterTransportsByDestination() {
        System.out.print("Enter destination: ");
        String dest = scanner.nextLine();
        List<Transport> list = transportService.getTransportsByDestination(dest, false);
         for (Transport t : list) {
             System.out.printf("Found: %s -> %s%n", t.getFromLocation(), t.getToLocation());
        }
    }

    private static void markTransportAsPaid() {
        System.out.println("(Feature placeholder)");
    }
    
    private static void deleteTransport() {
        System.out.println("(Feature placeholder: Enter ID to delete)");
    }

    // ======= export/import =======
    private static void exportTransports() {
        transportService.exportTransportsToFile("transports_export.csv");
        System.out.println("Exported to transports_export.csv");
    }

    private static void importTransports() {
        transportService.importTransportsFromFile("transports_export.csv");
        System.out.println("Imported from file.");
    }
}
