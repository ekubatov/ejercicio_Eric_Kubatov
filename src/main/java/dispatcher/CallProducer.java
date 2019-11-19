package dispatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import model.InboundCall;

/**
 * 
 * 
 * @author Eric Kubatov
 * 
 */
public class CallProducer implements Runnable {
	private static final int INBOUNDCALL_MINIMUM_COUNT = 10;
	private static final int INBOUNDCALL_MAXIMUM_COUNT = 20;
	private Boolean run;
	

	@Override
	public void run() {
		while (this.getRun()) {
			
			if(Dispatcher.getListInboundCalls().isEmpty()){
				int inboudCallCant = ThreadLocalRandom.current().nextInt(INBOUNDCALL_MINIMUM_COUNT, INBOUNDCALL_MAXIMUM_COUNT);
				List<InboundCall> calls = new ArrayList <>();
				for(int i=0; i<inboudCallCant; i++) {
					calls.add(InboundCall.createCall());
				}
				
				calls.forEach(call -> {	Dispatcher.addCalls(call);});
			}
		}
		
	}

	public synchronized Boolean getRun() {
		return run;
	}
	

	public synchronized void stop() {
		this.run = false;
	}

	
	public CallProducer() {
		this.run = true;
	}

}

