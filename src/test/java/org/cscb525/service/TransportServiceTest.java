package org.cscb525.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.cscb525.entity.Client;
import org.cscb525.entity.Company;
import org.cscb525.entity.Employee;
import org.cscb525.entity.Transport;
import org.cscb525.entity.Vehicle;
import org.cscb525.exception.TransportValidationException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TransportServiceTest {

    private TransportService transportService;
    private Company company;
    private Employee driver;
    private Vehicle truck;
    private Vehicle bus;
    private Client client;

    @BeforeEach
    void setUp() {
        transportService = new TransportService();
        company = new Company("Test Company");
        company.setId(1L);

        driver = new Employee("Test Driver", "Heavy Truck License", new BigDecimal("1000"), company);
        driver.setId(1L);

        truck = new Vehicle(Vehicle.VehicleType.TRUCK, "Heavy Truck License", company);
        truck.setId(1L);
        truck.setMaxCargoWeight(20000.00);

        bus = new Vehicle(Vehicle.VehicleType.BUS, "Bus License", company);
        bus.setId(2L);
        bus.setMaxPassengers(40);
        bus.setRequiredQualification("Bus License");

        client = new Client("Test Client", company);
        client.setId(1L);
    }
    //проверяват ценообразуването и капацитета
    @Test
    @DisplayName("Should validate minimum price for CARGO")
    void testCargoPricingValidation() {
        Transport transport = new Transport();
        transport.setType(Transport.TransportType.CARGO);
        transport.setCompany(company);
        transport.setDriver(driver);
        transport.setVehicle(truck);
        transport.setClient(client);
        transport.setFromLocation("A");
        transport.setToLocation("B");
        transport.setDepartureDateTime(LocalDate.now().atStartOfDay());
        transport.setArrivalDateTime(LocalDate.now().plusDays(1).atStartOfDay());
        

        transport.setCargoWeight(1000.00);

        transport.setPrice(new BigDecimal("400.00"));
        Exception exception = assertThrows(TransportValidationException.class, () -> {
            transportService.registerTransport(transport);
        });
        assertTrue(exception.getMessage().contains("is too low for weight"));


        transport.setPrice(new BigDecimal("500.00"));

    }

    @Test
    @DisplayName("Should validate minimum price for PASSENGERS")
    void testPassengerPricingValidation() {
        Employee busDriver = new Employee("Bus Driver", "Bus License", new BigDecimal("1000"), company);
        busDriver.setId(2L);
        
        Transport transport = new Transport();
        transport.setType(Transport.TransportType.PASSENGER);
        transport.setCompany(company);
        transport.setDriver(busDriver);
        transport.setVehicle(bus);
        transport.setClient(client);
        transport.setFromLocation("A");
        transport.setToLocation("B");
        transport.setDepartureDateTime(LocalDate.now().atStartOfDay());
        transport.setArrivalDateTime(LocalDate.now().plusDays(1).atStartOfDay());

        transport.setPassengerCount(40);

        transport.setPrice(new BigDecimal("40.00"));
        Exception exception = assertThrows(TransportValidationException.class, () -> {
            transportService.registerTransport(transport);
        });
        assertTrue(exception.getMessage().contains("is too low for"));
    }

    @Test
    @DisplayName("Should validate Vehicle MAX WEIGHT capacity")
    void testVehicleWeightCapacity() {
        Transport transport = new Transport();
        transport.setType(Transport.TransportType.CARGO);
        transport.setCompany(company);
        transport.setDriver(driver);
        transport.setVehicle(truck);
        transport.setClient(client);
        transport.setFromLocation("A");
        transport.setToLocation("B");
        transport.setDepartureDateTime(LocalDate.now().atStartOfDay());
        transport.setArrivalDateTime(LocalDate.now().plusDays(1).atStartOfDay());
        transport.setPrice(new BigDecimal("50000.00")); // High price ok

        // Max is 20,000. Try 25,000
        transport.setCargoWeight(25000.00);

        Exception exception = assertThrows(TransportValidationException.class, () -> {
            transportService.registerTransport(transport);
        });
        assertTrue(exception.getMessage().contains("exceeds vehicle allowed max weight"));
    }

    @Test
    @DisplayName("Should validate Vehicle MAX PASSENGER capacity")
    void testVehiclePassengerCapacity() {
        Employee busDriver = new Employee("Bus Driver", "Bus License", new BigDecimal("1000"), company);
        busDriver.setId(2L);

        Transport transport = new Transport();
        transport.setType(Transport.TransportType.PASSENGER);
        transport.setCompany(company);
        transport.setDriver(busDriver);
        transport.setVehicle(bus);
        transport.setClient(client);
        transport.setFromLocation("A");
        transport.setToLocation("B");
        transport.setDepartureDateTime(LocalDate.now().atStartOfDay());
        transport.setArrivalDateTime(LocalDate.now().plusDays(1).atStartOfDay());
        transport.setPrice(new BigDecimal("50000.00"));

        // Max is 50. Try 60
        transport.setPassengerCount(60);

        Exception exception = assertThrows(TransportValidationException.class, () -> {
            transportService.registerTransport(transport);
        });
        assertTrue(exception.getMessage().contains("exceeds vehicle capacity"));
    }
}
