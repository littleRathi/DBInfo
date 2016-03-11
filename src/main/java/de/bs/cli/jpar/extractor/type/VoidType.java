package de.bs.cli.jpar.extractor.type;

import de.bs.cli.jpar.extractor.ExtractedOption;
import de.bs.cli.jpar.process.Parameters;

/*
 * For internal use only
 */
public class VoidType extends Type {
	public VoidType(final ExtractedOption extractedArgument) {
		super(Void.class, extractedArgument);
	}

	@Override
	public void getManualDescription(StringBuilder descriptionBuilder) {
		descriptionBuilder.append(getOption().getOptionName()).append(" to show this help information.");
	}

	@Override
	public boolean isAssignable(Object value) {
		return false;
	}

	@Override
	public Object processArgs(String argumentName, String argumentValue, Parameters args) {
		return null;
	}

}
