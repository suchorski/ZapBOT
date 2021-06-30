package com.suchorski.zapbot.models.commands.autoroles;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.bot.BotGuild;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "guild_autoroles")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class GuildAutoRoles implements Serializable {
	
	private static final long serialVersionUID = 5569299854340257526L;

	@Id
	@Column(name = "guild_id", updatable = false, nullable = false)
	private Long id;
	
	@Column(length = 64, insertable = false)
	@ColumnDefault("NULL")
	private String autoRole = null;
	
	@OneToMany(mappedBy = "autoRoles", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("name asc")
	private Set<GuildAutoRole> autoRoles = new LinkedHashSet<>();
	
	@OneToOne(optional = false)
	@JoinColumn(name = "guild_id", referencedColumnName = "id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@MapsId
	private BotGuild guild;
	
	public GuildAutoRoles(BotGuild guild) {
		this.id = guild.getId();
		guild.setAutoRoles(this);
	}
	
	public void addAutoRole(GuildAutoRole autoRole) {
		autoRole.setAutoRoles(this);
		this.autoRoles.add(autoRole);
	}
	
	public void delAutoRole(GuildAutoRole autoRole) throws NothingFoundException {
		if (this.autoRoles.remove(autoRole)) {
			autoRole.setAutoRoles(null);
		} else {
			throw new NothingFoundException("cargo automático não encontrado");
		}
	}
	
}
