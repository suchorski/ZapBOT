package com.suchorski.zapbot.services.commands.announce;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.models.commands.social.GuildBirthday;
import com.suchorski.zapbot.repositories.commands.announce.BirthdaysRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;

@Service
@Transactional
public class BirthdaysService extends AbstractService<BirthdaysRepository, GuildBirthday, Long> {
	
	@Autowired private BirthdaysRepository repository;
	
	@PostConstruct
	private void init() {
		setRepository(repository);
		setNothingFoundMessage("aniversário não encontrado");
	}
	
	@Transactional(readOnly = true)
	public List<GuildBirthday> findAllToAnnounce() {
		return repository.findAllByChannelIdNotNull();
	}

	public void setChannelToAnnounce(GuildBirthday birthday, long channelId) {
		birthday.setChannelId(channelId);
		update(birthday);
	}

	public void disableBirthday(GuildBirthday birthday) {
		birthday.setChannelId(null);
		update(birthday);
	}

	public void setBirthdayRole(GuildBirthday birthday, long roleId) {
		birthday.setRole(roleId);
		update(birthday);
	}

}
