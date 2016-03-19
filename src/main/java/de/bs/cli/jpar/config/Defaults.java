package de.bs.cli.jpar.config;

import de.bs.cli.jpar.JParException;

public class Defaults {
	private static String LIST_DELIMITER = Consts.LIST_DELIMITER;
	private static String OPTION_PREFIX = Consts.OPTION_PRFIX;
	private static String OPTION_DELIMITER = Consts.OPTION_DELIMITER;
	
	private Defaults() {
	}
	
	public static final String getListDelimiter() {
		return LIST_DELIMITER;
	}
	
	public static final void setListDelimiter(final String newListDelimiter) {
		if (newListDelimiter == null || newListDelimiter.isEmpty()) {
			throw new JParException(""); // TODO: Exception
		}
		LIST_DELIMITER = newListDelimiter;
	}
	
	public static final String getOptionPrefix() {
		return OPTION_PREFIX;
	}
	
	public static final void setOptionPrefix(final String newOptionPrefix) {
		if (newOptionPrefix == null || newOptionPrefix.isEmpty()) {
			throw new JParException(""); // TODO: Exception
		}
		OPTION_PREFIX = newOptionPrefix;
	}
	
	public static final String getOptionDelimiter() {
		return OPTION_DELIMITER;
	}
	
	public static final void setOptionDelimiter(final String newOptionDelimiter) {
		if (newOptionDelimiter == null || newOptionDelimiter.isEmpty()) {
			throw new JParException(""); // TODO: Exception
		}
		OPTION_DELIMITER = newOptionDelimiter;
	}
}
