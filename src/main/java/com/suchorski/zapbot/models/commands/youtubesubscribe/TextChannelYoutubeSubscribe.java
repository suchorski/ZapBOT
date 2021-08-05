package com.suchorski.zapbot.models.commands.youtubesubscribe;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
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

@Entity(name = "textchannel_youtubesubscribe")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class TextChannelYoutubeSubscribe implements Serializable {

	private static final long serialVersionUID = -8446971046977930396L;

	@EmbeddedId
	private TextChannelYoutubeSubscribeID id;
	
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_textchannelyoutubesubscribe_textchannel"))
	@MapsId("textChannelId")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private BotTextChannel textChannel;
	
	public TextChannelYoutubeSubscribe(String youtubeChannelId, BotTextChannel textChannel) {
		this.id = new TextChannelYoutubeSubscribeID(youtubeChannelId, textChannel.getId());
		textChannel.addYoutubeSubscribe(this);
	}
	
}
