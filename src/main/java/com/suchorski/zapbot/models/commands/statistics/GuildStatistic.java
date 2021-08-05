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

import com.suchorski.zapbot.models.bot.BotGuild;
import com.suchorski.zapbot.models.commands.statistics.ids.GuildStatisticID;
import com.suchorski.zapbot.models.interfaces.statistics.StatisticGraph;
import com.suchorski.zapbot.utils.GraphDate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "guild_statistic")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class GuildStatistic implements Serializable, StatisticGraph {
	
	private static final long serialVersionUID = 1872621320141916315L;

	private static final String MSG_MEMBERS_JOINED = "Membros que entraram";
	private static final String MSG_MEMBERS_LEFT = "Membros que sairam";
	
	@EmbeddedId
	private GuildStatisticID id;
	
	@Column(nullable = false)
	@ColumnDefault("0")
	private Integer usersJoined = 0;
	
	@Column(nullable = false)
	@ColumnDefault("0")
	private Integer usersLeft = 0;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_guildstatistic_guild"))
	@MapsId("guildId")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private BotGuild guild;
	
	public GuildStatistic(LocalDate date, BotGuild guild) {
		this.id = new GuildStatisticID(date, guild.getId());
		guild.addGuildStatistic(this);
	}
	
	public GuildStatistic(LocalDate date, BotGuild guild, int usersJoined, int usersLeft) {
		this(date, guild);
		this.usersJoined = usersJoined;
		this.usersLeft = usersLeft;
	}
	
	public void addUsersJoined() {
		++this.usersJoined;
	}
	
	public void addUsersLeft() {
		++this.usersLeft;
	}

	@Override
	public GraphDate getDate() {
		return new GraphDate(id.getDate());
	}

	@Override
	public List<Statistic> getStatistics() {
		return Arrays.asList(new Statistic(MSG_MEMBERS_JOINED, usersJoined), new Statistic(MSG_MEMBERS_LEFT, usersLeft));
	}

}
