package com.suchorski.zapbot.events.commands.survey;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.OrderedMenu.Builder;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.exceptions.CommandUtilsException;
import com.suchorski.zapbot.memory.listeners.Messages;
import com.suchorski.zapbot.services.bot.GuildService;
import com.suchorski.zapbot.utils.CommandUtils;
import com.suchorski.zapbot.utils.Utils;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@Component
public class ChooserCommand extends BotCommand {

	@Autowired private EventWaiter eventWaiter;
	@Autowired private GuildService guildService;

	@PostConstruct
	public void init() {
		this.name = "escolher";
		this.help = "Escolhe um item da lista (o mais rápido escolhe)";
		this.aliases = new String[] { "chooser" };
		this.cooldown = Constants.COOLDOWNS.FAST;
		this.cooldownScope = CooldownScope.GUILD;
		this.arguments = "[@menção do usuário ou do cargo]";
		addPermissions(Permission.MESSAGE_MANAGE);
	}

	@Override
	protected String getZapRole(CommandEvent event) {
		return guildService.findOrCreate(event.getGuild().getIdLong()).getRoles().getHelper();
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		Message question = event.getChannel().sendMessage("Digite as opções para escolha (separados por um pipe (|) em um máximo de 10)").complete();
		Messages.addPass(event.getChannel().getIdLong(), event.getAuthor().getIdLong());
		eventWaiter.waitForEvent(MessageReceivedEvent.class, e -> {
			return e.getAuthor().equals(event.getAuthor()) && e.getChannel().equals(event.getChannel()) && !e.getMessage().equals(event.getMessage());
		}, e -> {
			try {
				String[] options = Utils.clear(e.getMessage().getContentRaw()).replaceAll("\\s\\x7C\\s", "|").split("\\x7C");
				CommandUtils.checkNumArgsBetween(options, 1, 10);
				Builder builder = new Builder();
				builder.setColor(Constants.COLORS.DEFAULT);
				builder.addChoices(options);
				builder.setEventWaiter(eventWaiter);
				builder.setSelection((m, i) -> {
					CommandUtils.success(event, String.format("opção escolhida foi: %s", options[i - 1]));
				});
				builder.setCancel(m -> {
					m.delete().queue();
					CommandUtils.warning(event, "escolha cancelada");
				});
				builder.setDescription("Escolha um das opções abaixo:");
				builder.setRoles(event.getMessage().getMentionedRoles().toArray(new Role[event.getMessage().getMentionedRoles().size()]));
				builder.setUsers(event.getMessage().getMentionedUsers().toArray(new User[event.getMessage().getMentionedUsers().size()]));
				builder.setTimeout(2, TimeUnit.MINUTES);
				question.delete().queue();
				e.getMessage().delete().queue();
				builder.build().display(event.getChannel());
			} catch (CommandUtilsException err) {
				CommandUtils.warning(event, err.getLocalizedMessage());
			}
		}, 2, TimeUnit.MINUTES, () -> {
			CommandUtils.warning(event, "você demorou muito para digitar as opções");
		});
	}

}
