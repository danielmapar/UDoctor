package br.com.udoctor.geocoding

import groovy.json.JsonSlurper
import org.codehaus.groovy.grails.commons.GrailsApplication

class GeocodingService {

    static transactional = "mongo"

    GrailsApplication grailsApplication
    Boolean servicoAtivo = true

    Map buscaJSON(String query) {
		
        if(servicoAtivo == true){

            URL urlJSON = new URL(grailsApplication.config.googleapi.url.json + "address=" + removerEspacos(query) + "&sensor=false")
            def geoCodeResultJSON = new JsonSlurper().parseText(urlJSON.getText())

            if (geoCodeResultJSON.status == "OVER_QUERY_LIMIT"){
                servicoAtivo = false
            }

            if (geoCodeResultJSON.status == "OK"){
                return [lat: Double.parseDouble(geoCodeResultJSON.results.geometry.location.lat[0]), lng: Double.parseDouble(geoCodeResultJSON.results.geometry.location.lng[0]), status: geoCodeResultJSON.status]
            }else{
                return [status: geoCodeResultJSON.status]
            }
        }else{
            return [status: "OVER_QUERY_LIMIT"]
        }
    }


    private String removerEspacos(String query) {
        query.replaceAll(" ", "+")
    }
}