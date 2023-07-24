package com.devsuperior.dscatalog.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.devsuperior.dscatalog.dto.UserUpdateDTO;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.resources.exceptions.FieldMessage;

/**
 * Classe responsável por validar a atualização de usuários.
 */
public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private UserRepository repository;

	/**
	 * Inicializa o validador.
	 * 
	 * @param ann anotação de validação personalizada
	 */
	@Override
	public void initialize(UserUpdateValid ann) {
		// Nenhuma lógica de inicialização é necessária neste caso.
	}

	/**
	 * Verifica se o objeto UserUpdateDTO é válido para atualização.
	 * 
	 * @param dto     o objeto UserUpdateDTO a ser validado
	 * @param context contexto de validação personalizado
	 * @return true se o objeto for válido, false caso contrário
	 */
	@Override
	public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {

		// Obtém as variáveis de URI da requisição (por exemplo, o ID do usuário).
		@SuppressWarnings("unchecked")
		var uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		long userId = Long.parseLong(uriVars.get("id"));

		List<FieldMessage> list = new ArrayList<>();

		// Verifica se o email já está sendo usado por outro usuário.
		User user = repository.findByEmail(dto.getEmail());
		if (user != null && userId != user.getId()) {
			list.add(new FieldMessage("email", "Email já existe"));
		}

		// Se houver erros, adiciona-os ao contexto de validação.
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		// Retorna true se a lista de erros estiver vazia, indicando que o objeto é válido.
		return list.isEmpty();
	}
}
