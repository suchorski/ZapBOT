package com.suchorski.zapbot.repositories.bot;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.models.bot.BotUser;

@Repository
public interface UserRepository extends JpaRepositoryImplementation<BotUser, Long> {

	public List<BotUser> findByRafflesQuantityGreaterThan(int i);
	
	@Modifying
	@Query("update user u set u.raffles.quantity = 0")
	public void resetRaffles();

	@Modifying
	@Query("update user u set u.premiumCoins = u.premiumCoins - " + Constants.PREMIUM.USER_COINS + " where u.premiumCoins >= " + Constants.PREMIUM.USER_COINS)
	public void withdrawPremium();

}
