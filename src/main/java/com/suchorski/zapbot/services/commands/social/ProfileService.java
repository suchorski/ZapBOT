package com.suchorski.zapbot.services.commands.social;

import java.awt.Color;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.exceptions.NotEnoughCoinsException;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.social.UserCoins;
import com.suchorski.zapbot.models.commands.social.UserProfile;
import com.suchorski.zapbot.models.commands.social.UserProfileBackground;
import com.suchorski.zapbot.repositories.commands.social.ProfileRepository;
import com.suchorski.zapbot.services.abstracs.AbstractService;

@Service
@Transactional
public class ProfileService extends AbstractService<ProfileRepository, UserProfile, Long> {
	
	@Autowired private ProfileRepository repository;
	@Autowired private CoinsService coinsService;
	
	@PostConstruct
	private void init() {
		setRepository(repository);
		setNothingFoundMessage("perfil não encontrado");
	}

	public void changeProfileColor(UserProfile profile, Color color) throws NotEnoughCoinsException {
		coinsService.subCoins(profile.getUser().getCoins(), Constants.COSTS.COLOR);
		profile.setColor(color);
		update(profile);
	}

	public void changeMessage(UserProfile profile, String message) {
		profile.setMessage(message);
		update(profile);
	}

	public void recommend(UserProfile recommendedProfile, UserCoins userCoins) throws NotEnoughCoinsException {
		coinsService.subCoins(userCoins, Constants.COSTS.RECOMMENDATION);
		recommendedProfile.recommend();
		update(recommendedProfile);
	}

	public void set(UserProfile profile, UserProfileBackground profileBackground) throws NothingFoundException {
		if (profile.getUser().getBackgrounds().contains(profileBackground)) {
			profile.setBackground(profileBackground);
			update(profile);
		} else {
			throw new NothingFoundException("você não possui esse fundo de perfil");
		}
	}

}
