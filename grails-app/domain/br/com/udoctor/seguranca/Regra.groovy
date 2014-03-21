package br.com.udoctor.seguranca;

import org.bson.types.ObjectId;

class Regra implements Serializable { 
	
	static mapWith = "mongo"
	
	ObjectId   id
	String     authority

	static mapping = {
		cache: "read-only"
		id index: true, indexAttributes: [unique:true, dropDups:true]
		authority index: true, indexAttributes: [unique:true, dropDups:true]
	}

	static constraints = {
		authority blank: false, unique: true
	}
}
