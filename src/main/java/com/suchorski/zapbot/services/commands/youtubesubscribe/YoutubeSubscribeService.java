package com.suchorski.zapbot.services.commands.youtubesubscribe;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.models.commands.youtubesubscribe.TextChannelYoutubeSubscribe;
import com.suchorski.zapbot.models.commands.youtubesubscribe.ids.TextChannelYoutubeSubscribeID;
import com.suchorski.zapbot.repositories.commands.youtubesubscribe.YoutubeSubscribeRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;

@Service
@Transactional
public class YoutubeSubscribeService extends AbstractService<YoutubeSubscribeRepository, TextChannelYoutubeSubscribe, TextChannelYoutubeSubscribeID> {
	
	@Autowired private YoutubeSubscribeRepository repository;
	
	@PostConstruct
	private void init() {
		setRepository(repository);
		setNothingFoundMessage("canal não encontrado");
	}

	@Transactional(readOnly = true)
	public List<TextChannelYoutubeSubscribe> findAllFromYoutubeChannelId(String youtubeChannelId) {
		return repository.findAllByIdYoutubeChannelId(youtubeChannelId);
	}

	public List<TextChannelYoutubeSubscribe> findAllFromGuildId(long guildId) {
		return repository.findAllByTextChannelGuildId(guildId);
	}

}
