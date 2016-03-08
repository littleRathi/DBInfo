package de.bs.program.argument;

public class ArgumentException extends RuntimeException {
	private static final long serialVersionUID = 20160303L;

	public ArgumentException(final String msg) {
		super(msg);
	}
	
	public ArgumentException(final String msg, final Object... formatValues) {
		super(String.format(msg, formatValues));
	}
	
	public ArgumentException(final Throwable exception, final String msg) {
		super(msg, exception);
	}
	
	public ArgumentException(final Throwable exception, final String msg, final Object... formatValues) {
		super(String.format(msg, formatValues), exception);
	}
}
