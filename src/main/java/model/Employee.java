package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import enumerator.EnumEmployeePosition;
import enumerator.EnumEmployeeState;

/**
 * Employee entity of domain model
 * 
 * @author Eric Kubatov
 * 
 */

public class Employee implements Runnable{

	private String fullName;
    private EnumEmployeePosition position;
    private EnumEmployeeState state;
    private InboundCall call;
    private List <InboundCall> listAsweredCalls;
   
	
	public Employee(String fullName, EnumEmployeePosition position) {
		this.fullName = fullName;
		this.position = position;
		this.state = EnumEmployeeState.AVAILABLE;
		this.listAsweredCalls = Collections.synchronizedList(new ArrayList());
	}
	
	public static Employee createEmployeeOperator(String name) {
		return new Employee(name, EnumEmployeePosition.OPERATOR);
	}

	public static Employee createEmployeeSupervisor(String name) {
		return new Employee(name, EnumEmployeePosition.SUPERVISOR);
	}

	public static Employee createEmployeeDirector(String name) {
		return new Employee(name, EnumEmployeePosition.DIRECTOR);
	}

	
	@Override
	public void run() {
		while (true) {
			if (this.getState() == EnumEmployeeState.BUSY) {
				try {
					TimeUnit.SECONDS.sleep(this.call.getDurationInSeconds());
					System.out.println("Llamada de "+this.call.getDurationInSeconds()+ " segundos atendida por "+this.fullName );
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				} finally {
					this.setState(EnumEmployeeState.AVAILABLE);				
				}
				this.listAsweredCalls.add(call);
			}
		}
		
	}
	
	/**
	 * Change state to the employee who will answer a inbound call and assign the call to the employee
	 *
	 */
	public synchronized void answerCall(InboundCall call) {
		try {
			this.setState(EnumEmployeeState.BUSY);
			this.call = call;			
		} catch (NoSuchElementException nseExcepction) {
			nseExcepction.printStackTrace();
		}
	}

	public String getFullName() {
		return fullName;
	}

	public EnumEmployeePosition getPosition() {
		return position;
	}

	public synchronized EnumEmployeeState getState() {
		return state;
	}

	public synchronized void setState(EnumEmployeeState state) {
		this.state = state;
	}

	public List<InboundCall> getListAsweredCalls() {
		return listAsweredCalls;
	}
	
	
	
	

}
