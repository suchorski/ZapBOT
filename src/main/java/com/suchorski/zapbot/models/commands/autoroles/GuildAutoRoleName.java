package com.suchorski.zapbot.models.commands.autoroles;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.hibernate.annotations.ColumnDefault;

import com.suchorski.zapbot.models.commands.autoroles.ids.GuildAutoRoleNameID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "guild_autorole_name")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class GuildAutoRoleName implements Serializable {
	
	private static final long serialVersionUID = 7530553310317820192L;

	@EmbeddedId
	private GuildAutoRoleNameID id;

	@Column(name = "message_id", insertable = false, nullable = false)
	@ColumnDefault("0")
	private Long messageId = 0L;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "guildId", foreignKey = @ForeignKey(name = "fk_guildautorolename_guildautoroles"))
	@MapsId("guildId")
	private GuildAutoRoles autoRoles;
	
	public GuildAutoRoleName(String name, GuildAutoRoles autoRoles) {
		this.id = new GuildAutoRoleNameID(name, autoRoles.getId());
		this.autoRoles = autoRoles;
	}

}
