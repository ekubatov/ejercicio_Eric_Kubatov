package dispatcher;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import enumerator.EnumEmployeeState;
import model.Employee;
import model.InboundCall;

/**
 * 
 * 
 * @author Eric Kubatov
 * 
 */
public class Dispatcher implements Runnable{
	
	private static int MAXIMUM_CONCURRENT_CALLS = 10;
	private ExecutorService executor;
	private ConcurrentLinkedDeque<Employee> employees;
	private static ConcurrentLinkedDeque<InboundCall> listInboundCalls = new ConcurrentLinkedDeque<InboundCall>();


	
	public Dispatcher(List<Employee> employees) {
		this.executor = new ThreadPoolExecutor(MAXIMUM_CONCURRENT_CALLS, MAXIMUM_CONCURRENT_CALLS, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>());
		this.employees = new ConcurrentLinkedDeque<Employee>(employees);
		//this.CallProducer = new CallProducer();
		//this.executor = Executors.newFixedThreadPool(employees.size() + 1);
	}
	
	public static ConcurrentLinkedDeque<InboundCall> getListInboundCalls() {
		return listInboundCalls;
	}

	public static void addCalls(InboundCall call) {
		listInboundCalls.add(call);
	}

	
	@Override
	public void run() {
		while (true) {
			if (listInboundCalls.isEmpty()) {
				continue;
			} else {
				Employee employee = this.dispatchCall(this.employees);
				if (employee == null) {
					continue;
				}
				try {
					InboundCall call = listInboundCalls.pollFirst();
					employee.answerCall(call);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void init() {
		for (Employee employee : this.employees) {
			this.executor.execute(employee);
		}
	}
	

	/**
	 * Finds next available employee
	 * 
	 */
	public synchronized Employee dispatchCall(Collection<Employee> employeeList) {

		Comparator<Employee> byPriotity = (Employee e1, Employee e2) -> Integer
				.compare(e2.getPosition().getPriorityAttention(), e1.getPosition().getPriorityAttention());


		Optional<Employee> employee = employeeList.stream().filter(e -> e.getState() == EnumEmployeeState.AVAILABLE)
				.sorted(byPriotity).findFirst();


		return employee.get();
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}

}
