package com.suchorski.zapbot.models.commands.youtubesubscribe;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.suchorski.zapbot.models.bot.BotTextChannel;
import com.suchorski.zapbot.models.commands.youtubesubscribe.ids.TextChannelYoutubeSubscribeID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class TextChannelYoutubeSubscribe implements Serializable {

	private static final long serialVersionUID = -8446971046977930396L;

	@EmbeddedId
	private TextChannelYoutubeSubscribeID id;
	
	@ManyToOne(optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@MapsId("textChannelId")
	private BotTextChannel textChannel;
	
	public TextChannelYoutubeSubscribe(String youtubeChannelId, BotTextChannel textChannel) {
		this.id = new TextChannelYoutubeSubscribeID(youtubeChannelId, textChannel.getId());
		textChannel.addYoutubeSubscribe(this);
	}
	
}
