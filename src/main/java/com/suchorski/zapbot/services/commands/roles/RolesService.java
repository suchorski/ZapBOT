package com.suchorski.zapbot.services.commands.roles;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.models.commands.roles.GuildRoles;
import com.suchorski.zapbot.repositories.commands.roles.RolesRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;

@Service
@Transactional
public class RolesService extends AbstractService<RolesRepository, GuildRoles, Long> {
	
	@Autowired private RolesRepository repository;
	
	@PostConstruct
	private void init() {
		setRepository(repository);
		setNothingFoundMessage("cargos não encontrados");
	}
	
	public void setAdministratorRole(GuildRoles roles, String name) {
		roles.setAdmin(name);
		update(roles);
	}
	
	public void setOperatorRole(GuildRoles roles, String name) {
		roles.setOperator(name);
		update(roles);
	}
	
	public void setHelperRole(GuildRoles roles, String name) {
		roles.setHelper(name);
		update(roles);
	}

}
