package com.suchorski.zapbot.repositories.commands.autoroles;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.suchorski.zapbot.models.commands.autoroles.GuildAutoRole;
import com.suchorski.zapbot.models.commands.autoroles.ids.GuildAutoRoleID;

@Repository
public interface AutoRoleRepository extends JpaRepositoryImplementation<GuildAutoRole, GuildAutoRoleID> {

}
