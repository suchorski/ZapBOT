package com.suchorski.zapbot.services.bot;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.bot.BotGuild;
import com.suchorski.zapbot.models.bot.BotTextChannel;
import com.suchorski.zapbot.repositories.bot.TextChannelRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;

@Service
@Transactional
public class TextChannelService extends AbstractService<TextChannelRepository, BotTextChannel, Long> {
	
	@Autowired private TextChannelRepository repository;
	
	@PostConstruct
	private void init() {
		setRepository(repository);
		setNothingFoundMessage("canal de texto não encontrado");
	}
	
	public BotTextChannel findOrCreate(long id, BotGuild guild) {
		try {
			return findById(id);
		} catch (NothingFoundException e) {
			BotTextChannel textChannel = new BotTextChannel(id);
			guild.addTextChannel(textChannel);
			return create(textChannel);
		}
	}

	public void switchOnlyLastMessage(BotTextChannel serverChannel) {
		serverChannel.switchOnlyLastMessage();
	}

}
