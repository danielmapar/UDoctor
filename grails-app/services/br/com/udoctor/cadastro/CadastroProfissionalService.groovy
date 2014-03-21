package br.com.udoctor.cadastro

import org.apache.commons.validator.DateValidator
import br.com.udoctor.validacao.Localizacao;

import br.com.udoctor.elasticsearch.ElasticSearchService;
import br.com.udoctor.geocoding.GeocodingService;
import org.codehaus.groovy.grails.commons.GrailsApplication;

import org.bson.types.ObjectId;

import org.codehaus.groovy.grails.web.context.ServletContextHolder;

import org.apache.commons.lang.StringUtils;

import org.imgscalr.Scalr;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.springframework.web.multipart.MultipartFile;

import br.com.udoctor.tipo.Enum.AcaoArquivoType;
import br.com.udoctor.tipo.Enum.CadastroType;
import br.com.udoctor.tipo.Enum.GeneroType;
import br.com.udoctor.tipo.Enum.RankType;
import br.com.udoctor.tipo.Enum.ArquivoType;
import br.com.udoctor.tipo.Enum.UsuarioType;
import br.com.udoctor.tipo.Enum.GeocodingType;
import br.com.udoctor.tipo.Enum.StatusType;

import br.com.udoctor.seguranca.Usuario;
import br.com.udoctor.usuario.Profissional;
import br.com.udoctor.usuario.prestador.Link;
import br.com.udoctor.usuario.prestador.Telefone;
import br.com.udoctor.usuario.todos.Endereco;
import br.com.udoctor.usuario.prestador.Arquivo;

import br.com.udoctor.modelo.PlanoDeSaude;
import br.com.udoctor.modelo.Profissao;

class CadastroProfissionalService{
	
	static transactional = 'mongo'
	
	GrailsApplication    grailsApplication
	ElasticSearchService elasticSearchService
    GeocodingService     geocodingService
	
	public Map<String,Object> salvarUsuario(Usuario usuario, final Map<String,Object> parametros, final CadastroType tipoCadastro, final Map<String,MultipartFile> arquivos){

		usuario                      = processarUsuario(usuario, parametros, tipoCadastro);
		Profissional profissional    = processarProfissional(usuario.profissional, parametros);
		
		List<Endereco> enderecos
		Boolean erroValidacaoEnderecos
		(enderecos, erroValidacaoEnderecos) = processarEnderecos(usuario.tipoUsuario, parametros);
		
		List<Link> links
		Boolean erroValidacaoLinks
		(links, erroValidacaoLinks) = processarLinks(parametros);
		
		List<Telefone> telefones
		Boolean erroValidacaoTelefones
		(telefones, erroValidacaoTelefones) = processarTelefones(parametros);

        List<ObjectId> planosDeSaude = processarPlanosDeSaude(parametros);

		Map<String,Object> retornoPersistencia = finalizarProcesso(usuario, profissional, enderecos, links, telefones, planosDeSaude, arquivos, parametros.avatar, parametros.certificadoCRM, erroValidacaoEnderecos, erroValidacaoLinks, erroValidacaoTelefones);
		
		return [usuario: usuario, profissional: profissional, enderecos: enderecos, links: links, telefones: telefones, planosDeSaude: planosDeSaude, arquivos: retornoPersistencia.arquivos, validacaoArquivos: retornoPersistencia.validacaoArquivos, salvoComSucesso: retornoPersistencia.salvoComSucesso]
	}

    private Usuario processarUsuario(Usuario usuario, final Map<String,Object> parametros, final CadastroType tipoCadastro){

        usuario.tipoCadastro  = tipoCadastro
        usuario.nome 	      = parametros.nome
        usuario.sobrenome     = parametros.sobrenome
        usuario.email 	      = parametros.email.toLowerCase()
        usuario.gerarUrlProfile(parametros.urlProfile)

        usuario.validate()
        return usuario
    }

