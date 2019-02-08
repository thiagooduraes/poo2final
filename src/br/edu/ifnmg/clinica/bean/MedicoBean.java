package br.edu.ifnmg.clinica.bean;

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
import org.primefaces.event.RowEditEvent;

import java.util.List;

import br.edu.ifnmg.clinica.model.Especialidade;
import br.edu.ifnmg.clinica.model.Medico;


@ManagedBean
public class MedicoBean {
	@PersistenceContext(unitName = "final")
	private EntityManager em;

	@Inject
	private Medico med;

	@Inject
	private Especialidade especialidade;

	private List<Especialidade> especialidades;

	private List<Medico> medicos;

	@Resource
	UserTransaction ut;

	@PostConstruct
	public void init() {
		this.med = new Medico();
		this.especialidades = listaEsp();
		this.medicos = listamed();
	}

	public List<Especialidade> getEspecialidades() {
		return especialidades;
	}

	public Especialidade getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(Especialidade especialidade) {
		this.especialidade = especialidade;
	}

	public List<Especialidade> listaEsp() {
		TypedQuery<Especialidade> tq = em.createNamedQuery("listarEspecialidades", Especialidade.class);
		return tq.getResultList();
//		try {
//			Query query = em.createQuery("select e from Especialidade e");
//			@SuppressWarnings("unchecked")
//			List<Especialidade> espec = query.getResultList();
//			return espec;
//		} catch (Exception e) {
//			return null;
//		}
	}

	public List<Medico> getmedicos() {
		return medicos;
	}

	public void setmedicos(List<Medico> medicos) {
		this.medicos = medicos;
	}

	public Medico getmed() {
		return med;
	}

	public void setmed(Medico med) {
		this.med = med;
	}

	public String salvarMedico() {
		try {
			ut.begin();
			em.merge(especialidade);
			med.setEspecialidade(especialidade);
			em.persist(med);
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
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucess!", "Medico cadastrado com sucesso"));
		return "med.xhtml?faces-redirect=true";
	}
	
	public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	public String editarMedico(Medico medic) {
		try {
			ut.begin();
			em.merge(especialidade);
			medic.setEspecialidade(especialidade);
			em.persist(medic);
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
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucess!", "Medico cadastrado com sucesso"));
		return "med.xhtml?faces-redirect=true";
	}

	public List<Medico> listamed() {
		TypedQuery<Medico> tq = em.createNamedQuery("listarMedicos", Medico.class);
				return tq.getResultList();
//		try {
//			Query query = em.createQuery("select e from Medico e");
//			@SuppressWarnings("unchecked")
//			List<Medico> medi = query.getResultList();
//			return medi;
//		} catch (Exception e) {
//			return null;
//		}
	}
	
	public void onRowEdit(RowEditEvent event) {	
		Medico medico = ((Medico) event.getObject());
		this.editarMedico(medico);
		FacesMessage msg = new FacesMessage("Médico editado", ((Especialidade) event.getObject()).getNome());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edição cancelada", ((Especialidade) event.getObject()).getNome());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

}
