package org.cscb525.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.cscb525.dao.ClientDao;
import org.cscb525.dao.CompanyDao;
import org.cscb525.dao.EmployeeDao;
import org.cscb525.dao.VehicleDao;
import org.cscb525.dto.EmployeeDto;
import org.cscb525.entity.Client;
import org.cscb525.entity.Company;
import org.cscb525.entity.Employee;
import org.cscb525.entity.Vehicle;

public class CompanyService {



    // ======= create =======
    public void createCompany(Company company) {
        CompanyDao.saveCompany(company);
    }

    // ======= read =======
    public Company getCompany(long id) {
        return CompanyDao.getById(id);
    }

    // ======= update =======
    public void hireEmployee(long companyId, Employee employee) {
        Company c = CompanyDao.getById(companyId);
        if (c != null) {
            employee.setCompany(c);
            EmployeeDao.save(employee);
        }
    }

    // ======= update =======
    public void buyVehicle(long companyId, Vehicle vehicle) {
        Company c = CompanyDao.getById(companyId);
        if (c != null) {
            vehicle.setCompany(c);
            VehicleDao.saveVehicle(vehicle);
        }
    }

    // ======= update =======
    public void addClient(long companyId, Client client) {
        Company c = CompanyDao.getById(companyId);
        if (c != null) {
            client.setCompany(c);
            ClientDao.save(client);
        }
    }

    // ======= read =======
    public List<Company> getCompaniesSortedByName() {
        return CompanyDao.getAll().stream()
                .sorted(Comparator.comparing(Company::getName))
                .collect(Collectors.toList());
    }

    // ======= read =======
    public List<Company> getCompaniesSortedByRevenue() {
        return CompanyDao.getAll().stream()
                .sorted(Comparator.comparing(Company::getName))
                .collect(Collectors.toList());
    }

    // ======= read =======
    public List<EmployeeDto> getEmployeesSortedBySalary() {
        return EmployeeDao.getAll().stream()
                .map(e -> new EmployeeDto(e.getName(), e.getQualification(), e.getSalary(), e.getCompany() != null ? e.getCompany().getName() : "N/A"))
                .distinct()
                .sorted(Comparator.comparing(EmployeeDto::getSalary).reversed())
                .collect(Collectors.toList());
    }

    // ======= read =======
    public List<EmployeeDto> getEmployeesSortedByQualification() {
        return EmployeeDao.getAll().stream()
                .sorted(Comparator.comparing(Employee::getQualification))
                .map(e -> new EmployeeDto(e.getName(), e.getQualification(), e.getSalary(), e.getCompany() != null ? e.getCompany().getName() : "N/A"))
                .collect(Collectors.toList());
    }
}
