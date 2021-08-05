package com.suchorski.zapbot.services.commands.social;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.models.commands.social.UserAutoReact;
import com.suchorski.zapbot.repositories.commands.social.AutoReactRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;

@Service
@Transactional
public class AutoReactService extends AbstractService<AutoReactRepository, UserAutoReact, Long> {
	
	@Autowired private AutoReactRepository repository;
	
	@PostConstruct
	private void init() {
		setRepository(repository);
		setNothingFoundMessage("auto reação não encontrada");
	}

	public void switchAutoReact(UserAutoReact autoReact) {
		autoReact.setEnabled(!autoReact.getEnabled());
		update(autoReact);
	}

	public void enableAutoReact(UserAutoReact autoReact, String emoji) {
		autoReact.setEnabled(true);
		autoReact.setEmoji(emoji);
		update(autoReact);
	}

}
