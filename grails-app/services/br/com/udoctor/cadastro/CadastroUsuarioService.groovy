package br.com.udoctor.cadastro;

import grails.plugins.springsecurity.SpringSecurityService;
import br.com.udoctor.seguranca.ConfirmacaoEmailService;

import org.bson.types.ObjectId;

import br.com.udoctor.seguranca.Usuario;
import br.com.udoctor.seguranca.Regra;
import br.com.udoctor.seguranca.UsuarioRegra;

import br.com.udoctor.tipo.Enum.UsuarioType;
import br.com.udoctor.tipo.Enum.CadastroType;

class CadastroUsuarioService {
	
	static transactional = 'mongo'
	
	SpringSecurityService   springSecurityService
	ConfirmacaoEmailService confirmacaoEmailService
	
	public Map salvarUsuario(Usuario usuario, final Map<String,Object> parametros, final CadastroType tipoCadastro){
		usuario                 = processarUsuario(usuario, parametros, tipoCadastro);
		Boolean salvoComSucesso = finalizarProcesso(usuario);
		
		return [usuario: usuario, salvoComSucesso: salvoComSucesso]
	}
	
	protected Usuario processarUsuario(Usuario usuario, final Map<String,Object> parametros, final CadastroType tipoCadastro){

		usuario.tipoCadastro  = tipoCadastro
		usuario.nome 	      = parametros.nome
		usuario.sobrenome     = parametros.sobrenome
		usuario.email 	      = parametros.email.toLowerCase()
        if(usuario.tipoUsuario != UsuarioType.CLIENTE){
            usuario.gerarUrlProfile(parametros.urlProfile)
        }

		if(parametros.termos && parametros.termos == "on"){
			usuario.termos  = parametros.termos
		}
		usuario.senha = parametros.senha

        usuario.validate()
		return usuario
	}
	
	private Boolean finalizarProcesso(Usuario usuario){
		
		if (!usuario.hasErrors()){
			
			final String authority = "ROLE_" + usuario.tipoUsuario.toString()
			final Regra regra = Regra.findByAuthority(authority)
			
			if(regra && usuario.save() && UsuarioRegra.create (usuario, regra, true)){
				if(regra.authority != 'ROLE_ADMINISTRADOR'){
					springSecurityService.reauthenticate usuario.email
					confirmacaoEmailService.enviarConfirmacao(id: usuario.id,
															  para: usuario.email)
				}
				return true
			}
		}
		return false
	}
}

