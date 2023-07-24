package com.devsuperior.dscatalog.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.resources.exceptions.FieldMessage;

/**
 * Classe que implementa a validação customizada para inserção de usuários.
 * 
 * Esta classe é responsável por verificar se o email informado no DTO de inserção já está
 * cadastrado no banco de dados. Caso esteja, é adicionada uma mensagem de erro na lista de
 * FieldMessage, que será utilizada para retornar as mensagens de validação ao cliente.
 * 
 * A validação é executada através do método isValid, que é chamado pelo mecanismo de validação
 * do Spring, de acordo com a anotação @UserInsertValid presente na classe UserInsertDTO.
 */
public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {
	
	@Autowired
	private UserRepository repository;
	
	/**
	 * Método de inicialização da classe de validação.
	 * 
	 * Este método é chamado pelo mecanismo de validação do Spring quando a classe é utilizada
	 * como validador para a anotação @UserInsertValid. Neste caso, não é necessário realizar
	 * nenhuma inicialização adicional, portanto o método está vazio.
	 * 
	 * @param ann anotação utilizada para marcar a classe como validadora.
	 */
	@Override
	public void initialize(UserInsertValid ann) {
		// Nenhuma inicialização necessária.
	}

	/**
	 * Método de validação customizada para inserção de usuários.
	 * 
	 * Este método verifica se o email informado no DTO de inserção já está cadastrado no banco
	 * de dados. Caso esteja, é adicionada uma mensagem de erro na lista de FieldMessage, que será
	 * utilizada para retornar as mensagens de validação ao cliente.
	 * 
	 * @param dto o objeto DTO contendo os dados do usuário a serem inseridos.
	 * @param context o contexto de validação fornecido pelo mecanismo do Spring.
	 * @return true se a validação passar (nenhum erro encontrado), false caso contrário.
	 */
	@Override
	public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
		// Lista para armazenar as mensagens de erro.
		List<FieldMessage> list = new ArrayList<>();
		
		// Verifica se o email já está cadastrado no banco de dados.
		User user = repository.findByEmail(dto.getEmail());
		if (user != null) {
			// Caso o email já exista, adiciona uma mensagem de erro à lista.
			list.add(new FieldMessage("email", "Email já existe"));
		}

		// Se houverem mensagens de erro na lista, adiciona-as ao contexto de validação.
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		
		// Retorna true se não houverem erros na lista, ou seja, a validação passou.
		return list.isEmpty();
	}
}
