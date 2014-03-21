package br.com.udoctor.administrador

import br.com.udoctor.modelo.Estado;

class AdministradorController {

	AdministradorService administradorService
	
	def cidades(Integer max, Integer offset){
				
		max = max ?: 10
		offset = offset ?: 0
		String estadoSelecionado = params.estadoSelecionado ?: null
		
		// Caso usu�rio clique em ativar/desativar
		if(params.ativar || params.desativar){
			administradorService.atualizarStatusCidade(params, max)
		}
		
		// Estados do dropdown
		List<Estado> estadosFiltro = Estado.list()
		
		// Estados e cidades da p�gina
		Map retorno = administradorService.listaDeEstadosPagina(estadoSelecionado, offset, max)
		List<Estado> estadosPagina  = retorno.estadosPagina 
		Integer quantidadeDeCidades = retorno.quantidadeDeCidades // Quantidade total de cidades no BD 
		
		return[estadosFiltro: estadosFiltro, estadosPagina: estadosPagina, estadoSelecionado: estadoSelecionado, quantidadeDeCidades: quantidadeDeCidades]
		
	}
	
	def cadastroCidade(){}
}
