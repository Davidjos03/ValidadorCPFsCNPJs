package com.validador.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.validador.models.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, String>{

	Transacao findByCodigo(long codigo);
}
