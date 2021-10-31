package jp.co.axa.apidemo.unit.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.axa.apidemo.controllers.EmployeeController;
import jp.co.axa.apidemo.util.JsonUtil;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.exception.NotFoundException;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import jp.co.axa.apidemo.services.EmployeeService;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class) // auto-configure the Spring MVC infrastructure for our unit tests.
public class EmployeeControllerTest {

	@Autowired
	private MockMvc mockMvc; // test MVC controllers without starting a full HTTP server.

	@MockBean
	private EmployeeService employeeService;

	@MockBean
	private EmployeeRepository employeeRepository;

	@Test
	public void listAllUEmployees_whenGetMethod() throws Exception {

		Employee employee = new Employee();
		employee.setId(1L);
		employee.setName("Test1");
		employee.setDepartment("Department1");
		employee.setSalary(450000);

		List<Employee> allEmployees = Arrays.asList(employee);

		given(employeeService.retrieveEmployees()).willReturn(allEmployees);

		mockMvc.perform(get("/api/v1/employees").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].name", is(employee.getName())))
				.andExpect(jsonPath("$[0].department", is(employee.getDepartment())))
				.andExpect(jsonPath("$[0].salary", is(employee.getSalary())));

	}

	@Test
	public void listEmployeeById_whenGetMethod() throws Exception {

		Employee employee = new Employee();
		employee.setId(2L);
		employee.setName("Test2");
		employee.setDepartment("Department2");
		employee.setSalary(650000);

		given(employeeService.getEmployee(employee.getId())).willReturn(employee);

		mockMvc.perform(get("/api/v1/employees/{employeeId}", 2L).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.name", is(employee.getName())))
				.andExpect(jsonPath("$.department", is(employee.getDepartment())))
				.andExpect(jsonPath("$.salary", is(employee.getSalary())));

	}

	@Test
	public void should_throw_exception_when_employee_doesnt_exist() throws Exception {
		Employee employee = new Employee();
		employee.setId(3L);
		employee.setName("Test3");
		employee.setDepartment("Department3");
		employee.setSalary(90000);

		Mockito.doThrow(new NotFoundException(employee.getId())).when(employeeService).getEmployee(employee.getId());

		mockMvc.perform(get("/api/v1/employees/{employeeId}", 3L).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void saveEmployee_whenPostMethod() throws Exception {

		Employee employee = new Employee();
		employee.setName("Test4");
		employee.setDepartment("Department4");
		employee.setSalary(90000);

		given(employeeService.saveEmployee(employee)).willReturn(employee);

		mockMvc.perform(
				post("/api/v1/employees").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(employee)))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.name", is(employee.getName())))
				.andExpect(jsonPath("$.department", is(employee.getDepartment())))
				.andExpect(jsonPath("$.salary", is(employee.getSalary())));
	}

	@Test
	public void removeEmployeeById_whenDeleteMethod() throws Exception {
		Employee employee = new Employee();
		employee.setId(5L);
		employee.setName("Test5");
		employee.setDepartment("Department5");
		employee.setSalary(90000);

		doNothing().when(employeeService).deleteEmployee(employee.getId());

		mockMvc.perform(delete("/api/v1/employees/{employeeId}", 6L).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	public void should_throw_exception_delete_when_employee_doesnt_exist() throws Exception {
		Employee employee = new Employee();
		employee.setId(6L);
		employee.setName("Test6");
		employee.setDepartment("Department6");
		employee.setSalary(90000);
		Mockito.doThrow(new NotFoundException(employee.getId())).when(employeeService).deleteEmployee(employee.getId());

		mockMvc.perform(delete("/api/v1/employees/{employeeId}", 6L).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void updateEmployee_whenPutEmployee() throws Exception {

		Employee employee = new Employee();
		employee.setId(7L);
		employee.setName("Test7");
		employee.setDepartment("Department7");
		employee.setSalary(90000);
		given(employeeService.updateEmployee(employee.getId(), employee)).willReturn(employee);

		mockMvc.perform(put("/api/v1/employees/{employeeId}", 7L).content(asJsonString(employee))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("name", is(employee.getName())))
				.andExpect(jsonPath("$.department", is(employee.getDepartment())))
				.andExpect(jsonPath("$.salary", is(employee.getSalary())));
	}

	@Test
	public void should_throw_exception_when_updateEmployee_doesnt_exist() throws Exception {
		Employee employee = new Employee();
		employee.setId(8L);
		employee.setName("Test8");
		employee.setDepartment("Department8");
		employee.setSalary(90000);
		Mockito.doThrow(new NotFoundException(employee.getId())).when(employeeService).updateEmployee(employee.getId(),
				employee);
		mockMvc.perform(put("/api/v1/employees/{employeeId}", 7L).content(asJsonString(employee))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
