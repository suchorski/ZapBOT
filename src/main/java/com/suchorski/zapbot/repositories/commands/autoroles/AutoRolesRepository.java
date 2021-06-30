package com.suchorski.zapbot.repositories.commands.autoroles;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.suchorski.zapbot.models.commands.autoroles.GuildAutoRoles;

@Repository
public interface AutoRolesRepository extends JpaRepositoryImplementation<GuildAutoRoles, Long> {

}
