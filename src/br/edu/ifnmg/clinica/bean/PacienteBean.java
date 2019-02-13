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

import br.edu.ifnmg.clinica.model.Convenio;
import br.edu.ifnmg.clinica.model.Paciente;

@ManagedBean
public class PacienteBean {
	@PersistenceContext(unitName = "final")
	private EntityManager em;

	@Inject
	private Paciente pac;

	@Inject
	private Convenio convenio;

	private Long conv_id;

	private String conv_id_str;

	private List<Convenio> convenios;

	private List<Paciente> pacientes;

	@Resource
	UserTransaction ut;

	@PostConstruct
	public void init() {
		this.convenios = listaConv();
		this.pacientes = listapac();
	}

	private List<Convenio> listaConv() {
		TypedQuery<Convenio> tq = em.createNamedQuery("listarConvenios", Convenio.class);
		return tq.getResultList();
		// try {
		// Query query = em.createQuery("select e from Convenio e");
		// @SuppressWarnings("unchecked")
		// List<Convenio> conv = query.getResultList();
		// return conv;
		// } catch (Exception e) {
		// return null;
		// }
	}

	public List<Paciente> getPacientes() {
		return pacientes;
	}

	public void setPacientes(List<Paciente> pacientes) {
		this.pacientes = pacientes;
	}

	public Paciente getPac() {
		return pac;
	}

	public void setPac(Paciente pac) {
		this.pac = pac;
	}

	public Convenio getConvenio() {
		return convenio;
	}

	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}

	public List<Convenio> getConvenios() {
		return convenios;
	}

	public void setConvenios(List<Convenio> convenios) {
		this.convenios = convenios;
	}

	public Long getConv_id() {
		return conv_id;
	}

	public void setConv_id(String str_id) {
		this.conv_id = Long.parseLong(str_id);
	}

	public String getConv_id_str() {
		return conv_id_str;
	}

	public void setConv_id_str(String conv_id_str) {
		this.conv_id_str = conv_id_str;
	}

	public List<Paciente> listapac() {
		TypedQuery<Paciente> tq = em.createNamedQuery("listarPacientes", Paciente.class);
		return tq.getResultList();
		// try {
		// Query query = em.createQuery("select e from Paciente e");
		// @SuppressWarnings("unchecked")
		// List<Paciente> pac = query.getResultList();
		// return pac;
		// } catch (Exception e) {
		// return null;
		// }
	}

	public String salvarPaciente() {
		this.setConv_id(conv_id_str);
		this.convenio.setId(this.conv_id);
		TypedQuery<Convenio> tq = em.createNamedQuery("findConvenio", Convenio.class);
		tq.setParameter("id", this.conv_id);
		Convenio conv = tq.getSingleResult();
		try {
			ut.begin();
			em.merge(conv);
			pac.setConvenio(conv);
			em.merge(pac);
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
	
	public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	public void removePaciente(Long id) {
		TypedQuery<Paciente> tq = em.createNamedQuery("findPaciente", Paciente.class);
		tq.setParameter("id", id);
		Paciente paci = tq.getSingleResult();
		if (paci != null) {
			try {
				ut.begin();
				Paciente removida = em.merge(paci);
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
