/*
 * Esta classe é responsável por aprimorar o token de acesso OAuth2 com informações adicionais do usuário.
 * Ela implementa a interface TokenEnhancer, permitindo a personalização do token antes de ser retornado ao cliente.
 */

package com.devsuperior.dscatalog.components;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.UserRepository;

@Component
public class JwtTokenEnhancer implements TokenEnhancer {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		
		// Recupera as informações do usuário a partir do UserRepository usando o email obtido da autenticação OAuth2.
		User user = userRepository.findByEmail(authentication.getName());
		
		// Cria um mapa para armazenar as informações adicionais do usuário a serem incluídas no token de acesso.
		Map<String, Object> map = new HashMap<>();
		map.put("userFirstName", user.getFirstName()); // Adiciona o primeiro nome do usuário ao mapa.
		map.put("userId", user.getId()); // Adiciona o ID do usuário ao mapa.

		// Realiza o cast do accessToken para DefaultOAuth2AccessToken para modificar suas informações adicionais.
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
		
		// Define o mapa de informações adicionais no token de acesso, incluindo o primeiro nome e o ID do usuário.
		token.setAdditionalInformation(map);
		
		// Retorna o token de acesso modificado com informações adicionais do usuário.
		return accessToken;
	}
}
