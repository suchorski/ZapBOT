package com.suchorski.zapbot.models.bot;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.models.bot.ids.BotMemberID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "member")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class BotMember implements Serializable {
	
	private static final long serialVersionUID = -7059056334140444757L;

	@EmbeddedId
	private BotMemberID id;
	
	@Column(insertable = false, nullable = false)
	@ColumnDefault("b'0'")
	private Boolean banned = false;
	
	@Column(insertable = false, updatable = false, nullable = false)
	@ColumnDefault("current_timestamp")
	private Instant joinedOn = Instant.now();
	
	@Column(name = "last_message", insertable = false, nullable = false)
	@ColumnDefault("current_timestamp")
	private Instant lastMessage = Instant.now();
	
	@Column(nullable = false, insertable = false)
	@ColumnDefault("1")
	private Integer level = 1;
	
	@Column(nullable = false, insertable = false)
	@ColumnDefault("0")
	private Long messages = 0L;
	
	@Column(nullable = false, insertable = false)
	@ColumnDefault("0")
	private Long xp = 0L;

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@MapsId("userId")
	private BotUser user;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "guild_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@MapsId("guildId")
	private BotGuild guild;
	
	@Transient
	private boolean levelUp;
	
	public BotMember(BotUser user, BotGuild guild) {
		this.id = new BotMemberID(user.getId(), guild.getId());
		this.user = user;
		this.guild = guild;
	}
	
	public void setUser(BotUser user) {
		this.user = user;
		user.getMembers().add(this);
	}
	
	public void setGuild(BotGuild guild) {
		this.guild = guild;
		guild.getMembers().add(this);
	}
	
	public long getMaxXp() {
		return level * Constants.XP.MAX_CALC;
	}

	public void addMessageXp(long points) {
		levelUp = false;
		++messages;
		xp += (long) (user.getXpMultiplier() * points);
		while (xp >= getMaxXp()) {
			xp -= level * getMaxXp();
			++level;
			levelUp = true;
		}
	}
	
}
