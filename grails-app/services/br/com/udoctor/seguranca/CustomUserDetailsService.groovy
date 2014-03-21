package br.com.udoctor.seguranca;

import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUserDetailsService;
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsService implements GrailsUserDetailsService {

   /**
	* Some Spring Security classes (e.g. RoleHierarchyVoter) expect at least
	* one role, so we give a user with no granted roles this one which gets
	* past that restriction but doesn't grant anything.
	*/
   static final List NO_ROLES = [new GrantedAuthorityImpl(SpringSecurityUtils.NO_ROLE)]

   UserDetails loadUserByUsername(String email, boolean loadRoles)
			throws UsernameNotFoundException {
	  return loadUserByUsername(email)
   }

   UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

	  Usuario.withTransaction { status ->

		 Usuario usuario = Usuario.findByEmail(email)
		 if (!usuario) throw new UsernameNotFoundException(
					  'Usuario nao encontrado', email)

		 def authorities = usuario.authorities.collect {
			 new GrantedAuthorityImpl(it.authority)
		 }
		 
		 return new CustomUserDetails(usuario.email, usuario.senha, usuario.enabled,
			!usuario.accountExpired, !usuario.passwordExpired,
			!usuario.accountLocked, authorities ?: NO_ROLES, usuario.id,
			usuario.nome, usuario.sobrenome, usuario.tipoUsuario, usuario.status)
	  }
   }
}