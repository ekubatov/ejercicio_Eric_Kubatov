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
	
	private Boolean run;
	private ExecutorService executor;
	private ConcurrentLinkedDeque<Employee> employees;
	private CallProducer callProducer;
	private static ConcurrentLinkedDeque<InboundCall> listInboundCalls = new ConcurrentLinkedDeque<>();



	public Dispatcher(List<Employee> employees, CallProducer producer) {
		this.employees = new ConcurrentLinkedDeque<Employee>(employees);
		this.callProducer = producer;
		// fijo  número de threads a ejecutar, es decir, que nos llevará la gestión de los procesos a ejecutar en los threads del programa.
		this.executor = Executors.newFixedThreadPool(employees.size() + 1);
	}
	
	public static ConcurrentLinkedDeque<InboundCall> getListInboundCalls() {
		return listInboundCalls;
	}

	public static synchronized void addCalls(InboundCall call) {
		listInboundCalls.add(call);
	}
	
	public synchronized Boolean getRun() {
		return run;
	}

	
	@Override
	public void run() {
		while (getRun()) {
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
	
	/**
	 * stop all threads
	 * 
	 */

	public synchronized void stop() {
		this.run = false;
		this.executor.shutdown();
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
		
		return (employee.isPresent()) ? employee.get():  null;
	
	}

	public synchronized void start() {
		this.run = true;
		this.executor.execute(this.callProducer);

		for (Employee employee : this.employees) {
			this.executor.execute(employee);
		}
	}

}
