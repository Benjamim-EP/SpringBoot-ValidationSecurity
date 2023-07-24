/**
 * WebSecurityConfig é uma classe de configuração que define as regras de segurança da aplicação.
 * Esta classe estende WebSecurityConfigurerAdapter, que é uma classe do Spring Security
 * que fornece configurações básicas para segurança em aplicações web.
 */
package com.devsuperior.dscatalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * O objeto BCryptPasswordEncoder é utilizado para codificar senhas de usuário
     * antes de armazená-las no banco de dados.
     */
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * O objeto UserDetailsService é responsável por carregar os dados do usuário
     * a partir do banco de dados, para autenticação e autorização.
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Configura o AuthenticationManagerBuilder para usar o UserDetailsService e o
     * BCryptPasswordEncoder para autenticar os usuários.
     *
     * @param auth O objeto AuthenticationManagerBuilder utilizado para configurar a autenticação.
     * @throws Exception Lança uma exceção caso ocorra algum erro na configuração.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    /**
     * Configura o WebSecurity para ignorar determinados caminhos durante a verificação de segurança.
     * Neste caso, estamos ignorando o caminho "/actuator/**", que é usado pelo Spring Boot Actuator
     * para expor informações e métricas da aplicação.
     *
     * @param web O objeto WebSecurity que será configurado.
     * @throws Exception Lança uma exceção caso ocorra algum erro na configuração.
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/actuator/**");
    }

    /**
     * Cria um bean para o AuthenticationManager, que é usado pelo Spring Security
     * para realizar a autenticação dos usuários.
     *
     * @return O objeto AuthenticationManager configurado.
     * @throws Exception Lança uma exceção caso ocorra algum erro na configuração.
     */
    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
