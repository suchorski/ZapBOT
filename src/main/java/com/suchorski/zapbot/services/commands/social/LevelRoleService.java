package com.suchorski.zapbot.services.commands.social;

import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.models.commands.social.GuildLevelRole;
import com.suchorski.zapbot.models.commands.social.ids.GuildLevelRoleID;
import com.suchorski.zapbot.repositories.commands.social.LevelRoleRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;

@Service
@Transactional
public class LevelRoleService extends AbstractService<LevelRoleRepository, GuildLevelRole, GuildLevelRoleID> {
	
	@Autowired private LevelRoleRepository repository;
	
	@PostConstruct
	private void init() {
		setRepository(repository);
		setNothingFoundMessage("cargo de nível não encontrado");
	}

	@Transactional(readOnly = true)
	public Set<GuildLevelRole> findAllLevels(long guildId, int level) {
		return repository.findAllByIdGuildIdAndIdLevelLessThanEqual(guildId, level);
	}

	@Transactional(readOnly = true)
	public List<GuildLevelRole> findAllFromGuild(long guildId) {
		return repository.findAllByIdGuildId(guildId);
	}

}
