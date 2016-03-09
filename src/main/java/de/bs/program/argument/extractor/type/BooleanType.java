package de.bs.program.argument.extractor.type;

import de.bs.program.argument.ArgumentException;
import de.bs.program.argument.ExceptionMessages;
import de.bs.program.argument.extractor.ExtractedArgument;
import de.bs.program.argument.process.Arguments;

public class BooleanType extends Type implements ExceptionMessages {
	public static final String TRUE = "+";
	public static final String FALSE = "-";

	public BooleanType(final ExtractedArgument extractedArgument) {
		super(Boolean.class, extractedArgument);
	}
	
	@Override
	public void getUsageDescription(final StringBuilder descriptionBuilder) {
		descriptionBuilder.append(getExtractedArgument().getArgumentName()).append("+ to enable or ").append(getExtractedArgument().getArgumentName())
			.append("- to disable.").toString();
	}
	
	@Override
	public boolean isAssignable(final Object value) {
		return TRUE.equals(value) || FALSE.equals(value);
	}
	
	@Override
	public Object processArgs(final String argumentName, final String argumentValue, final Arguments args) {
		if (getExtractedValues() != null) {
			throw new ArgumentException(EXC_TYPE_NOT_VALIDATEABLE, getExtractedArgument().getArgumentName());
		}
		boolean bool = false;
		if (TRUE.equals(argumentValue)) {
			bool = true;
		} else if (FALSE.equals(argumentValue)) {
			bool = false;
		} else {
			throw new ArgumentException(EXC_TYPE_WRONG_VALUE, getExtractedArgument().getArgumentName(), argumentValue);
		}
		return bool;
	}

}
