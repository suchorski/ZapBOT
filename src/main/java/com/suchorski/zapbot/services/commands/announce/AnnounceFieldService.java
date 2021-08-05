package com.suchorski.zapbot.services.commands.announce;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.models.commands.announce.GuildAnnounceField;
import com.suchorski.zapbot.models.commands.announce.ids.GuildAnnounceFieldID;
import com.suchorski.zapbot.repositories.commands.announce.AnnounceFieldRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;

@Service
@Transactional
public class AnnounceFieldService extends AbstractService<AnnounceFieldRepository, GuildAnnounceField, GuildAnnounceFieldID> {
	
	@Autowired private AnnounceFieldRepository repository;
	
	@PostConstruct
	private void init() {
		setRepository(repository);
		setNothingFoundMessage("campo de anúncio não encontrado");
	}

}
