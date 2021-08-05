package com.suchorski.zapbot.events.commands.others;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.suchorski.zapbot.components.Log;
import com.suchorski.zapbot.constants.Constants;
import com.suchorski.zapbot.events.abstracts.BotCommand;
import com.suchorski.zapbot.utils.CommandUtils;

import kong.unirest.Callback;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.json.JSONObject;
import net.dv8tion.jda.api.EmbedBuilder;

@Component
public class BibbleCommand extends BotCommand {

	@Value("${bot.bibble.token}") private String token;

	@Autowired private Log log;

	@PostConstruct
	public void init() {
		this.name = "biblia";
		this.help = "Mostra um versículo da bíblia";
		this.aliases = new String[] { "bibble", "verse", "verso", "versiculo" };
		this.cooldown = Constants.COOLDOWNS.FAST;
		this.cooldownScope = CooldownScope.GUILD;
	}

	@Override
	protected void zapExecute(CommandEvent event) {
		Map<String, String> headers = new HashMap<String, String>(1);
		if (!token.startsWith("TOKEN_FROM_")) {
			headers.put("Authorization", String.format("Bearer %s", token));
		}
		Unirest.get("https://www.abibliadigital.com.br/api/verses/nvi/random").headers(headers).asJsonAsync(new Callback<JsonNode>(){
			@Override
			public void completed(HttpResponse<JsonNode> hr) { 
				JSONObject response = hr.getBody().getObject();
				if (hr.getStatus() == 200) {
					String name = response.getJSONObject("book").getString("name");
					String author = response.getJSONObject("book").getString("author");
					String group = response.getJSONObject("book").getString("group");
					int chapter = response.getInt("chapter");
					int number = response.getInt("number");
					String text = response.getString("text");
					EmbedBuilder builder = new EmbedBuilder();
					builder.setColor(Constants.COLORS.DEFAULT);
					builder.setAuthor(author);
					builder.setDescription(text);
					builder.setFooter(group);
					builder.addField("Livro", name, true);
					builder.addField("Capítulo", String.format("%d", chapter), true);
					builder.addField("Versículo", String.format("%d", number), true);
					event.reply(builder.build(), m -> {
						CommandUtils.success(event.getMessage());
					}, m -> {					
						CommandUtils.error(event.getMessage());
					});
				} else {
					log.dangerf("Erro ao baixar dados da Bíblia: %s", hr.getBody().getObject().getString("msg"));
					CommandUtils.error(event, "algum erro ocorreu, por-fvor reporte ao dono do BOT");
				}
			}

			@Override
			public void failed(UnirestException e) {
				CommandUtils.error(event.getMessage());
			}

			@Override
			public void cancelled() {
				CommandUtils.error(event.getMessage());
			}
		});
	}

}
