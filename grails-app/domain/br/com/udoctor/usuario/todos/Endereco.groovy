package br.com.udoctor.usuario.todos;

import org.bson.types.ObjectId;

import br.com.udoctor.tipo.Enum.UsuarioType;
import br.com.udoctor.tipo.Enum.GeocodingType;

class Endereco implements Serializable { 
	
	static mapWith = "mongo"
	
	static transients = ["tipoUsuario", "nomeEstado", "nomeCidade"]
	UsuarioType   tipoUsuario
	String        nomeEstado
	String        nomeCidade
	
	Long          id
	String        cep
	String        logradouro
	String        complemento
	String        bairro
	ObjectId      estado
	Integer       cidade
	String        descricaoLocal
    GeocodingType statusGeoconding
	Double        latitude
	Double        longitude

	static constraints = {
		id(blank: false, nullable: false)
		cep(blank: false, nullable: false, minSize: 9, maxSize: 9)
		logradouro(blank: false, nullable: false, size: 3..100)
		complemento(blank: false, nullable: false, size: 1..50)
		bairro(blank: false, nullable: false, size: 3..30)
		cidade(blank: false, nullable: false)
		estado(blank: false, nullable: false)
		descricaoLocal(blank: false, nullable: true, size: 2..30, 
			validator: { String descricaoLocal, Endereco endereco ->
			if ((endereco.tipoUsuario != UsuarioType.CLIENTE ) &&
			   (!descricaoLocal  || descricaoLocal == "")) {
				return 'endereco.descricaoLocal.error.vazio'
			}
		})
		latitude(blank: false, nullable: true, maxSize: 20)
		longitude(blank: false, nullable: true, maxSize: 20)
        statusGeoconding(blank: true, nullable: true, inList:GeocodingType.lista(), minSize:1, maxSize:1)
	}
	
	static mapping = {
	}
	
}
