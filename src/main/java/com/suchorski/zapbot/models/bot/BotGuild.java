package com.suchorski.zapbot.models.bot;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.announce.GuildAnnounce;
import com.suchorski.zapbot.models.commands.autoroles.GuildAutoRoles;
import com.suchorski.zapbot.models.commands.reply.GuildReply;
import com.suchorski.zapbot.models.commands.roles.GuildRoles;
import com.suchorski.zapbot.models.commands.social.GuildBirthday;
import com.suchorski.zapbot.models.commands.social.GuildLevelRole;
import com.suchorski.zapbot.models.commands.statistics.GuildStatistic;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity(name = "guild")
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class BotGuild implements Serializable {
	
	private static final long serialVersionUID = 4284626548294206092L;

	@NonNull
	@Id
	private Long id;
	
	@Column(insertable = false, updatable = false, nullable = false)
	@ColumnDefault("current_timestamp")
	private Instant joinedOn = Instant.now();
	
	@Column(name = "auto_react", insertable = false, nullable = false)
	@ColumnDefault("b'1'")
	private Boolean autoReact = true;

	@Column(name = "coins_donated", insertable = false, nullable = false)
	@ColumnDefault("0")
	private Long coinsDonated = 0L;
	
	@OneToOne(mappedBy = "guild", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private GuildAnnounce announce;
	
	@OneToOne(mappedBy = "guild", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private GuildAutoRoles autoRoles;

	@OneToOne(mappedBy = "guild", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private GuildBirthday birthday;
	
	@OneToMany(mappedBy = "guild", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@OrderBy("level asc")
	private Set<GuildLevelRole> levelRoles = new LinkedHashSet<>();
	
	@OneToMany(mappedBy = "guild", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@OrderBy("command asc")
	private List<GuildReply> replys = new LinkedList<>();
	
	@OneToOne(mappedBy = "guild", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private GuildRoles roles;
	
	@OneToMany(mappedBy = "guild", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<BotTextChannel> textChannels = new HashSet<>();

	@OneToMany(mappedBy = "guild", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<BotVoiceChannel> voiceChannels = new HashSet<>();
	
	@OneToMany(mappedBy = "guild", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<GuildStatistic> guildStatistics = new ArrayList<>();
	
	@OneToMany(mappedBy = "guild", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<BotMember> members = new HashSet<>();
	
	public void setAnnounce(GuildAnnounce announce) {
		announce.setGuild(this);
		this.announce = announce;
	}
	
	public void setAutoRoles(GuildAutoRoles autoRoles) {
		autoRoles.setGuild(this);
		this.autoRoles = autoRoles;
	}
	
	public void setBirthday(GuildBirthday birthday) {
		birthday.setGuild(this);
		this.birthday = birthday;
	}
	
	public void addLevelRole(GuildLevelRole levelRole) {
		levelRole.setGuild(this);
		this.levelRoles.add(levelRole);
	}
	
	public void delLevelRole(GuildLevelRole levelRole) throws NothingFoundException {
		if (this.levelRoles.remove(levelRole)) {
			levelRole.setGuild(null);
		} else {
			throw new NothingFoundException("nível não encontrado");
		}
	}
	
	public void addReply(GuildReply reply) {
		reply.setGuild(this);
		this.replys.add(reply);
	}
	
	public void delReply(GuildReply reply) throws NothingFoundException {
		if (this.replys.remove(reply)) {
			reply.setGuild(null);
		} else {
			throw new NothingFoundException("resposta não encontrada");
		}
	}
	
	public void setRoles(GuildRoles roles) {
		roles.setGuild(this);
		this.roles = roles;
	}
	
	public void addTextChannel(BotTextChannel textChannel) {
		textChannel.setGuild(this);
		this.textChannels.add(textChannel);
	}
	
	public void delTextChannel(BotTextChannel textChannel) throws NothingFoundException {
		if (this.textChannels.remove(textChannel)) {
			textChannel.setGuild(null);
		} else {
			throw new NothingFoundException("canal de texto não encontrado");
		}
	}
	
	public void addVoiceChannel(BotVoiceChannel voiceChannel) {
		voiceChannel.setGuild(this);
		this.voiceChannels.add(voiceChannel);
	}
	
	public void delVoiceChannel(BotVoiceChannel voiceChannel) throws NothingFoundException {
		if (this.voiceChannels.remove(voiceChannel)) {
			voiceChannel.setGuild(null);
		} else {
			throw new NothingFoundException("canal de texto não encontrado");
		}
	}
	
	public void addGuildStatistic(GuildStatistic guildStatistic) {
		guildStatistic.setGuild(this);
		this.guildStatistics.add(guildStatistic);
	}
	
	public void delGuildStatistic(GuildStatistic guildStatistic) throws NothingFoundException {
		if (this.guildStatistics.remove(guildStatistic)) {
			guildStatistic.setGuild(null);
		} else {
			throw new NothingFoundException("estatística não encontrada");
		}
	}
	
	public void addMember(BotMember member) {
		member.setGuild(this);
		this.members.add(member);
	}
	
	public void delMember(BotMember member) throws NothingFoundException {
		if (this.members.remove(member)) {
			member.setGuild(null);
		} else {
			throw new NothingFoundException("membro não encontrado");
		}
	}
	
	public void addCoinsDonated(long coins) {
		this.coinsDonated += coins;
	}

	public void switchAutoReact() {
		this.autoReact = !this.autoReact;
	}
	
	@Transient
	public boolean isPremium() {
		return coinsDonated >= Constants.PREMIUM.GUILD_COINS; 
	}

}
