package de.bs.cli.jpar.extractor;

import de.bs.cli.jpar.CliProgram;
import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.config.Consts;
import de.bs.cli.jpar.config.ExceptionMessages;

public class ExtractedProgram implements ExceptionMessages {
	private String elName = Consts.PROG_SHORTCUT;
	private Class<?> type;
	
	private CliProgram program;
	
	public ExtractedProgram(final Class<?> programClass, final CliProgram programAnnotation) {
		if (programClass == null) {
			throw new JParException(EXC_EXTRACTOR_MISSING_PROG_CLASS);
		}
		if (programAnnotation == null) {
			throw new JParException(EXC_EXTRACTOR_MISSING_PROG_ANNOTATION, programClass.getName());
		}
		this.type = programClass;
		this.program = programAnnotation;
	}
	
	public String getElName() {
		return elName;
	}
	public Class<?> getType() {
		return type;
	}
	public String getProgramName() {
		return program.name();
	}
	public String getDescription() {
		return program.description();
	}
	public String getSignature() {
		return program.signature();
	}
}
