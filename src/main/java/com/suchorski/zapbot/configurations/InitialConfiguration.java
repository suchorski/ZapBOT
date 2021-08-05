package com.suchorski.zapbot.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.suchorski.zapbot.models.commands.social.UserProfileBackground;
import com.suchorski.zapbot.services.bot.OptionService;
import com.suchorski.zapbot.services.commands.social.ProfileBackgroundService;

@Configuration
public class InitialConfiguration {

	@Value("classpath:images/default.jpg")
	private Resource defaultBackground;

	@Autowired private OptionService optionService;
	@Autowired private ProfileBackgroundService profileBackgroundService;

	@Bean
	public CommandLineRunner developerRunner() {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				if (optionService.findAll().size() == 0) {
					UserProfileBackground profileBackground = new UserProfileBackground("Padrão", defaultBackground.getInputStream().readAllBytes());
					profileBackgroundService.create(profileBackground);
				}
			}
		};
	}

}
