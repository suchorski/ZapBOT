package com.suchorski.zapbot.repositories.commands.roles;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.suchorski.zapbot.models.commands.roles.GuildRoles;

@Repository
public interface RolesRepository extends JpaRepositoryImplementation<GuildRoles, Long> {

}
