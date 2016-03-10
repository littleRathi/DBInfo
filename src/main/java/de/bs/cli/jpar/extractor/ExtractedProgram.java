package de.bs.cli.jpar.extractor;

import de.bs.cli.jpar.CliProgram;
import de.bs.cli.jpar.JPar;

public class ExtractedProgram {
	private String elName = JPar.PROG;
	private Class<?> type;
	
	private CliProgram argumentProgram;
	
	public ExtractedProgram(final Class<?> programClass, final CliProgram programAnnotation) {
		this.type = programClass;
		this.argumentProgram = programAnnotation;
	}
	
	public String getElName() {
		return elName;
	}
	public Class<?> getType() {
		return type;
	}
	public String getProgramName() {
		return argumentProgram.name();
	}
	public String getDescription() {
		return argumentProgram.description();
	}
	public String getSignature() {
		return argumentProgram.signature();
	}
}
