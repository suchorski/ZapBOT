package com.suchorski.zapbot.memory.listeners;

import java.util.LinkedList;
import java.util.List;

public class Messages {
	
	private static List<Pass> passes = new LinkedList<Pass>();
	
	public static void addPass(long channelId, long userId) {
		passes.add(new Pass(channelId, userId));
	}

	public static boolean shouldIgnore(long channelId, long userId) {
		return passes.remove(new Pass(channelId, userId));
	}

}
