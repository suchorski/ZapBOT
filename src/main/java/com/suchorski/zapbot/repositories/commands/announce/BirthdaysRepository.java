package com.suchorski.zapbot.repositories.commands.announce;

import java.util.List;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.suchorski.zapbot.models.commands.social.GuildBirthday;

@Repository
public interface BirthdaysRepository extends JpaRepositoryImplementation<GuildBirthday, Long> {

	public List<GuildBirthday> findAllByChannelIdNotNull();

}
