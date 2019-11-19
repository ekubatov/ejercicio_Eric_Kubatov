package dispatcher;

import org.junit.Test;

import enumerator.EnumEmployeePosition;
import enumerator.EnumEmployeeState;
import model.Employee;
import model.InboundCall;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 
 * 
 * @author Eric Kubatov
 * 
 */
public class DispatcherTest {
	
	
	/**
	 * Test produce inbounds calls
	 * 
	 */
	@Test
	public void testCallProducer() throws InterruptedException {
		Integer minExpectedCalls = 10;
		Integer maxExpectedCalls = 20;
		CallProducer iniciadorDeLlamadas = new CallProducer();
		ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(iniciadorDeLlamadas);
        TimeUnit.SECONDS.sleep(1);
        executor.awaitTermination(2, TimeUnit.SECONDS);
       
		assertTrue(minExpectedCalls <= Dispatcher.getListInboundCalls().size());
		assertTrue(maxExpectedCalls >= Dispatcher.getListInboundCalls().size());
		System.out.println("TEST 1 testCallProducer: cantidad de llamadas llegadas= "+Dispatcher.getListInboundCalls().size());
		
		iniciadorDeLlamadas.stop();
	}
	
	/**
	 * Test assign a inbound call to Employee when all Employee are Availables
	 * 
	 */
	@Test
	public void testAssignToEmployeeAllAvailable() {
		Employee e3 = Employee.createEmployeeDirector("Eric Kubatov");
		Employee e2 = Employee.createEmployeeOperator("Tommy Shelby");
		Employee e1 = Employee.createEmployeeSupervisor("Clark Kent");
		List<Employee> staff = Arrays.asList(e1, e2, e3);

		Dispatcher dispatcher = new Dispatcher(staff, null);
		Employee employee = dispatcher.dispatchCall(staff);
		System.out.println("TEST 2 testAssignToEmployeeAllAvailable: llamada asignada a "+employee.getFullName());
		assertEquals(EnumEmployeePosition.OPERATOR.getPriorityAttention(), employee.getPosition().getPriorityAttention());
	}
	
	/**
	 * Test assign a inbound call to Employee when all are Busy
	 * 
	 */
	@Test
	public void testAssignAllBusy() {
		Employee e3 = Employee.createEmployeeDirector("Eric Kubatov");
		Employee e2 = Employee.createEmployeeOperator("Tommy Shelby");
		Employee e1 = Employee.createEmployeeSupervisor("Clark Kent");
		e2.setState(EnumEmployeeState.BUSY);
		e3.setState(EnumEmployeeState.BUSY);
		e1.setState(EnumEmployeeState.BUSY);
		List<Employee> staff = Arrays.asList(e1, e2, e3);

		Dispatcher dispatcher = new Dispatcher(staff, null);
		Employee employee = dispatcher.dispatchCall(staff);
		assertNull(employee);
	}
	
	/**
	 * Test assign a inbound call to Supervisor (Operators are busys)
	 * 
	 */
	@Test
	public void testAssignToSupervisor() {
	
		Employee e3 = Employee.createEmployeeDirector("Eric Kubatov");
		Employee e2 = Employee.createEmployeeOperator("Tommy Shelby");
		Employee e1 = Employee.createEmployeeSupervisor("Clark Kent");
		e2.setState(EnumEmployeeState.BUSY);
		List<Employee> staff = Arrays.asList(e1, e2, e3);

		Dispatcher dispatcher = new Dispatcher(staff, null);
		Employee employee = dispatcher.dispatchCall(staff);
		System.out.println("TEST 3 testAssignToSupervisor: llamada asignada a "+employee.getFullName());
		assertEquals(EnumEmployeePosition.SUPERVISOR.getPriorityAttention(), employee.getPosition().getPriorityAttention());
	}
	
	/**
	 * Test assign a inbound call to Director (Operators and supervisor are busys)
	 * 
	 */
	@Test
	public void testAssignToDirector() {
		Employee e3 = Employee.createEmployeeDirector("Eric Kubatov");
		Employee e2 = Employee.createEmployeeOperator("Tommy Shelby");
		Employee e1 = Employee.createEmployeeSupervisor("Clark Kent");
		e2.setState(EnumEmployeeState.BUSY);
		e1.setState(EnumEmployeeState.BUSY);
		List<Employee> staff = Arrays.asList(e1, e2, e3);

		Dispatcher dispatcher = new Dispatcher(staff, null);
		Employee employee = dispatcher.dispatchCall(staff);
		System.out.println("TEST 4 testAssignToDirector: llamada asignada a "+employee.getFullName());
		assertEquals(EnumEmployeePosition.DIRECTOR.getPriorityAttention(), employee.getPosition().getPriorityAttention());
	}
	

	/**
	 * Test dispatch many calls (calls > 10) to Employees
	 * 
	 */
	@Test
	public void testDispatchToEmployees() throws InterruptedException {
		List<Employee> staff = this.createEmployeeList();
		CallProducer iniciadorDeLlamadas = new CallProducer();
		Dispatcher dispatcher = new Dispatcher(staff, iniciadorDeLlamadas);
	    dispatcher.start();
		TimeUnit.SECONDS.sleep(1);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(dispatcher);
		TimeUnit.SECONDS.sleep(1);
		createListInboundCalls(10).stream().forEach(call -> {
			Dispatcher.addCalls(call);
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				fail();
			}
		});
		Integer minAnsweredCalls = 20;
		executor.awaitTermination(minAnsweredCalls, TimeUnit.SECONDS);
		Integer realAmountAnsweredCalls = staff.stream().mapToInt(employee -> employee.getListAsweredCalls().size()).sum();
		assertTrue(realAmountAnsweredCalls >= minAnsweredCalls);
		System.out.println("TEST testDispatchToEmployees: cantidad de llamadas atendidas= "+realAmountAnsweredCalls);
	}
	

	 /**
     * Create a Employee list for test
     *
     */
	private List<Employee> createEmployeeList(){

        List<Employee> staff = new ArrayList<Employee>();
		
		staff.add(Employee.createEmployeeDirector("Eric Kubatov"));

		staff.add(Employee.createEmployeeSupervisor("Lana Lang"));
		staff.add(Employee.createEmployeeSupervisor("Lois Lane"));
		staff.add(Employee.createEmployeeSupervisor("Clark Kent"));
		
		staff.add(Employee.createEmployeeOperator("Tommy Shelby"));
		staff.add(Employee.createEmployeeOperator("Bruno Diaz"));
		staff.add(Employee.createEmployeeOperator("Peter Parker"));
		staff.add(Employee.createEmployeeOperator("Bart Simpsons"));
		staff.add(Employee.createEmployeeOperator("Whalter white"));
		
		return staff;
	}
	
	 /**
     * Create a new random call list
     *
     * @param amountList: amount of calls to be created
     */
    private  List<InboundCall> createListInboundCalls(Integer amountList) {
 
        List<InboundCall> list = new ArrayList<InboundCall>();
        for (int i = 0; i < amountList; i++) {
        	list.add(InboundCall.createCall());
        }
        return list;
    }
	
	
}
