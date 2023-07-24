package com.devsuperior.dscatalog.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A classe Role representa uma entidade de papel/autoridade utilizada para autenticação e autorização.
 * É uma entidade persistente mapeada para a tabela "tb_role" no banco de dados.
 */
@Entity
@Table(name = "tb_role")
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Representa a autoridade do papel. Por exemplo, "ROLE_ADMIN", "ROLE_USER", etc.
	 */
	private String authority;
	
	/**
	 * Construtor padrão da classe.
	 */
	public Role() {
	}

	/**
	 * Construtor com parâmetros da classe.
	 *
	 * @param id        O identificador único do papel.
	 * @param authority A autoridade do papel.
	 */
	public Role(Long id, String authority) {
		super();
		this.id = id;
		this.authority = authority;
	}

	/**
	 * Obtém o identificador único do papel.
	 *
	 * @return O identificador único do papel.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Define o identificador único do papel.
	 *
	 * @param id O identificador único do papel.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Obtém a autoridade do papel.
	 *
	 * @return A autoridade do papel.
	 */
	public String getAuthority() {
		return authority;
	}

	/**
	 * Define a autoridade do papel.
	 *
	 * @param authority A autoridade do papel.
	 */
	public void setAuthority(String authority) {
		this.authority = authority;
	}

	/**
	 * Método que calcula o código de hash para a instância atual.
	 * 
	 * O método utiliza o algoritmo padrão de cálculo de código de hash, que
	 * multiplica um número primo (31) pelo código de hash atual e adiciona o 
	 * código de hash da propriedade 'id', caso esteja presente.
	 * 
	 * @return O código de hash calculado para a instância.
	 */
	@Override
	public int hashCode() {
		final int prime = 31; // Número primo usado no cálculo do código de hash
		int result = 1; // Valor inicial do resultado do código de hash
		
		// Se o id for diferente de nulo, calcula o código de hash usando a propriedade 'id'
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		
		return result;
	}

	/**
	 * Método que verifica se o objeto atual é igual a outro objeto fornecido.
	 * 
	 * O método compara as referências dos objetos e, em seguida, verifica se os
	 * objetos são da mesma classe. Caso sejam da mesma classe, compara a propriedade
	 * 'id' dos objetos para determinar a igualdade.
	 * 
	 * @param obj O objeto a ser comparado com a instância atual.
	 * @return true se os objetos forem iguais, caso contrário, false.
	 */
	@Override
	public boolean equals(Object obj) {
		// Verifica se os objetos têm a mesma referência na memória
		if (this == obj)
			return true;
		
		// Verifica se o objeto fornecido é nulo ou não é uma instância da mesma classe
		if (obj == null || getClass() != obj.getClass())
			return false;
		
		// Converte o objeto para a classe 'Role'
		Role other = (Role) obj;
		
		// Compara a propriedade 'id' dos objetos para determinar a igualdade
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		
		// Se todos os critérios forem atendidos, os objetos são considerados iguais
		return true;
	}
}
