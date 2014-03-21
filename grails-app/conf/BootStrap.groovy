import br.com.udoctor.elasticsearch.ElasticSearchService;
import br.com.udoctor.modelo.Cidade;
import br.com.udoctor.modelo.Estado;
import br.com.udoctor.modelo.PlanoDeSaude;
import br.com.udoctor.modelo.Profissao;
import br.com.udoctor.seguranca.Regra;
import br.com.udoctor.tipo.Enum.StatusType;

class BootStrap {
	
	def grailsApplication
	ElasticSearchService elasticSearchService
	
    def init = { servletContext ->
		
		elasticSearchService.iniciar()
		
        Regra.findByAuthority("ROLE_CLIENTE") ?: new Regra(authority: "ROLE_CLIENTE").save(flush: true)
        Regra.findByAuthority("ROLE_PROFISSIONAL") ?: new Regra(authority: "ROLE_PROFISSIONAL").save(flush: true)
        Regra.findByAuthority("ROLE_INSTITUICAO") ?: new Regra(authority: "ROLE_INSTITUICAO").save(flush: true)
        Regra.findByAuthority("ROLE_ADMIN") ?: new Regra(authority: "ROLE_ADMIN").save(flush: true)

        PlanoDeSaude.findAllByNomeAndStatus("UNIMED",StatusType.ATIVO) ?: new PlanoDeSaude(status: StatusType.ATIVO, nome: "UNIMED").save(flush: true)
        PlanoDeSaude.findAllByNomeAndStatus("GOLDENCROSS",StatusType.ATIVO) ?: new PlanoDeSaude(status: StatusType.ATIVO, nome: "GOLDENCROSS").save(flush: true)
        PlanoDeSaude.findAllByNomeAndStatus("AMIL",StatusType.ATIVO) ?: new PlanoDeSaude(status: StatusType.ATIVO, nome: "AMIL").save(flush: true)


        if(!Estado.findAllByNomeAndStatus("Minas Gerais",StatusType.ATIVO)){
            Estado estado =  new Estado(status: StatusType.ATIVO, nome: "Minas Gerais").save(flush: true)
            List<Cidade> cidades = new ArrayList<Cidade>()
            Cidade cidade = new Cidade()
            cidade.id = 0
            cidade.status = StatusType.ATIVO
            cidade.nome = "Belo Horizonte"
            cidades.add(cidade)
            estado.cidades = cidades
            estado.save(flush: true)
        }

        if(!Estado.findAllByNomeAndStatus("Sao Paulo",StatusType.ATIVO)){
            Estado estado =  new Estado(status: StatusType.ATIVO, nome: "Sao Paulo").save(flush: true)
            List<Cidade> cidades = new ArrayList<Cidade>()
            Cidade cidade = new Cidade()
            cidade.id = 0
            cidade.status = StatusType.ATIVO
            cidade.nome = "Sao Paulo"
            cidades.add(cidade)
            estado.cidades = cidades
            estado.save(flush: true)
        }
        Profissao.findAllByNomeAndStatus("Urologista",StatusType.ATIVO) ?: new Profissao(status: StatusType.ATIVO, nome: "Urologista").save()

    }
    def destroy = {
		elasticSearchService.finalizar()
    }
}

