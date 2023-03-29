package es.juntadeandalucia.ctpda.gestionpdt.model;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import es.juntadeandalucia.ctpda.gestionpdt.model.core.EntidadBasica;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Entity
@Table(name = "GE_LOG_CRON")
public class LogCron implements  EntidadBasica,Serializable{
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "LOG_CRON_ID")
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_LOG_CRON")
	@SequenceGenerator(name = "S_LOG_CRON", sequenceName = "S_LOG_CRON", allocationSize = 1)
	@Getter
	@Setter
	private Long id;	
	
	@Getter
	@Setter
	@Column(name = "F_FECHA")
	private Date fecha;
	
	@Setter
	@Getter
	@Column(name = "D_PROCESO")
	private String proceso;
	
	
	@Getter
	@Setter
	@Column(name = "D_LOG")
	private String log;

}
