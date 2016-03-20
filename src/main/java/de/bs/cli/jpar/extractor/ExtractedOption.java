package de.bs.cli.jpar.extractor;

import de.bs.cli.jpar.Option;
import de.bs.cli.jpar.config.Consts;
import de.bs.cli.jpar.config.Defaults;
import de.bs.cli.jpar.config.ExceptionMessages;
import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.extractor.type.Type;
import de.bs.cli.jpar.process.Parameters;

public abstract class ExtractedOption implements ExceptionMessages {

	private String elName; // like PROG or PW simple name given in @Argument.name().toUpperCase()
	private String optionName; // (Optional) only by options; would be -PW: "-" @Argument.name() ":"
	
	private Option option;
	private Type type;
	
	public static String asOptionName(final String name) {
		return Defaults.getOptionPrefix() + name;
	}
	
	public static String asElName(final String name) {
		return name.toUpperCase();
	}
	
	public ExtractedOption(final Option option, final ExtractedArguments arguments, final Class<?> targetType) {
		if (option.name() == null || !option.name().matches(Consts.ARGUMENT_NAME_PATTERN)) {
			throw new JParException(EXC_EXTRACTOR_NAME_WRONG_PATTERN, option.name(), Consts.ARGUMENT_NAME_PATTERN);
		}
		
		this.option = option;
		this.elName = asElName(this.option.name());
		this.optionName = asOptionName(this.option.name());
		this.type = Type.getTypeProcessor(targetType, this, arguments);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((elName == null) ? 0 : elName.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!getClass().isAssignableFrom(obj.getClass()))
			return false;
		ExtractedOption other = (ExtractedOption) obj;
		if (elName == null) {
			if (other.elName != null)
				return false;
		} else if (!elName.equals(other.elName))
			return false;
		return true;
	}
	
	public String getElName() {
		return elName;
	}
	public String getName() {
		return option.name();
	}
	public String getOptionName() {
		return optionName;
	}
	
	public Class<?> getTargetType() {
		return type.getTargetType();
	}
	public abstract String getTargetName();
	
	public Option getOption() {
		return option;
	}
	public Class<?> getSourceType() {
		return option.sourceType();
	}
	public String getManualDescription() {
		return option.description();
	}
	public boolean isRequired() {
		return option.required();
	}

	public Type getType() {
		return type;
	}
	
	public abstract void processArg(final Object program, final String option, final String argument, final Parameters args);
}
