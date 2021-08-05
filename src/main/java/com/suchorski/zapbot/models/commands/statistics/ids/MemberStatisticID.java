package com.suchorski.zapbot.models.commands.statistics.ids;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.ColumnDefault;

import com.suchorski.zapbot.models.bot.ids.BotMemberID;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class MemberStatisticID implements Serializable {
	
	private static final long serialVersionUID = 1804085372842317930L;

	@Column(updatable = false, nullable = false)
	@ColumnDefault("current_timestamp")
	private LocalDate date = LocalDate.now();
	
	@Column(updatable = false, nullable = false)
	private BotMemberID memberId;

}
