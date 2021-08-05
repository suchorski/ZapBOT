package com.suchorski.zapbot.models.commands.reply;

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
import com.suchorski.zapbot.models.commands.reply.ids.GuildReplyID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "guild_reply")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class GuildReply implements Serializable {
	
	private static final long serialVersionUID = -7716901625530357324L;

	@EmbeddedId
	private GuildReplyID id;
	
	@Column(length = 512, insertable = true, updatable = false, nullable = false)
	private String message;
	
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_guildreply_guild"))
	@MapsId("guildId")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private BotGuild guild;
	
	public GuildReply(String command, BotGuild guild, String message) {
		this.id = new GuildReplyID(command, guild.getId());
		this.message = message;
		guild.addReply(this);
	}
	
}
