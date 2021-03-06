package com.validador;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.validador.models.Requisicao;
import com.validador.models.Usuario;
import com.validador.resources.RequisicaoResource;
import com.validador.resources.UsuarioResource;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ValidadorApplicationTests {

	
	@Autowired
	UsuarioResource userResource;
	
	@Autowired
	RequisicaoResource requisicaoResource;
	
	int idUser;
	
	@Test
	public void testeSalvarUsuario() {
		Usuario user = new Usuario();
		user.setNomeUsuario("Teste Unitario");
		user.setSenha("12345678");
		
		Usuario usuario = userResource.cadastraUsuario(user);
		
		assertEquals(usuario.getNomeUsuario(), user.getNomeUsuario());
		
		idUser = usuario.getCdUsuario();
		
	}
	
	@Test
	public void testeAterarUsuario() {
		Usuario user = new Usuario();
		user.setNomeUsuario("Teste Unitario");
		user.setSenha("12345678");
		
		Usuario usuario = userResource.cadastraUsuario(user);
		
		
		idUser = usuario.getCdUsuario();
		
		
		user.setNomeUsuario("Teste Unitario Alterado");
		user.setSenha("12345678");
		user.setCdUsuario(idUser);
		
		usuario = userResource.alterarUser(user);
		
		assertEquals(usuario.getNomeUsuario(), user.getNomeUsuario());
		
	}
	
	@Test
	public void testeExcluirUsuario() {
		Usuario user = new Usuario();
		user.setNomeUsuario("Teste Unitario");
		user.setSenha("12345678");
		
		Usuario usuario = userResource.cadastraUsuario(user);
		

		idUser = usuario.getCdUsuario();
		
		user.setNomeUsuario("Teste Unitario");
		user.setSenha("12345678");
		user.setCdUsuario(idUser);
		
		usuario = userResource.deletaUsuario(user);
		
		assertEquals(usuario.getNomeUsuario(), user.getNomeUsuario());
		
	}
	
	@Test
	public void testeRalizaTransação() {
		Usuario user = new Usuario();
		user.setNomeUsuario("Teste Unitario");
		user.setSenha("12345678");
		
		Usuario usuario = userResource.cadastraUsuario(user);
		idUser = usuario.getCdUsuario();

		Requisicao tr = new Requisicao();
		
		tr.setCPFouCNPJ("056.790.581-03");
		tr.setUsuario(usuario);
		
		String requisicao = requisicaoResource.cadastraRequisicao(tr);
		
		assertEquals("O CPF 056.790.581-03 é válido. Valor da dívida do usuario:0.1", requisicao);

	}
	
	@Test
	public void testeRalizaeDeletaTransação() {
		Usuario user = new Usuario();
		user.setNomeUsuario("Teste Unitario");
		user.setSenha("12345678");
		
		Usuario usuario = userResource.cadastraUsuario(user);
		idUser = usuario.getCdUsuario();

		Requisicao tr = new Requisicao();
		
		tr.setCPFouCNPJ("056.790.581-03");
		tr.setUsuario(usuario);
		
		
		String rRequisicao = requisicaoResource.cadastraRequisicao(tr);

		
		assertEquals("O CPF 056.790.581-03 é válido. Valor da dívida do usuario:0.1", rRequisicao);

		requisicaoResource.deletaRequisicao(tr);
	}
	
	

}
