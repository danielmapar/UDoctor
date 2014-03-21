class UrlMappings {

	    static mappings = {
        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }

        /**
         * Pesquisa Controller mappings
         */
        "/"(controller: "pesquisa", action: "index")

        /**
         * HTTP Error redirects
         */
        "500"(view:'/error')

        /**
         * Cadastro Controller mappings
         */
        "/cadastro/"(controller: "cadastro", action: "cadastroInicial")
        "/cliente/editar/"(controller: "cadastro", action: "cadastroAdicionalCliente")
        "/instituicao/editar/"(controller: "cadastro", action: "cadastroAdicionalInstituicao")
        "/profissional/editar/"(controller: "cadastro", action: "cadastroAdicionalProfissional")

        /**
         * Profile Controller mappings
         */
        "/profile/$id?"(controller: "profile", action: "index")

        /**
         * Administrador mappings
         */
        "/admin/cidades/"(controller: "administrador", action: "cidades")

        /**
         * Spring Security Controller Mappings
         */
        "/login/$action?"(controller: "login")
        "/logout/$action?"(controller: "logout")

        /**
         * Confirmacao Email Controller Mappings
         */
        "/confirmacao/$id?" (controller: 'confirmacaoDeEmail', action: "receberConfirmacaoDeEmail")
        "/reenviar/confirmacao/" (controller: 'confirmacaoDeEmail', action: "reEnviarConfirmacaoDeEmail")

        /**
         * Indicar Controller Mappings
         */

        "/indicar/" (controller: 'indicar', action: 'indicarAmigo')

        /**
         * Senha Controller Mappings
         */
        "/senha/esqueci/"(controller: "senha", action: "esqueciMinhaSenha")
        "/senha/resetar/$id?"(controller: "senha", action: "resetarSenha")
        "/senha/mudar/"(controller: "senha", action: "mudarSenha")

    }
}
