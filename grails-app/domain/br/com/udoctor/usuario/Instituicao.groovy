package br.com.udoctor.usuario

import br.com.udoctor.usuario.prestador.Arquivo;
import org.bson.types.ObjectId;

import br.com.udoctor.tipo.Enum.RankType;
import br.com.udoctor.usuario.instituicao.Responsavel;
import br.com.udoctor.usuario.prestador.Link;
import br.com.udoctor.usuario.prestador.Telefone;
import br.com.udoctor.usuario.todos.Endereco;

class Instituicao implements Serializable { 
	 	
	static mapWith = "mongo"
	
	Long               id
	String             cnpj
	Arquivo            avatar
	Responsavel        responsavel
	String             especialidade
	String             patologia
	String             historico
	RankType      	   rank
	List<Endereco>     enderecos
	List<Telefone>	   telefones
	List<Link> 		   links
	List<ObjectId>     planosDeSaude
    List<Arquivo>      arquivos
	
	static embedded = ['avatar', 'responsavel', 'enderecos', 'telefones', 'links', 'planosDeSaude', 'arquivos']
		
	static constraints = {
		id(blank: false, nullable: false)
		cnpj(blank: false, nullable: false, unique: true, maxSize: 14, minSize:14)
		avatar(blank: true, nullable: true)
		responsavel(blank: true, nullable: true)
		especialidade(blank: false, nullable: false, maxSize: 1000)
		patologia(blank: true, nullable: true, maxSize: 1000)
		historico(blank: true, nullable: true, maxSize: 2000)
		rank(blank: false, nullable: false)
		enderecos(blank: true, nullable: true)
		telefones(blank: true, nullable: true)
		links(blank: true, nullable: true)
		planosDeSaude(blank: true, nullable: true)
        arquivos(blank: true, nullable: true)
	}
	
	static mapping = {		
	}
			
}
