package br.com.udoctor.seguranca

import org.apache.commons.lang.RandomStringUtils;
import org.bson.types.ObjectId;

import br.com.udoctor.tipo.Enum.CadastroType;
import br.com.udoctor.tipo.Enum.StatusType;
import br.com.udoctor.tipo.Enum.UsuarioType;
import br.com.udoctor.usuario.Cliente;
import br.com.udoctor.usuario.Instituicao;
import br.com.udoctor.usuario.Profissional;

class Usuario implements Serializable { 

	static mapWith = "mongo"
		
	transient springSecurityService	
	
	static transients = ["tipoCadastro", "senhaNaoModificada"]
	CadastroType tipoCadastro
	String       senhaNaoModificada
	
	ObjectId     id
	Date 	     dataDeCriacao = new Date()
	StatusType   status
	UsuarioType  tipoUsuario
	Instituicao  instituicao
	Profissional profissional
	Cliente      cliente
	String 		 nome
	String 		 sobrenome
	Boolean      termos
	String 		 email
	String 		 senha
	String       urlProfile
	boolean 	 enabled
	boolean 	 accountExpired
	boolean 	 accountLocked
	boolean 	 passwordExpired
		
	static embedded = ['instituicao', 'profissional', 'cliente']

	static constraints = {
		dataDeCriacao(blank: false, nullable: false)
		status(blank: false, nullable: false, inList:StatusType.lista(), minSize:1, maxSize:1)
		tipoUsuario(blank: true, nullable: true, inList:UsuarioType.lista(), minSize:1, maxSize:1)
		instituicao(blank: true, nullable: true, validator: {Instituicao instituicao, Usuario usuario->
			if(instituicao || usuario.profissional){
				if(!instituicao && !usuario.profissional) {
					return 'usuario.heranca.error.ao.menos.um'
				}
				if(instituicao && usuario.profissional) { 
					return 'usuario.heranca.error.somente.um'
				}
			}
		})
		profissional(blank: true, nullable: true)
		cliente(blank: true, nullable: true)
		nome(blank: false, nullable: false,  size: 2..20)
		sobrenome (blank: false, nullable: true,  size: 2..40, validator: { String sobrenome, Usuario usuario ->
				if(usuario.tipoUsuario != UsuarioType.INSTITUICAO &&
				  (!sobrenome || sobrenome == "")){
						return 'usuario.sobrenome.error.vazio'
				}
		})
		termos(blank: false, nullable: false, validator: { Boolean termos ->
				if(termos == false){
					return 'usuario.termos.error.invalido'
				}
		})
		email (blank: false, nullable: false, unique: true, email: true, size: 7..80)
		senha (blank: false, nullable: false, validator: { String senha, Usuario usuario ->
			   if(usuario.tipoCadastro != CadastroType.CADASTRO_ADICIONAL){
				   if ((senha) &&
					   (senha.toLowerCase().equals(usuario.nome.toLowerCase())  ||
						senha.toLowerCase().equals(usuario.email.toLowerCase()) ||
						(usuario.tipoUsuario != UsuarioType.INSTITUICAO && senha.toLowerCase().equals(usuario.sobrenome.toLowerCase())))
					   ) {
					   	if(usuario.tipoUsuario != UsuarioType.INSTITUICAO){
							   return 'usuario.senha.error.igual' // erro com sobrenome
					   	}else{
						   	   return 'usuario.instituicao.senha.error.igual' // erro sem sobrenome
						}
					}
					if ((senha) &&
						(senha.size() < 6 ||
						 senha.size() > 30)){
						 return 'usuario.senha.error.tamanho'
					}
			   }
		  })
		urlProfile(blank: false, nullable: true, unique: true,  size: 2..30, validator: { String urlProfile, Usuario usuario ->
				 if ((usuario.tipoUsuario != UsuarioType.CLIENTE && usuario.tipoCadastro == CadastroType.CADASTRO_ADICIONAL) &&
					 (!urlProfile || urlProfile == "")) {
						 return 'usuario.urlperfil.error.vazio'
					 }
				  }
	    )
	}

	static mapping = {
		id index: true, indexAttributes: [unique:true, dropDups:true]
		email index: true, indexAttributes: [unique:true, dropDups:true]
	}

    public void gerarUrlProfile(String urlProfileNovo) {

        if (((!urlProfile || urlProfile == '')  && (!urlProfileNovo  || urlProfileNovo == '' )) ||
               urlProfileNovo != urlProfile){

            if(urlProfile != '' && urlProfileNovo != '' && urlProfile != urlProfileNovo){
                urlProfile = urlProfileNovo
            }
            else if(nome && sobrenome && nome != '' && sobrenome != ''){
                urlProfile = (nome.take(20) + sobrenome.take(10)).toLowerCase().replaceAll(' ', '')
                while(Usuario.findByUrlProfile("${urlProfile}")){
                    urlProfile = (urlProfile.take(25) + new RandomStringUtils().randomNumeric(30-urlProfile.take(25).size()))
                }
            }else if(nome && nome != ''){
                urlProfile = nome.take(20).toLowerCase().replaceAll(' ', '')
                while(Usuario.findByUrlProfile("${urlProfile}")){
                    urlProfile = (urlProfile.take(25) + new RandomStringUtils().randomNumeric(30-urlProfile.take(25).size()))
                }
            }
        }
    }

	Set<Regra> getAuthorities() {
		UsuarioRegra.findAllByUsuario(this).collect { it.regra } as Set
	}

	def beforeInsert() {
		encodePassword()
	}
	
	def beforeUpdate() {
		if(senhaNaoModificada && senhaNaoModificada != senha){
			encodePassword()
		}
	}

	protected void encodePassword() {
		senha = springSecurityService.encodePassword(senha)
	}
	
}
