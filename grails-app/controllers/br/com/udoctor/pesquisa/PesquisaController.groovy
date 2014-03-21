package br.com.udoctor.pesquisa;

import br.com.udoctor.elasticsearch.ElasticSearchService;

class PesquisaController {
	
	//def elasticSearchService
	
	def index() {
		//cache "home_page"	
		
		//elasticSearchService.teste()
		
		if(chainModel){
			return[mensagem: chainModel?.mensagem, erro: chainModel?.erro]
		}		
	}
	def resultado(){}
}
