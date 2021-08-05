package com.suchorski.zapbot.events.schedules;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.suchorski.zapbot.components.Bot;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.models.commands.social.GuildBirthday;
import com.suchorski.zapbot.models.commands.social.UserBirthday;
import com.suchorski.zapbot.services.bot.UserService;
import com.suchorski.zapbot.services.commands.announce.BirthdaysService;
import com.suchorski.zapbot.services.commands.social.BirthdayService;
import com.suchorski.zapbot.services.commands.statistics.MemberStatisticsService;
import com.suchorski.zapbot.services.commands.statistics.TextChannelStatisticsService;
import com.suchorski.zapbot.services.commands.statistics.VoiceChannelStatisticsService;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

@Component
public class Schedulers {
	
	@Autowired private Bot bot;
	@Autowired private UserService userService;
	@Autowired private BirthdaysService birthdaysService;
	@Autowired private BirthdayService birthdayService;
	@Autowired private MemberStatisticsService memberStatisticsService;
	@Autowired private TextChannelStatisticsService textChannelStatisticsService;
	@Autowired private VoiceChannelStatisticsService voiceChannelStatisticsService;
	
	@Scheduled(cron = "0 0 14 * * *")
	public void raffle() {
		userService.raffle();
	}
	
	@Scheduled(cron = "0 0 18 * * *")
	public void birthdays() {
		List<GuildBirthday> guildBirthdays = birthdaysService.findAllToAnnounce();
		for (GuildBirthday gb : guildBirthdays) {
			List<UserBirthday> userBirthdays = birthdayService.findBirthdays(gb.getGuild());
			if (userBirthdays.size() > 0) {
				TextChannel channel = bot.getJda().getTextChannelById(gb.getGuild().getBirthday().getChannelId());
				EmbedBuilder builder = new EmbedBuilder();
				builder.setColor(Constants.COLORS.DEFAULT);
				builder.setAuthor("Parabéns aos aniversariantes do dia:");
				builder.setDescription(Constants.EMOJIS.CAKE);
				for (UserBirthday ub : userBirthdays) {
					long age = ChronoUnit.YEARS.between(ub.getDate(), LocalDate.now());
					builder.addField(String.format("Pelos %d anos,", age), String.format("<@%d>!", ub.getUser().getId()), true);
				}
				channel.sendMessage(builder.build()).queue();
			}
		}
	}
	
	@Scheduled(cron = "0 0 4 * * *")
	public void clearOldStatistics() {
		memberStatisticsService.deleteOldStatistics();
		textChannelStatisticsService.deleteOldStatistics();
		voiceChannelStatisticsService.deleteOldStatistics();
	}

	@Scheduled(cron = "0 0 0 * * *")
	public void payForPremium() {
		userService.payForPremium();
	}

}
