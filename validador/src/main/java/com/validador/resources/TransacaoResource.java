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

import com.validador.models.Transacao;
import com.validador.models.Usuario;
import com.validador.repository.TransacaoRepository;
import com.validador.repository.UsuarioRepository;
import com.validadorBO.ValidaCNPJ;
import com.validadorBO.ValidaCPF;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "API REST Validador")
@RestController
@RequestMapping("/validador")
public class TransacaoResource {

	@Autowired
	private TransacaoRepository et;

	@Autowired
	private UsuarioRepository ur;

	@ApiOperation(value = "Retorna uma lista de transacoes")
	@GetMapping(produces = "application/json")
	public @ResponseBody ArrayList<Transacao> listaTransacoes() {
		Iterable<Transacao> listaTransacoes = et.findAll();
		ArrayList<Transacao> transacoes = new ArrayList<Transacao>();
		for (Transacao transacao : listaTransacoes) {
			long codigo = transacao.getCodigo();
			transacao.add(linkTo(methodOn(TransacaoResource.class).transacao(codigo)).withSelfRel());
			transacoes.add(transacao);
		}
		return transacoes;
	}

	@ApiOperation(value = "Retorna uma transacao específico")
	@GetMapping(value = "/{codigo}", produces = "application/json")
	public @ResponseBody Transacao transacao(@PathVariable(value = "codigo") long codigo) {
		Transacao transacao = et.findByCodigo(codigo);
		transacao.add(linkTo(methodOn(TransacaoResource.class).listaTransacoes()).withRel("Lista de Transacoes"));
		return transacao;
	}

	@ApiOperation(value = "Salva uma transacao")
	@PostMapping()
	public String cadastraTransacao(@RequestBody @Valid Transacao transacao) {

		String tipoBusca = null, valido = null;
		transacao.setData(new Date(System.currentTimeMillis()));

		Usuario user = transacao.getUsuario();

		String digitos = "";
		char[] letras = transacao.getCPFouCNPJ().toCharArray();
		for (char letra : letras) {
			if (Character.isDigit(letra)) {
				digitos += letra;
			}
		}

		if (digitos.length() == 11) {
			transacao.setValido(ValidaCPF.isCPF(digitos));
			tipoBusca = "O CPF ";
			if (transacao.isValido()) {
				valido = " é válido.";
			} else {
				valido = " não é válido.";
			}
		} else {
			if (digitos.length() == 14) {
				transacao.setValido(ValidaCNPJ.isCNPJ(digitos));
				tipoBusca = "O CNPJ ";
				if (transacao.isValido()) {
					valido = " é válido.";
				} else {
					valido = " não é válido.";
				}
			} else {
				tipoBusca = "O valor ";
				valido = " não é CNPJ ou CPF, informe uma informação válida!";
			}

		}

		user.getTransacao().add(transacao);
		user.setValorDividaUsuario(0.1);
		transacao.setUsuario(user);

		if(transacao.getCodigo()!=0) {
			transacao.setCodigo(0);
		}
		et.save(transacao);

		float valDivida = (float) updateUser(user).getValorDividaUsuario();

		return tipoBusca + transacao.getCPFouCNPJ() + valido + " Valor da dívida do usuario:" + valDivida;
	}

	@ApiOperation(value = "Deleta uma transacao")
	@DeleteMapping()
	public Transacao deletaTransacao(@RequestBody Transacao transacao) {
		if (transacao.getCodigo() != 0) {

			List<Usuario> listUser = ur.findAll();

			Usuario userDB = null;

			for (Usuario usuario : listUser) {
				if (transacao.getUsuario().getCdUsuario() == usuario.getCdUsuario()) {
					userDB = usuario;
				}
			}

			for (int i = 0; 1 <= userDB.getTransacao().size(); i++) {
				if (transacao.getCodigo() == userDB.getTransacao().get(i).getCodigo()) {
					userDB.getTransacao().remove(i);
					break;
				}

			}
			ur.save(userDB);
			transacao.setUsuario(null);
			et.delete(transacao);
		}

		return transacao;
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
		

		user.getTransacao().addAll(userDB.getTransacao());
		user.setValorDividaUsuario(somaDivida);

		user.add(linkTo(methodOn(UsuarioResource.class).listaUsuarios()).withRel("Lista de Usuarios"));

		ur.save(user);

		return user;
	}

}
