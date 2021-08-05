package com.suchorski.zapbot.events.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.suchorski.zapbot.models.bot.BotOption;
import com.suchorski.zapbot.services.bot.OptionService;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;

@Component
public class SlashListener extends ListenerAdapter {
	
	@Value("${bot.public.key}") private String publicKey;
	
	@Autowired private OptionService optionService;
	
	@Override
    public void onSlashCommand(SlashCommandEvent event) {
		switch (event.getName()) {
		case "versao":
			BotOption option = optionService.findOrCreate();
			event.reply(String.format("ZapBOT v%s [build: %d] | Criado por <@314190663162003456>", option.getVersion(), option.getBuild())).addActionRow(Button.link("https://github.com/suchorski/ZapBOT", "GitHub")).setEphemeral(true).queue();
			break;
		case "invite":
			event.reply("Conecte o ZapBOT ao seu servidor também!").addActionRow(Button.link(String.format("https://discord.com/api/oauth2/authorize?client_id=%s&permissions=336063568&scope=applications.commands%%20bot", publicKey), "ZapBOT")).queue();
			break;
		case "suporte":
			event.reply("Entre na guilda para ter suporte ao ZapBOT!").addActionRow(Button.link("https://discord.gg/r78gRFAadC", "ZapBOT Suporte")).queue();
			break;
		default:
			event.reply("Comando desconhecido").setEphemeral(true).queue();
			break;
		}
	}
	
}
