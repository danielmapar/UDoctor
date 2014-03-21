package br.com.udoctor.elasticsearch

import org.bson.types.ObjectId
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.groovy.node.GNode
import org.elasticsearch.groovy.node.GNodeBuilder

import br.com.udoctor.seguranca.Usuario;
import br.com.udoctor.usuario.Instituicao;
import br.com.udoctor.usuario.Profissional;
import br.com.udoctor.usuario.instituicao.Responsavel;
import br.com.udoctor.usuario.prestador.Link;
import br.com.udoctor.usuario.prestador.Telefone;
import br.com.udoctor.usuario.todos.Endereco;

import br.com.udoctor.tipo.Enum.UsuarioType;

class ElasticSearchService {
	
	GNode node
    GrailsApplication grailsApplication
	
    void iniciar() {
		
		GNodeBuilder nodeBuilder = new GNodeBuilder()

        if (grailsApplication.config.elasticsearch.tipo.execucao == "local"){

            nodeBuilder.settings {
			    node {
				    local = true //  local = true
			    }
		    }
        }else if (grailsApplication.config.elasticsearch.tipo.execucao == "client"){

            nodeBuilder.settings {
                node {
                    client = true //  local = true
                }
            }
        }
		
		node = nodeBuilder.node()
    }
	
	void finalizar(){
		node.stop().close()
	}	

	private String processarUsuario(Usuario usuario){
		
		String json = ""
			
		json =  "{" + "\"nome\":\"${usuario.nome}\"," +
				"\"sobrenome\":\"${usuario.sobrenome}\"," +
				"\"tipoUsuario\":\"${usuario.tipoUsuario}\"," + // nao indexar
				"\"email\":\"${usuario.email}\"," +
				"\"urlProfile\":\"${usuario.urlProfile}\","
					
		if(usuario.tipoUsuario == UsuarioType.PROFISSIONAL && usuario.profissional){
			json += processarProfissional(usuario.profissional)	
		}else if(usuario.tipoUsuario == UsuarioType.INSTITUICAO && usuario.instituicao){
			json += processarInstituicao(usuario.instituicao)
		}	
			
		json += "}" 
		return json
	}
	
	private String processarInstituicao(Instituicao instituicao){
		
		String json =("\"instituicao\":" + "{" +
						"\"id\":\"${instituicao.id}\"," +
						"\"cnpj\":\"${instituicao.cnpj}\"," +
						"\"especialidade\":\"${instituicao.especialidade}\"," +
						"\"historico\":\"${instituicao.historico}\"," +
						"\"patologia\":\"${instituicao.patologia}\"," +
						"\"rank\":\"${instituicao.rank}\",") // nao indexar

        if(instituicao.planosDeSaude){  // opcional
            json += processarPlanosDeSaude(instituicao.planosDeSaude)
        }
        if(instituicao.links){          // opcional
            json += processarLinks(instituicao.links)
        }
        if(instituicao.responsavel){    // obrigatório
			json += processarResponsavel(instituicao.responsavel)
		}
		if(instituicao.enderecos){      // obrigatório
			json += processarEnderecos(instituicao.enderecos)
		}
		if(instituicao.telefones){     // obrigatório
			json += processarTelefones(instituicao.telefones)
		}

		json += "}"
		return json
	}
	
	private String processarProfissional(Profissional profissional){
		 
		String json =("\"profissional\":" + "{" +
						"\"id\":\"${profissional.id}\"," +
						"\"dataDeNascimento\":\"${profissional.dataDeNascimento}\"," +
						"\"codigoConselhoRegional\":\"${profissional.codigoConselhoRegional}\"," +
						"\"especialidade\":\"${profissional.especialidade}\"," +
						"\"patologia\":\"${profissional.patologia}\"," +
						"\"curriculo\":\"${profissional.curriculo}\"," +
						"\"rank\":\"${profissional.rank}\"," + // nao indexar
						"\"profissao\":\"${profissional.profissao}\",")

        if(profissional.planosDeSaude){   // opcional
            json += processarPlanosDeSaude(profissional.planosDeSaude)
        }
        if(profissional.links){          // opcional
            json += processarLinks(profissional.links)
        }
        if(profissional.enderecos){      // obrigatório
			json += processarEnderecos(profissional.enderecos)
		}
		if(profissional.telefones){     // obrigatório
			json += processarTelefones(profissional.telefones)
		}
						
		json += "}" 
		return json
	}
	
