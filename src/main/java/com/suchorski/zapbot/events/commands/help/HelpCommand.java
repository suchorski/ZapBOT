package com.suchorski.zapbot.events.commands.help;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Paginator.Builder;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.utils.CommandUtils;

import net.dv8tion.jda.api.Permission;

@Component
public class HelpCommand extends BotCommand {
	
	@Value("${bot.url.page}") private String urlPage;

	@Autowired private EventWaiter eventWaiter;
	@Autowired private List<BotCommand> commands;

	@Autowired private HelpAnnounceCommand helpAnnounceCommand;
	@Autowired private HelpAutoReactCommand helpAutoReactCommand;
	@Autowired private HelpAutoRoleCommand helpAutoRoleCommand;
	@Autowired private HelpBackgroundCommand helpBackgroundCommand;
	@Autowired private HelpBirthdayCommand helpBirthdayCommand;
	@Autowired private HelpLevelRoleCommand helpLevelRoleCommand;
	@Autowired private HelpProfileCommand helpProfileCommand;
	@Autowired private HelpRafflesCommand helpRafflesCommand;
	@Autowired private HelpReplyCommand helpReplyCommand;
	@Autowired private HelpSetRoleCommand helpSetRoleCommand;
	@Autowired private HelpStatisticsCommand helpStatisticsCommand;
	@Autowired private HelpSubscribeYoutubeCommand helpSubscribeYoutubeCommand;

	@PostConstruct
	public void init() {
		this.name = "ajuda";
		this.help = "Lista os comandos disponíveis";
		this.aliases = new String[] { "help" };
		this.cooldown = Constants.COOLDOWNS.SLOW;
		this.cooldownScope = CooldownScope.CHANNEL;
		this.hidden = true;
		commands.sort((c1, c2) -> {
			int category = (c1.getCategory() != null && c2.getCategory() != null) ? c1.getCategory().getName().compareToIgnoreCase(c2.getCategory().getName()) : 0;
			return category != 0 ? category : c1.getName().compareToIgnoreCase(c2.getName());
		});
		this.children = new Command[] {
				helpAnnounceCommand, helpAutoReactCommand, helpAutoRoleCommand, helpBackgroundCommand, 
				helpBirthdayCommand, helpLevelRoleCommand, helpProfileCommand, helpRafflesCommand,
				helpReplyCommand, helpSetRoleCommand, helpStatisticsCommand, helpSubscribeYoutubeCommand
		};
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		if (event.getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
			Builder builder = new Builder();
			builder.setEventWaiter(eventWaiter);
			builder.setColor(Constants.COLORS.DEFAULT);
			builder.setText(this.help);
			builder.setItemsPerPage(5);
			for (BotCommand c : commands) {
				if (!c.isChildOnly() && !c.isHidden() && !c.isOwnerCommand()) {
					String args = c.getArguments() == null ? "" : String.format(" %s", c.getArguments());
					builder.addItems(String.format("**`%s%s%s`** %s", event.getClient().getPrefix(), c.getName(), args, c.getHelp()));
				}
			}
			builder.setTimeout(5, TimeUnit.MINUTES);
			builder.setUsers(event.getAuthor());
			event.getMessage().delete().queue(m -> {
				builder.build().display(event.getChannel());
			}, m -> {
				CommandUtils.error(event.getMessage());
			});
		} else {
			CommandUtils.success(event, String.format("Lista de comandos disponíveis no site: %s/comandos ", urlPage));
		}
	}

}
