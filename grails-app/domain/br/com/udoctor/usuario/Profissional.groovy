package br.com.udoctor.usuario

import java.util.List;

import br.com.udoctor.usuario.prestador.Arquivo;
import org.bson.types.ObjectId;

import br.com.udoctor.tipo.Enum.GeneroType;
import br.com.udoctor.tipo.Enum.RankType;
import br.com.udoctor.usuario.prestador.Link;
import br.com.udoctor.usuario.prestador.Telefone;
import br.com.udoctor.usuario.todos.Endereco;

class Profissional implements Serializable { 
	
	static mapWith = "mongo"
	
	static transients = ["nomeProfissao"]
	String nomeProfissao
	
	Long             id
	Arquivo    		 avatar
	GeneroType 		 genero
	Date      		 dataDeNascimento 
	ObjectId         profissao
	String     	     codigoConselhoRegional
	String     	     especialidade
	String   		 patologia
	String           curriculo
	RankType         rank
	List<Endereco>   enderecos
	List<Telefone>   telefones
	List<Link>       links
	List<ObjectId>   planosDeSaude
	List<Arquivo>    arquivos
	
	static embedded = ['avatar', 'enderecos', 'telefones', 'links', 'planosDeSaude', 'arquivos']
	
	static constraints = {		
		id(blank: false, nullable: false)
		avatar(blank: true, nullable: true)
		genero(blank: false, nullable: false, inList:GeneroType.lista(), minSize:1, maxSize:1)
		dataDeNascimento(blank: false, nullable: false, max: new Date())
		profissao(blank: false, nullable: false)
		codigoConselhoRegional(blank: false, nullable: false, unique: true, maxSize: 9, minSize:9)
		especialidade(blank: false, nullable: false, maxSize: 1000)
		patologia(blank: true, nullable: true, maxSize: 1000)
		curriculo(blank: true, nullable: true, maxSize: 2000)
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
