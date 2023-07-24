/**
 * A classe StandardError representa uma resposta de erro padrão a ser retornada pelos endpoints de API REST
 * quando ocorre uma exceção.
 * Ela contém informações sobre o horário de registro, status HTTP, tipo de erro, mensagem de erro e caminho da requisição.
 */
package com.devsuperior.dscatalog.resources.exceptions;

import java.io.Serializable;
import java.time.Instant;

public class StandardError implements Serializable {
    private static final long serialVersionUID = 1L;

    private Instant timestamp; // O horário em que o erro ocorreu
    private Integer status; // O código de status HTTP da resposta
    private String error; // O tipo de erro que ocorreu
    private String message; // Uma mensagem de erro descritiva
    private String path; // O caminho da requisição que causou o erro

    /**
     * Construtor padrão para a classe StandardError.
     */
    public StandardError() {
    }

    /**
     * Obtém o horário em que o erro ocorreu.
     *
     * @return Instant que representa o horário do erro.
     */
    public Instant getTimestamp() {
        return timestamp;
    }

    /**
     * Define o horário do erro.
     *
     * @param timestamp Instant que representa o horário do erro.
     */
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Obtém o código de status HTTP da resposta.
     *
     * @return Integer que representa o código de status HTTP da resposta.
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * Define o código de status HTTP da resposta.
     *
     * @param status Integer que representa o código de status HTTP da resposta.
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * Obtém o tipo de erro que ocorreu.
     *
     * @return String que representa o tipo de erro que ocorreu.
     */
    public String getError() {
        return error;
    }

    /**
     * Define o tipo de erro que ocorreu.
     *
     * @param error String que representa o tipo de erro que ocorreu.
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Obtém a mensagem de erro descritiva.
     *
     * @return String que representa a mensagem de erro.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Define a mensagem de erro descritiva.
     *
     * @param message String que representa a mensagem de erro.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Obtém o caminho da requisição que causou o erro.
     *
     * @return String que representa o caminho da requisição que causou o erro.
     */
    public String getPath() {
        return path;
    }

    /**
     * Define o caminho da requisição que causou o erro.
     *
     * @param path String que representa o caminho da requisição que causou o erro.
     */
    public void setPath(String path) {
        this.path = path;
    }
}
