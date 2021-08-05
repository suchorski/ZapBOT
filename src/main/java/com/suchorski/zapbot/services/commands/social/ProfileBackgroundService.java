package com.suchorski.zapbot.services.commands.social;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.models.commands.social.UserProfileBackground;
import com.suchorski.zapbot.repositories.commands.social.ProfileBackgroundRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;

@Service
@Transactional
public class ProfileBackgroundService extends AbstractService<ProfileBackgroundRepository, UserProfileBackground, Integer> {
	
	@Autowired private ProfileBackgroundRepository repository;
	
	@PostConstruct
	private void init() {
		setRepository(repository);
		setNothingFoundMessage("fundo de perfil não encontrado");
	}
	
	@Transactional(readOnly = true)
	public UserProfileBackground getDefault() {
		return repository.findByName("Padrão").orElse(null);
	}

	@Transactional(readOnly = true)
	public List<UserProfileBackground> listAvailableToBuy() {
		return repository.findAllByVisibleTrue();
	}

}
