package com.suchorski.zapbot.services.bot;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.exceptions.BannedException;
import com.suchorski.zapbot.exceptions.NotEnoughCoinsException;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.exceptions.SomethingFoundException;
import com.suchorski.zapbot.models.bot.BotUser;
import com.suchorski.zapbot.models.commands.lucky.UserRaffles;
import com.suchorski.zapbot.models.commands.social.UserAutoReact;
import com.suchorski.zapbot.models.commands.social.UserBirthday;
import com.suchorski.zapbot.models.commands.social.UserCoins;
import com.suchorski.zapbot.models.commands.social.UserProfile;
import com.suchorski.zapbot.models.commands.social.UserProfileBackground;
import com.suchorski.zapbot.repositories.bot.UserRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;
import com.suchorski.zapbot.services.commands.social.ProfileBackgroundService;

@Service
@Transactional
public class UserService extends AbstractService<UserRepository, BotUser, Long> {
	
	@Autowired private UserRepository repository;
	@Autowired private ProfileBackgroundService profileBackgroundService;
	
	@PostConstruct
	public void init() {
		setRepository(repository);
		setNothingFoundMessage("usuário não encontrado");
	}
	
	public BotUser findOrCreate(long id) {
		try {
			return findById(id);
		} catch (NothingFoundException e) {
			UserProfileBackground defaultProfileBackground = profileBackgroundService.getDefault();
			BotUser user = new BotUser(id);
			user.setAutoReact(new UserAutoReact(user));
			user.setBirthday(new UserBirthday(user));
			user.setCoins(new UserCoins(user));
			user.getBackgrounds().add(defaultProfileBackground);
			user.setProfile(new UserProfile(user, defaultProfileBackground));
			user.setRaffles(new UserRaffles(user));
			return create(user);
		}
	}
	
	public void raffle() {
		List<BotUser> usersWithRaffle = repository.findByRafflesQuantityGreaterThan(0);
		if (usersWithRaffle.size() > 0) {
			IntStream raffles = usersWithRaffle.stream().mapToInt(u -> u.getRaffles().getQuantity());
			int total = raffles.sum();
			int ticket = (new Random()).nextInt(total);
			Iterator<BotUser> lucky = usersWithRaffle.iterator();
			BotUser u = usersWithRaffle.get(0);
			while (ticket > 0) {
				if (lucky.hasNext()) {
					u = lucky.next();
					ticket -= u.getRaffles().getQuantity();
				} else {
					break;
				}
			}
			u.getCoins().addCoins(Constants.COSTS.RAFFLE * total);
			update(u);
			repository.resetRaffles();
		}
	}

	public void setMultiplier(BotUser user, float multiplier) {
		user.setXpMultiplier(multiplier);
		update(user);
	}

	public void buyRaffles(BotUser user, int quantity) throws NotEnoughCoinsException {
		long coins = quantity * Constants.COSTS.RAFFLE;
		user.getCoins().subCoins(coins);
		user.getRaffles().addRaflles(quantity);
		update(user);
	}

	public void checkBanned(BotUser user) throws BannedException {
		if (user.getBanned()) {
			throw new BannedException("você não pode usar comandos do BOT");
		}
	}

	public void ban(BotUser user) {
		user.setBanned(true);
		update(user);
	}
	
	public void unban(BotUser user) {
		user.setBanned(false);
		update(user);
	}


	public void buyProfileBackground(BotUser user, UserProfileBackground profileBackground) throws SomethingFoundException, NotEnoughCoinsException {
		if (!user.getBackgrounds().contains(profileBackground)) {
			user.getCoins().subCoins(profileBackground.getPrice());
			user.getBackgrounds().add(profileBackground);
			update(user);
		} else {
			throw new SomethingFoundException("você já possui esse fundo de perfil");
		}
	}

	public void payForPremium() {
		repository.withdrawPremium();
	}

}
