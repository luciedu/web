package com.ipiecoles.communes.web.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// Annotation pour que Spring prenne en compte les éléments de configuration défénis
@Configuration

// Annotation permettant d'activer la sécurité pour notre appli web
@EnableWebSecurity

// WebSecurityConfiguration : classe définissant un certain nombre de comportements par défaut et de fonctionnalité autour de la sécurité.
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // Activation de la connexion par formulaire HTML
            .formLogin()

                // Lorsque l'on va accéder à une page protégée, vers où on redirige l'utilisateur pour qu'il puisse se connecter
                .loginPage("/login") // Défaut : /login

                // Où va-t-on si la connexion échoue ?
                .failureUrl("/login?error=true") //Défaut : /login?error

                //  Où va-t-on si la connexion réussit ?
                .defaultSuccessUrl("/?successfulConnection=true") // Pas de valeur par défaut

                // Définir le nom du paramètre contenant le nom d'utilisateur
                .usernameParameter("username") //Défaut : username

                // Définir le nom du paramètre contenant le mot de passe
                .passwordParameter("password") //Défaut : password

            // Gestion de la déconnexion
            .and().logout() // le .and() permet d'enchainer les configurations

                // Où va-t-on lorsque l'on souhaite se déconnecter ?
                .logoutUrl("/logout") // Défaut : /logout

                // Où va-t-on une fois la déconnexion effectuée ?
                .logoutSuccessUrl("/login?logout=true"); // Défaut : /login?logout

    }
}
