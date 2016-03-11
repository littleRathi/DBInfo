package de.bs.cli.jpar.extractor.type;

import java.util.Collection;

import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.ExceptionMessages;
import de.bs.cli.jpar.extractor.ExtractedOption;
import de.bs.cli.jpar.extractor.ExtractedValues;
import de.bs.cli.jpar.extractor.HelpParameter;
import de.bs.cli.jpar.process.Parameters;

public abstract class Type implements ExceptionMessages {

	private Class<?> targetType;
	private ExtractedOption option;
	private ExtractedValues values;
	
	public Type(final Class<?> targetType, final ExtractedOption extractedArgument) {
		this.targetType = targetType;
		this.option = extractedArgument;
	}
	
	public ExtractedOption getOption() {
		return option;
	}
	public void setValues(ExtractedValues values) {
		this.values = values;
	}
	public ExtractedValues getValues() {
		return values;
	}
	public Class<?> getTargetType() {
		return targetType;
	}
	
	public static Type getTypeProcessor(final Class<?> type, final ExtractedOption option) {
		if (type == Void.class && option.getClass() == HelpParameter.class) {
			return new VoidType(option);
		}
		if ((type == void.class || type == Void.class)) {
			throw new JParException(EXC_TYPE_NOT_SUPPORTED, type);
		}
		if (type == Boolean.class || type == boolean.class) {
			return new BooleanType(option);
		}
		if (type == String.class) {
			return new StringType(option);
		}
		if (Collection.class.isAssignableFrom(type)) {
			return new CollectionType(type, option);
		}
//		if (type == Set.class) {
//			return new SetType(Set.class, extractedArgument);
//		}
		if (option.getOption().sourceType() == Class.class) {
			return new ObjectType(type, option);	
		}
		throw new JParException(EXC_TYPE_UNSUPPORTED, type);
	}
	public abstract void getManualDescription(final StringBuilder descriptionBuilder);
	public abstract boolean isAssignable(final Object value);
	public abstract Object processArgs(final String option, final String argument, final Parameters args);
}
