package br.com.udoctor.cadastro;

import org.bson.types.ObjectId;

import org.springframework.web.multipart.MultipartFile;

import br.com.udoctor.seguranca.Usuario;
import br.com.udoctor.usuario.Cliente;
import br.com.udoctor.usuario.Profissional;
import br.com.udoctor.usuario.Instituicao;
import br.com.udoctor.usuario.instituicao.Responsavel;
import br.com.udoctor.usuario.prestador.Arquivo;
import br.com.udoctor.usuario.prestador.Link;
import br.com.udoctor.usuario.prestador.Telefone;
import br.com.udoctor.usuario.todos.Endereco;

import br.com.udoctor.tipo.Enum.CadastroType;
import br.com.udoctor.tipo.Enum.StatusType;
import br.com.udoctor.tipo.Enum.UsuarioType;

import br.com.udoctor.modelo.Estado;
import br.com.udoctor.modelo.PlanoDeSaude;
import br.com.udoctor.modelo.Profissao;

class CadastroController {

    CadastroUsuarioService      cadastroUsuarioService
    CadastroClienteService      cadastroClienteService
    CadastroProfissionalService cadastroProfissionalService
    CadastroInstituicaoService  cadastroInstituicaoService
	
	def springSecurityService
		
	static allowedMethods = [cadastroInicialCliente: 	  'POST',
							 cadastroInicialProfissional: 'POST',
							 cadastroInicialInstituicao:  'POST']

	def cadastroInicial(){}

	def cadastroInicialCliente(){

        // Parametros de Entrada
        Map<String,Object> parametros = new HashMap<String,Object>()
        // Usuário
        parametros.put("nome", params.nome)
        parametros.put("sobrenome", params.sobrenome)
        parametros.put("email", params.email)
        parametros.put("termos", params.termos)
        parametros.put("senha", params.senha)

        Map<String,Object> retorno = cadastroUsuarioService.salvarUsuario(new Usuario(status: StatusType.INATIVO, tipoUsuario: UsuarioType.CLIENTE, enabled: false),parametros, CadastroType.CADASTRO_INICIAL)
			
		Usuario usuario           = retorno.usuario
		Boolean salvoComSucessoCI = retorno.salvoComSucesso
			
		if (salvoComSucessoCI){
			chain(action:"cadastroAdicionalCliente", model:[salvoComSucessoCI: salvoComSucessoCI])
		}else{
			chain(action:"cadastroInicial", model:[cliente: usuario])
		}
		return
	}

	def cadastroAdicionalCliente(){

		Usuario 		usuario
		Cliente 		cliente
		Endereco 		endereco
		List<ObjectId>  planosDeSaudeUtilizados
        Arquivo         avatar
        String          validacaoAvatar
		Boolean         salvoComSucessoCA
		
		if (request.post) {

            // Parametros de Entrada
            Map<String,Object> parametros = new HashMap<String,Object>()
            // Usuário
            parametros.put("nome", params.nome)
            parametros.put("sobrenome", params.sobrenome)
            parametros.put("email", params.email)
            // Cliente
            parametros.put("genero", params.genero)
            parametros.put("dataDeNascimento", params.dataDeNascimento)
            // Endereço
            parametros.put("enderecos[0]", params["enderecos[0]"])
            // Planos de Saúde
            parametros.put("planosDeSaude", params.planosDeSaude)
            // Avatar
            parametros.put("avatar", params.avatar)

            MultipartFile arquivoAvatar = request.getFile("avatar")
            Map<String,Object> retorno = cadastroClienteService.salvarUsuario(springSecurityService.currentUser, parametros, CadastroType.CADASTRO_ADICIONAL, arquivoAvatar)
		
			usuario      			= retorno.usuario
			cliente      		    = retorno.cliente
			endereco     		    = retorno.endereco
			planosDeSaudeUtilizados = retorno.planosDeSaude
            avatar                  = retorno.avatar
            validacaoAvatar         = retorno.validacaoAvatar
			salvoComSucessoCA       = retorno.salvoComSucesso
		}else{
			usuario = springSecurityService.currentUser
			if(usuario.cliente){
				cliente = usuario.cliente
				if(cliente.enderecos){
					endereco = cliente.enderecos[0]
				}
				if(cliente.planosDeSaude){
					planosDeSaudeUtilizados = cliente.planosDeSaude
				}
                if(cliente.avatar){
                    avatar = cliente.avatar
                }
			}
		}
		
		List<PlanoDeSaude> planosDeSaude = PlanoDeSaude.findAllByStatus(StatusType.ATIVO)
		List<Estado> estados = Estado.findAllByStatus(StatusType.ATIVO)

		return [usuario                : usuario,
				cliente                : cliente,
				endereco               : endereco,
				planosDeSaudeUtilizados: planosDeSaudeUtilizados,
                avatar                 : avatar,
                validacaoAvatar        : validacaoAvatar,
				planosDeSaude          : planosDeSaude,
				estados                : estados,
				salvoComSucessoCA      : salvoComSucessoCA,
				salvoComSucessoCI      : chainModel?.salvoComSucessoCI]
	}

