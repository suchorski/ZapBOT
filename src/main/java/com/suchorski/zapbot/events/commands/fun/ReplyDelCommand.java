package com.suchorski.zapbot.events.commands.fun;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.models.commands.reply.ids.GuildReplyID;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.commands.reply.ReplyService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

@Component
public class ReplyDelCommand extends BotCommand {
	
	@Autowired private GuildService guildService;
	@Autowired private ReplyService replyService;
	
	@PostConstruct
	public void init() {
		this.name = "deletar";
		this.help = "Remove um comando personalizado";
		this.aliases = new String[] { "del", "remover" };
		this.arguments = "<comando>";
		this.childOnly = true;
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
			replyService.deleteById(new GuildReplyID(command.toString(), event.getGuild().getIdLong()));
			CommandUtils.success(event, "resposta removida");
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		}
		
	}

}
