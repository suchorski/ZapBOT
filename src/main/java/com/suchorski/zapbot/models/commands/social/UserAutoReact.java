package com.suchorski.zapbot.models.commands.social;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.models.bot.BotUser;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "user_autoreact")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class UserAutoReact implements Serializable {
	
	private static final long serialVersionUID = -358945467811773207L;

	@Id
	@Column(name = "user_id", updatable = false, nullable = false)
	private Long id;
	
	@Column(name = "auto_react", insertable = false, nullable = false)
	@ColumnDefault("b'0'")
	private Boolean enabled = false;
	
	@Column(name = "auto_react_emoji", length = 16, insertable = false, nullable = false)
	@ColumnDefault("'" + Constants.EMOJIS.HEART + "'")
	private String emoji = Constants.EMOJIS.HEART;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_userautoreact_user"))
	@OnDelete(action = OnDeleteAction.CASCADE)
	@MapsId
	private BotUser user;
	
	public UserAutoReact(BotUser user) {
		this.id = user.getId();
		user.setAutoReact(this);
	}
	
}
