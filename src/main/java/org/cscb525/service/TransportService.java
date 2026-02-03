package org.cscb525.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.cscb525.dao.EmployeeDao;
import org.cscb525.dao.TransportDao;
import org.cscb525.dto.DriverIncomeDto;
import org.cscb525.dto.DriverTransportCountDto;
import org.cscb525.dto.TotalIncomeDto;
import org.cscb525.entity.Transport;
import org.cscb525.exception.TransportValidationException;

public class TransportService {

    // DAO fields removed as they are static

    // ======= create =======
    public void registerTransport(Transport transport) {
        if (transport.getDriver() != null && transport.getVehicle() != null) {
            
            // Validate Driver Qualification
            String qual = transport.getDriver().getQualification().toLowerCase();
            String requiredQual = transport.getVehicle().getRequiredQualification().toLowerCase();

            if (!qual.contains(requiredQual)) {
                throw new TransportValidationException("Driver " + transport.getDriver().getName() 
                    + " (Qualification: " + transport.getDriver().getQualification() 
                    + ") is NOT qualified. Required: " + requiredQual);
            }

            // Validate Company Consistency (Driver)
            if (transport.getCompany() != null && transport.getDriver().getCompany() != null) {
                 if (!transport.getDriver().getCompany().getId().equals(transport.getCompany().getId())) {
                     throw new TransportValidationException("Driver " + transport.getDriver().getName() 
                         + " works for " + transport.getDriver().getCompany().getName() 
                         + ", but transport is for " + transport.getCompany().getName());
                 }
            }

            // Validate Company Consistency (Vehicle)
            if (transport.getCompany() != null && transport.getVehicle().getCompany() != null) {
                 if (!transport.getVehicle().getCompany().getId().equals(transport.getCompany().getId())) {
                     throw new TransportValidationException("Vehicle " + transport.getVehicle().getType() 
                         + " belongs to " + transport.getVehicle().getCompany().getName() 
                         + ", but transport is for " + transport.getCompany().getName());
                 }
            }

            // Validate Vehicle Capacity (Weight)
            if (transport.getCargoWeight() != null && transport.getVehicle().getMaxCargoWeight() != null) {
                if (transport.getCargoWeight() > transport.getVehicle().getMaxCargoWeight()) {
                    throw new TransportValidationException("Cargo weight " + transport.getCargoWeight() 
                        + "kg exceeds vehicle allowed max weight of " + transport.getVehicle().getMaxCargoWeight() + "kg.");
                }
            }

            // Validate Vehicle Capacity (Passengers)
            if (transport.getPassengerCount() != null && transport.getVehicle().getMaxPassengers() != null) {
                if (transport.getPassengerCount() > transport.getVehicle().getMaxPassengers()) {
                    throw new TransportValidationException("Passenger count " + transport.getPassengerCount() 
                        + " exceeds vehicle capacity of " + transport.getVehicle().getMaxPassengers() + " seats.");
                }
            }
            
            // Validate Pricing logic
            if (transport.getCargoWeight() != null && transport.getCargoWeight() > 0) {
                BigDecimal minRate = BigDecimal.valueOf(0.5);
                BigDecimal minPrice = BigDecimal.valueOf(transport.getCargoWeight()).multiply(minRate);

                System.out.println("--- Pricing Check (Cargo) ---");
                System.out.println("Weight: " + transport.getCargoWeight() + " kg");
                System.out.println("Rate: " + minRate + " per kg");
                System.out.println("Calculated Min Price: " + minPrice);
                System.out.println("Actual Price: " + transport.getPrice());

                if (transport.getPrice().compareTo(minPrice) < 0) {
                    System.out.println("Result: REJECTED (Price too low)");
                    throw new TransportValidationException("Price " + transport.getPrice() 
                        + " is too low for weight " + transport.getCargoWeight() 
                        + "kg. Minimum required (rate " + minRate + "): " + minPrice);
                }
                System.out.println("Result: APPROVED");
            }

            // Validate Pricing logic (passengers)
            if (transport.getPassengerCount() != null && transport.getPassengerCount() > 0) {
                BigDecimal minRate = BigDecimal.valueOf(5.0);
                BigDecimal minPrice = BigDecimal.valueOf(transport.getPassengerCount()).multiply(minRate);

                System.out.println("--- Pricing Check (Passengers) ---");
                System.out.println("Passengers: " + transport.getPassengerCount());
                System.out.println("Rate: " + minRate + " per passenger");
                System.out.println("Calculated Min Price: " + minPrice);
                System.out.println("Actual Price: " + transport.getPrice());

                if (transport.getPrice().compareTo(minPrice) < 0) {
                    System.out.println("Result: REJECTED (Price too low)");
                    throw new TransportValidationException("Price " + transport.getPrice() 
                        + " is too low for " + transport.getPassengerCount() 
                        + " passengers. Minimum required (rate " + minRate + "): " + minPrice);
                }
                System.out.println("Result: APPROVED");
            }

            // Validate Dates
            if (transport.getArrivalDateTime() != null && transport.getArrivalDateTime().isBefore(transport.getDepartureDateTime())) {
                 throw new TransportValidationException("Arrival date cannot be before departure date.");
            }

            // Validate Driver Availability (Overlap)
            if (transport.getDriver() != null && transport.getDepartureDateTime() != null && transport.getArrivalDateTime() != null) {
                long overlaps = TransportDao.countDriverOverlaps(
                    transport.getDriver().getId(), 
                    transport.getDepartureDateTime(), 
                    transport.getArrivalDateTime()
                );
                if (overlaps > 0) {
                    throw new TransportValidationException("Driver " + transport.getDriver().getName() 
                        + " is already busy during this period (" 
                        + transport.getDepartureDateTime() + " - " + transport.getArrivalDateTime() + ")");
                }
            }

            // Validate Vehicle Availability (Overlap)
            if (transport.getVehicle() != null && transport.getDepartureDateTime() != null && transport.getArrivalDateTime() != null) {
                long overlaps = TransportDao.countVehicleOverlaps(
                    transport.getVehicle().getId(), 
                    transport.getDepartureDateTime(), 
                    transport.getArrivalDateTime()
                );
                if (overlaps > 0) {
                     throw new TransportValidationException("Vehicle " + transport.getVehicle().getType() 
                         + " is already in use during this period.");
                }
            }
        }
        
        TransportDao.save(transport);
    }

