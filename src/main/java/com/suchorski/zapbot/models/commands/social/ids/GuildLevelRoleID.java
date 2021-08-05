package com.suchorski.zapbot.models.commands.social.ids;

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
public class GuildLevelRoleID implements Serializable {
	
	private static final long serialVersionUID = 8158597255449641451L;

	@Column(updatable = false, nullable = false)
	private Integer level;
	
	@Column(updatable = false, nullable = false)
	private Long guildId;

}
