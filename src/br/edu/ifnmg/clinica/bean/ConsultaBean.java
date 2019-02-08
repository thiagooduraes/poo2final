package br.edu.ifnmg.clinica.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import java.util.ArrayList;
import java.util.List;

import org.primefaces.context.RequestContext;

import br.edu.ifnmg.clinica.model.Consulta;
import br.edu.ifnmg.clinica.model.Medico;
import br.edu.ifnmg.clinica.model.Paciente;

@ManagedBean(name = "consultaBean")
public class ConsultaBean {
	@PersistenceContext(unitName = "final")
	private EntityManager em;

	@Inject
	private Consulta consulta;

	@Inject
	private Paciente paciente;

	@Inject
	private Medico medico;

	private List<Consulta> consultas;

	private List<Medico> medicos;

	private List<Paciente> pacientes;
	
	@Resource
	UserTransaction ut;
	
	@PostConstruct
	public void init() {
		this.consultas = listaCons();
		this.pacientes = listaPac();
		this.medicos = listaMed();
	}

	public List<Medico> getMedicos() {
		return medicos;
	}

	public void setMedicos(List<Medico> medicos) {
		this.medicos = medicos;
	}

	public List<Paciente> getPacientes() {
		return pacientes;
	}

	public void setPacientes(List<Paciente> pacientes) {
		this.pacientes = pacientes;
	}
	
	private List<Consulta> listaCons() {
		try {
			Query query = em.createQuery("select e from Consulta e");
			@SuppressWarnings("unchecked")
			List<Consulta> cons = query.getResultList();
			return cons;
		} catch (Exception e) {
			return null;
		}
	}

	private List<Paciente> listaPac() {
		try {
			Query query = em.createQuery("select e from Paciente e");
			@SuppressWarnings("unchecked")
			List<Paciente> pac = query.getResultList();
			return pac;
		} catch (Exception e) {
			return null;
		}
	}
	
	private List<Medico> listaMed() {
		try {
			Query query = em.createQuery("select e from Medico e");
			@SuppressWarnings("unchecked")
			List<Medico> med = query.getResultList();
			return med;
		} catch (Exception e) {
			return null;
		}
	}
	
	public Consulta getConsulta() {
		return consulta;
	}

	public void setConsulta(Consulta consulta) {
		this.consulta = consulta;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Medico getMedico() {
		return medico;
	}

	public void setMedico(Medico medico) {
		this.medico = medico;
	}

	public List<Consulta> getConsultas() {
		return consultas;
	}

	public void setConsultas(List<Consulta> consultas) {
		this.consultas = consultas;
	}
	
	public String salvarconsulta() {
		consulta.setPaciente(paciente);
		consulta.setMedico(medico);
		try {
			ut.begin();
			em.merge(consulta);
			ut.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException | NotSupportedException e) {
			RequestContext.getCurrentInstance().update("growl");
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Impossível salvar no BD"));
		}
		RequestContext.getCurrentInstance().update("growl");
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucess!", "Medico cadastrada com sucesso"));
		return "pac.xhtml?faces-redirect=true";
	}

}
