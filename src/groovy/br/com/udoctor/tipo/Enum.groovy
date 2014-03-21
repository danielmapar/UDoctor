package br.com.udoctor.tipo

class Enum {
	
	public enum UsuarioType{
		CLIENTE('CLIENTE'),
		INSTITUICAO('INSTITUICAO'), 
		PROFISSIONAL('PROFISSIONAL'), 
		ADMINISTRADOR('ADMINISTRADOR')
		
		final String valor;
		
		UsuarioType(String valor){
			this.valor = valor;
		}
		
		String toString(){
			return valor;
		}
		
		Boolean UsuarioType(UsuarioType usuario){
			return usuario == this
		}

		static lista() {
			[CLIENTE, INSTITUICAO, PROFISSIONAL, ADMINISTRADOR]
		}
		
		static Boolean usuarioTypeValido(String usuarioStr){
			for(UsuarioType usuarioType in lista()){
				if(usuarioType.toString() == usuarioStr){
					return true
				}
			}
		}
	}
	
    public enum StatusType{
        ATIVO('ATIVO'),
		INATIVO('INATIVO')
		
		final String valor;
		
		StatusType(String valor){
			this.valor = valor;
		}
		
		String toString(){
			return valor;
		}
		
		Boolean StatusType(StatusType status){
			return status == this
		}
		
        static lista() {
            [ATIVO, INATIVO]
        }
		
		static Boolean statusTypeValido(String statusStr){
			for(StatusType statusType in lista()){
				if(statusType.toString() == statusStr){
					return true
				}
			}
		}
    }
	
	public enum GeneroType{
		MASCULINO('MASCULINO'),
		FEMININO('FEMININO')
		
		final String valor;
		
		GeneroType(String valor){
			this.valor = valor;
		}
		
		String toString(){
			return valor;
		}
		
		Boolean GeneroType(GeneroType genero){
			return genero == this
		}
		
		static lista() {
			[MASCULINO, FEMININO]
		}
		
		static Boolean generoTypeValido(String generoStr){
			for(GeneroType generoType in lista()){
				if(generoType.toString() == generoStr){
					return true
				}
			}
		}
	}
	
	public enum CadastroType{
		CADASTRO_INICIAL('CADASTRO_INICIAL'),
		CADASTRO_ADICIONAL('CADASTRO_ADICIONAL')
		
		final String valor;
		
		CadastroType(String valor){
			this.valor = valor;
		}
		
		String toString(){
			return valor;
		}
		
		Boolean CadastroType(CadastroType cadastro){
			return cadastro == this
		}
		
		static lista() {
			[CADASTRO_INICIAL, CADASTRO_ADICIONAL]
		}
		
		static Boolean cadastroTypeValido(String cadastroStr){
			for(CadastroType cadastroType in lista()){
				if(cadastroType.toString() == cadastroStr){
					return true
				}
			}
		}
	}
	
	public enum RankType{
		RANKEADO('RANKEADO'),
		NAO_RANKEADO('NAO_RANKEADO')
 
		final String valor;
		
		RankType(String valor){
			this.valor = valor;
		}
		
		String toString(){
			return valor;
		}
		
		Boolean RankType(RankType rank){
			return rank == this
		}

		static lista() {
			[RANKEADO, NAO_RANKEADO]
		}
		
		static Boolean rankTypeValido(String rankStr){
			for(RankType rankType in lista()){
				if(rankType.toString() == rankStr){
					return true
				}
			}
		}
	}
	
	public enum StatusMensagemEmailType {
		CRIADO('CRIADO'),
		TENTADO('TENTADO'),
	    ENVIADO('ENVIADO'),
		ERRO('ERRO'),
		EXPIRADO('EXPIRADO'),
		ABORTADO('ABORTADO')
		
		final String valor;
		
		StatusMensagemEmailType(String valor){
			this.valor = valor;
		}
		
		String toString(){
			return valor;
		}
		
		Boolean StatusMensagemEmailType(StatusMensagemEmailType statusMensagemEmail){
			return statusMensagemEmail == this
		}

		static lista() {
			[CRIADO, TENTADO, ENVIADO, ERRO, EXPIRADO, ABORTADO]
		}
		
