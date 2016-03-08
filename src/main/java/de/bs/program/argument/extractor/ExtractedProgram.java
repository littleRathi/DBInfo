package de.bs.program.argument.extractor;

import de.bs.program.argument.ArgumentProgram;
import de.bs.program.argument.Arguments;

public class ExtractedProgram {
	private String elName = Arguments.PROG;
	private Class<?> type;
	
	private ArgumentProgram argumentProgram;
	
	public ExtractedProgram(final Class<?> programClass, final ArgumentProgram programAnnotation) {
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
