package com.suchorski.zapbot.models.commands.announce;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.suchorski.zapbot.models.commands.announce.ids.GuildAnnounceFieldID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "guild_announce_field")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class GuildAnnounceField implements Serializable {
	
	private static final long serialVersionUID = -8004576002209935437L;

	@EmbeddedId
	private GuildAnnounceFieldID id;
	
	@Column(length = 192, insertable = true, updatable = false, nullable = false)
	private String description;
	
	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	private GuildAnnounce announce;
	
	public GuildAnnounceField(String title, GuildAnnounce announce, String description) {
		this.id = new GuildAnnounceFieldID(title, announce.getId());
		this.description = description;
		announce.addField(this);
	}
	
}