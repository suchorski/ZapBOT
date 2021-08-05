package com.suchorski.zapbot.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import com.suchorski.zapbot.components.Bot;

@Configuration
public class BotConfiguration {
	
	@Autowired private Bot bot;
	
	@Bean
	public TaskExecutor botTaskExecutor() {
		return new SimpleAsyncTaskExecutor();
	}
	
	@Bean
	public CommandLineRunner botRunner(TaskExecutor botTaskExecutor) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				botTaskExecutor.execute(bot);
			}
		};
	}

}

