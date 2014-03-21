package br.com.udoctor.administrador

import org.bson.types.ObjectId;

import br.com.udoctor.modelo.Estado;
import br.com.udoctor.modelo.Cidade;
import br.com.udoctor.tipo.Enum.StatusType;

class AdministradorService {
	
	static transactional = 'mongo'
	
	def mongo
	
    Boolean atualizarStatusCidade(def params, Integer max) {
		
		def db = mongo.getDB("udoctor")
		
		String status
		if(params.ativar){
			status = StatusType.ATIVO.toString()
		}else if(params.desativar){
			status = StatusType.INATIVO.toString()
		}else{
			return
		}
		Integer registrosAtualizados = 0
		for(int i = 0; i < max; i++){
			if(params["enderecos["+i+"].ativo"]){
				String localizacaoId = params["enderecos["+i+"].cidades"]
				List localizacao = localizacaoId.split("\\;")
										
				ObjectId estadoId = new ObjectId(localizacao[0])
				Integer cidadeId = Integer.parseInt(localizacao[1])
							
				db.estado.update([_id: estadoId, 'cidades._id': cidadeId],
								 [$set:['cidades.$.status': status]], false, false) // upsert e multi
				
				registrosAtualizados += db.command( "getlasterror" ).n
			}
		}
		return registrosAtualizados
	}
		
	Map listaDeEstadosPagina(String estadoId, Integer offset, Integer max){
		
		List<Estado> estados
		if(estadoId != null && estadoId != "" ){
			estados = new ArrayList<Estado>()
			if(ObjectId.isValid("${estadoId}")){
				estados.add(Estado.get(new ObjectId("${estadoId}")))
			}else{
				estados = Estado.list()
			}
		}else{
			estados = Estado.list()
		}
		
		Integer quantidadeDeCidades = 0
		Integer registrosColetados = 0
		Integer indexLeitura = 0
		
		List<Estado> estadosPagina = new ArrayList<Estado>()
		
		estados.each{ Estado estado ->
			quantidadeDeCidades += estado.cidades.size()
			if((offset + 1) >= (quantidadeDeCidades - estado.cidades.size()) && (offset + 1) <= quantidadeDeCidades){
				if(registrosColetados == 0){
					indexLeitura = (estado.cidades.size() + offset) - quantidadeDeCidades
				} else if(registrosColetados > 0){
					indexLeitura = 0
				}
				Estado estadoPagina  = new Estado()
				estadoPagina.id = estado.id
				estadoPagina.nome = estado.nome
				estadoPagina.status = estado.status
				estadoPagina.cidades = new ArrayList<Cidade>()
				for(int i = indexLeitura; i < estado.cidades.size(); i++){
					if(registrosColetados == max){
						break
					}
					estadoPagina.cidades.add(estado.cidades.get(i))
					registrosColetados++
				}
				estadosPagina.add(estadoPagina)
			}
		}
		
		return [estadosPagina: estadosPagina, quantidadeDeCidades: quantidadeDeCidades]
		
	}
	
}
