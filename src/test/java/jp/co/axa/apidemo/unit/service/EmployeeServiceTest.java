package jp.co.axa.apidemo.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exception.NotFoundException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import jp.co.axa.apidemo.services.EmployeeServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {

	@InjectMocks //Creates an instance of the class and injects the mock created with the @Mock annotation into this instance
	EmployeeServiceImpl employeeService;

	@Mock // Used to simulate the behavior of a real object
	private EmployeeRepository employeeRepository;
	
	
	
	//By calling the verify method, we’re checking that our repository was called
	//by calling assertThat we’re checking that our service answered our call with the correct expected value.

	@Test //Tells JUnit that the method to which this annotation is attached can be run as a test case
	public void shouldReturnAllEmployees() {
		List<Employee> Employees = new ArrayList<>();
		
		Employees.add(new Employee());

		given(employeeRepository.findAll()).willReturn(Employees);

		List<Employee> expected = employeeService.retrieveEmployees();

		assertEquals(expected, Employees);
		verify(employeeRepository).findAll();
	}

	@Test
	public void whenGivenId_shouldReturnEmployee_ifFound() {
		Employee employee = new Employee();
		employee.setId(1L);
		employee.setName("Test1");
		employee.setDepartment("Department1");
		employee.setSalary(450000);

		when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

		Employee expected = employeeService.getEmployee(employee.getId());

		assertThat(expected).isSameAs(employee);
		verify(employeeRepository).findById(employee.getId());
	}

	@Test(expected = NotFoundException.class)
	public void should_throw_exception_when_Employee_doesnt_exist() {
		Employee employee = new Employee();
		employee.setId(1L);
		employee.setName("Test1");
		employee.setDepartment("Department1");
		employee.setSalary(450000);

		given(employeeRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));
		employeeService.getEmployee(employee.getId());
	}

	@Test
	public void whenSaveEmployee_shouldReturnEmployee() {
		Employee employee = new Employee();
		employee.setName("Test1");
		employee.setDepartment("Department1");
		employee.setSalary(450000);

		when(employeeRepository.save(ArgumentMatchers.any(Employee.class))).thenReturn(employee);

		Employee created = employeeService.saveEmployee(employee);

		assertThat(created.getName()).isSameAs(employee.getName());
		assertThat(created.getDepartment()).isSameAs(employee.getDepartment());
		assertThat(created.getSalary()).isSameAs(employee.getSalary());
		verify(employeeRepository).save(employee);
	}

	@Test
	public void whenGivenId_shouldDeleteUser_ifFound() {
		Employee employee = new Employee();
		employee.setId(1L);
		employee.setName("Test1");
		employee.setDepartment("Department1");
		employee.setSalary(450000);
		when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

		employeeService.deleteEmployee(employee.getId());
		verify(employeeRepository).deleteById(employee.getId());
	}

	@Test(expected = RuntimeException.class)
	public void should_throw_exception_when_deleteEmployee_doesnot_exist() {
		Employee employee = new Employee();
		employee.setId(1L);
		employee.setName("Test1");
		employee.setDepartment("Department1");
		employee.setSalary(450000);

		given(employeeRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));
		employeeService.deleteEmployee(employee.getId());
	}

	@Test
	public void whenGivenId_shouldUpdateEmployee_ifFound() {
		Employee employee = new Employee();
		employee.setId(1L);
		employee.setName("Test1");
		employee.setDepartment("Department1");
		employee.setSalary(450000);

		Employee newEmployee = new Employee();
		newEmployee.setName("Test2");
		newEmployee.setDepartment("Department2");
		newEmployee.setSalary(40000);
		given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));
		employeeService.updateEmployee(employee.getId(), newEmployee);

		verify(employeeRepository).save(newEmployee);
		verify(employeeRepository).findById(employee.getId());
	}

	@Test(expected = RuntimeException.class)
	public void should_throw_exception_when_employee_doesnot_exist() {
		Employee employee = new Employee();
		employee.setId(1L);
		employee.setName("Test1");
		employee.setDepartment("Department1");
		employee.setSalary(450000);

		Employee newEmployee = new Employee();
		newEmployee.setId(2L);
		newEmployee.setName("Test2");
		newEmployee.setDepartment("Department2");
		newEmployee.setSalary(40000);

		given(employeeRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));
		employeeService.updateEmployee(employee.getId(), newEmployee);
	}
}
