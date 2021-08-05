package com.suchorski.zapbot.models.commands.social;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.suchorski.zapbot.models.bot.BotGuild;
import com.suchorski.zapbot.models.commands.social.ids.GuildLevelRoleID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "guild_levelrole")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class GuildLevelRole implements Serializable {
	
	private static final long serialVersionUID = 1600312704794496483L;

	@EmbeddedId
	private GuildLevelRoleID id;
	
	@Column(length = 64, updatable = false, nullable = false)
	private String role;
	
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_guildlevelrole_guild"))
	@MapsId("guildId")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private BotGuild guild;
	
	public GuildLevelRole(int level, BotGuild guild, String role) {
		this.id = new GuildLevelRoleID(level, guild.getId());
		this.role = role;
		guild.addLevelRole(this);
	}
	
}