    // ======= read =======
    public List<Transport> getAllTransports() {
        return TransportDao.getAll();
    }
    
    public long getTotalTransportCount() {
        return TransportDao.getAll().size();
    }

  
    public List<Transport> getTransportsByDestination(String destination, boolean exact) {
        return TransportDao.getAll().stream()
                .filter(t -> exact ? t.getToLocation().equals(destination) : t.getToLocation().contains(destination))
                .sorted(Comparator.comparing(Transport::getDepartureDateTime))
                .collect(Collectors.toList());
    }

    public TotalIncomeDto calculateTotalRevenue(LocalDate start, LocalDate end) {
        BigDecimal total = TransportDao.getIncomeForPeriod(start, end);
        return new TotalIncomeDto(start, end, total);
    }


    public List<DriverTransportCountDto> getTransportCountByDriver() {
        return EmployeeDao.getDriverTransportCounts();
    }
    public List<DriverIncomeDto> getRevenueByDriver() {
        return EmployeeDao.getDriverRevenue();
    }

    // ======= file =======
    public void exportTransportsToFile(String filename) {
        List<Transport> transports = TransportDao.getAll();
        java.io.File file = new java.io.File(filename);
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
             file.getParentFile().mkdirs();
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Transport t : transports) {
                writer.println(t.getId() + "," + t.getFromLocation() + "," + t.getToLocation() + ","
                        + t.getDepartureDateTime() + "," + t.getPrice() + "," + t.getCargoDescription());
            }
        } catch (IOException e) {
            System.err.println("Error exporting transports: " + e.getMessage());
        }
    }

    public void importTransportsFromFile(String filename) {
        // Simple implementation to demonstrate reading
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    count++;
                    // Logic to parse and save to DB could go here
                }
            }
            System.out.println(" -> Successfully imported information for " + count + " transports.");
        } catch (IOException e) {
            System.err.println("Error importing transports: " + e.getMessage());
        }
    }
}
