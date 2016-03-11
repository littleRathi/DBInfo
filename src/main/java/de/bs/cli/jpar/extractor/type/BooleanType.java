package de.bs.cli.jpar.extractor.type;

import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.ExceptionMessages;
import de.bs.cli.jpar.extractor.ExtractedOption;
import de.bs.cli.jpar.extractor.ExtractedValues;
import de.bs.cli.jpar.process.Parameters;

public class BooleanType extends Type implements ExceptionMessages {
	public static final String TRUE = "+";
	public static final String TRUE_IMPLIZIT = "";
	public static final String FALSE = "-";

	public BooleanType(final ExtractedOption option) {
		super(Boolean.class, option);
		if (option.getSourceType() != null && option.getSourceType() != Void.class) {
			throw new JParException(""); // TODO exception: source darf bei boolean nicht gesetzt sein.
		}
	}
	
	@Override
	public void setValues(ExtractedValues values) {
		throw new JParException(EXC_TYPE_NOT_VALIDATEABLE, getOption().getOptionName());
	}
	
	@Override
	public void getManualDescription(final StringBuilder descriptionBuilder) {
		descriptionBuilder.append(getOption().getOptionName()).append(":+ or ")
			.append(getOption()).append(" to enable or also")
			.append(getOption().getOptionName())
			.append(":- to disable.").toString();
	}
	
	@Override
	public boolean isAssignable(final Object value) {
		return TRUE.equals(value) || FALSE.equals(value);
	}
	
	@Override
	public Object processArgs(final String option, final String argument, final Parameters args) {
		if (getValues() != null) {
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
