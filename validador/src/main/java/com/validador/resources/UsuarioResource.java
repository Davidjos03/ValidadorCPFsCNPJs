package com.validador.resources;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.validador.models.Requisicao;
import com.validador.models.Usuario;
import com.validador.repository.RequisicaoRepository;
import com.validador.repository.UsuarioRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "API REST Validador")
@RestController
@RequestMapping("/usuario")
public class UsuarioResource {

	@Autowired
	private UsuarioRepository ur;

	@Autowired
	private RequisicaoRepository et;

	@ApiOperation(value = "Retorna uma lista de Usuarios")
	@GetMapping(produces = "application/json")
	public @ResponseBody ArrayList<Usuario> listaUsuarios() {
		Iterable<Usuario> listaUsuarios = ur.findAll();
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
		for (Usuario usuario : listaUsuarios) {
			long cdUsuario = usuario.getCdUsuario();
			usuario.add(linkTo(methodOn(UsuarioResource.class).usuario(cdUsuario)).withSelfRel());
			usuarios.add(usuario);
		}
		return usuarios;
	}

	@ApiOperation(value = "Retorna um usuario específico")
	@GetMapping(value = "/{cdUsuario}", produces = "application/json")
	public @ResponseBody Usuario usuario(@PathVariable(value = "cdUsuario") long cdUsuario) {
		List<Usuario> usuarios = ur.findAll();
		
		Usuario usuario = null;
		for(Usuario u: usuarios) {
			if(u.getCdUsuario() == cdUsuario) {
				usuario = u;
				break;
			}
		}
		
		
		usuario.add(linkTo(methodOn(UsuarioResource.class).listaUsuarios()).withRel("Lista de Usuarios"));
		return usuario;
	}

	@ApiOperation(value = "Salva um usuario")
	@PostMapping()
	public Usuario cadastraUsuario(@RequestBody @Valid Usuario usuario) {

		ur.save(usuario);

		int cdUsuario = usuario.getCdUsuario();

		usuario.add(linkTo(methodOn(UsuarioResource.class).usuario(cdUsuario)).withSelfRel());
		return usuario;
	}

	@ApiOperation(value = "Deleta um usuario")
	@DeleteMapping()
	public Usuario deletaUsuario(@RequestBody Usuario user) {
		List<Usuario> listUser = ur.findAll();
		Usuario userDB = null;

		for (Usuario usuario : listUser) {
			if (user.getCdUsuario() == usuario.getCdUsuario()) {
				userDB = usuario;
				try {
					int j = userDB.getRequisicao().size();
					
					for (int i = 0; i <= j-1;i++) {
						
						Requisicao req = userDB.getRequisicao().get(i); 
						
						userDB.getRequisicao().remove(req);
						ur.save(userDB);
						
						req.setUsuario(null);
						et.delete(req);
						
						i--;
						j--;
						
						
					}
				} catch (Exception LazyInitializationException) {
					
				}
			}
		}

		ur.delete(userDB);
		return userDB;
	}

	@ApiOperation(value = "Altera um usuario específico")
	@PutMapping()
	public Usuario alterarUser(@RequestBody @Valid Usuario user) {
		List<Usuario> listUser = ur.findAll();

		Usuario userDB = null;

		for (Usuario usuario : listUser) {
			if (user.getCdUsuario() == usuario.getCdUsuario()) {
				userDB = usuario;
			}
		}

		userDB.setNomeUsuario(user.getNomeUsuario());
		userDB.setSenha(user.getSenha());
		userDB.setValorDividaUsuario(user.getValorDividaUsuario());

		user.add(linkTo(methodOn(UsuarioResource.class).listaUsuarios()).withRel("Lista de Usuarios"));

		ur.save(user);

		return user;
	}

}
