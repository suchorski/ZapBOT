package com.suchorski.zapbot.models.bot;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.bot.ids.BotMemberID;
import com.suchorski.zapbot.models.commands.statistics.MemberStatistic;

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
	
	private static final long serialVersionUID = 2503179006185418848L;

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
	
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<MemberStatistic> memberStatistics = new ArrayList<>();

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_member_user"))
	@OnDelete(action = OnDeleteAction.CASCADE)
	@MapsId("userId")
	private BotUser user;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "guild_id", foreignKey = @ForeignKey(name = "fk_member_guild"))
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
	
	public void addMemberStatistic(MemberStatistic memberStatistic) {
		memberStatistic.setMember(this);
		this.memberStatistics.add(memberStatistic);
	}
	
	public void delMemberStatistic(MemberStatistic memberStatistic) throws NothingFoundException {
		if (this.memberStatistics.remove(memberStatistic)) {
			memberStatistic.setMember(null);
		} else {
			throw new NothingFoundException("estatística não encontrada");
		}
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
			xp -= getMaxXp();
			++level;
			levelUp = true;
		}
	}
	
}
