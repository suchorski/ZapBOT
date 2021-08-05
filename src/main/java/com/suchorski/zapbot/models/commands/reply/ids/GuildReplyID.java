package com.suchorski.zapbot.models.commands.reply.ids;

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
public class GuildReplyID implements Serializable {
	
	private static final long serialVersionUID = -8375424137999393662L;

	@Column(length = 32, updatable = false, nullable = false)
	private String command;
	
	@Column(updatable = false, nullable = false)
	private Long guildId;

}
