package br.com.udoctor.email.assincrono;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class EmailValidator {
    public static boolean caixaDeEmail(String valor) {
        boolean resultado = true;
        try {
            InternetAddress enderecoEmail = new InternetAddress(valor);
            enderecoEmail.validate();
        } catch (AddressException ex) {
        	resultado = false;
        }
        return resultado;
    }
}
