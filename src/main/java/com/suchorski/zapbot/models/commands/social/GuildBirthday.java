package com.suchorski.zapbot.models.commands.social;

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

@Entity(name = "guild_birthday")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class GuildBirthday implements Serializable {
	
	private static final long serialVersionUID = -2966650438352864202L;

	@Id
	@Column(name = "guild_id", updatable = false, nullable = false)
	private Long id;
	
	@Column(insertable = false, nullable = true)
	@ColumnDefault("NULL")
	private Long channelId = null;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "guild_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_guildbirthday_guild"))
	@MapsId
	@OnDelete(action = OnDeleteAction.CASCADE)
	private BotGuild guild;
	
	public GuildBirthday(BotGuild guild) {
		this.id = guild.getId();
		guild.setBirthday(this);
	}
	
}
