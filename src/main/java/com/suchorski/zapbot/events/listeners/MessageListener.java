package com.suchorski.zapbot.events.listeners;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suchorski.zapbot.components.Bot;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.exceptions.NotEnoughCoinsException;
import com.suchorski.zapbot.models.bot.BotGuild;
import com.suchorski.zapbot.models.bot.BotMember;
import com.suchorski.zapbot.models.bot.BotTextChannel;
import com.suchorski.zapbot.models.bot.BotUser;
import com.suchorski.zapbot.models.commands.social.GuildLevelRole;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.bot.MemberService;
import com.suchorski.zapbot.services.bot.TextChannelService;
import com.suchorski.zapbot.services.commands.social.AutoReactService;
import com.suchorski.zapbot.services.commands.social.CoinsService;
import com.suchorski.zapbot.services.commands.social.LevelRoleService;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Component
public class MessageListener extends ListenerAdapter {

	private List<Pass> passes = new LinkedList<Pass>();

	@Autowired private GuildService guildService;
	@Autowired private MemberService memberService;
	@Autowired private CoinsService coinsService;
	@Autowired private TextChannelService textChannelService;
	@Autowired private LevelRoleService serverLevelRoleService;
	@Autowired private AutoReactService autoReactService;
	@Autowired private Bot bot;

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (!shouldIgnore(event.getGuild().getIdLong(), event.getChannel().getIdLong(), event.getAuthor().getIdLong())) {
			if (event.isFromType(ChannelType.TEXT)) {
				if (!event.getAuthor().isBot() && !event.getMessage().getContentRaw().startsWith(Constants.OPTIONS.PREFFIX)) {
					BotMember member = memberService.findOrCreate(event.getAuthor().getIdLong(), event.getGuild().getIdLong());
					memberService.addMessageXp(member, event.getMessage().getContentRaw().trim().length());
					if (member.isLevelUp()) {
						event.getChannel().sendMessage(String.format("Parabéns %s, você agora está no nível %d!", event.getAuthor().getAsMention(), member.getLevel())).queue();
						Set<GuildLevelRole> levelRoles = serverLevelRoleService.findAllLevels(member.getGuild().getId(), member.getLevel());
						levelRoles.forEach(lr -> {
							JDA jda = bot.getJda();
							jda.getGuildById(lr.getId().getGuildId()).getRolesByName(lr.getRole(), true).forEach(role -> {
								jda.getGuildById(lr.getId().getGuildId()).addRoleToMember(member.getUser().getId(), role).queue();
							});
						});
					}
					BotGuild guild = member.getGuild();
					if (guild.getAutoReact()) {
						BotUser user = member.getUser();
						if (user.getAutoReact().getEnabled()) {
							try {
								coinsService.subCoins(user.getCoins(), Constants.COSTS.AUTO_REACT);
								event.getMessage().addReaction(user.getAutoReact().getEmoji()).queue();
							} catch (NotEnoughCoinsException e) {
								autoReactService.switchAutoReact(user.getAutoReact());
							}
						}
					}
				}
				BotTextChannel textChannel = textChannelService.findOrCreate(event.getChannel().getIdLong(), guildService.findOrCreate(event.getGuild().getIdLong()));
				if (textChannel.getOnlyLastMessage()) {
					int count = 0;
					boolean deleted = false;
					MessageHistory history = event.getChannel().getHistory();
					List<Message> messages = history.getRetrievedHistory();
					do {
						count = messages.size();
						history.retrievePast(10).complete();
						messages = history.getRetrievedHistory();
						for (int i = Math.max(1, count); i < messages.size(); ++i) {
							if (messages.get(i).getAuthor().getIdLong() == event.getAuthor().getIdLong()) {
								messages.get(i).delete().queue();
								deleted = true;
								break;
							}
						}
						if (deleted) {
							break;
						}
					} while (count < messages.size());
				}
			}
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@EqualsAndHashCode
	class Pass {
		private long guildId;
		private long channelId;
		private long userId;
	}

	public void addPass(long guildId, long channelId, long userId) {
		passes.add(new Pass(guildId, channelId, userId));
	}

	public boolean shouldIgnore(long guildId, long channelId, long userId) {
		return passes.remove(new Pass(guildId, channelId, userId));
	}

}
