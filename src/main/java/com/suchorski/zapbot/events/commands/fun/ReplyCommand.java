package com.suchorski.zapbot.events.commands.fun;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.reply.GuildReply;
import com.suchorski.zapbot.models.commands.reply.ids.GuildReplyID;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.commands.reply.ReplyService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

@Component
public class ReplyCommand extends BotCommand {
	
	@Autowired private GuildService guildService;
	@Autowired private ReplyService replyService;
	@Autowired private ReplyAddCommand replyAddCommand;
	@Autowired private ReplyDelCommand replyDelCommand;
	
	@PostConstruct
	public void init() {
		this.name = "r";
		this.help = "Responde a um comando personalizado";
		this.aliases = new String[] { "reply", "responder" };
		this.arguments = "<comando>";
		this.children = new Command[] { replyAddCommand, replyDelCommand };
	}
	
	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getHelper();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			StringBuffer command = new StringBuffer();
			CommandUtils.splitArgs(Utils.clear(event.getArgs()), command);
			GuildReply reply = replyService.findById(new GuildReplyID(command.toString(), event.getGuild().getIdLong()));
			CommandUtils.success(event, reply.getMessage());
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		} catch (NothingFoundException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		}
		
	}

}
