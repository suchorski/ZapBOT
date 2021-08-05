package com.suchorski.zapbot.events.commands.statistics;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.models.bot.BotMember;
import com.suchorski.zapbot.models.bot.BotTextChannel;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.bot.MemberService;
import com.suchorski.zapbot.services.bot.TextChannelService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.GraphGenerator;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

@Component
public class StatisticsCommand extends BotCommand {

	@Autowired private GuildService guildService;
	@Autowired private MemberService memberService;
	@Autowired private TextChannelService textChannelService;
	@Autowired private StatisticsGuildCommand statisticsGuildCommand;
	@Autowired private StatisticsTopCommand statisticsTopCommand;

	@PostConstruct
	public void init() {
		this.name = "estatisticas";
		this.help = "Mostra as estatísticas do canal";
		this.aliases = new String[] { "statistics", "stats", "estatísticas" };
		this.cooldown = Constants.COOLDOWNS.SLOW;
		this.cooldownScope = CooldownScope.CHANNEL;
		this.arguments = "[@menção do canal | @menção do usuário]";
		this.children = new Command[] { statisticsGuildCommand, statisticsTopCommand };
		addPermissions(Permission.MESSAGE_ATTACH_FILES);
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getHelper();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			InputStream imageStream;
			Optional<Member> optional = event.getMessage().getMentionedMembers().stream().findAny();
			if (optional.isPresent()) {
				Member m = optional.get();
				BotMember member = memberService.findOrCreate(m.getIdLong(), event.getGuild().getIdLong());
				imageStream = GraphGenerator.generateGraph(String.format("Estatísticas de %s (@%s#%s)", m.getEffectiveName(), m.getUser().getName(), m.getUser().getDiscriminator()), member.getMemberStatistics(), !member.getGuild().isPremium());
			} else {
				TextChannel channel = event.getMessage().getMentionedChannels().stream().findFirst().orElse(event.getTextChannel());
				BotTextChannel textChannel = textChannelService.findOrCreate(channel.getIdLong(), guildService.findOrCreate(event.getGuild().getIdLong()));
				imageStream = GraphGenerator.generateGraph(String.format("Estatísticas do canal #%s", channel.getName()), textChannel.getTextChannelStatistics(), !textChannel.getGuild().isPremium());
			}
			event.getChannel().sendFile(imageStream, "graph.png").queue(m -> {
				CommandUtils.success(event.getMessage());
			}, m -> {
				CommandUtils.error(event.getMessage());
			});
		} catch (IOException e) {
			CommandUtils.error(event, e.getLocalizedMessage());
		}
	}

}
