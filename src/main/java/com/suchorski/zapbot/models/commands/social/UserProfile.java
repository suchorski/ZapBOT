package com.suchorski.zapbot.models.commands.social;

import java.awt.Color;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.suchorski.zapbot.converters.ColorConverter;
import com.suchorski.zapbot.models.bot.BotUser;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "user_profile")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class UserProfile implements Serializable {
	
	private static final long serialVersionUID = 9176877375798952464L;

	@Id
	@Column(name = "user_id", updatable = false, nullable = false)
	private Long id;
	
	@Column(insertable = false, nullable = false)
	@ColumnDefault("0")
	private Integer recommendations = 0;
	
	@Column(name = "social_profile_message", length = 256, insertable = false, nullable = false)
	@ColumnDefault("'Somente mais um usuário do ZapBOT!'")
	private String message = "Somente mais um usuário do ZapBOT!";
	
	@Convert(converter = ColorConverter.class)
	@Column(name = "social_profile_color", insertable = false, nullable = false)
	@ColumnDefault("3027768")
	private Color color = new Color(3027768);
	
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_userprofile_background"))
	private UserProfileBackground background;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_userprofile_user"))
	@MapsId
	@OnDelete(action = OnDeleteAction.CASCADE)
	private BotUser user;
	
	public UserProfile(BotUser user, UserProfileBackground background) {
		this.id = user.getId();
		this.background = background;
		user.setProfile(this);
	}

	public void recommend() {
		++this.recommendations;
	}
	
}
