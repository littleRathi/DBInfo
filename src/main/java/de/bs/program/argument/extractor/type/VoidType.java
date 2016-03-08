package de.bs.program.argument.extractor.type;

import de.bs.program.argument.extractor.ExtractedArgument;
import de.bs.program.argument.process.Arguments;

/*
 * For internal use only
 */
public class VoidType extends Type {
	public VoidType(Class<?> targetType, ExtractedArgument extractedArgument) {
		super(targetType, extractedArgument);
	}

	@Override
	public void getUsageDescription(StringBuilder descriptionBuilder) {
		descriptionBuilder.append(getExtractedArgument().getArgumentName()).append(" to show this help information.");
	}

	@Override
	public boolean isAssignable(Object value) {
		return false;
	}

	@Override
	public Object processArgs(String option, String argPart, Arguments args) {
		return null;
	}

}
