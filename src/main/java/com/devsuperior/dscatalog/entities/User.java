package com.devsuperior.dscatalog.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * A classe User representa uma entidade de usuário no aplicativo.
 *
 * Essa classe é anotada com @Entity da JPA para indicar que ela é uma entidade
 * e pode ser mantida no banco de dados. A anotação @Table especifica o nome
 * da tabela na qual os registros do usuário serão armazenados.
 */
@Entity
@Table(name = "tb_user")
public class User implements UserDetails, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String firstName;
	private String lastName;

	@Column(unique = true)
	private String email;
	private String password;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "tb_user_role",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	/**
	 * Construtor padrão para a classe User.
	 * Ele cria um objeto User vazio com valores padrão.
	 */
	public User() {
	}

	/**
	 * Construtor para a classe User com parâmetros.
	 *
	 * @param id O identificador exclusivo do usuário.
	 * @param firstName O primeiro nome do usuário.
	 * @param lastName O sobrenome do usuário.
	 * @param email O endereço de e-mail do usuário.
	 * @param password A senha do usuário.
	 */
	public User(Long id, String firstName, String lastName, String email, String password) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

	/**
	 * Obtenha o identificador exclusivo do usuário.
	 *
	 * @return O ID do usuário.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Set the unique identifier of the user.
	 *
	 * @param id The user's ID.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Get the first name of the user.
	 *
	 * @return The first name of the user.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Set the first name of the user.
	 *
	 * @param firstName The first name of the user.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Get the last name of the user.
	 *
	 * @return The last name of the user.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Set the last name of the user.
	 *
	 * @param lastName The last name of the user.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Get the email address of the user.
	 *
	 * @return The email address of the user.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Set the email address of the user.
	 *
	 * @param email The email address of the user.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Get the password of the user.
	 *
	 * @return The user's password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set the password of the user.
	 *
	 * @param password The user's password.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Obtenha as funções associadas ao usuário.
	 *
	 * @return O conjunto de funções atribuídas ao usuário.
	 */
	public Set<Role> getRoles() {
		return roles;
	}

	/**
	 * Gera o código hash para o objeto User com base em sua ID.
	 *
	 * @return O código hash do objeto User.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * Compara dois objetos User para igualdade.
	 *
	 * @param obj O objeto a ser comparado com o usuário.
	 * @return True se os objetos forem iguais, false caso contrário.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * Obtenha as autoridades (funções) atribuídas ao usuário.
	 *
	 * @return Uma coleção de objetos GrantedAuthority que representam as funções do usuário.
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority()))
				.collect(Collectors.toList());
	}

	/**
	 * Get the username of the user.
	 *
	 * @return The email address of the user (used as the username).
	 */
	@Override
	public String getUsername() {
		return email;
	}

	/**
	 * Verificar se a conta do usuário não expirou.
	 *
	 * @return True se a conta não estiver expirada, false caso contrário.
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * Verificar se a conta do usuário não está bloqueada.
	 *
	 * @return True se a conta não estiver bloqueada, false caso contrário.
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * Verificar se as credenciais do usuário não estão expiradas.
	 *
	 * @return True se as credenciais não estiverem expiradas, false caso contrário.
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * Verificar se a conta do usuário está ativada.
	 *
	 * @return True se a conta estiver ativada, false caso contrário.
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}
}
