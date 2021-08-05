package com.suchorski.zapbot.services.commands.autoroles;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.models.commands.autoroles.GuildAutoRole;
import com.suchorski.zapbot.models.commands.autoroles.ids.GuildAutoRoleID;
import com.suchorski.zapbot.repositories.commands.autoroles.AutoRoleRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;

@Service
@Transactional
public class AutoRoleService extends AbstractService<AutoRoleRepository, GuildAutoRole, GuildAutoRoleID> {
	
	@Autowired private AutoRoleRepository repository;
	
	@PostConstruct
	private void init() {
		setRepository(repository);
		setNothingFoundMessage("auto cargo não encontrado");
	}

}
