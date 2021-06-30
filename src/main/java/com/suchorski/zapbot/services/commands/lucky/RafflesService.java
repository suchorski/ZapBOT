package com.suchorski.zapbot.services.commands.lucky;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.models.commands.lucky.UserRaffles;
import com.suchorski.zapbot.repositories.commands.lucky.RafflesRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;

@Service
@Transactional
public class RafflesService extends AbstractService<RafflesRepository, UserRaffles, Long> {
	
	@Autowired private RafflesRepository repository;
	
	@PostConstruct
	private void init() {
		setRepository(repository);
		setNothingFoundMessage("rifas não encontrada");
	}

}
