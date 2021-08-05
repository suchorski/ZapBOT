package com.suchorski.zapbot.events.commands.administration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.models.commands.social.GuildLevelRole;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.services.commands.social.LevelRoleService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

import net.dv8tion.jda.api.Permission;

@Component
public class LevelRoleAddCommand extends BotCommand {

	@Autowired private GuildService guildService;
	@Autowired private LevelRoleService levelRoleService;

	@PostConstruct
	public void init() {
		this.name = "adicionar";
		this.help = "Adiciona regras de níveis de experiência";
		this.aliases = new String[] { "add" };
		this.childOnly = true;
		this.arguments = "<nivel> <@menção do cargo>";
		addPermissions(Permission.MANAGE_ROLES);
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getAdmin();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		try {
			StringBuffer levelArg = new StringBuffer();
			StringBuffer roleArg = new StringBuffer();
			CommandUtils.splitArgs(Utils.clear(event.getArgs()), levelArg, roleArg);
			int level = Integer.parseInt(levelArg.toString());
			event.getMessage().getMentionedRoles().stream().findFirst().ifPresentOrElse(r -> {
				GuildLevelRole levelRole = new GuildLevelRole(level, guildService.findOrCreate(event.getGuild().getIdLong()), r.getName());
				int max = Constants.PREMIUM.MAX.LEVELS[levelRole.getGuild().isPremium() ? 1 : 0];
				if (levelRole.getGuild().getLevelRoles().size() < max) {
					levelRoleService.create(levelRole);
					CommandUtils.success(event, "nível adicionado com sucesso");					
				} else {					
					CommandUtils.warning(event, String.format("você só pode adicionar %d níveis", max));					
				}
			}, () -> {
				CommandUtils.warning(event, "você precisa mencionar um cargo");				
			});
		} catch (NumberFormatException e) {
			CommandUtils.warning(event, "número inválido");
		} catch (CommandUtilsException e) {
			CommandUtils.warning(event, e.getLocalizedMessage());
		}
	}

}
