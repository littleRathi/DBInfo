package de.bs.cli.jpar;

public class JParException extends RuntimeException {
	private static final long serialVersionUID = 20160303L;

	public JParException(final String msg) {
		super(msg);
	}
	
	public JParException(final String msg, final Object... formatValues) {
		super(String.format(msg, formatValues));
	}
	
	public JParException(final Throwable exception, final String msg) {
		super(msg, exception);
	}
	
	public JParException(final Throwable exception, final String msg, final Object... formatValues) {
		super(String.format(msg, formatValues), exception);
	}
}
