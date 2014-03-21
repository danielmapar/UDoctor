package br.com.udoctor.formatacao

class PaginacaoTagLib {
	/**
	 *
	 * @attr total REQUERIDO O n�mero total de registros para paginar
	 * @attr action o nome da action para utilzar na constru��o do link, caso n�o especificado a action atual ser� linkada
	 * @attr controller o nome do controller para utilzar na constru��o do link, caso n�o especificado o controller atual ser� linkado
	 * @attr id O id que ser� utilizado no link
	 * @attr params Um map contendo parametros da request
	 * @attr omitAnt Quando n�o apresentar o bot�o voltar (caso true, o bot�o Anterior n�o ser� apresentado)
	 * @attr omitProx Quando n�o apresentar o bot�o pr�ximo (caso true, o bot�o Pr�ximo n�o ser� apresentado)
	 * @attr omitPrim Quando n�o apresentar o primeiro link (caso true, o primeiro link n�o ser� apresentado)
	 * @attr omitUlt Quando n�o apresentar o �ltimo link (caso true, o �ltimo link n�o ser� apresentado)
	 * @attr max O n�mero de registros apresentados por p�gina (padr�o igual a 10). Utilizado SOMENTE se params.max est� v�zio
	 * @attr passosmax O n�mero de passos (links) apresentas por pagina��o (padr�o igual a 10). Utilizado SOMENTE se params.passosmax estiver vazio
	 * @attr offset Utilizado SOMENTE SE params.offset estiver v�zio
	 */
	Closure paginacao = { attrs ->
		def writer = out
		if (attrs.total == null) {
			throwTagError("Tag [paginacao] precisa receber o atributo [total]")
		}

		def total = attrs.int('total') ?: 0
		def offset = params.int('offset') ?: 0
		def max = params.int('max')
		def passosmax = (attrs.int('passosmax') ?: 10)

		if (!offset) offset = (attrs.int('offset') ?: 0)
		if (!max) max = (attrs.int('max') ?: 10)

		def linkParams = [:]
		if (attrs.params) linkParams.putAll(attrs.params)
		linkParams.offset = offset - max
		linkParams.max = max
		//if (params.sort) linkParams.sort = params.sort
		//if (params.order) linkParams.order = params.order

		def linkTagAttrs = [:]
	
		if(attrs.action) {
			linkTagAttrs.action = attrs.action
		}
		if (attrs.controller) {
			linkTagAttrs.controller = attrs.controller
		}
		if (attrs.id != null) {
			linkTagAttrs.id = attrs.id
		}
		linkTagAttrs.params = linkParams

		// determinar variaveis de pagina��o
		def passos = passosmax > 0
		int passoAtual = (offset / max) + 1
		int primeiroPasso = 1
		int ultimoPasso = Math.round(Math.ceil(total / max))

		writer << '<div class="pagination">'
		writer << '<ul>'
		
		// apresentar link anterior quando n�o estiver no primeiroPasso, a n�o ser que omitAnt seja true
		if (passoAtual > primeiroPasso && !attrs.boolean('omitAnt')) {
			linkParams.offset = offset - max
			writer << '<li class="previous">'
			writer <<  "<a href=\"/udoctor/${linkTagAttrs.controller}/${linkTagAttrs.action}?"
			Integer tamanhoMap = linkParams.size()
			linkParams.each{ chave, valor -> 
				writer <<  "${chave}=${valor}"
				if(tamanhoMap > 1) writer << "&amp;"
				tamanhoMap--
			}
			writer <<  '" title="Anterior">'
			writer <<  '<i class="icon-chevron-left"></i>'
			writer <<  "</a></li>"
		}
		
		// Apresenta os passos(links) quando a variavel passos for igual a true e ultimoPasso n�o for igual a primeiroPasso
		if (passos && ultimoPasso > primeiroPasso) {
			
			// determinar variaveis de inicio e final da paginacao
			int passoInicial = passoAtual - Math.round(passosmax / 2) + (passosmax % 2)
			int passoFinal = passoAtual + Math.round(passosmax / 2) - 1

			if (passoInicial < primeiroPasso) {
				passoInicial = primeiroPasso
				passoFinal = passosmax
			}
			if (passoFinal > ultimoPasso) {
				passoInicial = ultimoPasso - passosmax + 1
				if (passoInicial < primeiroPasso) {
					passoInicial = primeiroPasso
				}
				passoFinal = ultimoPasso
			}
			// apresenta link primeiroPasso quando passoInicial n�o for o primeiroPasso
			if (passoInicial > primeiroPasso && !attrs.boolean('omitPrim')) {
				linkParams.offset = 0
				writer << "<li>"
				writer << "<a href=\"/udoctor/${linkTagAttrs.controller}/${linkTagAttrs.action}?"
				Integer tamanhoMap = linkParams.size()
				linkParams.each{ chave, valor ->
					writer <<  "${chave}=${valor}"
					if(tamanhoMap > 1) writer <<  "&amp;"
					tamanhoMap--
				}
				writer << "\">${primeiroPasso.toString()}</a></li>"
			}
			
			//mostrar um "gap" se passoInicial n�o estiver localizado imediatamente depois do primeiroPasso e se omitPrim ou omitAnt igual a false
			if (passoInicial > primeiroPasso+1 && (!attrs.boolean('omitPrim') || !attrs.boolean('omitAnt')) ) {
				writer << '<li><a href="#">..</a></li>'
			}
			// apresentar passos(links) da paginacao
			(passoInicial..passoFinal).each { i ->
				if (passoAtual == i) {
					writer << "<li class=\"active\"><a href=\"#\">${i}</a></li>"
				}
				else {
					linkParams.offset = (i - 1) * max
					writer << "<li>"
					writer << "<a href=\"/udoctor/${linkTagAttrs.controller}/${linkTagAttrs.action}?"
					Integer tamanhoMap = linkParams.size()
					linkParams.each{ chave, valor ->
						writer <<  "${chave}=${valor}"
						if(tamanhoMap > 1) writer <<  "&amp;"
						tamanhoMap--
					}
					writer << "\">${i.toString()}</a></li>"
				}
			}
			
			//mostrar um "gap" se passoInicial n�o estiver imediatamente antes do primeiroPasso e se omitUlt ou omitProx igual a false
			if (passoFinal+1 < ultimoPasso && (!attrs.boolean('omitUlt') || !attrs.boolean('omitProx'))) {
				writer << '<li><a href="#">..</a></li>'
			}
			// apresentar link ultimoPasso quando passoFinal n�o for ultimoPasso
			if (passoFinal < ultimoPasso && !attrs.boolean('omitUlt')) {
				linkParams.offset = (ultimoPasso - 1) * max
				writer << "<li>"
				writer << "<a href=\"/udoctor/${linkTagAttrs.controller}/${linkTagAttrs.action}?"
				Integer tamanhoMap = linkParams.size()
				linkParams.each{ chave, valor ->
					writer <<  "${chave}=${valor}"
					if(tamanhoMap > 1) writer <<  "&amp;"
					tamanhoMap--
				}
				writer << "\">${ultimoPasso.toString()}</a></li>"
			}
		}
		// apresentar pr�ximo link quando n�o for ultimoPasso a n�o ser que omitProx igual a true
		if (passoAtual < ultimoPasso && !attrs.boolean('omitProx')) {
			linkParams.offset = offset + max
			writer << '<li class="next">'
			writer <<  "<a href=\"/udoctor/${linkTagAttrs.controller}/${linkTagAttrs.action}?"
			Integer tamanhoMap = linkParams.size()
			linkParams.each{ chave, valor ->
				writer <<  "${chave}=${valor}"
				if(tamanhoMap > 1) writer << "&amp;"
				tamanhoMap--
			}
			writer <<  '" title="Pr&oacute;xima">'
			writer <<  '<i class="icon-chevron-right"></i>'
			writer <<  "</a></li>"
		}
		writer << "</ul>"
	}
}
