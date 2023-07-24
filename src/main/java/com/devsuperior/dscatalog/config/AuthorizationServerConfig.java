package com.devsuperior.dscatalog.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.devsuperior.dscatalog.components.JwtTokenEnhancer;

/**
 * A classe AuthorizationServerConfig é uma classe de configuração do Spring responsável por configurar as
 * configurações e endpoints do Authorization Server para o processo de autenticação e autorização OAuth2.
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    // Injetando propriedades de configuração usando a anotação Spring @Value
    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    @Value("${jwt.duration}")
    private Integer jwtDuration;

    // Injetando o BCryptPasswordEncoder para codificar o segredo do cliente
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Injetando o JwtAccessTokenConverter responsável por converter os tokens de acesso para o formato JWT
    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;

    // Injetando o JwtTokenStore responsável por armazenar os tokens JWT
    @Autowired
    private JwtTokenStore tokenStore;

    // Injetando o AuthenticationManager para lidar com a autenticação do usuário
    @Autowired
    private AuthenticationManager authenticationManager;

    // Injetando o JwtTokenEnhancer responsável por aprimorar os tokens JWT
    @Autowired
    private JwtTokenEnhancer tokenEnhancer;

    /**
     * Configura as configurações de segurança para o Authorization Server.
     *
     * @param security O AuthorizationServerSecurityConfigurer usado para configurar aspectos de segurança.
     * @throws Exception Se ocorrer um erro durante a configuração.
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    /**
     * Configura os detalhes do cliente para serem armazenados em memória para os clientes OAuth2.
     *
     * @param clients O ClientDetailsServiceConfigurer usado para configurar os detalhes do cliente.
     * @throws Exception Se ocorrer um erro durante a configuração.
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(clientId)
                .secret(passwordEncoder.encode(clientSecret))
                .scopes("read", "write")
                .authorizedGrantTypes("password")
                .accessTokenValiditySeconds(jwtDuration);
    }

    /**
     * Configura os endpoints para o Authorization Server.
     *
     * @param endpoints O AuthorizationServerEndpointsConfigurer usado para configurar os endpoints.
     * @throws Exception Se ocorrer um erro durante a configuração.
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // Criando uma TokenEnhancerChain para encadear vários aprimoradores de token juntos
        TokenEnhancerChain chain = new TokenEnhancerChain();
        chain.setTokenEnhancers(Arrays.asList(accessTokenConverter, tokenEnhancer));

        // Configurando o gerenciador de autenticação, armazenamento de token, conversor de token de acesso e aprimorador de token
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore)
                .accessTokenConverter(accessTokenConverter)
                .tokenEnhancer(chain);
    }
}
