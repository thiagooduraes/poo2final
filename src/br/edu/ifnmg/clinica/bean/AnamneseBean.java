package br.edu.ifnmg.clinica.bean;

import java.util.List;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.edu.ifnmg.clinica.model.Anamnese;;

@Stateful
public class AnamneseBean {
	@PersistenceContext(unitName = "final")
	private EntityManager entityManager;

	public void addAnamnese(Anamnese anamnese) throws Exception {
		entityManager.persist(anamnese);
	}

	public void removeAnamnese(Anamnese anamnese) throws Exception {
		anamnese = entityManager.find(Anamnese.class,anamnese);
		entityManager.remove(anamnese);
	}

	public Anamnese getAnamnese(Long id) throws Exception {
		TypedQuery<Anamnese> tq = entityManager.createQuery("select a from Anamnese a where id = ?", Anamnese.class);
		tq.setParameter(1, id);
		return tq.getSingleResult();
	}

	public List<Anamnese> getAnamnese() throws Exception {
		TypedQuery<Anamnese> tq = entityManager.createQuery("select a from Anamnese a", Anamnese.class);
		return tq.getResultList();
	}

}
