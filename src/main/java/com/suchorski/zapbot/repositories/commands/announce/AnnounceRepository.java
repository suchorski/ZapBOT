package com.suchorski.zapbot.repositories.commands.announce;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.suchorski.zapbot.models.commands.announce.GuildAnnounce;

@Repository
public interface AnnounceRepository extends JpaRepositoryImplementation<GuildAnnounce, Long> {

}
