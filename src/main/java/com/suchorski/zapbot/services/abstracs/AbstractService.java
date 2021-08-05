package com.suchorski.zapbot.services.abstracs;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.exceptions.NothingFoundException;

import lombok.Getter;
import lombok.Setter;

@Transactional
@Getter
@Setter
public abstract class AbstractService<R extends JpaRepositoryImplementation<E, ID>, E, ID> {

	private R repository;
	private String nothingFoundMessage = "não encontrado";

	public E create(E entity) {
		return repository.saveAndFlush(entity);
	}

	@Transactional(readOnly = true)
	public E findById(ID id) throws NothingFoundException {
		return repository.findById(id).orElseThrow(() -> new NothingFoundException(nothingFoundMessage));
	}

	@Transactional(readOnly = true)
	public List<E> findAll() {
		return repository.findAll();
	}

	public E update(E entity) {
		return repository.save(entity);
	}

	public void delete(E entity) {
		repository.delete(entity);
	}

	public void deleteById(ID id) {
		repository.deleteById(id);
	}

	public void deleteByIdWithoutException(ID id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException ignore) { }
	}

}