	private Profissional processarProfissional(Profissional profissional, final Map<String,Object> parametros){

		// Caso um profissional ainda não esteja relacionado a um usuário
		if(!profissional){
			profissional = new Profissional()
			profissional.id = 0
		}
		
		profissional.codigoConselhoRegional = parametros.codigoConselhoRegional
		profissional.especialidade          = parametros.especialidade
		profissional.patologia              = parametros.patologia
		profissional.curriculo              = parametros.curriculo
		profissional.rank                   = RankType.NAO_RANKEADO
		
		if(parametros.genero && parametros.genero != "" && GeneroType.generoTypeValido(parametros.genero)){
			profissional.genero = parametros.genero
		}		
		
		if(parametros.profissao && parametros.profissao != "" && ObjectId.isValid(parametros.profissao)){
			ObjectId profissao = new ObjectId(parametros.profissao)
			if(Profissao.get(profissao)){
				profissional.profissao = profissao
			}
		}
		
		if(parametros.dataDeNascimento && parametros.dataDeNascimento != "" && DateValidator.getInstance().isValid(parametros.dataDeNascimento, "dd/MM/yyyy", true)){
			profissional.dataDeNascimento = Date.parse("dd/MM/yyyy", parametros.dataDeNascimento)
		}
		
		profissional.validate()
		return profissional
	}

	private List processarEnderecos(final UsuarioType tipoUsuario, final Map<String,Object> parametros){

        Boolean erroValidacaoEnderecos = false
		List<Endereco> enderecos = new ArrayList<Endereco>()
		
		String localizacaoId
		for(i in 0..4){
			
			if(parametros["enderecos["+i+"]"] && parametros["enderecos["+i+"]"].ativo == 'X'){

				Endereco endereco       = new Endereco()
				endereco.id      		= i
				endereco.tipoUsuario    = tipoUsuario // Para avaliar o campo descricaoLocal
				endereco.cep            = parametros["enderecos["+i+"]"].cep
				endereco.logradouro     = parametros["enderecos["+i+"]"].logradouro
				endereco.complemento    = parametros["enderecos["+i+"]"].complemento
				endereco.bairro         = parametros["enderecos["+i+"]"].bairro
				localizacaoId           = parametros["enderecos["+i+"]"].cidade
				endereco.descricaoLocal = parametros["enderecos["+i+"]"].descricaoLocal

                def (estado, cidade) = Localizacao.validar(localizacaoId)

				if(estado != null && cidade != null){
					endereco.estado = estado
					endereco.cidade = cidade
				}

				endereco.validate()
				erroValidacaoEnderecos = endereco.hasErrors()
				enderecos.add(endereco)
			}else{
				break
			}
		} 
		return [enderecos, erroValidacaoEnderecos]
	}

	private List processarLinks(final Map<String,Object> parametros){

        Boolean erroValidacaoLinks = false
		List<Link> links = null
		
		for(i in 0..9){
			
			if(parametros["links["+i+"]"] && parametros["links["+i+"]"].ativo == 'X'){

                if(!links){
                    links = new ArrayList<Link>()
                }

				Link link 		   = new Link()
				link.id      	   = i
				link.nome  		   = parametros["links["+i+"]"].nome
				link.descricaoLink = parametros["links["+i+"]"].descricaoLink

				link.validate()
				erroValidacaoLinks = link.hasErrors()
                links.add(link)
			}else{
				break
			}
		}
        // Caso somente o primeiro esteja preenchido
        if (links && links.size() == 1 && (links[0].nome == "" || !links[0].nome) &&
           (links[0].descricaoLink == "" || !links[0].descricaoLink)){
            links = null
        }

		return [links, erroValidacaoLinks]
	}

