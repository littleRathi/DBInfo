package de.bs.program.argument.extractor;

import de.bs.program.argument.Argument;
import de.bs.program.argument.ArgumentException;
import de.bs.program.argument.ExceptionMessages;
import de.bs.program.argument.extractor.type.Type;
import de.bs.program.argument.process.Arguments;

public abstract class ExtractedArgument implements ExceptionMessages {
	private static final String ARGUMENT_NAME_PATTERN = "^[a-zA-Z0-9]+$";

	private String elName; // like PROG or PW simple name given in @Argument.name().toUpperCase()
	private String argumentName; // (Optional) only by options; would be -PW: "-" @Argument.name() ":"
	
	private Argument argument;
	private Type type;
	
	public ExtractedArgument(final Argument argument) {
		if (argument.name() == null || !argument.name().matches(ARGUMENT_NAME_PATTERN)) {
			throw new ArgumentException(EXC_EXTRACTOR_NAME_WRONG_PATTERN, argument.name(), ARGUMENT_NAME_PATTERN);
		}
		
		this.argument = argument;
		this.elName = this.argument.name().toUpperCase();
		this.argumentName = "-" + this.argument.name();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((elName == null) ? 0 : elName.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!getClass().isAssignableFrom(obj.getClass()))
			return false;
		ExtractedArgument other = (ExtractedArgument) obj;
		if (elName == null) {
			if (other.elName != null)
				return false;
		} else if (!elName.equals(other.elName))
			return false;
		return true;
	}
	
	public String getElName() {
		return elName;
	}
	public String getName() {
		return argument.name();
	}
	public String getArgumentName() {
		return argumentName;
	}
	
	public Class<?> getTargetType() {
		return type.getTargetType();
	}
	protected void setTargetType(final Class<?> targetType) {
		this.type = Type.getTypeProcessor(targetType, this);
	}
	public abstract String getTargetName();
	
	public Argument getArgument() {
		return argument;
	}
	public Class<?> getSourceType() {
		return argument.sourceType();
	}
	public String getDescription() {
		return argument.description();
	}
	public boolean isRequired() {
		return argument.required();
	}

	public String getDelimiter() {
		return type.getExtractedValues() != null ? type.getExtractedValues().getDelimiter() : null;
	}
	public String[][] getValues() {
		return type.getExtractedValues() != null ? type.getExtractedValues().getValues() : null;
	}
	public Type getType() {
		return type;
	}
	
	public abstract void prozessArg(final Object program, final String argumentName, final String argumentValue, final Arguments args);
}
