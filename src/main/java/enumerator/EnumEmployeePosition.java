package enumerator;
/**
 * 
 * 
 * @author Eric Kubatov
 * 
 */
public enum EnumEmployeePosition {

	OPERATOR(1), SUPERVISOR(2), DIRECTOR(3);
	
	private int priorityAttention;
	
	EnumEmployeePosition(int priority){
		this.priorityAttention = priority;
	}

	public int getPriorityAttention() {
		return priorityAttention;
	}
	
	
}
