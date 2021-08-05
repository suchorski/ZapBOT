package com.suchorski.zapbot.repositories.commands.lucky;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.suchorski.zapbot.models.commands.lucky.UserRaffles;

@Repository
public interface RafflesRepository extends JpaRepositoryImplementation<UserRaffles, Long> {

}
