package jp.co.axa.apidemo.services;

import java.util.List;

import jp.co.axa.apidemo.entities.Employee;

public interface EmployeeService {

    public List<Employee> retrieveEmployees();

    public Employee getEmployee(Long employeeId);

    public Employee saveEmployee(Employee employee);

    public void deleteEmployee(Long employeeId);

    public Employee updateEmployee(Long employeeId, Employee employee);
}