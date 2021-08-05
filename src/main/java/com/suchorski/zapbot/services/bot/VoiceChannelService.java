package com.suchorski.zapbot.services.bot;

import java.time.LocalDate;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.bot.BotGuild;
import com.suchorski.zapbot.models.bot.BotVoiceChannel;
import com.suchorski.zapbot.models.commands.statistics.VoiceChannelStatistic;
import com.suchorski.zapbot.models.commands.statistics.ids.VoiceChannelStatisticID;
import com.suchorski.zapbot.repositories.bot.VoiceChannelRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;

@Service
@Transactional
public class VoiceChannelService extends AbstractService<VoiceChannelRepository, BotVoiceChannel, Long> {
	
	@Autowired private VoiceChannelRepository repository;
	
	@PostConstruct
	private void init() {
		setRepository(repository);
		setNothingFoundMessage("canal de voz não encontrado");
	}
	
	public BotVoiceChannel findOrCreate(long id, BotGuild guild) {
		try {
			return findById(id);
		} catch (NothingFoundException e) {
			BotVoiceChannel voiceChannel = new BotVoiceChannel(id);
			guild.addVoiceChannel(voiceChannel);
			return create(voiceChannel);
		}
	}

	public void addMinutesToStatistics(BotVoiceChannel voiceChannel, int minutes) {
		voiceChannel.getVoiceChannelStatistics().stream().filter(s -> s.getId().equals(new VoiceChannelStatisticID(LocalDate.now(), voiceChannel.getId()))).findFirst().ifPresentOrElse(s -> {
			s.addMinutes(minutes);
		}, () -> {
			voiceChannel.getVoiceChannelStatistics().add(new VoiceChannelStatistic(LocalDate.now(), voiceChannel, minutes));
		});
		update(voiceChannel);
	}

}
