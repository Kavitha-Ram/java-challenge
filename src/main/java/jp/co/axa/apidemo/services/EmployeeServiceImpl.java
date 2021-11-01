package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exception.NotFoundException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	public void setEmployeeRepository(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	public List<Employee> retrieveEmployees() {
		List<Employee> employees = employeeRepository.findAll();
		return employees;
	}

	@Cacheable(cacheNames = "employees", key="#employeeId")
	public Employee getEmployee(Long employeeId) {
		return employeeRepository.findById(employeeId).orElseThrow(() -> new NotFoundException(employeeId));

	}

	public Employee saveEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}

	@CacheEvict(cacheNames = "employees", key = "#employeeId")
	public void deleteEmployee(Long employeeId) {
		employeeRepository.findById(employeeId).orElseThrow(() -> new NotFoundException(employeeId));

		employeeRepository.deleteById(employeeId);
	}

	@CachePut(cacheNames = "employees", key="#employeeId")
	public Employee updateEmployee(Long employeeId, Employee employee) {
		employeeRepository.findById(employeeId)
          .orElseThrow(() -> new NotFoundException(employeeId));

		employee.setId(employeeId);
		return employeeRepository.save(employee);
	}

}