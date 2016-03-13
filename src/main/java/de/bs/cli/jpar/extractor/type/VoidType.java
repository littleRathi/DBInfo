package de.bs.cli.jpar.extractor.type;

import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.extractor.ExtractedArguments;
import de.bs.cli.jpar.extractor.ExtractedOption;
import de.bs.cli.jpar.process.Parameters;

/*
 * For internal use only
 */
public class VoidType extends Type {
	public VoidType(final ExtractedOption option, final ExtractedArguments arguments) {
		super(Void.class, option, arguments);
		if (option.getSourceType() != null && option.getSourceType() != Void.class) {
			throw new JParException(EXC_TYPE_SOURCE_MUST_NOT_BE_SET, option.getOptionName(), option.getSourceType(), "Boolean");
		}
		if (arguments != null) {
			throw new JParException(EXC_TYPE_ARGUMENTS_NOT_ALLOWED, option.getOptionName());
		}
	}

	@Override
	public void getManualDescription(StringBuilder descriptionBuilder) {
		descriptionBuilder.append(getOption().getOptionName()).append(" to show this help information.");
	}

	@Override
	public Object processArgs(String option, String argument, Parameters args) {
		return null;
	}

}