		static Boolean statusMensagemEmailTypeValido(String statusMensagemEmailStr){
			for(StatusMensagemEmailType statusMensagemEmailType in lista()){
				if(statusMensagemEmailType.toString() == statusMensagemEmailStr){
					return true
				}
			}
		}
	}
	public enum MensagemEmailType {
		CONVITE('CONVITE'),
		CONFIRMACAO_DE_EMAIL('CONFIRMACAO_DE_EMAIL'),
		RE_CONFIRMACAO_DE_EMAIL('RE_CONFIRMACAO_DE_EMAIL'),
		ESQUECI_MINHA_SENHA('ESQUECI_MINHA_SENHA'),
		GENERICO('GENERICO')
		
		final String valor;
		
		MensagemEmailType(String valor){
			this.valor = valor;
		}
		
		String toString(){
			return valor;
		}
		
		Boolean MensagemEmailType(MensagemEmailType mensagemEmail){
			return mensagemEmail == this
		}

		static lista() {
			[CONVITE, CONFIRMACAO_DE_EMAIL, RE_CONFIRMACAO_DE_EMAIL, ESQUECI_MINHA_SENHA, GENERICO]
		}
		
		static Boolean mensagemEmailTypeValido(String mensagemEmailStr){
			for(MensagemEmailType mensagemEmailType in lista()){
				if(mensagemEmailType.toString() == mensagemEmailStr){
					return true
				}
			}
		}
	}
    public enum ArquivoType {
        AVATAR('AVATAR'),
        CERTIFICADO_CRM('CERTIFICADO_CRM')

        final String valor;

        ArquivoType(String valor){
            this.valor = valor;
        }

        String toString(){
            return valor;
        }

        Boolean ArquivoType(ArquivoType arquivo){
            return arquivo == this
        }

        static lista() {
            [AVATAR, CERTIFICADO_CRM]
        }

        static Boolean arquivoTypeValido(String arquivoStr){
            for(ArquivoType arquivoType in lista()){
                if(arquivoType.toString() == arquivoStr){
                    return true
                }
            }
        }
    }

    public enum GeocodingType {
        OK('OK'),   //  indica que não ocorreu nenhum erro; o endereço foi analisado e pelo menos um geocódigo foi retornado.
        ZERO_RESULTS('ZERO_RESULTS'), // indica que o geocódigo foi concluído, mas não retornou resultados. Isso poderá ocorrer se o geocódigo tiver transmitido um address não existente ou um latlng em um local remoto.
        OVER_QUERY_LIMIT('OVER_QUERY_LIMIT'), //  indica que você ultrapassou sua cota.
        REQUEST_DENIED('REQUEST_DENIED'), //  indica que sua solicitação foi negada, geralmente devido à falta de um parâmetro sensor.
        INVALID_REQUEST('INVALID_REQUEST'), // geralmente indica que a consulta (address ou latlng) está ausente.
        OTHER_ERROR('OTHER_ERROR') // Outro erros

        final String valor;

        GeocodingType(String valor){
            this.valor = valor;
        }

        String toString(){
            return valor;
        }

        Boolean GeocodingType(GeocodingType geocoding){
            return geocoding == this
        }

        static lista() {
            [OK, ZERO_RESULTS, OVER_QUERY_LIMIT, REQUEST_DENIED, INVALID_REQUEST, OTHER_ERROR]
        }

        static Boolean geocodingTypeValido(String geocodingStr){
            for(GeocodingType geocodingType in lista()){
                if(geocodingType.toString() == geocodingStr){
                    return true
                }
            }
        }
    }
	
	public enum AcaoArquivoType {
		CRIAR('CRIAR'),
		MANTER('MANTER'),
		DELETAR('DELETAR')

		final String valor;

		AcaoArquivoType(String valor){
			this.valor = valor;
		}

		String toString(){
			return valor;
		}

		Boolean AcaoArquivoType(AcaoArquivoType acaoArquivo){
			return acaoArquivo == this
		}

		static lista() {
			[CRIAR, MANTER, DELETAR]
		}

		static Boolean acaoArquivoTypeValido(String acaoArquivoStr){
			for(AcaoArquivoType acaoArquivoType in lista()){
				if(acaoArquivoType.toString() == acaoArquivoStr){
					return true
				}
			}
		}
	}
}
