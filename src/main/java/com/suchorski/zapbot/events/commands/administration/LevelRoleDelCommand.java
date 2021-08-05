package com.suchorski.zapbot.events.commands.administration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.exceptions.NothingFoundException;
import com.suchorski.zapbot.models.commands.social.GuildLevelRole;
import com.suchorski.zapbot.models.commands.social.ids.GuildLevelRoleID;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.commands.social.LevelRoleService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

@Component
public class LevelRoleDelCommand extends BotCommand {

	@Autowired private GuildService guildService;
	@Autowired private LevelRoleService levelRoleService;

	@PostConstruct
	public void init() {
		this.name = "remover";
		this.help = "Remove regras de níveis de experiência";
		this.aliases = new String[] { "del", "remove", "remover" };
		this.childOnly = true;
		this.arguments = "<nivel>";
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getAdmin();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			StringBuffer arg = new StringBuffer();
			CommandUtils.splitArgs(Utils.clear(event.getArgs()), arg);
			int level = Integer.parseInt(arg.toString());
			GuildLevelRole levelRole = levelRoleService.findById(new GuildLevelRoleID(level, event.getGuild().getIdLong()));
			levelRoleService.delete(levelRole);
			CommandUtils.success(event, "nível removido com sucesso");
		} catch (NumberFormatException e) {
			CommandUtils.warning(event, "número inválido");				
		} catch (NothingFoundException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		}
	}

}
