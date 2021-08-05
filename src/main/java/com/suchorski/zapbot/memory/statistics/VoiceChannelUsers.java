package com.suchorski.zapbot.memory.statistics;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class VoiceChannelUsers {
	
	public static Map<Long, Instant> voiceUsers = new HashMap<Long, Instant>();
	
	public static void addUser(long id) {
		voiceUsers.put(id, Instant.now());
	}
	
	public static int delUser(long id) {
		 Instant instant = voiceUsers.remove(id);
		 if (instant != null) {
			 return (int) Duration.between(instant, Instant.now()).abs().toMinutes();
		 }
		 return 0;
	}

}
