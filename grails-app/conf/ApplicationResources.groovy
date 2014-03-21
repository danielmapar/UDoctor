modules = {
    'application' {
        resource url:'js/application.js'
    }

    // Dependencias UDOCTOR
    'easing'{
        resource url:'js/easing/jquery.easing.js'
    }

    'bootstrap-fileupload' {
        resource url: 'css/bootstrap-fileupload/bootstrap-fileupload.min.css'
        resource url: 'js/bootstrap-fileupload/bootstrap-fileupload.min.js'
    }

    'bootstrap'{
        resource url:'css/bootstrap/bootstrap.min.css'
        resource url:'css/bootstrap/bootstrap-responsive.min.css'
        resource url:'js/bootstrap/bootstrap.min.js'
    }

    'chosen'{
        resource url:'css/chosen/chosen.css'
        resource url:'js/chosen/chosen.jquery.js'
    }

    'fancybox'{
        resource url:'css/fancybox/jquery.fancybox.css'
        resource url:'js/fancybox/jquery.fancybox.js'
    }

    'maskedinput'{
        resource url:'js/maskedinput/jquery.maskedinput.js'
    }

    'udoctor'{
        resource url:'css/udoctor/style.css'
        resource url:'js/udoctor/scripts.js'
    }

    'datepicker'{
        resource url:'css/bootstrap-datepicker/datepicker.css'
        resource url:'js/bootstrap-datepicker/bootstrap-datepicker.js'
        resource url:'js/bootstrap-datepicker/locales/bootstrap-datepicker.pt-BR.js'
    }

    'update'{
        resource url:'js/atualizar-browser/update.js'
    }

    // Modulos UDOCTOR

    'core' {
        dependsOn 'jquery, easing, bootstrap, bootstrap-fileupload, fancybox, chosen, maskedinput, datepicker, udoctor, update'
    }
}