	def cadastroInicialProfissional(){

        // Parametros de Entrada
        Map<String,Object> parametros = new HashMap<String,Object>()
        // Usuário
        parametros.put("nome", params.nome)
        parametros.put("sobrenome", params.sobrenome)
        parametros.put("email", params.email)
        parametros.put("termos", params.termos)
        parametros.put("senha", params.senha)

        Map<String,Object> retorno = cadastroUsuarioService.salvarUsuario(new Usuario(status: StatusType.INATIVO, tipoUsuario: UsuarioType.PROFISSIONAL, enabled: false),parametros, CadastroType.CADASTRO_INICIAL)
			
		Usuario usuario            = retorno.usuario
		Boolean salvoComSucessoCI  = retorno.salvoComSucesso
			
		if (salvoComSucessoCI){
			chain(action:"cadastroAdicionalProfissional", model:[salvoComSucessoCI: salvoComSucessoCI])
		}else{
			chain(action:"cadastroInicial", model:[profissional: usuario])
		}
		return
	}

	def cadastroAdicionalProfissional(){
		
		Usuario 	       usuario
		Profissional       profissional
		List<Endereco>     enderecos
		List<Link> 		   links
		List<Telefone>     telefones
		List<ObjectId>     planosDeSaudeUtilizados
		List<Arquivo>      arquivos  
		Map<String,String> validacaoArquivos
		Boolean            salvoComSucessoCA
		
		if (request.post) {

            // Parametros de Entrada
            Map<String,Object> parametros = new HashMap<String,Object>()
            // Usuário
            parametros.put("nome", params.nome)
            parametros.put("sobrenome", params.sobrenome)
            parametros.put("email", params.email)
            parametros.put("urlProfile", params.urlProfile)
            // Profissional
            parametros.put("codigoConselhoRegional", params.codigoConselhoRegional)
            parametros.put("especialidade", params.especialidade)
            parametros.put("patologia", params.patologia)
            parametros.put("curriculo", params.curriculo)
            parametros.put("genero", params.genero)
            parametros.put("profissao", params.profissao)
            parametros.put("dataDeNascimento", params.dataDeNascimento)
            // Endereços
            String chave
            for(int i = 0; i < 5; i++){
                chave = "enderecos[${i}]"
                if(params["enderecos[${i}].ativo"] == 'X'){
                    parametros.put(chave, params["enderecos[${i}]"])
                }else{
                    break
                }
            }

            // Links
            for(int i = 0; i < 10; i++){
                chave = "links[${i}]"
                if(params["links["+i+"].ativo"] == 'X'){
                    parametros.put(chave, params["links[${i}]"])
                }else{
                    break
                }
            }
            // Telefones
            for(int i = 0; i < 10; i++){
                chave = "telefones[${i}]"
                if(params["telefones["+i+"].ativo"] == 'X'){
                    parametros.put(chave, params["telefones[${i}]"])
                }else{
                    break
                }
            }
            // Planos de Saúde
            parametros.put("planosDeSaude", params.planosDeSaude)
            // Arquivos
            parametros.put("avatar", params.avatar)
            parametros.put("certificadoCRM", params.certificadoCRM)

            Map<String,MultipartFile> arquivosMap = [avatar: request.getFile("avatar"), certificadoCRM: request.getFile("certificadoCRM")]
            Map<String,Object> retorno =  cadastroProfissionalService.salvarUsuario(springSecurityService.currentUser, parametros, CadastroType.CADASTRO_ADICIONAL, arquivosMap)
			
			usuario          		= retorno.usuario
			profissional			= retorno.profissional
			enderecos				= retorno.enderecos
			links				    = retorno.links
			telefones			    = retorno.telefones
			planosDeSaudeUtilizados	= retorno.planosDeSaude
			arquivos                = retorno.arquivos
			validacaoArquivos       = retorno.validacaoArquivos
			salvoComSucessoCA	    = retorno.salvoComSucesso
			
		}else{
			usuario = springSecurityService.currentUser
			
			if(usuario.profissional){
				profissional = usuario.profissional
				if(profissional.enderecos){
					enderecos = profissional.enderecos
				}
				if(profissional.telefones){
					telefones = profissional.telefones
				}
				if(profissional.links){
					links = profissional.links
				}
				if(profissional.planosDeSaude){
					planosDeSaudeUtilizados = profissional.planosDeSaude
				}
				if(profissional.arquivos){
					arquivos = profissional.arquivos
				}
			}
		}
	
		List<PlanoDeSaude> planosDeSaude = PlanoDeSaude.findAllByStatus(StatusType.ATIVO)
		List<Profissao> profissoes = Profissao.findAllByStatus(StatusType.ATIVO)
		List<Estado> estados = Estado.findAllByStatus(StatusType.ATIVO)
		
		return [usuario                : usuario,
				profissional           : profissional,
				enderecos              : enderecos,
				links                  : links,
				telefones              : telefones,
				planosDeSaudeUtilizados: planosDeSaudeUtilizados,
				planosDeSaude          : planosDeSaude,
				profissoes             : profissoes,
				estados        		   : estados,
				arquivos               : arquivos,
				validacaoArquivos 	   : validacaoArquivos,
				salvoComSucessoCA      : salvoComSucessoCA,
				salvoComSucessoCI      : chainModel?.salvoComSucessoCI]
	}

