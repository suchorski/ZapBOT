package com.suchorski.zapbot.services.commands.announce;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.announce.GuildAnnounce;
import com.suchorski.zapbot.models.commands.announce.GuildAnnounceField;
import com.suchorski.zapbot.repositories.commands.announce.AnnounceRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;

@Service
@Transactional
public class AnnounceService extends AbstractService<AnnounceRepository, GuildAnnounce, Long> {
	
	@Autowired private AnnounceRepository repository;
	
	@PostConstruct
	private void init() {
		setRepository(repository);
		setNothingFoundMessage("anúncio não encontrado");
	}

	public void setChannelId(GuildAnnounce announce, long channelId) {
		announce.setChannelId(channelId);
		update(announce);
	}

	public void setAuthor(GuildAnnounce announce, String author) {
		announce.setAuthor(author);
		update(announce);
	}
	
	public void setDescription(GuildAnnounce announce, String description) {
		announce.setDescription(description);
		update(announce);
	}
	
	public void setFooter(GuildAnnounce announce, String footer) {
		announce.setFooter(footer);
		update(announce);
	}
	
	public void addField(GuildAnnounce announce, GuildAnnounceField announceField) {
		announce.addField(announceField);
		update(announce);
	}

	public void delField(GuildAnnounce announce, GuildAnnounceField announceField) throws NothingFoundException {
		announce.delField(announceField);
		update(announce);
	}

}
