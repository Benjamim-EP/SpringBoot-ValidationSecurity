/**
 * Essa classe representa os endpoints RESTful da API para gerenciamento de usuários.
 */
package com.devsuperior.dscatalog.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.dto.UserUpdateDTO;
import com.devsuperior.dscatalog.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

	@Autowired
	private UserService service;
	
	/**
	 * Recupera uma lista paginada de usuários.
	 *
	 * @param pageable Informações de paginação (número da página, tamanho, ordenação).
	 * @return Um ResponseEntity contendo a lista paginada de usuários e um status 200 (OK).
	 */
	@GetMapping
	public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable) {
		Page<UserDTO> list = service.findAllPaged(pageable);		
		return ResponseEntity.ok().body(list);
	}

	/**
	 * Recupera um usuário específico pelo seu ID.
	 *
	 * @param id O ID do usuário a ser recuperado.
	 * @return Um ResponseEntity contendo o DTO do usuário e um status 200 (OK) se encontrado,
	 *         ou um status 404 (Não Encontrado) se o usuário não for encontrado.
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
		UserDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
	
	/**
	 * Cria um novo usuário com as informações fornecidas.
	 *
	 * @param dto O DTO contendo as informações do usuário a ser inserido.
	 * @return Um ResponseEntity contendo o novo DTO do usuário criado e um status 201 (Criado),
	 *         juntamente com a URI do novo usuário criado no cabeçalho Location.
	 */
	@PostMapping
	public ResponseEntity<UserDTO> insert(@RequestBody @Valid UserInsertDTO dto) {
		UserDTO newDto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(newDto.getId()).toUri();
		return ResponseEntity.created(uri).body(newDto);
	}

	/**
	 * Atualiza um usuário existente com as informações fornecidas.
	 *
	 * @param id  O ID do usuário a ser atualizado.
	 * @param dto O DTO contendo as informações atualizadas do usuário.
	 * @return Um ResponseEntity contendo o novo DTO do usuário atualizado e um status 200 (OK) se atualizado com sucesso,
	 *         ou um status 404 (Não Encontrado) se o usuário não for encontrado.
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody @Valid UserUpdateDTO dto) {
		UserDTO newDto = service.update(id, dto);
		return ResponseEntity.ok().body(newDto);
	}

	/**
	 * Exclui um usuário com o ID especificado.
	 *
	 * @param id O ID do usuário a ser excluído.
	 * @return Um ResponseEntity com um status 204 (Sem Conteúdo) se o usuário for excluído com sucesso,
	 *         ou um status 404 (Não Encontrado) se o usuário não for encontrado.
	 */
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
