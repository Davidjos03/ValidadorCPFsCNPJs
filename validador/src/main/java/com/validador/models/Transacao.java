package com.validador.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Transacao extends ResourceSupport{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long codigo;
	
	
	private Date data;
	
	@NotNull
	private String CPFouCNPJ;
	
	private boolean valido;
	
	public long getCodigo() {
		return codigo;
	}
	
	@ManyToOne
	@JoinColumn(name = "usuario_cd_usuario")
	private Usuario usuario;
	

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getCPFouCNPJ() {
		return CPFouCNPJ;
	}

	public void setCPFouCNPJ(String cPFouCNPJ) {
		CPFouCNPJ = cPFouCNPJ;
	}

	public boolean isValido() {
		return valido;
	}

	public void setValido(boolean valido) {
		this.valido = valido;
	}

	
}
