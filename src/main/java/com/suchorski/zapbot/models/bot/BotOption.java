package com.suchorski.zapbot.models.bot;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "option")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class BotOption implements Serializable {
	
	private static final long serialVersionUID = -7128556938289069145L;

	@Id
	@ColumnDefault("1")
	private Integer id = 1;

	@Column(nullable = false)
	@ColumnDefault("'0.0.0'")
	private String version = "0.0.0";
	
	@Column(nullable = false)
	@ColumnDefault("0")
	private Long build = 0L;

	@Column(nullable = false)
	@ColumnDefault("'1000-01-01 00:00:00'")
	private LocalDateTime lastUpdate = LocalDateTime.now();
	
}
