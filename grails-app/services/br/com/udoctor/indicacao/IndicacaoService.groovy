package br.com.udoctor.indicacao

import org.bson.types.ObjectId;

import br.com.udoctor.email.assincrono.EmailAssincronoService;

import br.com.udoctor.tipo.Enum.MensagemEmailType;

import br.com.udoctor.usuario.ClienteIndicado;

class IndicacaoService {
	
	static transactional = 'mongo'
	
	EmailAssincronoService emailAssincronoService
	
    public def indicarAmigo(ObjectId usuarioId, String nome, String email, String nomeAmigo, String emailAmigo) {

		ClienteIndicado clienteIndicado = new ClienteIndicado()

		// Caso usuario(amigo) esteja loggado
		if(usuarioId){
			clienteIndicado.usuarioId = usuarioId
		}

		clienteIndicado.nome       = nome
		clienteIndicado.email      = email
		clienteIndicado.nomeAmigo  = nomeAmigo
		clienteIndicado.emailAmigo = emailAmigo
		
		ClienteIndicado clienteIndicadoExistente = ClienteIndicado.find(clienteIndicado)
		
		if(clienteIndicadoExistente || clienteIndicado.save()){		
			String view = "/templatesEmail/indicacao/convite"
			String assuntoPadrao  = "Seu amigo ${clienteIndicado.nome} recomendou-lhe o udoctor.com.br"

			String mensagem 
			String erro 
			try {
				emailAssincronoService.enviarEmail {
					para clienteIndicado.emailAmigo
					assunto assuntoPadrao
					def argsBody = [view:view, amigo: nome]
					bodyArgs argsBody, true // recebe um Map de atributos e caso a view utilize HTML, o segundo atributo � true
					tipoDeMensagem MensagemEmailType.CONVITE 
					dataDeInicioParaEnvio new Date(System.currentTimeMillis())    // Na pr�xima execu��o do job esse e-mail ser� processado
					dataDeExpiracao new Date(System.currentTimeMillis()+3600000)   // Deve ser enviado em 1 hora, default infinito
					imediatamente false
					prioridade 8
				}
				mensagem = "Convite enviado com sucesso!"
			} catch (Throwable t) {
				throw t
				erro = "Erro durante o envio de email de confirma��o!"
			}
			return [mensagem: mensagem, erro: erro]
		}
		return clienteIndicado
    }
}
