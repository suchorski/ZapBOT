package com.suchorski.zapbot.models.commands.autoroles;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
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

	private static final long serialVersionUID = -24083532205988998L;

	@EmbeddedId
	private GuildAutoRoleID id;

	@Column(length = 64, updatable = false, nullable = false)
	private String role;
	
	@Column(name = "name_id", length = 32, updatable = false, nullable = false)
	private String nameId;

	@ManyToOne(optional = false)
	@JoinColumns(value = {
		@JoinColumn(name = "name_id", referencedColumnName = "name", insertable = false, updatable = false),
		@JoinColumn(name = "guildId", referencedColumnName = "guildId", insertable = false, updatable = false)
	}, foreignKey = @ForeignKey(name = "fk_guildautorole_guildautorolename"))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private GuildAutoRoleName name;

	@ManyToOne(optional = false)
	@JoinColumn(name = "guildId", foreignKey = @ForeignKey(name = "fk_guildautorole_guildautoroles"))
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
		this.nameId = name.getId().getName();
	}

}
