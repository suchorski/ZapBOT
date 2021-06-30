package com.suchorski.zapbot.models.commands.autoroles.ids;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

import com.suchorski.zapbot.converters.EmojiConverter;

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
public class GuildAutoRoleID implements Serializable {
	
	private static final long serialVersionUID = -394569666798997913L;

	@Convert(converter = EmojiConverter.class)
	@Column(updatable = false, nullable = false)
	private String emoji;
	
	@Column(updatable = false, nullable = false)
	private Long guildId;

}
