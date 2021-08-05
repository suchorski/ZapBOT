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

import com.suchorski.zapbot.models.bot.BotTextChannel;
import com.suchorski.zapbot.models.commands.statistics.ids.TextChannelStatisticID;
import com.suchorski.zapbot.models.interfaces.statistics.StatisticGraph;
import com.suchorski.zapbot.utils.GraphDate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "textchannel_statistic")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class TextChannelStatistic implements Serializable, StatisticGraph {
	
	private static final long serialVersionUID = -1033346171010545023L;

	public static final String MSG_MESSAGES_SENT = "Mensagens enviadas";
	public static final String MSG_MESSAGES_REMOVED = "Mensagens deletadas";
	
	@EmbeddedId
	private TextChannelStatisticID id;
	
	@Column(nullable = false)
	@ColumnDefault("0")
	private Integer messagesReceived = 0;

	@Column(nullable = false)
	@ColumnDefault("0")
	private Integer messagesDeleted = 0;
	
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_textchannelstatistic_textchannel"))
	@MapsId("textChannelId")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private BotTextChannel textChannel;
	
	public TextChannelStatistic(LocalDate date, BotTextChannel textChannel) {
		this.id = new TextChannelStatisticID(date, textChannel.getId());
		textChannel.addTextChannelStatistic(this);
	}

	public TextChannelStatistic(LocalDate date, BotTextChannel textChannel, int messagesReceived, int messagesDeleted) {
		this.id = new TextChannelStatisticID(date, textChannel.getId());
		textChannel.addTextChannelStatistic(this);
		this.messagesReceived = messagesReceived;
		this.messagesDeleted = messagesDeleted;
	}
	
	public void addMessageReceived() {
		++this.messagesReceived;
	}

	public void addMessageDeleted() {
		++this.messagesDeleted;
	}

	@Override
	public GraphDate getDate() {
		return new GraphDate(id.getDate());
	}

	@Override
	public List<Statistic> getStatistics() {
		return Arrays.asList(new Statistic(MSG_MESSAGES_SENT, messagesReceived), new Statistic(MSG_MESSAGES_REMOVED, messagesDeleted));
	}

}
