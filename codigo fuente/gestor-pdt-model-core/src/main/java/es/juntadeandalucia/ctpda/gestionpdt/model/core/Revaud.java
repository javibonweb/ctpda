package es.juntadeandalucia.ctpda.gestionpdt.model.core;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@RevisionEntity(RevaudListener.class)
@Table(name ="GE_REVAUD")
public class Revaud {
	
	/**
	 * serial id
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "REV_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_REVAUD")
	@SequenceGenerator(name = "S_REVAUD", sequenceName = "S_REVAUD", allocationSize = 1)
	@RevisionNumber
	private Long id;

		
	/** The fecha modificacion. */
	@RevisionTimestamp
	@Column (name="F_MODIFICACION")
	private Date fechaModificacion;
	
	/** The usu modificacion. */
	@Column (name="USU_MODIFICACION")
	@Size(max=25)
	@Getter
	@Setter
	private String usuModificacion;
}
