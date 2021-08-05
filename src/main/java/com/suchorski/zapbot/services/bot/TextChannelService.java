package com.suchorski.zapbot.services.bot;

import java.time.LocalDate;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.bot.BotGuild;
import com.suchorski.zapbot.models.bot.BotTextChannel;
import com.suchorski.zapbot.models.commands.statistics.TextChannelStatistic;
import com.suchorski.zapbot.models.commands.statistics.ids.TextChannelStatisticID;
import com.suchorski.zapbot.repositories.bot.TextChannelRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;

@Service
@Transactional
public class TextChannelService extends AbstractService<TextChannelRepository, BotTextChannel, Long> {
	
	@Value("${bot.youtube.subscribe.secret}") private String youtubeSubscribeSecret;
	@Value("${bot.url.api}") private String url;
	
	@Autowired private TextChannelRepository repository;
	
	@PostConstruct
	private void init() {
		setRepository(repository);
		setNothingFoundMessage("canal de texto não encontrado");
	}
	
	@Override
	public void delete(BotTextChannel entity) {
		entity.setUrl(url);
		entity.setYoutubeSubscribeSecret(youtubeSubscribeSecret);
		super.delete(entity);
	}
	
	@Override
	public void deleteById(Long id) {
		try {
			delete(findById(id));
		} catch (NothingFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void deleteByIdWithoutException(Long id) {
		try {
			delete(findById(id));
		} catch (NothingFoundException ignore) { }
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

	public void switchOnlyLastMessage(BotTextChannel textChannel) {
		textChannel.switchOnlyLastMessage();
	}
	
	public void addMessageReceivedToStatistics(BotTextChannel textChannel) {
		textChannel.getTextChannelStatistics().stream().filter(s -> s.getId().equals(new TextChannelStatisticID(LocalDate.now(), textChannel.getId()))).findFirst().ifPresentOrElse(s -> {
			s.addMessageReceived();
		}, () -> {
			textChannel.getTextChannelStatistics().add(new TextChannelStatistic(LocalDate.now(), textChannel, 1, 0));
		});
		update(textChannel);
	}
	
	public void addMessageDeletedToStatistics(BotTextChannel textChannel) {
		textChannel.getTextChannelStatistics().stream().filter(s -> s.getId().equals(new TextChannelStatisticID(LocalDate.now(), textChannel.getId()))).findFirst().ifPresentOrElse(s -> {
			s.addMessageDeleted();
		}, () -> {
			textChannel.getTextChannelStatistics().add(new TextChannelStatistic(LocalDate.now(), textChannel, 0, 1));
		});
		update(textChannel);
	}

}
