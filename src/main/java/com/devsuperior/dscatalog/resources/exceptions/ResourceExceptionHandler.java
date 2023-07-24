package com.devsuperior.dscatalog.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

/**
 * A classe ResourceExceptionHandler é um Controller Advice que lida e fornece respostas padronizadas de erro para várias exceções que podem ocorrer durante as solicitações da API.
 *
 * Ela trata três tipos de exceções:
 * - ResourceNotFoundException: Quando um recurso solicitado não é encontrado no sistema.
 * - DatabaseException: Quando ocorre um erro relacionado ao banco de dados.
 * - MethodArgumentNotValidException: Quando ocorrem erros de validação durante o processamento da carga de dados da solicitação.
 */
@ControllerAdvice
public class ResourceExceptionHandler {

	/**
	 * Manipula a ResourceNotFoundException e retorna um ResponseEntity com um objeto de erro padronizado.
	 *
	 * @param e       O objeto ResourceNotFoundException gerado.
	 * @param request O objeto HttpServletRequest que representa a solicitação atual.
	 * @return Um ResponseEntity com o objeto StandardError contendo os detalhes do erro.
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Recurso não encontrado");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	/**
	 * Manipula a DatabaseException e retorna um ResponseEntity com um objeto de erro padronizado.
	 *
	 * @param e       O objeto DatabaseException gerado.
	 * @param request O objeto HttpServletRequest que representa a solicitação atual.
	 * @return Um ResponseEntity com o objeto StandardError contendo os detalhes do erro.
	 */
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> database(DatabaseException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Exceção do banco de dados");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	/**
	 * Manipula a MethodArgumentNotValidException e retorna um ResponseEntity com um objeto de erro padronizado contendo erros de validação.
	 *
	 * @param e       O objeto MethodArgumentNotValidException gerado.
	 * @param request O objeto HttpServletRequest que representa a solicitação atual.
	 * @return Um ResponseEntity com o objeto ValidationError contendo os detalhes do erro de validação.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		ValidationError err = new ValidationError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Exceção de validação");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());

		// Extrai e adiciona os erros de validação de campos ao objeto ValidationError
		for (FieldError f : e.getBindingResult().getFieldErrors()) {
			err.addError(f.getField(), f.getDefaultMessage());
		}

		return ResponseEntity.status(status).body(err);
	}
}
