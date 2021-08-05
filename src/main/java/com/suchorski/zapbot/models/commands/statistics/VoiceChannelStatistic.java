package com.suchorski.zapbot.models.commands.statistics;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.suchorski.zapbot.models.bot.BotVoiceChannel;
import com.suchorski.zapbot.models.commands.statistics.ids.VoiceChannelStatisticID;
import com.suchorski.zapbot.models.interfaces.statistics.StatisticGraph;
import com.suchorski.zapbot.utils.GraphDate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "voicechannel_statistic")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class VoiceChannelStatistic implements Serializable, StatisticGraph {
	
	private static final long serialVersionUID = 7515841471324594015L;

	public static final String MSG_TIME_IN_MINUTES = "Tempo em minutos";
	
	@EmbeddedId
	private VoiceChannelStatisticID id;
	
	@Column(nullable = false)
	@ColumnDefault("0")
	private Integer minutes = 0;
	
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_voicechannelstatistic_voicechannel"))
	@MapsId("voiceChannelId")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private BotVoiceChannel voiceChannel;
	
	public VoiceChannelStatistic(LocalDate date, BotVoiceChannel voiceChannel) {
		this.id = new VoiceChannelStatisticID(date, voiceChannel.getId());
		voiceChannel.addVoiceChannelStatistic(this);
	}
	
	public VoiceChannelStatistic(LocalDate date, BotVoiceChannel voiceChannel, int minutes) {
		this(date, voiceChannel);
		this.minutes += minutes;
	}

	public void addMinutes(int minutes) {
		this.minutes += minutes;
	}

	@Override
	public GraphDate getDate() {
		return new GraphDate(id.getDate());
	}

	@Override
	public List<Statistic> getStatistics() {
		return Arrays.asList(new Statistic(MSG_TIME_IN_MINUTES, minutes));
	}

}
