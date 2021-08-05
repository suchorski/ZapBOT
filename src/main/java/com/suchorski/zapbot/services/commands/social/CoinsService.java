package com.suchorski.zapbot.services.commands.social;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.constants.Constants.COINS;
import com.suchorski.zapbot.exceptions.NotEnoughCoinsException;
import com.suchorski.zapbot.models.commands.social.UserCoins;
import com.suchorski.zapbot.repositories.commands.social.CoinsRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;

@Service
@Transactional
public class CoinsService extends AbstractService<CoinsRepository, UserCoins, Long> {

	@Autowired private CoinsRepository repository;

	@PostConstruct
	private void init() {
		setRepository(repository);
		setNothingFoundMessage("moedas não encontradas");
	}

	public void addCoins(UserCoins coins, long quantity) {
		coins.addCoins(quantity);
		update(coins);
	}

	public void subCoins(UserCoins coins, long quantity) throws NotEnoughCoinsException {
		coins.subCoins(quantity);
		update(coins);		
	}

	@Transactional(readOnly = true)
	public List<UserCoins> top3() {
		return repository.findFirst3ByOrderByQuantityDesc();
	}

	public long getCoins(UserCoins coins) throws Exception {
		long random = (new Random()).nextInt(COINS.MAX - COINS.MIN) + COINS.MIN;
		if (coins.getLast().plus(1, ChronoUnit.HOURS).isBefore(Instant.now())) {
			coins.setLast(Instant.now());
			addCoins(coins, random);
			coins.getUser().addPremiumCoins();
			if (coins.getUser().isPremium()) {
				return (long) (random * 1.2f);
			}
			return random;
		}
		throw new Exception("Você só pode pegar moedas uma vez por hora.");
	}

}
