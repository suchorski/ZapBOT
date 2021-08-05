package com.suchorski.zapbot.services.bot;

import java.time.LocalDate;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.exceptions.NotEnoughCoinsException;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.bot.BotGuild;
import com.suchorski.zapbot.models.commands.announce.GuildAnnounce;
import com.suchorski.zapbot.models.commands.autoroles.GuildAutoRoles;
import com.suchorski.zapbot.models.commands.roles.GuildRoles;
import com.suchorski.zapbot.models.commands.social.GuildBirthday;
import com.suchorski.zapbot.models.commands.social.UserCoins;
import com.suchorski.zapbot.models.commands.statistics.GuildStatistic;
import com.suchorski.zapbot.models.commands.statistics.ids.GuildStatisticID;
import com.suchorski.zapbot.repositories.bot.GuildRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;
import com.suchorski.zapbot.services.commands.social.CoinsService;

@Service
@Transactional
public class GuildService extends AbstractService<GuildRepository, BotGuild, Long> {
	
	@Autowired private GuildRepository repository;
	@Autowired private CoinsService coinsService;
	
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

	public void addMemberJoinedToStatistics(BotGuild guild) {
		guild.getGuildStatistics().stream().filter(s -> s.getId().equals(new GuildStatisticID(LocalDate.now(), guild.getId()))).findFirst().ifPresentOrElse(s -> {
			s.addUsersJoined();
		}, () -> {
			guild.getGuildStatistics().add(new GuildStatistic(LocalDate.now(), guild, 1, 0));
		});
		update(guild);
	}

	public void addMemberLeftToStatistics(BotGuild guild) {
		guild.getGuildStatistics().stream().filter(s -> s.getId().equals(new GuildStatisticID(LocalDate.now(), guild.getId()))).findFirst().ifPresentOrElse(s -> {
			s.addUsersLeft();
		}, () -> {
			guild.getGuildStatistics().add(new GuildStatistic(LocalDate.now(), guild, 0, 1));
		});
		update(guild);
	}

	public void donate(BotGuild guild, UserCoins userCoins, long quantity) throws NotEnoughCoinsException {
		coinsService.subCoins(userCoins, quantity);
		guild.addCoinsDonated(quantity);
		update(guild);
	}

}
