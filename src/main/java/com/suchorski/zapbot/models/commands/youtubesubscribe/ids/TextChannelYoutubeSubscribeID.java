package com.suchorski.zapbot.models.commands.youtubesubscribe.ids;

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
public class TextChannelYoutubeSubscribeID implements Serializable {
	
	private static final long serialVersionUID = 502577590739700838L;

	@Column(length = 64, updatable = false, nullable = false)
	private String youtubeChannelId;
	
	@Column(updatable = false, nullable = false)
	private Long textChannelId;

}
