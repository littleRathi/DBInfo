package de.bs.program.argument.extractor.type;

import de.bs.program.argument.ArgumentException;
import de.bs.program.argument.extractor.ExtractedArgument;
import de.bs.program.argument.process.Arguments;

public class StringType extends Type {
	public StringType(final ExtractedArgument extractedArgument) {
		super(String.class, extractedArgument);
	}
	
	@Override
	public void getUsageDescription(final StringBuilder descriptionBuilder) {
		descriptionBuilder.append(getExtractedArgument().getArgumentName()).append("<STRING>").toString();
	}
	
	@Override
	public boolean isAssignable(final Object value) {
		return true;
	}
	
	@Override
	public Object processArgs(final String argumentName, final String argumentValue, final Arguments args) {
		if (getExtractedValues() != null && !getExtractedValues().validValue(argumentValue)) {
			throw new ArgumentException(EXC_TYPE_VALUE_NOT_VALID, argumentValue, getExtractedArgument().getArgumentName());
		}
		return argumentValue;
	}
}
