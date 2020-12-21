package com.validador.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.validador.models.Requisicao;

public interface RequisicaoRepository extends JpaRepository<Requisicao, String>{

	Requisicao findByCodigo(long codigo);
}
