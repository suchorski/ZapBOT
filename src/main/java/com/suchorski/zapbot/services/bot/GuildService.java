package com.suchorski.zapbot.services.bot;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.bot.BotGuild;
import com.suchorski.zapbot.models.commands.announce.GuildAnnounce;
import com.suchorski.zapbot.models.commands.autoroles.GuildAutoRoles;
import com.suchorski.zapbot.models.commands.roles.GuildRoles;
import com.suchorski.zapbot.models.commands.social.GuildBirthday;
import com.suchorski.zapbot.repositories.bot.GuildRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;

@Service
@Transactional
public class GuildService extends AbstractService<GuildRepository, BotGuild, Long> {
	
	@Autowired private GuildRepository repository;
	
	@PostConstruct
	private void init() {
		setRepository(repository);
		setNothingFoundMessage("guilda não encontrada");
	}
	
	public BotGuild findOrCreate(long id) {
		try {
			return findById(id);
		} catch (NothingFoundException e) {
			BotGuild guild = new BotGuild(id);
			guild.setAnnounce(new GuildAnnounce(guild));
			guild.setAutoRoles(new GuildAutoRoles(guild));
			guild.setBirthday(new GuildBirthday(guild));
			guild.setRoles(new GuildRoles(guild));
			return create(guild);
		}
	}

	public void setAutoReact(BotGuild guild, boolean active) {
		guild.setAutoReact(active);
		update(guild);
	}

	public void switchAutoReact(BotGuild guild) {
		guild.switchAutoReact();
		update(guild);
	}
	
}
