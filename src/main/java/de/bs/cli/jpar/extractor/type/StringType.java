package de.bs.cli.jpar.extractor.type;

import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.extractor.ExtractedArguments;
import de.bs.cli.jpar.extractor.ExtractedOption;
import de.bs.cli.jpar.process.Parameters;

public class StringType extends Type {
	public StringType(final ExtractedOption option, final ExtractedArguments arguments) {
		super(String.class, option, arguments);
		
		if (option.getSourceType() != null && option.getSourceType() != Void.class) {
			throw new JParException(EXC_TYPE_SOURCE_MUST_NOT_BE_SET, option.getOptionName(), option.getSourceType(), "String");
		}
	}
	
	@Override
	public void getManualDescription(final StringBuilder descriptionBuilder) {
		descriptionBuilder.append(getOption().getOptionName()).append("<STRING>");
	}
	
	@Override
	public Object processArgs(final String option, final String argument, final Parameters args) {
		if (getArguments() != null && !getArguments().validValue(argument)) {
			throw new JParException(EXC_TYPE_VALUE_NOT_VALID, argument, getOption().getOptionName());
		}
		return argument;
	}
}
