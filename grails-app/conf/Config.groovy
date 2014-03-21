// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [
    all:           '*/*',
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['senha']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

environments {
    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
        // TODO: grails.serverURL = "http://www.changeme.com"
    }
}

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.codehaus.groovy.grails.web.servlet',        // controllers
           'org.codehaus.groovy.grails.web.pages',          // GSP
           'org.codehaus.groovy.grails.web.sitemesh',       // layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping',        // URL mapping
           'org.codehaus.groovy.grails.commons',            // core / classloading
           'org.codehaus.groovy.grails.plugins',            // plugins
           'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
}
// Clean cache
//grails.cache.clearAtStartup = true

// Mail plugin
grails {
    mail {
        host = "smtp.gmail.com"
        port = 465
        username = "daniel.marchena.parreira@gmail.com"
        password = "Dertyu765"
        props = ["mail.smtp.auth":"true",
                "mail.smtp.socketFactory.port":"465",
                "mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
                "mail.smtp.socketFactory.fallback":"false"]
    }
}

// Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName    = 'br.com.udoctor.seguranca.Usuario'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'br.com.udoctor.seguranca.UsuarioRegra'
grails.plugins.springsecurity.authority.className               = 'br.com.udoctor.seguranca.Regra'
grails.plugins.springsecurity.userLookup.usernamePropertyName   = 'email'
grails.plugins.springsecurity.userLookup.passwordPropertyName   = 'senha'

//grails.plugins.springsecurity.useSessionFixationPrevention = true
grails.plugins.springsecurity.password.algorithm           = 'bcrypt'
grails.plugins.springsecurity.password.bcrypt.logrounds    = 15
grails.plugins.springsecurity.logout.afterLogoutUrl        = '/'

grails.plugins.springsecurity.securityConfigType = "InterceptUrlMap"

grails.plugins.springsecurity.interceptUrlMap = [

        /**
         * Pesquisa Controller mappings
         */

        '/*':  ['IS_AUTHENTICATED_ANONYMOUSLY'],

        /**
         * Cadastro Controller mappings
         */

        '/cadastro/**' : ["isAnonymous()"],
        '/cadastro/cadastroInicial/**' : ["isAnonymous()"],
        '/cadastro/cadastroInicialCliente/**' : ["isAnonymous()"],
        '/cadastro/cadastroInicialProfissional/**' : ["isAnonymous()"],
        '/cadastro/cadastroInicialInstituicao/**' : ["isAnonymous()"],


        '/cliente/editar/**': ['ROLE_CLIENTE'],
        '/cadastro/cadastroAdicionalCliente/**' : ['ROLE_CLIENTE'],

        '/profissional/editar/**': ['ROLE_PROFISSIONAL'],
        '/cadastro/cadastroAdicionalProfissional/**' : ['ROLE_PROFISSIONAL'],

        '/instituicao/editar/**': ['ROLE_INSTITUICAO'],
        '/cadastro/cadastroAdicionalInstituicao/**' : ['ROLE_INSTITUICAO'],

        /**
         * Profile Controller mappings
         */

        '/profile/**'  : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/profile/index/**' : ['IS_AUTHENTICATED_ANONYMOUSLY'],

        /**
         * Administrador mappings
         */
        '/admin/cidades/**' : ['ROLE_ADMIN'],
        '/administrador/cidade/**'  : ['ROLE_ADMIN'],

        /**
         * Spring Security Controller Mappings
         */

        '/login/**' : ["isAnonymous()"],
        '/login/index/**' : ["isAnonymous()"],

        '/logout/**' : ["isAuthenticated()"],
        '/logout/index/**' : ["isAuthenticated()"],

        /**
         * Confirmacao Email Controller Mappings
         */

        '/confirmacao/**'  : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/confirmacaoDeEmail/receberConfirmacaoDeEmail/**'  : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/reenviar/confirmacao/**' : ["isAnonymous()"],
        '/confirmacaoDeEmail/reEnviarConfirmacaoDeEmail/**'  : ["isAnonymous()"],

        /**
         * Indicar Controller Mappings
         */

        '/indicar/**'  : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/indicarAmigo/**' : ['IS_AUTHENTICATED_ANONYMOUSLY'],

        /**
         * Senha Controller Mappings
         */
        '/senha/esqueci/**' : ["isAnonymous()"],
        '/senha/esqueciMinhaSenha/**'  : ["isAnonymous()"],

        '/senha/resetar/**' : ["isAnonymous()"],
        '/senha/resetarSenha/**' : ["isAnonymous()"],

        '/senha/mudar/**' : ["isAuthenticated()"],
        '/senha/mudarSenha/**' : ["isAuthenticated()"],

        /**
         * Dependencias JS, CSS e Imagens Mappings
         */

        '/js/**':        ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/css/**':       ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/images/**':    ['IS_AUTHENTICATED_ANONYMOUSLY']
]

// Cache-headers plugin:
cache.headers.presets = [
        home_page: [store: true, shared:true]
]

// Email assincrono:
assincrono.email.default.tentativa.intervalo=60000l               // 5 minutos - >300000l
assincrono.email.default.maximo.tentativas.contador=2              // Serao realizadas 2 tentativas de enviar o e-mail
assincrono.email.enviar.repetir.intervalo=60000l                   // 60 segundos
assincrono.email.expirado.coletor.repetir.intervalo=607000l        // 10 minutos e 7 segundos
assincrono.email.mensagens.enviar.de.uma.vez=100	               // Enviar 100 mensagens de email por lote
assincrono.email.mensagens.expirar.de.uma.vez=500				   // Expirar 500 mensagens de email por lote
assincrono.email.enviar.imediatamente=false						   // Nao ativar job de envio ao gravar um novo e-mail no db
assincrono.email.deletar.depois.de.enviar=true				       // Deletar e-mail do banco depois de enviar o e-mail com sucesso
assincrono.email.default.de = "daniel.marchena.parreira@gmail.com" // Endereco de e-mail default
assincrono.email.desabilitado = false                       	   // Desabilitar os jobs de envio de e-mail e coletor de e-mail expirados

// Confirmacao de Email:
confirmacao.email.default.de = "daniel.marchena.parreira@gmail.com" // Endereco de e-mail default
confirmacao.email.eliminar.obsoleto.intervalo = 60000l              // 5 minutos


// Upload de Arquivo:
arquivo.diretorio.nome.avatar = "avatar"
arquivo.diretorio.nome.certificadoCRM = "certificadoCRM"
arquivo.tamanho.limite.avatar = 1000000l // 1 mb
arquivo.tamanho.limite.certificadoCRM = 5000000l // 5 mb

// Google Maps API
googleapi.url.xml = 'http://maps.googleapis.com/maps/api/geocode/xml?'
googleapi.url.json = 'http://maps.googleapis.com/maps/api/geocode/json?'
googleapi.producao.key = 'AIzaSyB2NWo0EOcbSU01eiUsZFbsvGMc0VEJGec'
googleapi.localhost.key = 'AIzaSyAhqMXZ8SH3XHcsegPqc8GIrXHWgXhilwQ'

// Elasticsearch
elasticsearch.tipo.execucao = "local"