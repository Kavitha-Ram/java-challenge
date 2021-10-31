package jp.co.axa.apidemo.controllers;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import lombok.extern.log4j.Log4j2;

/**
 * @author Kavitha
 *
 */
@RestController
@RequestMapping("/api/v1")
@Log4j2
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	/**
	 * @return All employees
	 */
	@GetMapping("/employees")
	public List<Employee> getEmployees() {
		List<Employee> employees = employeeService.retrieveEmployees();
		log.info("Get all employee details Successfully");
		return employees;
	}

	/**
	 * @param employeeId(Long)
	 * @return Corresponding Employee by Id and status OK if present else send
	 *         status NOT_FOUND
	 */
	@GetMapping("/employees/{employeeId}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Employee> getEmployee(@PathVariable(name = "employeeId") Long employeeId) {
		log.info("Get an employee detail with employeeId", employeeId);
		Employee employee = employeeService.getEmployee(employeeId);
		return ResponseEntity.ok().body(employee);
	}

	/**
	 * @param employee
	 * @return employee with status CREATED
	 */
	@PostMapping("/employees")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Employee> saveEmployee(@Valid @RequestBody Employee employee) {
		Employee savedEmployee = employeeService.saveEmployee(employee);
		log.info("Employee Saved Successfully");
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{employeeId}")
				.buildAndExpand(employee.getId()).toUri();
		return ResponseEntity.created(uri).body(savedEmployee);
	}

	/**
	 * @param employeeId
	 * @return Corresponding Employee will be deleted if present else send status
	 *         NOT_FOUND
	 */
	@DeleteMapping("/employees/{employeeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteEmployee(@PathVariable(name = "employeeId") Long employeeId) {
		employeeService.deleteEmployee(employeeId);
		log.info("Employee Deleted Successfully");
	}

	/**
	 * @param employee
	 * @param employeeId
	 * @return Corresponding Employee details will be updated if present else send
	 *         status NOT_FOUND
	 */
	@PutMapping("/employees/{employeeId}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> updateEmployee(@Valid @RequestBody Employee employee,
			@PathVariable(name = "employeeId") Long employeeId) {
		Employee updatedEmployee = employeeService.updateEmployee(employeeId, employee);
		log.info("Employee Details updated Successfully");
		return ResponseEntity.ok().body(updatedEmployee);
	}

}
