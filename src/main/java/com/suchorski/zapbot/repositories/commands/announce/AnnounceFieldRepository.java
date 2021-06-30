package com.suchorski.zapbot.repositories.commands.announce;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.suchorski.zapbot.models.commands.announce.GuildAnnounceField;
import com.suchorski.zapbot.models.commands.announce.ids.GuildAnnounceFieldID;

@Repository
public interface AnnounceFieldRepository extends JpaRepositoryImplementation<GuildAnnounceField, GuildAnnounceFieldID> {

}
