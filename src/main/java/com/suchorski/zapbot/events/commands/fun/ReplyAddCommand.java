package com.suchorski.zapbot.events.commands.fun;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.models.commands.reply.GuildReply;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.commands.reply.ReplyService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

import net.dv8tion.jda.api.Permission;

@Component
public class ReplyAddCommand extends BotCommand {
	
	@Autowired private GuildService guildService;
	@Autowired private ReplyService replyService;
	
	@PostConstruct
	public void init() {
		this.name = "adicionar";
		this.help = "Adiciona um comando personalizado";
		this.aliases = new String[] { "add" };
		this.arguments = "<comando> <mensagem>";
		this.childOnly = true;
		this.botPermissions = new Permission[] { Permission.MESSAGE_ADD_REACTION };
	}
	
	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getHelper();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			StringBuffer command = new StringBuffer();
			StringBuffer message = new StringBuffer();
			CommandUtils.splitArgs(Utils.clear(event.getArgs()), 1, command, message);
			GuildReply reply = new GuildReply(command.toString(), guildService.findOrCreate(event.getGuild().getIdLong()), message.toString());
			replyService.addReply(reply);
			CommandUtils.success(event, "resposta adicionada");
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		}
		
	}

}
