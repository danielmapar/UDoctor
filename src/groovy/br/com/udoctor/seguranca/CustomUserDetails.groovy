package br.com.udoctor.seguranca;

import org.bson.types.ObjectId;
import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUser;
import org.springframework.security.core.GrantedAuthority;

import br.com.udoctor.tipo.Enum.UsuarioType;
import br.com.udoctor.tipo.Enum.StatusType;

class CustomUserDetails extends GrailsUser {

   final StatusType  status
   final String      nome
   final String      sobrenome
   final UsuarioType tipoUsuario

   CustomUserDetails(String username, String password, boolean enabled,
                 boolean accountNonExpired, boolean credentialsNonExpired,
                 boolean accountNonLocked,
                 Collection<GrantedAuthority> authorities,
                 ObjectId id, String nome, String sobrenome, UsuarioType tipoUsuario, StatusType status) {
      super(username, password, enabled, accountNonExpired,
            credentialsNonExpired, accountNonLocked, authorities, id)

	  this.status       = status
      this.nome         = nome
	  this.sobrenome    = sobrenome
	  this.tipoUsuario  = tipoUsuario
   }

}
