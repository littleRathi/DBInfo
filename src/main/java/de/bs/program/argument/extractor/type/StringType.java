package de.bs.program.argument.extractor.type;

import de.bs.program.argument.ArgumentException;
import de.bs.program.argument.extractor.ExtractedArgument;
import de.bs.program.argument.process.Arguments;

public class StringType extends Type {
	public StringType(final Class<?> targetType, final ExtractedArgument extractedArgument) {
		super(targetType, extractedArgument);
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
	public Object processArgs(final String option, final String argPart, final Arguments args) {
		String result = argPart.substring(option.length());
		if (getExtractedValues() != null && !getExtractedValues().validValue(result)) {
			throw new ArgumentException(EXC_TYPE_VALUE_NOT_VALID, result, getExtractedArgument().getArgumentName());
		}
		return result;
	}
}
