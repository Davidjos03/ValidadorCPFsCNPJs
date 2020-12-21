package com.validador.resources;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.validador.models.Requisicao;
import com.validador.models.Usuario;
import com.validador.repository.RequisicaoRepository;
import com.validador.repository.UsuarioRepository;
import com.validadorBO.ValidaCNPJ;
import com.validadorBO.ValidaCPF;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "API REST Validador")
@RestController
@RequestMapping("/validador")
public class RequisicaoResource {

	@Autowired
	private RequisicaoRepository et;

	@Autowired
	private UsuarioRepository ur;

	@ApiOperation(value = "Retorna uma lista de Requisicoes")
	@GetMapping(produces = "application/json")
	public @ResponseBody ArrayList<Requisicao> listaRequisicaoes() {
		Iterable<Requisicao> listaRequisicaoes = et.findAll();
		ArrayList<Requisicao> requisicoes = new ArrayList<Requisicao>();
		for (Requisicao requisicao : listaRequisicaoes) {
			long codigo = requisicao.getCodigo();
			requisicao.add(linkTo(methodOn(RequisicaoResource.class).requisicao(codigo)).withSelfRel());
			requisicoes.add(requisicao);
		}
		return requisicoes;
	}

	@ApiOperation(value = "Retorna uma requisicao específico")
	@GetMapping(value = "/{codigo}", produces = "application/json")
	public @ResponseBody Requisicao requisicao(@PathVariable(value = "codigo") long codigo) {
		Requisicao requisicao = et.findByCodigo(codigo);
		requisicao.add(linkTo(methodOn(RequisicaoResource.class).listaRequisicaoes()).withRel("Lista de Requisicaoes"));
		return requisicao;
	}

	@ApiOperation(value = "Salva uma requisicao")
	@PostMapping()
	public String cadastraRequisicao(@RequestBody @Valid Requisicao requisicao) {

		String tipoBusca = null, valido = null;
		requisicao.setData(new Date(System.currentTimeMillis()));

		Usuario user = requisicao.getUsuario();

		String digitos = "";
		char[] letras = requisicao.getCPFouCNPJ().toCharArray();
		for (char letra : letras) {
			if (Character.isDigit(letra)) {
				digitos += letra;
			}
		}

		if (digitos.length() == 11) {
			requisicao.setValido(ValidaCPF.isCPF(digitos));
			tipoBusca = "O CPF ";
			if (requisicao.isValido()) {
				valido = " é válido.";
			} else {
				valido = " não é válido.";
			}
		} else {
			if (digitos.length() == 14) {
				requisicao.setValido(ValidaCNPJ.isCNPJ(digitos));
				tipoBusca = "O CNPJ ";
				if (requisicao.isValido()) {
					valido = " é válido.";
				} else {
					valido = " não é válido.";
				}
			} else {
				tipoBusca = "O valor ";
				valido = " não é CNPJ ou CPF, informe uma informação válida!";
			}

		}

		user.getRequisicao().add(requisicao);
		user.setValorDividaUsuario(0.1);
		requisicao.setUsuario(user);

		if(requisicao.getCodigo()!=0) {
			requisicao.setCodigo(0);
		}
		et.save(requisicao);

		float valDivida = (float) updateUser(user).getValorDividaUsuario();

		return tipoBusca + requisicao.getCPFouCNPJ() + valido + " Valor da dívida do usuario:" + valDivida;
	}

	@ApiOperation(value = "Deleta uma requisicao")
	@DeleteMapping()
	public Requisicao deletaRequisicao(@RequestBody Requisicao requisicao) {
		if (requisicao.getCodigo() != 0) {

			List<Usuario> listUser = ur.findAll();

			Usuario userDB = null;

			for (Usuario usuario : listUser) {
				if (requisicao.getUsuario().getCdUsuario() == usuario.getCdUsuario()) {
					userDB = usuario;
				}
			}

			for (int i = 0; 1 <= userDB.getRequisicao().size(); i++) {
				if (requisicao.getCodigo() == userDB.getRequisicao().get(i).getCodigo()) {
					userDB.getRequisicao().remove(i);
					break;
				}

			}
			ur.save(userDB);
			requisicao.setUsuario(null);
			et.delete(requisicao);
		}

		return requisicao;
	}

	public Usuario updateUser(Usuario user) {
		List<Usuario> listUser = ur.findAll();

		Usuario userDB = null;

		for (Usuario usuario : listUser) {
			if (user.getCdUsuario() == usuario.getCdUsuario()) {
				userDB = usuario;
			}
		}

		float somaDivida = (float) (user.getValorDividaUsuario() + userDB.getValorDividaUsuario());
		

		user.getRequisicao().addAll(userDB.getRequisicao());
		user.setValorDividaUsuario(somaDivida);

		user.add(linkTo(methodOn(UsuarioResource.class).listaUsuarios()).withRel("Lista de Usuarios"));

		ur.save(user);

		return user;
	}

}
