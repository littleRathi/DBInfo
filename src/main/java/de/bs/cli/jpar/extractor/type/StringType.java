package de.bs.cli.jpar.extractor.type;

import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.extractor.ExtractedOption;
import de.bs.cli.jpar.process.Parameters;

public class StringType extends Type {
	public StringType(final ExtractedOption extractedArgument) {
		super(String.class, extractedArgument);
	}
	
	@Override
	public void getManualDescription(final StringBuilder descriptionBuilder) {
		descriptionBuilder.append(getOption().getOptionName()).append("<STRING>").toString();
	}
	
	@Override
	public boolean isAssignable(final Object value) {
		return true;
	}
	
	@Override
	public Object processArgs(final String argumentName, final String argumentValue, final Parameters args) {
		if (getValues() != null && !getValues().validValue(argumentValue)) {
			throw new JParException(EXC_TYPE_VALUE_NOT_VALID, argumentValue, getOption().getOptionName());
		}
		return argumentValue;
	}
}
