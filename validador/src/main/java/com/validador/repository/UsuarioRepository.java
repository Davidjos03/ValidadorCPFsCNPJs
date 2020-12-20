package com.validador.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.validador.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String>{
	
	Usuario findByCdUsuario(long cdUsuario);
}
