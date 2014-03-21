package br.com.udoctor.cadastro

import org.apache.commons.validator.DateValidator;
import br.com.udoctor.validacao.Localizacao;

import br.com.udoctor.geocoding.GeocodingService;
import org.codehaus.groovy.grails.commons.GrailsApplication;

import org.bson.types.ObjectId;

import org.codehaus.groovy.grails.web.context.ServletContextHolder;

import org.imgscalr.Scalr
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.springframework.web.multipart.MultipartFile;

import br.com.udoctor.tipo.Enum.CadastroType;
import br.com.udoctor.tipo.Enum.GeneroType;
import br.com.udoctor.tipo.Enum.GeocodingType;
import br.com.udoctor.tipo.Enum.AcaoArquivoType;
import br.com.udoctor.tipo.Enum.ArquivoType;
import br.com.udoctor.tipo.Enum.UsuarioType;

import br.com.udoctor.seguranca.Usuario;
import br.com.udoctor.usuario.Cliente;
import br.com.udoctor.usuario.todos.Endereco;
import br.com.udoctor.modelo.PlanoDeSaude;
import br.com.udoctor.usuario.prestador.Arquivo

class CadastroClienteService{
	
	static transactional = 'mongo'

    GeocodingService     geocodingService
    GrailsApplication    grailsApplication
	
	public Map salvarUsuario(Usuario usuario, final Map<String,Object> parametros, final CadastroType tipoCadastro, final MultipartFile arquivoAvatar){

        usuario                                = processarUsuario(usuario, parametros, tipoCadastro);
		Cliente cliente                        = processarCliente(usuario.cliente, parametros);
		Endereco endereco                      = processarEndereco(usuario.tipoUsuario, parametros);
        List<ObjectId> planosDeSaude           = processarPlanosDeSaude(parametros);
        Map<String,Object> retornoPersistencia = finalizarProcesso(usuario, cliente, endereco, planosDeSaude, arquivoAvatar, parametros.avatar);

		return [usuario: usuario, cliente: cliente, endereco: endereco, avatar: retornoPersistencia.avatar, planosDeSaude: planosDeSaude, salvoComSucesso: retornoPersistencia.salvoComSucesso, validacaoAvatar: retornoPersistencia.validacaoAvatar]
	}

    private Usuario processarUsuario(Usuario usuario, final Map<String,Object> parametros, final CadastroType tipoCadastro){

        usuario.tipoCadastro  = tipoCadastro
        usuario.nome 	      = parametros.nome
        usuario.sobrenome     = parametros.sobrenome
        usuario.email 	      = parametros.email.toLowerCase()

        usuario.validate()
        return usuario
    }

	private Cliente processarCliente(Cliente cliente, final Map<String,Object> parametros){
		
		// Caso um cliente ainda não esteja relacionado a um usuário
		if(!cliente){
			cliente = new Cliente()
			cliente.id = 0
		}
		
		if(parametros.genero && parametros.genero != "" && GeneroType.generoTypeValido(parametros.genero)){
			cliente.genero = parametros.genero
		}

		if(parametros.dataDeNascimento && parametros.dataDeNascimento != "" && DateValidator.getInstance().isValid(parametros.dataDeNascimento, "dd/MM/yyyy", true)){
			cliente.dataDeNascimento = Date.parse("dd/MM/yyyy", parametros.dataDeNascimento)
		}
		
		cliente.validate()
		return cliente
	}

