package com.bookCatalog.bookcatalog.services.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Uma anotação usada para realizar validações em atualizações de usuários.
 * 
 * Essa anotação é utilizada para marcar uma classe que representa uma atualização de usuário, e indica que a validação deve ser feita utilizando a classe UserUpdateValidator.
 * A classe UserUpdateValidator será responsável por lidar com a lógica de validação com base nas restrições especificadas nesta anotação.
 * 
 * Exemplo de uso:
 * 
 * ```
 * @UserUpdateValid
 * public class UserUpdateDTO {
 *     // Classe que representa os dados da atualização do usuário.
 *     // Adicione campos e anotações de validação conforme necessário.
 * }
 * ```
 * 
 * Restrições:
 * - A classe anotada deve ser um DTO (Objeto de Transferência de Dados) que representa os dados da atualização do usuário.
 * - A classe UserUpdateValidator será responsável pela validação com base nas restrições especificadas nesta anotação.
 * 
 * Atributos:
 * - message: Uma mensagem de erro personalizada que será exibida se a validação falhar. Padrão: "Erro de validação".
 * - groups: Uma lista de classes de grupos de validação a que essa restrição pertence. Padrão: lista vazia.
 * - payload: Uma lista de classes de dados para metadados. Padrão: lista vazia.
 * 
 * Exemplo de uso:
 * 
 * ```
 * @UserUpdateValid(message = "Atualização de usuário inválida", groups = ValidationGroup.class)
 * public class UserUpdateDTO {
 *     // Classe que representa os dados da atualização do usuário.
 *     // Adicione campos e anotações de validação conforme necessário.
 * }
 * ```
 * 
 * @see UserUpdateValidator
 * @see javax.validation.Constraint
 */
@Constraint(validatedBy = UserUpdateValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserUpdateValid {
    /**
     * Especifica a mensagem de erro personalizada para falhas de validação.
     * 
     * @return A mensagem de erro personalizada.
     */
    String message() default "Erro de validação";

    /**
     * Especifica os grupos de validação a que essa restrição pertence.
     * 
     * @return Os grupos de validação.
     */
    Class<?>[] groups() default {};

    /**
     * Especifica as classes de dados para metadados.
     * 
     * @return As classes de dados para metadados.
     */
    Class<? extends Payload>[] payload() default {};
}
