package br.edu.ifnmg.clinica.bean;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
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

import java.util.ArrayList;
import java.util.List;

import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

import br.edu.ifnmg.clinica.model.Especialidade;

@ManagedBean
public class EspecialidadeBean {
	@PersistenceContext(unitName = "final")
	private EntityManager em;

	@Inject
	private Especialidade esp;

	@Resource
	UserTransaction ut;

	private List<Especialidade> especialidades;

	@PostConstruct
	public void init() {
		this.especialidades = listaEsp();
	}

	public List<Especialidade> getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidades(List<Especialidade> especialidades) {
		this.especialidades = especialidades;
	}

	public Especialidade getEsp() {
		return esp;
	}

	public void setEsp(Especialidade esp) {
		this.esp = esp;
	}

	public String salvarEspecialidade() {
		try {
			ut.begin();
			em.merge(esp);
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
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucess!", "Especialidade cadastrada com sucesso"));
		return "esp.xhtml?faces-redirect=true";
	}
	
	public String editarEspecialidade(Especialidade espec) {
		try {
			ut.begin();
			em.merge(espec);
			ut.commit();
		} catch (SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
				| HeuristicRollbackException | SystemException | NotSupportedException e) {
			RequestContext.getCurrentInstance().update("growl");
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Impossível salvar no BD"));
		}
		RequestContext.getCurrentInstance().update("growl");
		return "esp.xhtml?faces-redirect=true";
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

	public List<Especialidade> getListaEspecialidades() {
		List<Especialidade> espescialidades = this.listaEsp();
		return espescialidades;
	}

	public List<SelectItem> getListEspecialidades() {
		List<SelectItem> list = new ArrayList<SelectItem>();
		List<Especialidade> espescialidades = this.listaEsp();
		for (Especialidade especialidade : espescialidades) {
			list.add(new SelectItem(especialidade, especialidade.getNome()));
		}
		return list;
	}
	
	public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

	public void removeEspecialidade(Long id) {
		TypedQuery<Especialidade> tq = em.createNamedQuery("findEspecialidade", Especialidade.class);
		tq.setParameter("id", id);
		Especialidade espec = tq.getSingleResult();
		if (espec != null) {
			try {
				ut.begin();
				Especialidade removida = em.merge(espec);
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

	public void onRowEdit(RowEditEvent event) {	
		Especialidade espec = ((Especialidade) event.getObject());
		this.editarEspecialidade(espec);
		FacesMessage msg = new FacesMessage("Especialidade editada", ((Especialidade) event.getObject()).getNome());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edição cancelada", ((Especialidade) event.getObject()).getNome());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

}