	private List processarTelefones(final Map<String,Object> parametros){

        Boolean erroValidacaoTelefones = false
		List<Telefone> telefones = new ArrayList<Telefone>()

        StringUtils stringUtils = new StringUtils()

		for(i in 0..9){

			if(parametros["telefones["+i+"]"] && parametros["telefones["+i+"]"].ativo == 'X'){

				Telefone telefone 		= new Telefone()
				telefone.id             = i
				telefone.numeroCompleto = parametros["telefones["+i+"]"].numeroCompleto
				
				if(telefone.numeroCompleto != "" && telefone.numeroCompleto.size() == 14){
					telefone.prefixo = stringUtils.substring(telefone.numeroCompleto, 1 , 3)
					telefone.numero  = stringUtils.substring(telefone.numeroCompleto, 5 , 9)
					telefone.numero += stringUtils.substring(telefone.numeroCompleto, 10 , 14)
				}
				telefone.validate()
				erroValidacaoTelefones = telefone.hasErrors() 
				telefones.add(telefone)
			}else{
				break
			}
		}
		return [telefones, erroValidacaoTelefones]
	}

    private List<ObjectId> processarPlanosDeSaude(final Map<String,Object> parametros){

        if(parametros.planosDeSaude){
            List<ObjectId> planosDeSaude = new ArrayList<ObjectId>()
            if(parametros.planosDeSaude instanceof String && parametros.planosDeSaude != "" && ObjectId.isValid(parametros.planosDeSaude)){
                ObjectId planoDeSaudeId = new ObjectId(parametros.planosDeSaude)
                if(PlanoDeSaude.get(planoDeSaudeId)){
                    planosDeSaude.add(planoDeSaudeId)
                }
            }else{
                for(String planoDeSaude : parametros.planosDeSaude){
                    if(planoDeSaude != "" && ObjectId.isValid(planoDeSaude)){
                        ObjectId planoDeSaudeId = new ObjectId(planoDeSaude)
                        if(PlanoDeSaude.get(planoDeSaudeId)){
                            planosDeSaude.add(planoDeSaudeId)
                        }
                    }
                }
            }
            return planosDeSaude
        }
        return null
    }

    private List<Endereco> processarGeocoding(List<Endereco> enderecos){

        for(int i  = 0; i < enderecos.size(); i++){
            Endereco endereco = enderecos.get(i)
            String query = endereco.logradouro + ', ' + endereco.complemento + ' - ' + endereco.bairro + ', ' + endereco.cidade + ' - ' + endereco.estado + ', ' + endereco.cep
            Map retorno = geocodingService.buscaJSON(query)

            if(retorno && retorno.status == "OK"){
                endereco.longitude = retorno.lng
                endereco.latitude  = retorno.lat
                endereco.statusGeoconding = retorno.status
            }else if (retorno && GeocodingType.geocodingTypeValido(retorno.status)){
                endereco.statusGeoconding = retorno.status
            }else{
                endereco.statusGeoconding = GeocodingType.OTHER_ERROR
            }
            enderecos[i] = endereco
        }
        return enderecos
    }

