package es.juntadeandalucia.ctpda.gestionpdt.web.expedientes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import es.juntadeandalucia.ctpda.gestionpdt.model.Expedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.PersonasExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.PlazosExpdte;
import es.juntadeandalucia.ctpda.gestionpdt.model.SujetosObligados;
import es.juntadeandalucia.ctpda.gestionpdt.model.SujetosObligadosExpedientes;
import es.juntadeandalucia.ctpda.gestionpdt.model.TramiteExpediente;
import es.juntadeandalucia.ctpda.gestionpdt.model.ValoresDominio;
import es.juntadeandalucia.ctpda.gestionpdt.service.PlazosExpdteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.SituacionesExpedientesService;
import es.juntadeandalucia.ctpda.gestionpdt.service.SujetosObligadosService;
import es.juntadeandalucia.ctpda.gestionpdt.service.TramiteExpedienteService;
import es.juntadeandalucia.ctpda.gestionpdt.service.ValoresDominioService;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;
import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.ValidacionException;
import es.juntadeandalucia.ctpda.gestionpdt.util.FechaUtils;
import es.juntadeandalucia.ctpda.gestionpdt.web.core.Constantes;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.BaseBean;
import es.juntadeandalucia.ctpda.gestionpdt.web.util.UtilsComun;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@ViewScoped
public class ComunExpedientesBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	
	@Autowired
	private PlazosExpdteService plazosExpdteService;

	@Autowired
	private ValoresDominioService valoresDominioService;
	
	@Autowired
	private SujetosObligadosService sujetosObligadosService;	
	
	@Autowired
	private UtilsComun utilsComun;

	
	@Getter	@Setter
	private String persona;

	@Getter	@Setter
	private String representante;

	@Getter	@Setter
	private String sujetoObligado;

	@Getter	@Setter
	private String panelActualizado;

	@Getter	@Setter
	private String panelSituacion;


	@Getter	@Setter
	private String panelSituacionAdicional;
	
	@Getter	@Setter
	private String panelDescSeguimiento1;

	@Getter	@Setter
	private String panelInfSeguimiento1;

	@Getter	@Setter
	private String panelDescSeguimiento2;

	@Getter	@Setter
	private String panelInfSeguimiento2;

	@Getter	@Setter
	private String panelDescSeguimiento3;

	@Getter	@Setter
	private String panelInfSeguimiento3;
	
	
	@Getter	@Setter
	private Boolean tieneDpd;
	
	@Getter	@Setter
	private Boolean tramitacionAnonima;
	
	@Getter	@Setter
	private Boolean abstencionRecusacion;
	
	//----------

	@Getter @Setter
	private StringBuilder panelPlazos;
	
	@Getter @Setter
	private List<PlazosExpdte> plazosExpdtePanelPlazos;

	@Getter @Setter
	private List<SujetosObligados> listaAnidamientosSujetoObligado;

	
	
	
	@Override
	@PostConstruct
	public void init() {
		super.init();

	}
	
	public void actualizarCabecera (Expedientes expedienteGuardado, PersonasExpedientes personaExpedienteGuardado, SujetosObligadosExpedientes sujetosObligadosExpedientesGuardado) {
		List<PlazosExpdte> plazosExpdte = 
				plazosExpdteService.plazosExpdteActivosNoCumplidosByExpediente(expedienteGuardado.getId());
		this.actualizarCabecera(expedienteGuardado, personaExpedienteGuardado, sujetosObligadosExpedientesGuardado, plazosExpdte);
	}

	public void actualizarCabecera (Expedientes expedienteGuardado, PersonasExpedientes personaExpedienteGuardado, SujetosObligadosExpedientes sujetosObligadosExpedientesGuardado, List<PlazosExpdte> listaPlazosExpdteGuardado) {
		
		if(expedienteGuardado != null) {
			ValoresDominio valorSituacion= valoresDominioService.obtener(expedienteGuardado.getValorSituacionExpediente().getId());
			panelSituacion = valorSituacion.getDescripcion();
			panelSituacionAdicional = expedienteGuardado.getSituacionAdicional();
			panelInfSeguimiento1 = expedienteGuardado.getInfSeguimiento1();
			panelDescSeguimiento1 = expedienteGuardado.getDescSeguimiento1();			
			panelInfSeguimiento2 = expedienteGuardado.getInfSeguimiento2();
			panelDescSeguimiento2 = expedienteGuardado.getDescSeguimiento2();			
			panelInfSeguimiento3 = expedienteGuardado.getInfSeguimiento3();
			panelDescSeguimiento3 = expedienteGuardado.getDescSeguimiento3();			
			
			
			
			if(expedienteGuardado.getFechaUltimaPersistencia() != null) {
				panelActualizado = FechaUtils.dateToStringFechaYHora(expedienteGuardado.getFechaUltimaPersistencia()) + " ("+ expedienteGuardado.getUsuUltimaPersistencia() + ")";
			}else {
				panelActualizado = "";
			}
			
			this.tramitacionAnonima = expedienteGuardado.getTramitacionAnonima();
			this.abstencionRecusacion = expedienteGuardado.getAbstencionRecusacion();
		}
		
		if (personaExpedienteGuardado != null) {
			if(Boolean.TRUE.equals(personaExpedienteGuardado.getInteresado()))
			{
				persona = personaExpedienteGuardado.getPersonas().getNombreAp() + " (INTERESADO)";	
			}else{
				persona = personaExpedienteGuardado.getPersonas().getNombreAp();
			}
						
			if(personaExpedienteGuardado.getPersonasRepre() != null) {
				representante = personaExpedienteGuardado.getPersonasRepre().getNombreAp();
			}else {
				representante = "";
			}	
		}
		
		if (sujetosObligadosExpedientesGuardado != null) {
			sujetoObligado = sujetosObligadosExpedientesGuardado.getSujetosObligados().getDescripcion();
			listaAnidamientosSujetoObligado = obtenerSujetosObligadosAscendentes(sujetosObligadosExpedientesGuardado.getSujetosObligados());
		}
		

		if(sujetosObligadosExpedientesGuardado != null)
		{
			tieneDpd = sujetosObligadosExpedientesGuardado.getDpd();
		}
		
				
		plazosExpdtePanelPlazos(listaPlazosExpdteGuardado,expedienteGuardado);
	}
	
	private void plazosExpdtePanelPlazos(List<PlazosExpdte> listaPlazosExpdteGuardado,Expedientes expedienteGuardado) {
		if(listaPlazosExpdteGuardado != null && !listaPlazosExpdteGuardado.isEmpty())
		{
			plazosExpdtePanelPlazos = listaPlazosExpdteGuardado; 
		}else {
			plazosExpdtePanelPlazos = plazosExpdteService.plazosExpdteActivosNoCumplidosByExpediente(expedienteGuardado.getId());	
		}
		
		if(plazosExpdtePanelPlazos != null && !plazosExpdtePanelPlazos.isEmpty()) {
			for(PlazosExpdte plazosExpdte : plazosExpdtePanelPlazos) {
				panelPlazos.append(plazosExpdte.getValorTipoPlazo().getAbreviatura());
				panelPlazos.append(" - ");
				panelPlazos.append(FechaUtils.dateToStringFecha(plazosExpdte.getFechaLimite()));
				panelPlazos.append(";");
				panelPlazos.append("\n");
			} 
		}
	}
	

	
	private List<SujetosObligados> obtenerSujetosObligadosAscendentes(SujetosObligados sujetosObligadosSeleccionado) {
		List<SujetosObligados> listaSujObliAscendentes = new ArrayList<>();
		SujetosObligados sujObliPadre = null;
		if(sujetosObligadosSeleccionado.getSujetosObligadosPadre() != null && sujetosObligadosSeleccionado.getSujetosObligadosPadre().getId() != null) {
			sujObliPadre = sujetosObligadosService.obtener(sujetosObligadosSeleccionado.getSujetosObligadosPadre().getId());
		}

		while (sujObliPadre != null && sujObliPadre.getActiva()) {
			listaSujObliAscendentes.add(sujObliPadre);
			if(sujObliPadre.getSujetosObligadosPadre() != null && sujObliPadre.getSujetosObligadosPadre().getId() != null) {
				sujObliPadre = sujetosObligadosService.obtener(sujObliPadre.getSujetosObligadosPadre().getId());
			}else{
				sujObliPadre = null;
			}
		}

		return listaSujObliAscendentes;
	}


	public void limpiarCamposPrincipales() {
		persona = null;
		representante = null;
		sujetoObligado = null;
		listaAnidamientosSujetoObligado = null;
	}

	/** Método que limpia los campos del panel. */
	public void limpiarPanel() {
		panelActualizado = null;
		panelSituacion = null;
		panelSituacionAdicional = null;

		panelDescSeguimiento1 = null;
		panelInfSeguimiento1 = null;
		panelDescSeguimiento2 = null;
		panelInfSeguimiento2 = null;
		panelDescSeguimiento3 = null;
		panelInfSeguimiento3 = null;

		panelPlazos = new StringBuilder();
		panelPlazos.append("");
	}
	
	/** Método que limpia los campos plazos del panel. */
	public void limpiarPanelPlazos() {
		panelPlazos = new StringBuilder();
		panelPlazos.append("");
	}

	//**********************************************************************************
	//Validación de cambio a situación final factible.
	//Debería estar en un service
	
	@Autowired
	private TramiteExpedienteService tramiteExpedienteService;
	@Autowired
	private SituacionesExpedientesService situacionesExpedientesService;
	
	public boolean puedeCambiarASituacion(Expedientes exp, TramiteExpediente tram, ValoresDominio situ) throws ValidacionException {
		boolean puedeCambiar = true;
		
		ValoresDominio valorTipoExp = exp.getValorTipoExpediente();
		boolean esCambioASituFinal = situacionesExpedientesService.esSituacionFinal(valorTipoExp.getId(), situ.getId());
		Long idTram = tram == null? 0L : tram.getId();
		puedeCambiar = !esCambioASituFinal || !hayTramitesAbiertos(exp.getId(), idTram);

		return puedeCambiar;
	}

	private boolean hayTramitesAbiertos(Long idExp, Long idTra) {
		return this.tramiteExpedienteService.existeTramiteActivoNoFinalizadoDistinto(idExp, idTra);
	}
	
	public void cerrarPlazosAbiertosExpediente(Long idExp) throws BaseException {
		List<ValoresDominio> tiposPlazo = valoresDominioService.findValoresDominioByCodigoDominio(Constantes.COD_TIPO_PLAZO);
		
		for(ValoresDominio tipoPlazo : tiposPlazo) {
			Long idPlazo = tipoPlazo.getId();
			PlazosExpdte plazosExpdte = plazosExpdteService.findPlazosExpdteByExpTipPla(idExp,idPlazo);

			if(plazosExpdte != null) {
				String observaciones = "Plazo cerrado automáticamente al finalizar el expediente. "
						+ "Usuario: " + SecurityContextHolder.getContext().getAuthentication().getName() 
						+ " y fecha: " + FechaUtils.stringFechaYHora();
				
				utilsComun.finalizarPlazoExpdte(plazosExpdte, observaciones);
			}
		}	
	}
	
}
