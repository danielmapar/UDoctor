package br.com.udoctor.formatacao;

import org.apache.commons.lang.StringUtils;

class CnpjTagLib {
	
	def cnpj = { attrs ->
		
		StringUtils stringUtils = new StringUtils();
		String cnpj 
		
		if(attrs.value){
			cnpj = stringUtils.substring(attrs.value, 0, 2) 
			cnpj += '.' 
			cnpj += stringUtils.substring(attrs.value, 2, 5)  
			cnpj += '.'
			cnpj += stringUtils.substring(attrs.value, 5, 8)  
			cnpj += '/'
			cnpj += stringUtils.substring(attrs.value, 8, 12) 
			cnpj += '.' 
			cnpj += stringUtils.substring(attrs.value, 12, 14)
		}
		
		out << cnpj
	}
	
}
