/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.hiik.websocketserver.config;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.annotation.FacesConfig;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;

/**
 *
 * @author vaganovdv
 */

@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/start.xhtml",
                errorPage = "/start.xhtml",
                useForwardToLogin = false
            )
)

@FacesConfig
@ApplicationScoped
public class AppConfig
{
    
}
