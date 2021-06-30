package com.suchorski.zapbot.repositories.bot;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.suchorski.zapbot.models.bot.BotTextChannel;

@Repository
public interface TextChannelRepository extends JpaRepositoryImplementation<BotTextChannel, Long> {

}
