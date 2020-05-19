package relacional;
import java.sql.Time;

public class RondaPlaneada {
	
	private Time time;
	private String diaSemana;
	
	/**
	 * @param time
	 * @param diaSemana
	 */
	public RondaPlaneada(Time time, String diaSemana) {
		super();
		this.time = time;
		this.diaSemana = diaSemana;
	}
	/**
	 * @return the time
	 */
	public Time getTime() {
		return time;
	}
	/**
	 * @return the diaSemana
	 */
	public String getDiaSemana() {
		return diaSemana;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(Time time) {
		this.time = time;
	}
	/**
	 * @param diaSemana the diaSemana to set
	 */
	public void setDiaSemana(String diaSemana) {
		this.diaSemana = diaSemana;
	}

}
