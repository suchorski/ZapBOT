package com.suchorski.zapbot.models.commands.autoroles;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.suchorski.zapbot.models.commands.autoroles.ids.GuildAutoRoleID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "guild_autorole")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class GuildAutoRole implements Serializable {

	private static final long serialVersionUID = 8403845008100806682L;

	@EmbeddedId
	private GuildAutoRoleID id;

	@Column(length = 64, updatable = false, nullable = false)
	private String role;

	@ManyToOne(optional = false)
	@JoinColumns({
		@JoinColumn(name = "guild_autorole_name_id", referencedColumnName = "name"),
		@JoinColumn(name = "guild_autorole_guild_id", referencedColumnName = "guildId")
	})
	@OnDelete(action = OnDeleteAction.CASCADE)
	private GuildAutoRoleName name;

	@ManyToOne(optional = false)
	@JoinColumn(name = "guild_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@MapsId("guildId")
	private GuildAutoRoles autoRoles;
	
	public GuildAutoRole(String emoji, GuildAutoRoles autoRoles, String role, GuildAutoRoleName name) {
		this.id = new GuildAutoRoleID(emoji, autoRoles.getId());
		this.role = role;
		autoRoles.addAutoRole(this);
		setAutoRoleName(name);
	}

	public void setAutoRoleName(GuildAutoRoleName name) {
		name.setAutoRoles(autoRoles);
		this.name = name;
	}

}
