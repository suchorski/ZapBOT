package com.suchorski.zapbot.constants;

import java.awt.Color;

public class Constants {
	
	public static String VERSION = "1.0.0";
	public static Long BUILD = 1L;
	
	public static class OPTIONS {
		public static final String PREFFIX = ".";
	}
	
	public static class XP {		
		public static final long MAX_CALC = 1000L;
	}

	public static class COLORS {		
		public static final Color DEFAULT = Color.BLACK;
		public static final Color OWNER = Color.ORANGE;
		public static final Color SUCCESS = Color.GREEN;
		public static final Color WARNING = Color.YELLOW;
		public static final Color DANGER = Color.RED;
	}

	public static class EMOJIS {
		public static final String SUCCESS = "\u2714";
		public static final String INFO = "\u2139";
		public static final String WARNING = "\u2755";
		public static final String ERROR = "\u274C";
		public static final String MARK = "\u2705";
		public static final String HEART = "\u2764";
		public static final String LOADING = "\uD83D\uDD01";
		public static final String CAKE = "\ud83C\uDF82";
		public static final String ZERO = "\u0030\u20E3";
		public static final String ONE = "\u0031\u20E3";
		public static final String TWO = "\u0032\u20E3";
		public static final String THREE = "\u0033\u20E3";
		public static final String FOUR = "\u0034\u20E3";
		public static final String FIVE = "\u0035\u20E3";
		public static final String SIX = "\u0036\u20E3";
		public static final String SEVEN = "\u0037\u20E3";
		public static final String EIGHT = "\u0038\u20E3";
		public static final String NINE = "\u0039\u20E3";
		public static final String TEN = "\uD83D\uDD1F";
		public static final String FIRST = "\uD83E\uDD47";
		public static final String SECOND = "\uD83E\uDD48";
		public static final String THIRD = "\uD83E\uDD49";
	}

	public static class COOLDOWNS {
		public static final int FAST = 3;
		public static final int SLOW = 10;
		public static final int SLOWER = 30;
	}

	public static final String[] NUMBERS = {
			EMOJIS.ONE, EMOJIS.TWO, EMOJIS.THREE, EMOJIS.FOUR, EMOJIS.FIVE,
			EMOJIS.SIX, EMOJIS.SEVEN, EMOJIS.EIGHT, EMOJIS.NINE, EMOJIS.TEN
	};

	public static final String[] PLACES = {
			EMOJIS.FIRST, EMOJIS.SECOND, EMOJIS.THIRD
	};

	public static class COSTS {
		public static final long AUTO_REACT = 25L;
		public static final long COLOR = 5000L;
		public static final long HEADS_OR_TAILS = 100L;
		public static final long HIGHLIGHT = 1000L;
		public static final long RAFFLE = 200L;
		public static final long RECOMMENDATION = 100000L;
		public static final long SCRATCH_OFF = 250L;
	}
	
}
