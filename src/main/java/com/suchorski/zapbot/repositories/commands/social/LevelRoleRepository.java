package com.suchorski.zapbot.repositories.commands.social;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.suchorski.zapbot.models.commands.social.GuildLevelRole;
import com.suchorski.zapbot.models.commands.social.ids.GuildLevelRoleID;

@Repository
public interface LevelRoleRepository extends JpaRepositoryImplementation<GuildLevelRole, GuildLevelRoleID> {

	public List<GuildLevelRole> findAllByIdGuildId(long guildId);

	public Set<GuildLevelRole> findAllByIdGuildIdAndIdLevelLessThanEqual(long guildId, int level);

}
