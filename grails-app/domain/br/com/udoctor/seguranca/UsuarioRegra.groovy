package br.com.udoctor.seguranca;

import org.apache.commons.lang.builder.HashCodeBuilder
import org.bson.types.ObjectId;

class UsuarioRegra implements Serializable {
	
	static mapWith = "mongo"
	
	ObjectId id
	Usuario  usuario
	Regra    regra

	boolean equals(other) {
		if (!(other instanceof UsuarioRegra)) {
			return false
		}

		other.usuario?.id == usuario?.id &&
			other.regra?.id == regra?.id
	}

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (usuario) builder.append(usuario.id)
		if (regra) builder.append(regra.id)
		builder.toHashCode()
	}

	static UsuarioRegra get(long usuarioId, long regraId) {
		UsuarioRegra.findByIdAndRegra(usuarioId, regraId)
	}

	static UsuarioRegra create(Usuario usuario, Regra regra, boolean flush = false) {
		new UsuarioRegra(usuario: usuario, regra: regra).save(flush: flush, insert: true)
	}

	static boolean remove(Usuario usuario, Regra regra, boolean flush = false) {
		UsuarioRegra instance = UsuarioRegra.findByUsuarioAndRegra(usuario, regra)
		if (!instance) {
			return false
		}

		instance.delete(flush: flush)
		true
	}

	static void removeAll(Usuario usuario) {
		UsuarioRegra.findAllByUsuario(usuario)*.delete()
	}

	static void removeAll(Regra regra) {
		UsuarioRegra.findAllByRegra(regra)*.delete()
	}

	static mapping = {
		id index: true, indexAttributes: [unique:true, dropDups:true]
		compoundIndex regra:1, usuario:1
		version false
	}
}
