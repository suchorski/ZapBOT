package com.suchorski.zapbot.models.commands.lucky.ids;

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
public class TextChannelDrawID implements Serializable {
	
	private static final long serialVersionUID = -3723631819348377533L;

	@Column(updatable = false, nullable = false)
	private Long messageId;
	
	@Column(updatable = false, nullable = false)
	private Long textChannelId;

}
