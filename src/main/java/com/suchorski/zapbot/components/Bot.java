package com.suchorski.zapbot.components;

import java.util.List;

import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.events.futures.DrawFuture;
import com.suchorski.zapbot.models.bot.BotOption;
import com.suchorski.zapbot.services.bot.OptionService;
import com.suchorski.zapbot.services.commands.lucky.DrawService;

import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDA.Status;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

@Component
public class Bot implements Runnable {

	@Value("${bot.token}") private String DISCORD_TOKEN;
	@Value("${bot.owner.id}") private String OWNER_ID;

	@Autowired private Log log;
	@Autowired private EventWaiter eventWaiter;
	@Autowired private List<BotCommand> commands;
	@Autowired private List<ListenerAdapter> listenerAdapters;
	@Autowired private TaskScheduler taskScheduler;
	@Autowired private OptionService optionService;
	@Autowired private DrawService drawService;
	
	@Getter private JDA jda;

	@Override
	public void run() {
		try {
			BotOption option = optionService.findOrCreate();
			CommandClientBuilder client = new CommandClientBuilder();
			client.setOwnerId(OWNER_ID);
			client.setActivity(Activity.listening(": .ajuda"));
			client.setStatus(OnlineStatus.ONLINE);
			client.setEmojis(Constants.EMOJIS.SUCCESS, Constants.EMOJIS.WARNING, Constants.EMOJIS.ERROR);
			client.setPrefix(Constants.OPTIONS.PREFFIX);
			client.useHelpBuilder(false);
			commands.forEach(c -> { if (!c.isChildOnly()) { client.addCommand(c); } });
			JDABuilder builder = JDABuilder.createDefault(DISCORD_TOKEN);
			builder.disableCache(CacheFlag.ACTIVITY);
			builder.enableCache(CacheFlag.CLIENT_STATUS);
			builder.enableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS);
			builder.addEventListeners(eventWaiter, client.build());
			listenerAdapters.forEach(la -> builder.addEventListeners(la));
			jda = builder.build();
			jda.awaitStatus(Status.CONNECTED);
			if (option.getBuild() != Constants.BUILD) {
				log.infof(String.format("Atualizando bot para a versão %s!", Constants.VERSION));
				CommandListUpdateAction commands = jda.updateCommands();
				commands.addCommands(new CommandData("versao", "Mostra a versão do BOT"));
				commands.addCommands(new CommandData("invite", "Convida o BOT para sua guilda"));
				commands.addCommands(new CommandData("suporte", "Gera um link de convite para a guilda de suporte"));
				commands.queue();
				optionService.updateVersion(option, Constants.VERSION, Constants.BUILD);
			}
			drawService.findAll().forEach(d -> {
				taskScheduler.schedule(new DrawFuture(d.getId().getTextChannelId(), d.getId().getMessageId(), d.getEmoji(), d.getQuantity(), d.getPrize(), jda, drawService), d.getScheduledDate());
			});
			log.infof("Conectado!");
		} catch (LoginException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
