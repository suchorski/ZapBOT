package com.suchorski.zapbot.models.bot;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.statistics.VoiceChannelStatistic;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity(name = "voicechannel")
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class BotVoiceChannel implements Serializable {
	
	private static final long serialVersionUID = -6116726063823261114L;

	@NonNull
	@Id
	private Long id;
	
	@Column(insertable = false, updatable = false, nullable = false)
	@ColumnDefault("current_timestamp")
	private Instant creationDate = Instant.now();
	
	@OneToMany(mappedBy = "voiceChannel", cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@OrderBy("id.date asc")
	private List<VoiceChannelStatistic> voiceChannelStatistics = new ArrayList<>();
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "guild_id", foreignKey = @ForeignKey(name = "fk_voicechannel_guild"))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private BotGuild guild;
	
	public void addVoiceChannelStatistic(VoiceChannelStatistic voiceChannelStatistic) {
		voiceChannelStatistic.setVoiceChannel(this);
		this.voiceChannelStatistics.add(voiceChannelStatistic);
	}
	
	public void delVoiceChannelStatistic(VoiceChannelStatistic voiceChannelStatistic) throws NothingFoundException {
		if (this.voiceChannelStatistics.remove(voiceChannelStatistic)) {
			voiceChannelStatistic.setVoiceChannel(null);
		} else {
			throw new NothingFoundException("estatística não encontrada");
		}
	}

}
