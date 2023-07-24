package com.devsuperior.dscatalog.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.devsuperior.dscatalog.entities.User;

/**
 * Classe que representa o objeto de transferência de dados (DTO) para a entidade User.
 * Um objeto UserDTO é usado para transportar informações sobre um usuário entre as camadas da aplicação.
 * Implementa a interface Serializable para permitir que os objetos sejam convertidos em bytes, se necessário.
 */
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único do usuário.
     */
    private Long id;

    /**
     * Primeiro nome do usuário.
     */
    @NotBlank(message = "Campo obrigatório")
    private String firstName;

    /**
     * Sobrenome do usuário.
     */
    private String lastName;

    /**
     * Endereço de email do usuário.
     */
    @Email(message = "Favor entrar um email válido")
    private String email;

    /**
     * Conjunto de papéis (roles) associados ao usuário.
     */
    Set<RoleDTO> roles = new HashSet<>();

    /**
     * Construtor padrão sem argumentos.
     */
    public UserDTO() {
    }

    /**
     * Construtor com argumentos.
     * @param id O identificador único do usuário.
     * @param firstName O primeiro nome do usuário.
     * @param lastName O sobrenome do usuário.
     * @param email O endereço de email do usuário.
     */
    public UserDTO(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * Construtor que recebe uma entidade User para inicializar o objeto UserDTO com base nos dados da entidade.
     * @param entity A entidade User que será usada para criar o objeto UserDTO.
     */
    public UserDTO(User entity) {
        id = entity.getId();
        firstName = entity.getFirstName();
        lastName = entity.getLastName();
        email = entity.getEmail();
        entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
    }
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<RoleDTO> getRoles() {
		return roles;
	}
}
