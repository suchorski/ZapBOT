package com.suchorski.zapbot.models.bot;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.lucky.TextChannelDraw;
import com.suchorski.zapbot.models.commands.youtubesubscribe.TextChannelYoutubeSubscribe;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity(name = "textchannel")
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class BotTextChannel implements Serializable {
	
	private static final long serialVersionUID = 4157631683739100920L;

	@NonNull
	@Id
	private Long id;
	
	@Column(insertable = false, updatable = false, nullable = false)
	@ColumnDefault("current_timestamp")
	private Instant creationDate = Instant.now();
	
	@Column(name = "only_last_message", insertable = false, updatable = true, nullable = false)
	@ColumnDefault("b'0'")
	private Boolean onlyLastMessage = false;
	
	@OneToMany(mappedBy = "textChannel", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<TextChannelDraw> draws = new HashSet<>();
	
	@OneToMany(mappedBy = "textChannel", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<TextChannelYoutubeSubscribe> youtubeSubscribes = new HashSet<>();
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "guild_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private BotGuild guild;
	
	public void addDraw(TextChannelDraw draw) {
		draw.setTextChannel(this);
		this.draws.add(draw);
	}
	
	public void delDraw(TextChannelDraw draw) throws NothingFoundException {
		if (this.draws.remove(draw)) {
			draw.setTextChannel(this);
		} else {
			throw new NothingFoundException("sorteio não encontrado");
		}
	}
	
	public void addYoutubeSubscribe(TextChannelYoutubeSubscribe youtubeSubscribe) {
		youtubeSubscribe.setTextChannel(this);
		this.youtubeSubscribes.add(youtubeSubscribe);
	}
	
	public void delYoutubeSubscribe(TextChannelYoutubeSubscribe youtubeSubscribe) throws NothingFoundException {
		if (this.youtubeSubscribes.remove(youtubeSubscribe)) {
			youtubeSubscribe.setTextChannel(this);
		} else {
			throw new NothingFoundException("canal não encontrado");
		}
	}

	public void switchOnlyLastMessage() {
		this.onlyLastMessage = !this.onlyLastMessage;
	}
	
}