    private Map<String,Object> processarArquivos(final String descricaoArquivo, final List<Arquivo> arquivosExistentes, final Map<String,MultipartFile> arquivos, final AcaoArquivoType certificadoCRMAcao, final AcaoArquivoType avatarAcao){

        // Caso não haja necessidade de criar, ou deletar os arquivos, sai do processamento
        if(arquivosExistentes && avatarAcao == AcaoArquivoType.MANTER && certificadoCRMAcao == AcaoArquivoType.MANTER){
            return null
        }

        // Mensagens de erro, alerta referentes ao avatar e certificadoCRM
        Map<String,String> validacaoArquivos = new HashMap<String,String>()

        // Nova lista de arquivos
        List<Arquivo> novosArquivos = new ArrayList<Arquivo>()

        def servletContext                         = ServletContextHolder.servletContext
        String caminhoArmazenamentoAvatar          = servletContext.getRealPath(grailsApplication.config.arquivo.diretorio.nome.avatar ?: 'avatar')
        String caminhoArmazenamentoCertificadoCRM  = servletContext.getRealPath(grailsApplication.config.arquivo.diretorio.nome.certificadoCRM ?: 'certificadoCRM')

        // Cria diretorios
        File diretorio
        diretorio = new File(caminhoArmazenamentoAvatar)
        if (!diretorio.exists() && !diretorio.mkdir()){
            validacaoArquivos.put("erro", "Ocorreu um erro ao tentar salvar seu avatar, tente mais tarde!")
            return [novosArquivos : novosArquivos, validacaoArquivos : validacaoArquivos]
        }
        diretorio = new File(caminhoArmazenamentoCertificadoCRM)
        if (!diretorio.exists() && !diretorio.mkdir()){
            validacaoArquivos.put("erro", "Ocorreu um erro ao tentar salvar seu certificado do CRM, tente mais tarde!")
            return [novosArquivos : novosArquivos, validacaoArquivos : validacaoArquivos]
        }

        // Valida e realiza upload do certificadoCRM
        if (certificadoCRMAcao == AcaoArquivoType.CRIAR && arquivos.certificadoCRM && !arquivos.certificadoCRM.isEmpty()) {

            String tipoArquivo = arquivos.certificadoCRM.getContentType().split("/")[1]
            String extensaoArquivo = arquivos.certificadoCRM.originalFilename.substring(arquivos.certificadoCRM.originalFilename.lastIndexOf("."))
            Long tamanhoLimite = grailsApplication.config.arquivo.tamanho.limite.certificadoCRM ?: 1000000l

            // Valida extensão e tamaho do certificadoCRM
            if (((tipoArquivo.equals("vnd.openxmlformats-officedocument.wordprocessingml.document") ||
                  tipoArquivo.equals("msword") ||
                  tipoArquivo.equals("pdf")) &&
                    arquivos.certificadoCRM.size <= tamanhoLimite)) {

                Arquivo certificadoCRM = new Arquivo()
                certificadoCRM.id = 0
                certificadoCRM.tipoArquivo = ArquivoType.CERTIFICADO_CRM
                certificadoCRM.nomeOriginal = arquivos.certificadoCRM.originalFilename
                certificadoCRM.nome = UUID.randomUUID().toString() + '-' + descricaoArquivo + '-certificadoCRM' + extensaoArquivo
                certificadoCRM.tamanhoOriginal = arquivos.certificadoCRM.size
                certificadoCRM.extensao = extensaoArquivo
                certificadoCRM.detalhes = arquivos.certificadoCRM.getContentType()

                File arquivoOriginal = new File("${caminhoArmazenamentoCertificadoCRM}/${certificadoCRM.nome}")
                arquivos.certificadoCRM.transferTo(arquivoOriginal)
                novosArquivos.add(certificadoCRM)

            } else {
                validacaoArquivos.put("erro", "Seu certificado do CRM deve estar no formato Word, ou PDF com tamanho máximo de 10mb!")
            }
        } else if (certificadoCRMAcao == AcaoArquivoType.MANTER && arquivosExistentes && arquivosExistentes.size() >= 1) {
            // Adiciona certificadoCRM já existente
            novosArquivos.add(arquivosExistentes.get(0))
        } else {
            validacaoArquivos.put("erro", "Favor selecionar seu certificado CRM para upload!")
        }

        // Valida e realiza upload do avatar
        if (avatarAcao == AcaoArquivoType.CRIAR && arquivos.avatar && !arquivos.avatar.isEmpty()){

            String tipoArquivo = arquivos.avatar.getContentType().split("/")[1]
            Long tamanhoLimite = grailsApplication.config.arquivo.tamanho.limite.avatar ?: 5000000l

            if((tipoArquivo.equals("jpeg")  ||
                    tipoArquivo.equals("png")   ||
                    tipoArquivo.equals("gif") ) && arquivos.avatar.size <= tamanhoLimite){

                // Caso existam erros de validação no certificadoCRM o sistema não deve salvar o avatar
                if(validacaoArquivos.isEmpty()){

                    String extensaoArquivo = arquivos.avatar.originalFilename.substring(arquivos.avatar.originalFilename.lastIndexOf("."))
                    String nomeArquivo = UUID.randomUUID().toString() + '-' + descricaoArquivo

                    Arquivo avatar         = new Arquivo()
                    avatar.id              = 1
                    avatar.tipoArquivo     = ArquivoType.AVATAR
                    avatar.nomeOriginal    = arquivos.avatar.originalFilename
                    avatar.nome            = nomeArquivo + '-avatar' + extensaoArquivo
                    avatar.nomeThumbnail   = nomeArquivo + '-avatar-thumbnail' + extensaoArquivo
                    avatar.tamanhoOriginal = arquivos.avatar.size
                    avatar.extensao        = extensaoArquivo
                    avatar.detalhes        = arquivos.avatar.getContentType()

                    File arquivoOriginal  = new File("${caminhoArmazenamentoAvatar}/${avatar.nome}")
                    arquivos.avatar.transferTo(arquivoOriginal)

                    File arquivoThumbnail = new File("${caminhoArmazenamentoAvatar}/${avatar.nomeThumbnail}")
                    BufferedImage thumbnail = Scalr.resize(ImageIO.read(arquivoOriginal), 170);
                    ImageIO.write(thumbnail, extensaoArquivo[1..-1], arquivoThumbnail)

                    novosArquivos.add(avatar)
                }
            }else{
                validacaoArquivos.put("alerta", "Seu avatar � inv�lido, ele deve estar no formato JPEG, PNG, ou GIF com tamanho m�ximo de 5mb!")
            }
        }else if(avatarAcao == AcaoArquivoType.MANTER && arquivosExistentes && arquivosExistentes.size() == 2){
            // Adiciona avatar já existente
            novosArquivos.add(arquivosExistentes.get(1))
        }else if(avatarAcao == AcaoArquivoType.DELETAR && arquivosExistentes && arquivosExistentes.size() == 2){
            /*File arquivoThumbnail = new File("${caminhoArmazenamentoAvatar}/${arquivosExistentes.get(1).nomeThumbnail}");
            File arquivoOriginal  = new File("${caminhoArmazenamentoAvatar}/${arquivosExistentes.get(1).nome}");
            arquivoThumbnail.delete()
            arquivoOriginal.delete()*/
        }

        return [novosArquivos : novosArquivos, validacaoArquivos : validacaoArquivos]
    }

