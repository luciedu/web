package com.ipiecoles.communes.web.security;

import com.ipiecoles.communes.web.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Annotation pour que Spring prenne en compte les éléments de configuration défénis
@Configuration

// Annotation permettant d'activer la sécurité pour notre appli web
@EnableWebSecurity

//Prise en compte des annotation de sécurité
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)

// WebSecurityConfiguration : classe définissant un certain nombre de comportements par défaut et de fonctionnalité autour de la sécurité.
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                //Service chargé d'effectuer les opérations d'authentification
                .userDetailsService(userDetailsService)
                //Définit l'algorithme de hachage pour les mots de passe
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //Algo BCrypt
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/webjars/**");// * => /webjars/test.js ** => //webjars/test/test/test.js
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    // La page d'accueil / et d'inscription...
                    .antMatchers("/", "/register")
                    //... est accessible à tous
                    .permitAll()
                    // gestion des access avec les rôles
                    //.antMatchers("/communes/*")
                    //.hasRole("ADMIN")
                    // Et toutes les autres requêtes...
                    .anyRequest()
                    // ... demandent à être authentifiées
                    .authenticated()

                //Activation de la connexion par formulaire HTML
                .and().formLogin()
                    //Lorsque l'on va accéder à une page protégée, vers où on redirige l'utilisateur pour qu'il puisse se connecter
                    .loginPage("/login") //Défaut : /login
                    //Autoriser la page de login à tous
                    .permitAll()
                    //Où va-t-on si la connexion échoue ?
                    .failureUrl("/login?error=true") //Défaut : /login?error
                    //Où va-t-on lorsque la connexion réussit ?
                    .defaultSuccessUrl("/?successfulConnection=true")// Pas de valeur par défaut
                    //Définir le nom du paramètre contenant le nom d'utilisateur
                    .usernameParameter("username")//Défaut : username
                    //Définir le nom du paramètre contenant le password
                    .passwordParameter("password")//Défaut : password

                //Gestion de la déconnexion
                .and().logout()
                    //Où va-t-on lorsque l'on souhaite se déconnecter ?
                    .logoutUrl("/logout") //Défaut : /logout
                    //Où va-t-on une fois la déconnexion effectuée
                    .logoutSuccessUrl("/login?logout=true"); //Défaut /login?logout


    }


}
