package enumerator;
/**
 * Parametrización de las llamadas
 * 
 * @author Eric Kubatov
 * 
 */
public enum EnumInboundCall {

	MAXIMA_DURACION(10,"maximun duration in seconds"), MINIMA_DURACION(5,"minimun duration in seconds");
	
	private Integer duracion;
	private String descripcion;
	
	EnumInboundCall(Integer duracion, String desc) {
		this.duracion = duracion;
		this.descripcion = desc;
	}

	public Integer getDuracion() {
		return duracion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	
}
