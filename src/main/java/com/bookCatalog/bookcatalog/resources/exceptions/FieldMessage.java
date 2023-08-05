/**
 * A classe FieldMessage representa uma mensagem contendo informações sobre um erro específico de campo.
 * Ela é utilizada no contexto de tratamento de exceções para fornecer informações detalhadas de erro
 * ao validar a entrada do usuário ou lidar com erros de lógica de negócio.
 *
 * Essa classe implementa a interface Serializable para permitir que instâncias de FieldMessage
 * sejam convertidas em um fluxo de bytes para armazenamento ou transmissão.
 *
 * Exemplo de uso:
 * FieldMessage fieldMessage = new FieldMessage("username", "O nome de usuário deve ter pelo menos 5 caracteres.");
 *
 * @version 1.0
 * @since 2023-07-12
 */
package com.bookCatalog.bookcatalog.resources.exceptions;

import java.io.Serializable;

public class FieldMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * O nome do campo que gerou o erro.
     */
    private String fieldName;

    /**
     * A mensagem de erro associada ao campo.
     */
    private String message;

    /**
     * Construtor padrão da classe FieldMessage.
     * Cria uma nova instância sem nome de campo e sem mensagem.
     */
    public FieldMessage() {
    }

    /**
     * Construtor da classe FieldMessage.
     * Cria uma nova instância com o nome de campo e mensagem de erro especificados.
     *
     * @param fieldName O nome do campo que gerou o erro.
     * @param message   A mensagem de erro associada ao campo.
     */
    public FieldMessage(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    /**
     * Obtém o nome do campo que gerou o erro.
     *
     * @return O nome do campo.
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Define o nome do campo que gerou o erro.
     *
     * @param fieldName O nome do campo.
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Obtém a mensagem de erro associada ao campo.
     *
     * @return A mensagem de erro.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Define a mensagem de erro associada ao campo.
     *
     * @param message A mensagem de erro.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
