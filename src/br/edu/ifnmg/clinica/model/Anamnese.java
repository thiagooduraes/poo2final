package br.edu.ifnmg.clinica.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Anamnese {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, length = 50)
	private String queixa_principal;
	@Column(nullable = false, length = 50)
	private String inicio;
	@Column(nullable = false, length = 50)
	private String sintomas;
	@Column(nullable = false, length = 50)
	private String medicamentos_utilizados;
	@Column(nullable = false, length = 50)
	private String portador_doenca_cronica;
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "consulta", nullable = false)
	private Consulta consulta;

	public String getQueixa_principal() {
		return queixa_principal;
	}

	public void setQueixa_principal(String queixa_principal) {
		this.queixa_principal = queixa_principal;
	}

	public String getInicio() {
		return inicio;
	}

	public void setInicio(String inicio) {
		this.inicio = inicio;
	}

	public String getSintomas() {
		return sintomas;
	}

	public void setSintomas(String sintomas) {
		this.sintomas = sintomas;
	}

	public String getMedicamentos_utilizados() {
		return medicamentos_utilizados;
	}

	public void setMedicamentos_utilizados(String medicamentos_utilizados) {
		this.medicamentos_utilizados = medicamentos_utilizados;
	}

	public String getPortador_doenca_cronica() {
		return portador_doenca_cronica;
	}

	public void setPortador_doenca_cronica(String portador_doenca_cronica) {
		this.portador_doenca_cronica = portador_doenca_cronica;
	}

	public Consulta getConsulta() {
		return consulta;
	}

	public void setConsulta(Consulta consulta) {
		this.consulta = consulta;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
