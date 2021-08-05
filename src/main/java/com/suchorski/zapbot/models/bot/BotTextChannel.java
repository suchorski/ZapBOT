package com.suchorski.zapbot.models.bot;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.lucky.TextChannelDraw;
import com.suchorski.zapbot.models.commands.statistics.TextChannelStatistic;
import com.suchorski.zapbot.models.commands.youtubesubscribe.TextChannelYoutubeSubscribe;
import com.suchorski.zapbot.utils.YoutubeUtils;

import kong.unirest.Unirest;
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
	
	private static final long serialVersionUID = -1850576987469867257L;

	@Transient private String youtubeSubscribeSecret;
	@Transient private String url;

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
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<TextChannelDraw> draws = new HashSet<>();

	@OneToMany(mappedBy = "textChannel", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<TextChannelYoutubeSubscribe> youtubeSubscribes = new HashSet<>();

	@OneToMany(mappedBy = "textChannel", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<TextChannelStatistic> textChannelStatistics = new ArrayList<>();

	@ManyToOne(optional = false)
	@JoinColumn(name = "guild_id", foreignKey = @ForeignKey(name = "fk_textchannel_guild"))
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
			youtubeSubscribe.setTextChannel(null);
		} else {
			throw new NothingFoundException("canal não encontrado");
		}
	}

	public void addTextChannelStatistic(TextChannelStatistic textChannelStatistic) {
		textChannelStatistic.setTextChannel(this);
		this.textChannelStatistics.add(textChannelStatistic);
	}

	public void delTextChannelStatistic(TextChannelStatistic textChannelStatistic) throws NothingFoundException {
		if (this.textChannelStatistics.remove(textChannelStatistic)) {
			textChannelStatistic.setTextChannel(null);
		} else {
			throw new NothingFoundException("estatística não encontrada");
		}
	}

	public void switchOnlyLastMessage() {
		this.onlyLastMessage = !this.onlyLastMessage;
	}

	@PreRemove
	private void unsubscribeAll() {
		if (youtubeSubscribes != null && url != null) {
			getYoutubeSubscribes().forEach(ys -> {
				String channelId = ys.getId().getYoutubeChannelId();
				Map<String, Object> params = YoutubeUtils.generateUnsubscribeMap(channelId, youtubeSubscribeSecret, url);
				Unirest.post("https://pubsubhubbub.appspot.com/subscribe").fields(params).asString();
			});
		}
	}

}
