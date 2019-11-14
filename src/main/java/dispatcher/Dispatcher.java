package dispatcher;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import enumerator.EnumEmployeeState;
import model.Employee;

/**
 * 
 * 
 * @author Eric Kubatov
 * 
 */
public class Dispatcher implements Runnable{
	
private static  int MAXIMUN_CONCURRENT_CALLS  = 10;
ExecutorService executor = new ThreadPoolExecutor(10, 10,0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Finds next available employee
	 * 
	 */
	public Employee findEmployee(Collection<Employee> employeeList) {

		Comparator<Employee> byPriotity = (Employee e1, Employee e2) -> Integer
				.compare(e2.getPosition().getPriorityAttention(), e1.getPosition().getPriorityAttention());


		Optional<Employee> employee = employeeList.stream().filter(e -> e.getState() == EnumEmployeeState.AVAILABLE)
				.sorted(byPriotity).findFirst();


		return employee.get();
	}

}
