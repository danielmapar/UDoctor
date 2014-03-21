package br.com.udoctor.usuario

import br.com.udoctor.usuario.prestador.Arquivo;
import org.bson.types.ObjectId;

import br.com.udoctor.tipo.Enum.GeneroType;
import br.com.udoctor.usuario.todos.Endereco;

class Cliente implements Serializable { 
	
	static mapWith = "mongo"
	
	Long               id
	Arquivo            avatar
	GeneroType         genero
	Date               dataDeNascimento
	List<Endereco>     enderecos
	List<ObjectId>     planosDeSaude
	
	static embedded = ['avatar', 'enderecos', 'planosDeSaude']
	
	static constraints = {	
		id(blank: false, nullable: false)
		avatar(blank: true, nullable: true)
		genero(blank: false, nullable: true, inList:GeneroType.lista(), minSize:1, maxSize:1)
		dataDeNascimento(blank: false, nullable: true, max: new Date())
		enderecos(blank: true, nullable: true)
		planosDeSaude(blank: true, nullable: true)
	}
	static mapping = {
	}
}
