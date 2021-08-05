package com.suchorski.zapbot.events.abstracts;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.utils.CommandUtils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

public class GenericHelpListSubCommand<T extends BotCommand> extends BotCommand {

	private T mainCommand;
	
	public void initialize(T mainCommand) {
		this.mainCommand = mainCommand;
		this.name = mainCommand.getName();
		this.help = String.format("Ajuda do comando: %s", name);
		this.aliases = mainCommand.getAliases();
		this.cooldown = Constants.COOLDOWNS.SLOW;
		this.cooldownScope = CooldownScope.CHANNEL;
		this.childOnly = true;
		this.botPermissions = new Permission[] { Permission.MESSAGE_ADD_REACTION };
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Constants.COLORS.DEFAULT);
		builder.setAuthor(this.help);
		builder.addField(String.format("%s%s %s", event.getClient().getPrefix(), mainCommand.getName(), mainCommand.getArguments()), mainCommand.getHelp(), false);
		for (Command c : mainCommand.getChildren()) {
			if (!c.isHidden() && !c.isOwnerCommand()) {
				builder.addField(String.format("%s%s %s %s", event.getClient().getPrefix(), mainCommand.getName(), c.getName(), c.getArguments()), c.getHelp(), false);
			}
		}
		event.reply(builder.build());
		CommandUtils.success(event.getMessage());
	}
	
}
