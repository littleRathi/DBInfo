package de.bs.cli.jpar.extractor.type;

import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.ExceptionMessages;
import de.bs.cli.jpar.extractor.ExtractedOption;
import de.bs.cli.jpar.process.Parameters;

public class BooleanType extends Type implements ExceptionMessages {
	public static final String TRUE = "+";
	public static final String FALSE = "-";

	public BooleanType(final ExtractedOption extractedArgument) {
		super(Boolean.class, extractedArgument);
	}
	
	@Override
	public void getUsageDescription(final StringBuilder descriptionBuilder) {
		descriptionBuilder.append(getExtractedArgument().getOptionName()).append("+ to enable or ").append(getExtractedArgument().getOptionName())
			.append("- to disable.").toString();
	}
	
	@Override
	public boolean isAssignable(final Object value) {
		return TRUE.equals(value) || FALSE.equals(value);
	}
	
	@Override
	public Object processArgs(final String argumentName, final String argumentValue, final Parameters args) {
		if (getExtractedValues() != null) {
			throw new JParException(EXC_TYPE_NOT_VALIDATEABLE, getExtractedArgument().getOptionName());
		}
		boolean bool = false;
		if (TRUE.equals(argumentValue)) {
			bool = true;
		} else if (FALSE.equals(argumentValue)) {
			bool = false;
		} else {
			throw new JParException(EXC_TYPE_WRONG_VALUE, getExtractedArgument().getOptionName(), argumentValue);
		}
		return bool;
	}

}
