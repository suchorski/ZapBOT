package com.suchorski.zapbot.models.commands.announce.ids;

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
public class GuildAnnounceFieldID implements Serializable {
	
	private static final long serialVersionUID = 2579902351103303561L;

	@Column(length = 32, updatable = false, nullable = false)
	private String title;
	
	@Column(updatable = false, nullable = false)
	private Long guildId;

}
