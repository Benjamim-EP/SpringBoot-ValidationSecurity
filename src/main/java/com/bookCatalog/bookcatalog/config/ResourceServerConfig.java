package com.bookCatalog.bookcatalog.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    // Injeção de dependência do ambiente para verificar perfis ativos
    @Autowired
    private Environment env;

    // Injeção de dependência do token store JWT
    @Autowired
    private JwtTokenStore tokenStore;

    // Endpoints públicos que não requerem autenticação
    private static final String[] PUBLIC = { "/oauth/token", "/h2-console/**" };

    // Endpoints acessíveis por OPERATOR e ADMIN, mas só leitura (HTTP GET)
    private static final String[] OPERATOR_OR_ADMIN = { "/products/**", "/categories/**" };

    // Endpoints acessíveis apenas por ADMIN
    private static final String[] ADMIN = { "/users/**" };    

    // Configuração do Resource Server para utilizar o token store JWT
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore);
    }

    // Configuração das regras de autorização para os endpoints
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // Permitir acesso à interface H2 Console em ambiente de teste
        if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
            http.headers().frameOptions().disable();
        }
        http.authorizeRequests(requests -> requests
            // Endpoints públicos não requerem autenticação
            .antMatchers(PUBLIC).permitAll()
            // Endpoints de leitura (HTTP GET) acessíveis por OPERATOR e ADMIN
            .antMatchers(HttpMethod.GET, OPERATOR_OR_ADMIN).permitAll()
            // Endpoints de criação, edição e exclusão (exceto HTTP GET) acessíveis por OPERATOR e ADMIN
            .antMatchers(OPERATOR_OR_ADMIN).hasAnyRole("OPERATOR", "ADMIN")
            // Endpoints acessíveis apenas por ADMIN
            .antMatchers(ADMIN).hasRole("ADMIN")
            // Todos os outros endpoints requerem autenticação
            .anyRequest().authenticated());
    }   
}
