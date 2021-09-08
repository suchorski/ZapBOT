package com.suchorski.zapbot.services.commands.social;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.models.bot.BotGuild;
import com.suchorski.zapbot.models.commands.social.UserBirthday;
import com.suchorski.zapbot.repositories.commands.social.BirthdayRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;

@Service
@Transactional
public class BirthdayService extends AbstractService<BirthdayRepository, UserBirthday, Long> {

	@Autowired private BirthdayRepository repository;

	@PostConstruct
	private void init() {
		setRepository(repository);
		setNothingFoundMessage("aniversário não encontrado");
	}

	@Transactional(readOnly = true)
	public List<UserBirthday> findBirthdays(BotGuild g) {
		return repository.findBirthdays(g.getId(), LocalDate.now().getDayOfMonth(), LocalDate.now().getMonthValue());
	}

	public void setBirthday(UserBirthday birthday, LocalDate date) throws Exception {
		if (birthday.getLastSet().plus(180, ChronoUnit.DAYS).isBefore(Instant.now())) {
			birthday.setLastSet(Instant.now());
			birthday.setDate(date);
			update(birthday);
		} else {
			throw new Exception("Você só pode alterar a data de aniversário uma vez a cada 6 meses");
		}
	}

}
