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

@ManagedBean
public class ConvenioBean {
	@PersistenceContext(unitName = "final")
	private EntityManager em;

	@Inject
	private Convenio conv;

	@Resource
	UserTransaction ut;

	private List<Convenio> convenios;

	@PostConstruct
	public void init() {
		this.convenios = listaConv();
	}

	public List<Convenio> getConvenios() {
		return convenios;
	}

	public void setConvenios(List<Convenio> convenios) {
		this.convenios = convenios;
	}

	public Convenio getConv() {
		return conv;
	}

	public void setConv(Convenio conv) {
		this.conv = conv;
	}

	public String salvarConvenio() {
		try {
			ut.begin();
			em.merge(conv);
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
		return "conv.xhtml?faces-redirect=true";
	}

	public List<Convenio> listaConv() {
		TypedQuery<Convenio> tq = em.createNamedQuery("listarConvenios", Convenio.class);
		return tq.getResultList();
//		try {
//			Query query = em.createQuery("select e from Convenio e");
//			@SuppressWarnings("unchecked")
//			List<Convenio> convs = query.getResultList();
//			return convs;
//		} catch (Exception e) {
//			return null;
//		}
	}
}
