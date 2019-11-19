package enumerator;
/**
 * 
 * 
 * @author Eric Kubatov
 * 
 */
public enum EnumEmployeePosition {

	OPERATOR(3), SUPERVISOR(2), DIRECTOR(1);
	
	private int priorityAttention;
	
	EnumEmployeePosition(int priority){
		this.priorityAttention = priority;
	}

	public int getPriorityAttention() {
		return priorityAttention;
	}
	
	
}
