package de.bs.cli.jpar.extractor.type;

import java.util.Collection;

import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.ExceptionMessages;
import de.bs.cli.jpar.extractor.ExtractedOption;
import de.bs.cli.jpar.extractor.ExtractedArguments;
import de.bs.cli.jpar.extractor.HelpParameter;
import de.bs.cli.jpar.process.Parameters;

public abstract class Type implements ExceptionMessages {

	private Class<?> targetType;
	private ExtractedOption option;
	private ExtractedArguments arguments;
	
	public Type(final Class<?> targetType, final ExtractedOption option, final ExtractedArguments arguments) {
		this.targetType = targetType;
		this.option = option;
		this.arguments = arguments;
	}
	
	public ExtractedOption getOption() {
		return option;
	}
	public ExtractedArguments getArguments() {
		return arguments;
	}
	public Class<?> getTargetType() {
		return targetType;
	}
	
	public static Type getTypeProcessor(final Class<?> type, final ExtractedOption option, final ExtractedArguments arguments) {
		if (type == Void.class && option.getClass() == HelpParameter.class) {
			return new VoidType(option, arguments);
		}
		if ((type == void.class || type == Void.class)) {
			throw new JParException(EXC_TYPE_NOT_SUPPORTED, type);
		}
		if (type == Boolean.class || type == boolean.class) {
			return new BooleanType(option, arguments);
		}
		if (type == String.class) {
			return new StringType(option, arguments);
		}
		if (Collection.class.isAssignableFrom(type)) {
			return new CollectionType(type, option, arguments);
		}
		if (option.getOption().sourceType() == Class.class) {
			return new ClassObjectType(type, option, arguments);	
		}
		throw new JParException(EXC_TYPE_UNSUPPORTED, type);
	}
	public abstract void getManualDescription(final StringBuilder descriptionBuilder);
	public abstract Object processArgs(final String option, final String argument, final Parameters args);


	protected void manualDescriptionForValidValues(final StringBuilder descriptionBuilder) {
		
	}
}
