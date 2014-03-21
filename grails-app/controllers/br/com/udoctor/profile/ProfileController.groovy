package br.com.udoctor.profile;

import br.com.udoctor.tipo.Enum.StatusType;
import br.com.udoctor.tipo.Enum.UsuarioType;

import br.com.udoctor.seguranca.Usuario;
import br.com.udoctor.usuario.Instituicao;
import br.com.udoctor.usuario.Profissional;
import br.com.udoctor.usuario.instituicao.Responsavel
import br.com.udoctor.usuario.prestador.Arquivo;
import br.com.udoctor.usuario.prestador.Link;
import br.com.udoctor.usuario.prestador.Telefone;
import br.com.udoctor.usuario.todos.Endereco;
import br.com.udoctor.modelo.Estado;
import br.com.udoctor.modelo.PlanoDeSaude;
import br.com.udoctor.modelo.Profissao;

class ProfileController {

	def springSecurityService
	
	static allowedMethods = [index: "GET"]
	
	def index() {
		
		String urlProfile = params.id

		Usuario usuario
		if(urlProfile && urlProfile != ""){
			usuario = Usuario.findByUrlProfileAndEnabledAndStatus("${urlProfile}", true, StatusType.ATIVO) 
		}else if(springSecurityService.loggedIn){
			usuario = springSecurityService.currentUser
			if(!(usuario.enabled && usuario.status == StatusType.ATIVO)){
				chain(controller: "pesquisa", action: "index", model: [erro: "Esse profile encontra-se inativo!"])
				return
			}
		}

		if(usuario){

			if(usuario.tipoUsuario == UsuarioType.INSTITUICAO){

				Instituicao 			instituicao
				Responsavel             responsavel
				List<Endereco> 			enderecos
				List<String>    		planosDeSaude
				List<Telefone> 			telefones
				List<Link> 				links
                Arquivo                 avatar

				if(usuario.instituicao){
					instituicao = usuario.instituicao
					
					if(instituicao.responsavel){
						responsavel = instituicao.responsavel
						responsavel.nomeProfissao = Profissao.get(responsavel.profissao).nome
					}
                    if(instituicao.arquivos && instituicao.arquivos.size() == 2){
                        avatar = instituicao.arquivos.get(1) // Pega avatar
                    }
					if(instituicao.enderecos){
						Estado estado
						for(int i = 0; i < instituicao.enderecos.size(); i++){
							estado = Estado.get(instituicao.enderecos[i].estado)
							instituicao.enderecos[i].nomeEstado = estado.nome
							instituicao.enderecos[i].nomeCidade = estado.cidades.get(instituicao.enderecos[i].cidade).nome
						}
						enderecos = instituicao.enderecos
					}
					if(instituicao.planosDeSaude){
						planosDeSaude = new ArrayList<String>()
						for(int i = 0; i < instituicao.planosDeSaude.size(); i++){
							planosDeSaude.add(PlanoDeSaude.get(instituicao.planosDeSaude[i]).nome)
						}
					}
					if(instituicao.telefones){
						telefones = instituicao.telefones
					}
					if(instituicao.links){
						links = instituicao.links
					}

					render(view: "instituicao", model: [usuario       : usuario,
														instituicao   : instituicao,
														responsavel   : responsavel,
                                                        avatar        : avatar,
														enderecos     : enderecos,
														links         : links,
														telefones     : telefones,
														planosDeSaude : planosDeSaude])
                    return
				}
			}else if(usuario.tipoUsuario == UsuarioType.PROFISSIONAL){
				
				Profissional 	profissional
				List<Endereco> 	enderecos
				List<String>    planosDeSaude
				List<Telefone> 	telefones
				List<Link> 		links
                Arquivo         avatar

				if(usuario.profissional){
					profissional = usuario.profissional
					
					profissional.nomeProfissao = Profissao.get(profissional.profissao).nome
					
					if(profissional.enderecos){
						Estado estado
						for(int i = 0; i < profissional.enderecos.size(); i++){
							estado = Estado.get(profissional.enderecos[i].estado)
							profissional.enderecos[i].nomeEstado = estado.nome
							profissional.enderecos[i].nomeCidade = estado.cidades.get(profissional.enderecos[i].cidade).nome
						}
						enderecos = profissional.enderecos
					}
                    if(profissional.arquivos && profissional.arquivos.size() == 2){
                        avatar = profissional.arquivos.get(1) // Pega avatar
                    }
					if(profissional.planosDeSaude){
						planosDeSaude = new ArrayList<String>()
						for(int i = 0; i < profissional.planosDeSaude.size(); i++){
							planosDeSaude.add(PlanoDeSaude.get(profissional.planosDeSaude[i]).nome)
						}
					}
					if(profissional.telefones){
						telefones = profissional.telefones
					}
                    if(profissional.links){
                        links = profissional.links
                    }

					render(view: "profissional", model: [usuario         : usuario,
													     profissional    : profissional,
                                                         avatar          : avatar,
														 enderecos       : enderecos,
                                                         links           : links,
														 telefones       : telefones,
														 planosDeSaude   : planosDeSaude])
                    return
				}
			}
		}

		chain(controller: "pesquisa", action: "index", model: [erro: "Esse profile n�o foi encontrado!"])
		return
	}
}