	private Endereco processarEndereco(final UsuarioType tipoUsuario, final Map<String,Object> parametros){
		
		String localizacaoId

		Endereco endereco      = new Endereco()
		endereco.id            = 0
		endereco.tipoUsuario   = tipoUsuario // Para nao avaliar o campo descricaoLocal
		endereco.cep 		   = parametros["enderecos[0]"].cep
		endereco.logradouro    = parametros["enderecos[0]"].logradouro
		endereco.complemento   = parametros["enderecos[0]"].complemento
		endereco.bairro        = parametros["enderecos[0]"].bairro
		localizacaoId          = parametros["enderecos[0]"].cidade

        def (estado, cidade) = Localizacao.validar(localizacaoId)

        if(estado != null && cidade != null){
            endereco.estado = estado
            endereco.cidade = cidade
        }

		if(endereco.cep    || endereco.logradouro || endereco.complemento ||
		   endereco.bairro || endereco.cidade     || endereco.estado){
			endereco.validate()
		}else{
            endereco = null // Endereço não preenchido
        }
		return endereco
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

    private Endereco processarGeocoding(Endereco endereco){

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
        return endereco
    }

    private def processarAvatar(final String descricaoArquivo, final Arquivo avatarExistente, final MultipartFile arquivoAvatar, final def dadosAvatar){

        // Determina o que deve ser realizado com o avatar
        AcaoArquivoType avatarAcao
        if(avatarExistente && dadosAvatar && dadosAvatar.inputStream && dadosAvatar.inputStream instanceof ByteArrayInputStream){
            // Manter o avatar atual
            return avatarExistente
        }else if(dadosAvatar && dadosAvatar.inputStream && dadosAvatar.inputStream instanceof FileInputStream){
            avatarAcao = AcaoArquivoType.CRIAR
        }else{
            avatarAcao = AcaoArquivoType.DELETAR
        }

        def servletContext                 = ServletContextHolder.servletContext
        String caminhoArmazenamentoAvatar  = servletContext.getRealPath(grailsApplication.config.arquivo.diretorio.nome.avatar ?: 'avatar')

        // Cria diretório do avatar caso não exista
        File diretorio = new File(caminhoArmazenamentoAvatar)
        if (!diretorio.exists() && !diretorio.mkdir()){
            return "Ocorreu um erro ao tentar salvar seu avatar, tente mais tarde!"
        }

        if (avatarAcao == AcaoArquivoType.CRIAR && arquivoAvatar && !arquivoAvatar.isEmpty()){

            String tipoArquivo = arquivoAvatar.getContentType().split("/")[1]
            Long tamanhoLimite = grailsApplication.config.arquivo.tamanho.limite.avatar ?: 5000000l

            if((tipoArquivo.equals("jpeg")  ||
                tipoArquivo.equals("png")   ||
                tipoArquivo.equals("gif") ) && arquivoAvatar.size <= tamanhoLimite){

                String extensaoArquivo = arquivoAvatar.originalFilename.substring(arquivoAvatar.originalFilename.lastIndexOf("."))
                String nomeArquivo = UUID.randomUUID().toString() + '-' + descricaoArquivo

                Arquivo avatar         = new Arquivo()
                avatar.id              = 0
                avatar.tipoArquivo     = ArquivoType.AVATAR
                avatar.nomeOriginal    = arquivoAvatar.originalFilename
                avatar.nome            = nomeArquivo + '-avatar' + extensaoArquivo
                avatar.nomeThumbnail   = nomeArquivo + '-avatar-thumbnail' + extensaoArquivo
                avatar.tamanhoOriginal = arquivoAvatar.size
                avatar.extensao        = extensaoArquivo
                avatar.detalhes        = arquivoAvatar.getContentType()

                File arquivoOriginal  = new File("${caminhoArmazenamentoAvatar}/${avatar.nome}")
                arquivoAvatar.transferTo(arquivoOriginal)

                File arquivoThumbnail = new File("${caminhoArmazenamentoAvatar}/${avatar.nomeThumbnail}")
                BufferedImage thumbnail = Scalr.resize(ImageIO.read(arquivoOriginal), 170);
                ImageIO.write(thumbnail, extensaoArquivo[1..-1], arquivoThumbnail)

                return avatar

            }else{
                return "Seu avatar deve estar no formato JPEG, PNG, ou GIF com tamanho máximo de 5mb!"
            }
        }else if(avatarAcao == AcaoArquivoType.DELETAR && avatarExistente){
            /*File arquivoThumbnail = new File("${caminhoArmazenamentoAvatar}/${avatarExistente.nomeThumbnail}");
            File arquivoOriginal  = new File("${caminhoArmazenamentoAvatar}/${avatarExistente.nome}");
            arquivoThumbnail.delete()
            arquivoOriginal.delete()*/
        }
        return null
    }

    private String validarAvatar(final MultipartFile avatar){

        if (avatar && !avatar.isEmpty()){

            String tipoArquivo = avatar.getContentType().split("/")[1]
            Long tamanhoLimite = grailsApplication.config.arquivo.tamanho.limite.avatar ?: 5000000l

            if(!((tipoArquivo.equals("jpeg")  ||
                  tipoArquivo.equals("png")   ||
                  tipoArquivo.equals("gif")) && avatar.size <= tamanhoLimite)){

                return "Seu avatar deve estar no formato JPEG, PNG, ou GIF com tamanho máximo de 5mb"
            }
        }
        return [:]
    }

	private Map<String,Object> finalizarProcesso(Usuario usuario, Cliente cliente, Endereco endereco, List<ObjectId> planosDeSaude, MultipartFile arquivoAvatar, def dadosAvatar){
		
		Boolean salvoComSucesso = false
        String  validacaoAvatar
        Arquivo avatar

		if (!usuario.hasErrors() &&
			!cliente.hasErrors() &&
			(endereco == null || (endereco && !endereco.hasErrors()))){  // Verifica se o endereço é vazio, ou esta preenchido sem erros

			if(planosDeSaude){
				cliente.planosDeSaude = planosDeSaude
			}
            if (endereco){
				cliente.enderecos = new ArrayList<Endereco>()
				cliente.enderecos[0] = endereco // processarGeocoding(endereco)
            }

            def retorno = processarAvatar(usuario.nome + usuario.sobrenome, cliente.avatar, arquivoAvatar, dadosAvatar);
            if(retorno && retorno instanceof String){
                // Alerta no momento de validar o avatar
                validacaoAvatar = retorno
            }else{
                avatar = retorno
            }

            cliente.avatar = avatar
			usuario.cliente = cliente

			salvoComSucesso = usuario.save() ?: false

		}else{
            // Valida o avatar sem armazena-lo
            validacaoAvatar = validarAvatar(arquivoAvatar)
        }
		return [salvoComSucesso: salvoComSucesso, validacaoAvatar: validacaoAvatar, avatar: avatar]
	}
}
