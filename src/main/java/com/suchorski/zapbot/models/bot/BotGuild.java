package com.suchorski.zapbot.models.bot;

import java.io.Serializable;
import java.time.Instant;
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

import org.hibernate.annotations.ColumnDefault;

import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.announce.GuildAnnounce;
import com.suchorski.zapbot.models.commands.autoroles.GuildAutoRoles;
import com.suchorski.zapbot.models.commands.reply.GuildReply;
import com.suchorski.zapbot.models.commands.roles.GuildRoles;
import com.suchorski.zapbot.models.commands.social.GuildBirthday;
import com.suchorski.zapbot.models.commands.social.GuildLevelRole;

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
	
	private static final long serialVersionUID = -8908672913453512816L;

	@NonNull
	@Id
	private Long id;
	
	@Column(insertable = false, updatable = false, nullable = false)
	@ColumnDefault("current_timestamp")
	private Instant joinedOn = Instant.now();
	
	@Column(name = "auto_react", insertable = false, updatable = true, nullable = false)
	@ColumnDefault("b'1'")
	private Boolean autoReact = true;
	
	@OneToOne(mappedBy = "guild", cascade = CascadeType.ALL, orphanRemoval = true)
	private GuildAnnounce announce;
	
	@OneToOne(mappedBy = "guild", cascade = CascadeType.ALL, orphanRemoval = true)
	private GuildAutoRoles autoRoles;

	@OneToOne(mappedBy = "guild", cascade = CascadeType.ALL, orphanRemoval = true)
	private GuildBirthday birthday;
	
	@OneToMany(mappedBy = "guild", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("level asc")
	private Set<GuildLevelRole> levelRoles = new LinkedHashSet<>();
	
	@OneToMany(mappedBy = "guild", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("command asc")
	private List<GuildReply> replys = new LinkedList<>();
	
	@OneToOne(mappedBy = "guild", cascade = CascadeType.ALL, orphanRemoval = true)
	private GuildRoles roles;
	
	@OneToMany(mappedBy = "guild", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<BotTextChannel> textChannels = new HashSet<>();
	
	@OneToMany(mappedBy = "guild", cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<BotMember> members = new HashSet<>();
	
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

	public void switchAutoReact() {
		this.autoReact = !this.autoReact;
	}
	
}
