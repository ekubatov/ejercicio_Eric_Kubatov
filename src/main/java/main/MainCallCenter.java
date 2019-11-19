package main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import dispatcher.CallProducer;
import dispatcher.Dispatcher;
import model.Employee;
/**
 * 
 * 
 * @author Eric Kubatov
 * 
 */
public class MainCallCenter {

	public static void main(String[] args) {

		List<Employee> staff = createListEmployee();	
	    CallProducer iniciadorDeLlamadas = new CallProducer();

	    Dispatcher dispatcher = new Dispatcher(staff, iniciadorDeLlamadas);
        dispatcher.start();
        
        try {
			TimeUnit.SECONDS.sleep(1);
			ExecutorService executorService = Executors.newSingleThreadExecutor();
			executorService.execute(dispatcher);
			TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ex) {
        	System.out.println("Error "+ ex.getMessage());
		}

	}
	
	private static List<Employee> createListEmployee() {
        
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

}
