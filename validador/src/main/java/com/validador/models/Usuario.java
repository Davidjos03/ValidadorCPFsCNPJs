package com.validador.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Usuario extends ResourceSupport{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cdUsuario;
	
	@NotBlank
	private String nomeUsuario;
	
	@NotBlank
	private String senha;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name="usuario_transacao")
	private List<Transacao> transacao = new ArrayList<Transacao>();
	
	public List<Transacao> getTransacao() {
		return transacao;
	}

	public void setTransacao(List<Transacao> transacao) {
		this.transacao = transacao;
	}

	private double valorDividaUsuario;

	public int getCdUsuario() {
		return cdUsuario;
	}

	public void setCdUsuario(int cdUsuario) {
		this.cdUsuario = cdUsuario;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public double getValorDividaUsuario() {
		return valorDividaUsuario;
	}

	public void setValorDividaUsuario(double valorDividaUsuario) {
		this.valorDividaUsuario = valorDividaUsuario;
	}
	
	
	
}
