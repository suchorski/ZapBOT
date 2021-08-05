package com.suchorski.zapbot.models.commands.roles;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.suchorski.zapbot.models.bot.BotGuild;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "guild_roles")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class GuildRoles implements Serializable {
	
	private static final long serialVersionUID = 1444090122608309499L;

	@Id
	@Column(name = "guild_id")
	private Long id;
	
	@Column(length = 64, insertable = false, nullable = false)
	@ColumnDefault("'bot-administrador'")
	private String admin = "bot-administrador";
	
	@Column(length = 64, insertable = false, nullable = false)
	@ColumnDefault("'bot-operador'")
	private String operator = "bot-operador";
	
	@Column(length = 64, insertable = false, nullable = false)
	@ColumnDefault("'bot-ajudante'")
	private String helper = "bot-ajudante";
	
	@OneToOne(optional = false)
	@JoinColumn(name = "guild_id", foreignKey = @ForeignKey(name = "fk_guildroles_guild"))
	@MapsId
	@OnDelete(action = OnDeleteAction.CASCADE)
	private BotGuild guild;
	
	public GuildRoles(BotGuild guild) {
		this.id = guild.getId();
		guild.setRoles(this);
	}
	
}
