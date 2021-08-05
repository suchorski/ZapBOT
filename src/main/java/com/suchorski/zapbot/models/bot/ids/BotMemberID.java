package com.suchorski.zapbot.models.bot.ids;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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
public class BotMemberID implements Serializable {
	
	private static final long serialVersionUID = 1207190425232612892L;

	@Column(updatable = false, nullable = false)
	private Long userId;
	
	@Column(updatable = false, nullable = false)
	private Long guildId;

}
