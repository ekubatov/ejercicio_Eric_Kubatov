package model;

import java.util.concurrent.ThreadLocalRandom;

import enumerator.EnumInboundCall;

/**
 * InboundCall entity of domain model
 * 
 * @author Eric Kubatov
 * 
 */
public class InboundCall {

	private Integer id;
	private Integer durationInSeconds;
	public static int cont = 1;

	public InboundCall(Integer durationInSeconds) {
		this.durationInSeconds = durationInSeconds;
		this.id = cont;
		InboundCall.cont++;

	}

	public static InboundCall createCall() {
		return new InboundCall(ThreadLocalRandom.current().nextInt(EnumInboundCall.MINIMA_DURACION.getDuracion(),
				EnumInboundCall.MAXIMA_DURACION.getDuracion() + 1));
	}

	public synchronized Integer getId() {
		return id;
	}

	public synchronized Integer getDurationInSeconds() {
		return durationInSeconds;
	}

	
}
