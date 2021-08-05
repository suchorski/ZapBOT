package com.suchorski.zapbot.events.listeners;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suchorski.zapbot.components.Bot;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.exceptions.NotEnoughCoinsException;
import com.suchorski.zapbot.memory.listeners.Messages;
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

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Component
public class MessageTextListener extends ListenerAdapter {

	@Autowired private GuildService guildService;
	@Autowired private MemberService memberService;
	@Autowired private CoinsService coinsService;
	@Autowired private TextChannelService textChannelService;
	@Autowired private LevelRoleService serverLevelRoleService;
	@Autowired private AutoReactService autoReactService;
	@Autowired private Bot bot;

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (!Messages.shouldIgnore(event.getChannel().getIdLong(), event.getAuthor().getIdLong())) {
			if (event.isFromType(ChannelType.TEXT)) {
				BotMember member = memberService.findOrCreate(event.getAuthor().getIdLong(), event.getGuild().getIdLong());
				if (!event.getAuthor().isBot() && !event.getMessage().getContentRaw().startsWith(Constants.OPTIONS.PREFFIX)) {
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
								if (!user.isPremium()) {
									coinsService.subCoins(user.getCoins(), Constants.COSTS.AUTO_REACT);
								}
								event.getMessage().addReaction(user.getAutoReact().getEmoji()).queue();
							} catch (NotEnoughCoinsException e) {
								autoReactService.switchAutoReact(user.getAutoReact());
							}
						}
					}
				}
				BotTextChannel textChannel = textChannelService.findOrCreate(event.getChannel().getIdLong(), guildService.findOrCreate(event.getGuild().getIdLong()));
				textChannelService.addMessageReceivedToStatistics(textChannel);
				if (!event.getAuthor().isBot()) {
					memberService.addMessageToStatistics(member);
				}
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

	@Override
	public void onMessageDelete(MessageDeleteEvent event) {
		if (event.isFromType(ChannelType.TEXT)) {
			BotTextChannel textChannel = textChannelService.findOrCreate(event.getChannel().getIdLong(), guildService.findOrCreate(event.getGuild().getIdLong()));
			textChannelService.addMessageDeletedToStatistics(textChannel);
		}
	}

}
