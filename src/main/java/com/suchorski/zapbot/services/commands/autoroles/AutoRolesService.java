package com.suchorski.zapbot.services.commands.autoroles;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.models.commands.autoroles.GuildAutoRole;
import com.suchorski.zapbot.models.commands.autoroles.GuildAutoRoles;
import com.suchorski.zapbot.repositories.commands.autoroles.AutoRolesRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;

@Service
@Transactional
public class AutoRolesService extends AbstractService<AutoRolesRepository, GuildAutoRoles, Long> {
	
	@Autowired private AutoRolesRepository repository;
	
	@PostConstruct
	private void init() {
		setRepository(repository);
		setNothingFoundMessage("cargos automáticos não encontrado");
	}

	public void setAutoRole(GuildAutoRoles autoRoles, String name) {
		autoRoles.setAutoRole(name);
		update(autoRoles);
	}

	public void unsetAutoRole(GuildAutoRoles autoRoles) {
		autoRoles.setAutoRole(null);
		update(autoRoles);
	}

	public void addAutoRole(GuildAutoRoles autoRoles, GuildAutoRole autoRole) {
		autoRoles.addAutoRole(autoRole);
		update(autoRoles);
	}

}
