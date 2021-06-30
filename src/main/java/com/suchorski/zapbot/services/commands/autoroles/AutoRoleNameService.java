package com.suchorski.zapbot.services.commands.autoroles;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.autoroles.GuildAutoRoleName;
import com.suchorski.zapbot.models.commands.autoroles.GuildAutoRoles;
import com.suchorski.zapbot.models.commands.autoroles.ids.GuildAutoRoleNameID;
import com.suchorski.zapbot.repositories.commands.autoroles.AutoRoleNameRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;

@Service
@Transactional
public class AutoRoleNameService extends AbstractService<AutoRoleNameRepository, GuildAutoRoleName, GuildAutoRoleNameID> {
	
	@Autowired private AutoRoleNameRepository repository;
	
	@PostConstruct
	private void init() {
		setRepository(repository);
		setNothingFoundMessage("nome para auto cargo não encontrado");
	}
	
	public GuildAutoRoleName findOrCreate(GuildAutoRoleNameID id, GuildAutoRoles autoRoles) {
		try {
			return findById(id);
		} catch (NothingFoundException e) {
			return create(new GuildAutoRoleName(id.getName(), autoRoles));
		}
	}

	@Transactional(readOnly = true)
	public List<GuildAutoRoleName> findAllFromGuildId(long guildId) {
		return repository.findAllByIdGuildId(guildId);
	}

	public void setMessageId(GuildAutoRoleName autoRoleName, long messageId) {
		autoRoleName.setMessageId(messageId);
		update(autoRoleName);
	}

}
