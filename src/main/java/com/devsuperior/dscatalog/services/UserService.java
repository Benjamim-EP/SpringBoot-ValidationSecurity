package com.devsuperior.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.RoleDTO;
import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.dto.UserUpdateDTO;
import com.devsuperior.dscatalog.entities.Role;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

/**
 * The UserService class is responsible for managing user-related operations.
 * It implements the UserDetailsService interface, which allows it to be used
 * as a service for Spring Security user authentication.
 */
@Service
public class UserService implements UserDetailsService {

	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository repository;

	@Autowired
	private RoleRepository roleRepository;

	/**
	 * Retrieves a paginated list of users.
	 *
	 * @param pageable The pagination configuration.
	 * @return A Page of UserDTO containing user information.
	 */
	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable) {
		Page<User> list = repository.findAll(pageable);
		return list.map(x -> new UserDTO(x));
	}

	/**
	 * Finds a user by its ID.
	 *
	 * @param id The ID of the user to be found.
	 * @return A UserDTO containing the user information.
	 * @throws ResourceNotFoundException If the user with the specified ID is not found.
	 */
	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> obj = repository.findById(id);
		User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new UserDTO(entity);
	}

	/**
	 * Inserts a new user into the database.
	 *
	 * @param dto The UserInsertDTO containing the user information to be inserted.
	 * @return A UserDTO containing the inserted user information.
	 */
	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		User entity = new User();
		copyDtoToEntity(dto, entity);
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		entity = repository.save(entity);
		return new UserDTO(entity);
	}

	/**
	 * Updates an existing user with the provided ID.
	 *
	 * @param id  The ID of the user to be updated.
	 * @param dto The UserUpdateDTO containing the updated user information.
	 * @return A UserDTO containing the updated user information.
	 * @throws ResourceNotFoundException If the user with the specified ID is not found.
	 */
	@Transactional
	public UserDTO update(Long id, UserUpdateDTO dto) {
		try {
			User entity = repository.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new UserDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	/**
	 * Deletes a user with the provided ID.
	 *
	 * @param id The ID of the user to be deleted.
	 * @throws ResourceNotFoundException If the user with the specified ID is not found.
	 * @throws DatabaseException        If the user cannot be deleted due to a database integrity violation.
	 */
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}

	/**
	 * Copies user information from a UserDTO to a User entity.
	 *
	 * @param dto    The UserDTO containing the information to be copied.
	 * @param entity The User entity to copy the information to.
	 */
	private void copyDtoToEntity(UserDTO dto, User entity) {

		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());

		entity.getRoles().clear();
		for (RoleDTO roleDto : dto.getRoles()) {
			Role role = roleRepository.getOne(roleDto.getId());
			entity.getRoles().add(role);
		}
	}

	/**
	 * Loads a user by its username (email) for Spring Security authentication.
	 *
	 * @param username The email of the user to be loaded.
	 * @return A UserDetails instance representing the authenticated user.
	 * @throws UsernameNotFoundException If the user with the specified email is not found.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = repository.findByEmail(username);
		if (user == null) {
			logger.error("User not found: " + username);
			throw new UsernameNotFoundException("Email not found");
		}
		logger.info("User found: " + username);
		return user;
	}
}
