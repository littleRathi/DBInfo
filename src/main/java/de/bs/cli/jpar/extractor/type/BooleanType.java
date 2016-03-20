package de.bs.cli.jpar.extractor.type;

import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.config.ExceptionMessages;
import de.bs.cli.jpar.extractor.ExtractedOption;
import de.bs.cli.jpar.extractor.ExtractedArguments;
import de.bs.cli.jpar.process.Parameters;

public class BooleanType extends Type implements ExceptionMessages {
	public static final String TRUE = "+";
	public static final String TRUE_IMPLIZIT = "";
	public static final String FALSE = "-";

	public BooleanType(final ExtractedOption option, final ExtractedArguments arguments) {
		super(Boolean.class, option, arguments);
		if (arguments != null) {
			throw new JParException(EXC_TYPE_NOT_VALIDATEABLE, getOption().getOptionName());
		}
		if (option.getSourceType() != null && option.getSourceType() != Void.class) {
			throw new JParException(EXC_TYPE_SOURCE_MUST_NOT_BE_SET, option.getOptionName(), option.getSourceType(), "Boolean");
		}
	}
	
	@Override
	public void getManualDescription(final StringBuilder descriptionBuilder) {
		descriptionBuilder.append(getOption().getOptionName()).append(":+ or ")
			.append(getOption()).append(" to enable or also")
			.append(getOption().getOptionName())
			.append(":- to disable.");
	}
	
	@Override
	public Object processArgs(final String option, final String argument, final Parameters args) {
		if (getArguments() != null) {
			throw new JParException(EXC_TYPE_NOT_VALIDATEABLE, getOption().getOptionName());
		}
		boolean bool = false;
		if (TRUE_IMPLIZIT.equals(argument)) {
			bool = true;
		} else if (TRUE.equals(argument)) {
			bool = true;
		} else if (FALSE.equals(argument)) {
			bool = false;
		} else {
			throw new JParException(EXC_TYPE_WRONG_VALUE, getOption().getOptionName(), argument);
		}
		return bool;
	}
}