    private Map<String,String> validarAvatar(final MultipartFile avatar, final AcaoArquivoType avatarAcao, final List<Arquivo> arquivosExistentes){

        if(arquivosExistentes && avatarAcao == AcaoArquivoType.MANTER){
            return [:]
        }

        if (avatar && !avatar.isEmpty()){

            String tipoArquivo = avatar.getContentType().split("/")[1]
            Long tamanhoLimite = grailsApplication.config.arquivo.tamanho.limite.avatar ?: 5000000l

            if(!((tipoArquivo.equals("jpeg")  ||
                    tipoArquivo.equals("png")   ||
                    tipoArquivo.equals("gif")) && avatar.size <= tamanhoLimite)){

                return ["alerta": "Seu avatar deve estar no formato JPEG, PNG, ou GIF com tamanho máximo de 5mb"]
            }
        }
        return [:]
    }

    private Map<String,String> validarCertificadoCRM(final MultipartFile certificadoCRM, final AcaoArquivoType certificadoCRMAcao, final List<Arquivo> arquivosExistentes){

        if(arquivosExistentes && certificadoCRMAcao == AcaoArquivoType.MANTER){
            return [:]
        }

        if (certificadoCRM && !certificadoCRM.isEmpty()){

            String tipoArquivo = certificadoCRM.getContentType().split("/")[1]
            Long tamanhoLimite = grailsApplication.config.arquivo.tamanho.limite.certificadoCRM ?: 1000000l

            // Valida extensao e tamaho do certificadoCRM
            if(!(((tipoArquivo.equals("vnd.openxmlformats-officedocument.wordprocessingml.document") ||
                    tipoArquivo.equals("msword") ||
                    tipoArquivo.equals("pdf")) &&
                    certificadoCRM.size <= tamanhoLimite))){
                return ["erro": "Seu certificado do CRM deve estar no formato Word, ou PDF com tamanho máximo de 10mb!"]
            }else{
                return[:]
            }
        }else{
            return ["erro": "Favor selecione seu certificado CRM para upload!"]
        }
    }

