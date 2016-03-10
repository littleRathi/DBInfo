package de.bs.cli.jpar.process;

public class Parameters {
	private String[] args;
	private int position = -1;
	
	public Parameters(final String[] args) {
		this.args = args;
	}
	
	public boolean next() {
		position++;
		return position < args.length;
	}
	
	public String get() {
		return args[position];
	}
	
	public boolean before() {
		--position;
		return position >= 0;
	}
	
	public void reset() {
		position = 0;
	}
}
