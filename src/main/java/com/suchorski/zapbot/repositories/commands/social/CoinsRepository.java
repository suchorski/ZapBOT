package com.suchorski.zapbot.repositories.commands.social;

import java.util.List;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.suchorski.zapbot.models.commands.social.UserCoins;

@Repository
public interface CoinsRepository extends JpaRepositoryImplementation<UserCoins, Long> {

	public List<UserCoins> findFirst3ByOrderByQuantityDesc();

}