	def cadastroInicialInstituicao(){

        // Parametros de Entrada
        Map<String,Object> parametros = new HashMap<String,Object>()
        // Usuário
        parametros.put("nome", params.nome)
        parametros.put("sobrenome", params.sobrenome)
        parametros.put("email", params.email)
        parametros.put("termos", params.termos)
        parametros.put("senha", params.senha)

        Map<String,Object> retorno = cadastroUsuarioService.salvarUsuario(new Usuario(status: StatusType.INATIVO, tipoUsuario: UsuarioType.INSTITUICAO, enabled: false),parametros, CadastroType.CADASTRO_INICIAL)
			
		Usuario usuario            = retorno.usuario
		Boolean salvoComSucessoCI  = retorno.salvoComSucesso
			
		if (salvoComSucessoCI){
			chain(action:"cadastroAdicionalInstituicao", model:[salvoComSucessoCI: salvoComSucessoCI])
		}else{
			chain(action:"cadastroInicial", model:[instituicao: usuario])
		}
		return
	}

	def cadastroAdicionalInstituicao(){
		
		Usuario 		   usuario
		Instituicao 	   instituicao
		Responsavel	       responsavel
		List<Endereco> 	   enderecos
		List<Link> 		   links
		List<Telefone>     telefones
		List<ObjectId>     planosDeSaudeUtilizados
		List<Arquivo>      arquivos  
		Map<String,String> validacaoArquivos
		Boolean            salvoComSucessoCA
		
		if (request.post) {

            // Parametros de Entrada
            Map<String,Object> parametros = new HashMap<String,Object>()
            // Usuário
            parametros.put("nome", params.nome)
            parametros.put("sobrenome", params.sobrenome)
            parametros.put("email", params.email)
            parametros.put("urlProfile", params.urlProfile)
            // Instituição
            parametros.put("cnpj", params.cnpj)
            parametros.put("especialidade", params.especialidade)
            parametros.put("patologia", params.patologia)
            parametros.put("historico", params.historico)
            // Responsavel
            parametros.put("nomeResponsavel", params.nomeResponsavel)
            parametros.put("sobrenomeResponsavel", params.sobrenomeResponsavel)
            parametros.put("genero", params.genero)
            parametros.put("profissao", params.profissao)
            parametros.put("codigoConselhoRegional", params.codigoConselhoRegional)
            // Endereços
            String chave
            for(int i = 0; i < 5; i++){
                chave = "enderecos[${i}]"
                if(params["enderecos[${i}].ativo"] == 'X'){
                    parametros.put(chave, params["enderecos[${i}]"])
                }else{
                    break
                }
            }
            // Links
            for(int i = 0; i < 10; i++){
                chave = "links[${i}]"
                if(params["links["+i+"].ativo"] == 'X'){
                    parametros.put(chave, params["links[${i}]"])
                }else{
                    break
                }
            }
            // Telefones
            for(int i = 0; i < 10; i++){
                chave = "telefones[${i}]"
                if(params["telefones["+i+"].ativo"] == 'X'){
                    parametros.put(chave, params["telefones[${i}]"])
                }else{
                    break
                }
            }
            // Planos de Saúde
            parametros.put("planosDeSaude", params.planosDeSaude)
            // Arquivos
            parametros.put("avatar", params.avatar)
            parametros.put("certificadoCRM", params.certificadoCRM)

            Map<String,MultipartFile> arquivosMap = [avatar: request.getFile("avatar"), certificadoCRM: request.getFile("certificadoCRM")]
            Map<String,Object> retorno = cadastroInstituicaoService.salvarUsuario(springSecurityService.currentUser, parametros, CadastroType.CADASTRO_ADICIONAL, arquivosMap)

			usuario                    = retorno.usuario
			instituicao	               = retorno.instituicao
			enderecos                  = retorno.enderecos
			links         		       = retorno.links
			telefones        		   = retorno.telefones
			planosDeSaudeUtilizados    = retorno.planosDeSaude
			responsavel                = retorno.responsavel
			arquivos                   = retorno.arquivos
            validacaoArquivos    	   = retorno.validacaoArquivos		
            salvoComSucessoCA          = retorno.salvoComSucesso

		}else{
			usuario = springSecurityService.currentUser

			if(usuario.instituicao){
				instituicao = usuario.instituicao
				if(instituicao.enderecos){
					enderecos = instituicao.enderecos
				}
				if(instituicao.telefones){
					telefones = instituicao.telefones
				}
				if(instituicao.links){
					links = instituicao.links
				}
				if(instituicao.planosDeSaude){
					planosDeSaudeUtilizados = instituicao.planosDeSaude
				}
				if(instituicao.arquivos){
					arquivos = instituicao.arquivos
				}
				if(instituicao.responsavel){
					responsavel = instituicao.responsavel
				}
			}
		}
		
		List<PlanoDeSaude> planosDeSaude = PlanoDeSaude.findAllByStatus(StatusType.ATIVO)
		List<Profissao> profissoes = Profissao.findAllByStatus(StatusType.ATIVO)
		List<Estado> estados = Estado.findAllByStatus(StatusType.ATIVO)
		
		return [usuario			        : usuario,
				instituicao             : instituicao,
				enderecos               : enderecos,
				links                   : links,
				telefones               : telefones,
				responsavel             : responsavel,
				planosDeSaudeUtilizados : planosDeSaudeUtilizados,
				planosDeSaude		    : planosDeSaude,
				profissoes			    : profissoes,
				estados         	    : estados,
				arquivos                : arquivos,
                validacaoArquivos 	    : validacaoArquivos,
				salvoComSucessoCA       : salvoComSucessoCA,
				salvoComSucessoCI       : chainModel?.salvoComSucessoCI]
	}
}