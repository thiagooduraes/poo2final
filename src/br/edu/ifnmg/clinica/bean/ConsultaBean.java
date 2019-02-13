package br.edu.ifnmg.clinica.bean;

import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.primefaces.context.RequestContext;

import br.edu.ifnmg.clinica.model.Consulta;
import br.edu.ifnmg.clinica.model.Medico;
import br.edu.ifnmg.clinica.model.Paciente;

@ManagedBean
public class ConsultaBean {
	@PersistenceContext(unitName = "final")
	private EntityManager em;

	@Inject
	private Consulta consulta;

	@Inject
	private Paciente paciente;

	@Inject
	private Medico medico;
	
	private Long med_id;
	
	private String med_id_str;
	
	private Long pac_id;
	
	private String pac_id_str;

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
	
	public Long getMed_id() {
		return med_id;
	}

	public void setMed_id(String str) {
		this.med_id = Long.parseLong(str);
	}

	public String getMed_id_str() {
		return med_id_str;
	}

	public void setMed_id_str(String med_id_str) {
		this.med_id_str = med_id_str;
	}

	public Long getPac_id() {
		return pac_id;
	}

	public void setPac_id(String str) {
		this.pac_id = Long.parseLong(str);
	}

	public String getPac_id_str() {
		return pac_id_str;
	}

	public void setPac_id_str(String pac_id_str) {
		this.pac_id_str = pac_id_str;
	}

	private List<Consulta> listaCons() {
		TypedQuery<Consulta> tq = em.createNamedQuery("listarConsultas", Consulta.class);
		return tq.getResultList();
//		try {
//			Query query = em.createQuery("select e from Consulta e");
//			@SuppressWarnings("unchecked")
//			List<Consulta> cons = query.getResultList();
//			return cons;
//		} catch (Exception e) {
//			return null;
//		}
	}

	private List<Paciente> listaPac() {
		TypedQuery<Paciente> tq = em.createNamedQuery("listarPacientes", Paciente.class);
		return tq.getResultList();
//		try {
//			Query query = em.createQuery("select e from Paciente e");
//			@SuppressWarnings("unchecked")
//			List<Paciente> pac = query.getResultList();
//			return pac;
//		} catch (Exception e) {
//			return null;
//		}
	}
	
	private List<Medico> listaMed() {
		TypedQuery<Medico> tq = em.createNamedQuery("listarMedicos", Medico.class);
		return tq.getResultList();
//		try {
//			Query query = em.createQuery("select e from Medico e");
//			@SuppressWarnings("unchecked")
//			List<Medico> med = query.getResultList();
//			return med;
//		} catch (Exception e) {
//			return null;
//		}
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
		this.setMed_id(this.med_id_str);
		this.medico.setId(this.med_id);
		TypedQuery<Medico> tq = em.createNamedQuery("findMedico", Medico.class);
		tq.setParameter("id", this.med_id);
		Medico medic = tq.getSingleResult();
		
		this.setPac_id(this.pac_id_str);
		this.paciente.setId(this.pac_id);
		TypedQuery<Paciente> tq2 = em.createNamedQuery("findPaciente", Paciente.class);
		tq2.setParameter("id", this.med_id);
		Paciente pac = tq2.getSingleResult();
		try {
			ut.begin();
			em.merge(medic);
			consulta.setMedico(medic);
			em.merge(pac);
			consulta.setPaciente(pac);
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
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucess!", "Consulta cadastrada com sucesso"));
		return "cons.xhtml?faces-redirect=true";
	}

	public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

	public void removeConsulta(Long id) {
		TypedQuery<Consulta> tq = em.createNamedQuery("findConsulta", Consulta.class);
		tq.setParameter("id", id);
		Consulta cons = tq.getSingleResult();
		if (cons != null) {
			try {
				ut.begin();
				Consulta removida = em.merge(cons);
				em.remove(removida);
				ut.commit();
				addMessage("Excluído", "Dado excluído com sucesso");
			} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
					| HeuristicRollbackException | SystemException | NotSupportedException e) {
				RequestContext.getCurrentInstance().update("growl");
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Impossível salvar no BD"));
			}
		}
	}
}
