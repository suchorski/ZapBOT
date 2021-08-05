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
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.suchorski.zapbot.models.bot.BotMember;
import com.suchorski.zapbot.models.commands.statistics.ids.MemberStatisticID;
import com.suchorski.zapbot.models.interfaces.statistics.StatisticGraph;
import com.suchorski.zapbot.utils.GraphDate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "member_statistic")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class MemberStatistic implements Serializable, StatisticGraph {
	
	private static final long serialVersionUID = 2003610649470821067L;

	public static final String MSG_MESSAGES_SENT = "Mensagens enviadas";
	public static final String MSG_MINUTES_TALKED = "Minutos conversados";
	
	@EmbeddedId
	private MemberStatisticID id;
	
	@Column(nullable = false)
	@ColumnDefault("0")
	private Integer messages = 0;

	@Column(nullable = false)
	@ColumnDefault("0")
	private Integer minutes = 0;
	
	@ManyToOne(optional = false)
	@JoinColumns(value = { @JoinColumn, @JoinColumn }, foreignKey = @ForeignKey(name = "fk_memberstatistic_member"))
	@MapsId("memberId")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private BotMember member;
	
	public MemberStatistic(LocalDate date, BotMember member) {
		this.id = new MemberStatisticID(date, member.getId());
		member.addMemberStatistic(this);
	}

	public MemberStatistic(LocalDate date, BotMember member, int messages, int minutes) {
		this(date, member);
		this.messages = messages;
		this.minutes = minutes;
	}
	
	public void addMessage() {
		++this.messages;
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
		return Arrays.asList(new Statistic(MSG_MESSAGES_SENT, messages), new Statistic(MSG_MINUTES_TALKED, minutes));
	}

}
