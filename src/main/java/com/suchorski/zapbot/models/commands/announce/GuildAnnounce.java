package com.suchorski.zapbot.models.commands.announce;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.bot.BotGuild;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "guild_announce")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class GuildAnnounce implements Serializable {
	
	private static final long serialVersionUID = -3534430766096333657L;

	@Id
	@Column(name = "guild_id", updatable = false, nullable = false)
	private Long id;
	
	@Column(insertable = false, nullable = true)
	@ColumnDefault("NULL")
	private Long channelId = null;
	
	@Column(length = 128, insertable = false, nullable = false)
	@ColumnDefault("'%s#%s | Bem-vindo(a)!'")
	private String author = "%s#%s | Bem-vindo(a)!";
	
	@Column(length = 512, insertable = false, nullable = false)
	@ColumnDefault("'Olá %s, seja bem-vindo(a)!'")
	private String description = "Olá %s, seja bem-vindo(a)!";
	
	@OneToMany(mappedBy = "announce", cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<GuildAnnounceField> fields = new HashSet<>();
	
	@Column(length = 24, insertable = false, nullable = false)
	@ColumnDefault("'Aproveite %s!'")
	private String footer = "Aproveite %s!";
	
	@OneToOne(optional = false)
	@JoinColumn(name = "id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_guildannounce_guild"))
	@MapsId
	private BotGuild guild;
	
	public GuildAnnounce(BotGuild guild) {
		this.id = guild.getId();
		guild.setAnnounce(this);
	}
	
	public void addField(GuildAnnounceField field) {
		field.setAnnounce(this);
		this.fields.add(field);
	}
	
	public void delField(GuildAnnounceField field) throws NothingFoundException {
		if (this.fields.remove(field)) {
			field.setAnnounce(this);
		} else {
			throw new NothingFoundException("campo não encontrado");
		}
	}
	
}