	private Map<String,Object> finalizarProcesso(Usuario usuario, Profissional profissional, List<Endereco> enderecos, List<Link> links, List<Telefone> telefones, List<ObjectId> planosDeSaude, final Map<String,MultipartFile> arquivos, final def dadosAvatar, final def dadosCertificadoCRM, final Boolean erroValidacaoEnderecos, final Boolean erroValidacaoLinks, final Boolean erroValidacaoTelefones){
		
		Boolean salvoComSucesso = false

        // Determina o que deve ser realizado com os arquivos de avatar e certificadoCRM
        AcaoArquivoType avatarAcao
        if(dadosAvatar && dadosAvatar.inputStream && dadosAvatar.inputStream instanceof ByteArrayInputStream){
            avatarAcao = AcaoArquivoType.MANTER
        }else if(dadosAvatar && dadosAvatar.inputStream && dadosAvatar.inputStream instanceof FileInputStream){
            avatarAcao = AcaoArquivoType.CRIAR
        }else{
            avatarAcao = AcaoArquivoType.DELETAR
        }

        AcaoArquivoType certificadoCRMAcao
        if(dadosCertificadoCRM && dadosCertificadoCRM.inputStream && dadosCertificadoCRM.inputStream instanceof ByteArrayInputStream){
            certificadoCRMAcao = AcaoArquivoType.MANTER
        }else if(dadosCertificadoCRM && dadosCertificadoCRM.inputStream && dadosCertificadoCRM.inputStream instanceof FileInputStream){
            certificadoCRMAcao = AcaoArquivoType.CRIAR
        }else{
            certificadoCRMAcao = AcaoArquivoType.DELETAR
        }

		if (!usuario.hasErrors()      &
			!profissional.hasErrors() &
			!erroValidacaoEnderecos   &
			!erroValidacaoLinks       &
			!erroValidacaoTelefones){

			profissional.planosDeSaude = planosDeSaude
			profissional.enderecos     = enderecos //processarGeocoding(enderecos)
			profissional.telefones     = telefones
			profissional.links         = links

            // Processar arquivos do formulário
            def retorno = processarArquivos(usuario.urlProfile, profissional.arquivos, arquivos, certificadoCRMAcao, avatarAcao);
            if(retorno && retorno.validacaoArquivos.isEmpty()){
                profissional.arquivos  = retorno.novosArquivos
            }else if(retorno && !retorno.validacaoArquivos.isEmpty()){
                return [salvoComSucesso: salvoComSucesso, arquivos: profissional.arquivos, validacaoArquivos: retorno.validacaoArquivos]
            }
            usuario.profissional = profissional
            usuario.status       = StatusType.ATIVO

            salvoComSucesso = usuario.save() ?: false
			
			if (salvoComSucesso == true){
				elasticSearchService.indexar(usuario)
			}
			
			return [salvoComSucesso: salvoComSucesso, arquivos: profissional.arquivos]
			
        }else{

			Map<String,String> validacaoArquivosTemporarios = new HashMap<String,String>()
            validacaoArquivosTemporarios.putAll(validarCertificadoCRM(arquivos.certificadoCRM, certificadoCRMAcao, profissional.arquivos))
			validacaoArquivosTemporarios.putAll(validarAvatar(arquivos.avatar, avatarAcao, profissional.arquivos))

			return [salvoComSucesso: salvoComSucesso, arquivos: profissional.arquivos, validacaoArquivos: validacaoArquivosTemporarios]
		}
	}
}
