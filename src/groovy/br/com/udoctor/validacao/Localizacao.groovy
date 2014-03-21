package br.com.udoctor.validacao

import org.bson.types.ObjectId;

import br.com.udoctor.modelo.Estado;

class Localizacao {

	static ArrayList validar(String localizacaoId){

        if(localizacaoId && localizacaoId != ""){
			List localizacao = localizacaoId.split("\\;")
			if(localizacao[0] && localizacao[0] != "" && localizacao[1] && localizacao[1] != "" && ObjectId.isValid(localizacao[1]) && localizacao[0].isInteger()){
                ObjectId estadoId = new ObjectId(localizacao[1])
				Integer cidadeId = Integer.parseInt(localizacao[0])
				Estado  estado   = Estado.get(estadoId)
				if(estado && estado.cidades.size >= cidadeId){
					return [estadoId,cidadeId]
				}
			}
		}
		return [null,null]
	}
}