	private String processarEnderecos(List<Endereco> enderecos){
		
		String json = ("\"enderecos\":" + "[")
		for(int i = 0; i < enderecos.size; i++){
			Endereco endereco = enderecos[i]
			json += ("{" +
				"\"id\":\"${endereco.id}\"," +
				"\"cep\":\"${endereco.cep}\"," +
				"\"logradouro\":\"${endereco.logradouro}\"," +
				"\"complemento\":\"${endereco.complemento}\"," +
				"\"bairro\":\"${endereco.bairro}\"," +
				"\"estado\":\"${endereco.estado}\"," +
				"\"cidade\":\"${endereco.cidade}\"," +
				"\"descricaoLocal\":\"${endereco.descricaoLocal}\"," +
				"\"latitude\":\"${endereco.latitude}\"," +
				"\"longitude\":\"${endereco.longitude}\"" +
			"}")
			if(i+1 < enderecos.size){
				json += ","
			}
		}
		json += ("],")
		return json
	}
	
	private String processarLinks(List<Link> links){
		
		String json = ("\"links\":" + "[")
		for(int i = 0; i < links.size; i++){
			Link link = links[i]
			json += ("{" +
					 	 "\"id\":\"${link.id}\"," +
						 "\"nome\":\"${link.nome}\"," +
						 "\"descricao\":\"${link.descricaoLink}\"" +
					 "}")
			if(i+1 < links.size){
				json += ","
			}
		}
		json += ("],")
		return json
	}
	
	private String processarTelefones(List<Telefone> telefones){
	
		String json = ("\"telefones\":" + "[")
		for(int i = 0; i < telefones.size; i++){
			Telefone telefone = telefones[i]
			json += ("{" +
				"\"id\":\"${telefone.id}\"," +
				"\"prefixo\":\"${telefone.prefixo}\"," +
				"\"numero\":\"${telefone.numero}\"," +
				"\"numeroCompleto\":\"${telefone.numeroCompleto}\"" +
			"}")
			if(i+1 < telefones.size){
				json += ","
			}
		}
		json += ("]")   // sem vírgula
		return json
	}
	
	private String processarPlanosDeSaude(List<ObjectId> planosDeSaude){
		
		String json = ("\"planosDeSaude\":" + "[")
		for(int i = 0; i < planosDeSaude.size; i++){
			ObjectId planoDeSaude = planosDeSaude[i]
			json += ("\"${planoDeSaude}\"")
			if(i+1 < planosDeSaude.size){
				json += ","
			}
		}
		json += ("],")
		return json
	}
	
	private String processarResponsavel(Responsavel responsavel){
		return ("\"responsavel\":" + "{" +
						"\"id\":\"${responsavel.id}\"," +
						"\"nome\":\"${responsavel.nome}\"," +
						"\"sobrenome\":\"${responsavel.sobrenome}\"," +
						"\"genero\":\"${responsavel.genero}\"," +
						"\"codigoConselhoRegional\":\"${responsavel.codigoConselhoRegional}\"," +
						"\"profissao\":\"${responsavel.profissao}\"},")
	}
	
	void indexar(Usuario usuario){
	
	  if(usuario){
		  IndexResponse response = node.client.prepareIndex("usuario", usuario.tipoUsuario.toString().toLowerCase(), usuario.id.toString())
		  .setSource(processarUsuario(usuario))
		  .execute()
		  .actionGet();
	  }
	  
	}
}
	
