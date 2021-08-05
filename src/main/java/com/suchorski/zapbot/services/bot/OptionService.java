package com.suchorski.zapbot.services.bot;

import java.time.LocalDateTime;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.bot.BotOption;
import com.suchorski.zapbot.repositories.bot.OptionRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;

@Service
@Transactional
public class OptionService extends AbstractService<OptionRepository, BotOption, Integer> {
	
	private static Integer ID_OPTION = 1;
	
	@Autowired private OptionRepository repository;
	
	@PostConstruct
	private void init() {
		setRepository(repository);
		setNothingFoundMessage("opção não encontrada");
	}
	
	public BotOption findOrCreate() {
		try {
			return findById(ID_OPTION);
		} catch (NothingFoundException e) {
			return create(new BotOption());
		}
	}
	
	public void updateVersion(BotOption option, String version, long build) {
		option.setVersion(version);
		option.setBuild(build);
		option.setLastUpdate(LocalDateTime.now());
		update(option);
	}

}
