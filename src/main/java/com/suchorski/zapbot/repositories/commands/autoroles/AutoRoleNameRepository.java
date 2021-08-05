package com.suchorski.zapbot.repositories.commands.autoroles;

import java.util.List;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.suchorski.zapbot.models.commands.autoroles.GuildAutoRoleName;
import com.suchorski.zapbot.models.commands.autoroles.ids.GuildAutoRoleNameID;

@Repository
public interface AutoRoleNameRepository extends JpaRepositoryImplementation<GuildAutoRoleName, GuildAutoRoleNameID> {

	public List<GuildAutoRoleName> findAllByIdGuildId(long guildId);

}
