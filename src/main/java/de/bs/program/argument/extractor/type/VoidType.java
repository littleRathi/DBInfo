package de.bs.program.argument.extractor.type;

import de.bs.program.argument.extractor.ExtractedArgument;
import de.bs.program.argument.process.Arguments;

/*
 * For internal use only
 */
public class VoidType extends Type {
	public VoidType(final ExtractedArgument extractedArgument) {
		super(Void.class, extractedArgument);
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
	public Object processArgs(String argumentName, String argumentValue, Arguments args) {
		return null;
	}

}
