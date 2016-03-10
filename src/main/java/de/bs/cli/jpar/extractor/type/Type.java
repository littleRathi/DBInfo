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
	private ExtractedOption extractedArgument;
	private ExtractedValues extractedValues;
	
	public Type(final Class<?> targetType, final ExtractedOption extractedArgument) {
		this.targetType = targetType;
		this.extractedArgument = extractedArgument;
	}
	
	public ExtractedOption getExtractedArgument() {
		return extractedArgument;
	}
	public void setExtractedValues(ExtractedValues extractedValues) {
		this.extractedValues = extractedValues;
	}
	public ExtractedValues getExtractedValues() {
		return extractedValues;
	}
	public Class<?> getTargetType() {
		return targetType;
	}
	
	public static Type getTypeProcessor(final Class<?> type, final ExtractedOption extractedArgument) {
		if (type == Void.class && extractedArgument.getClass() == HelpParameter.class) {
			return new VoidType(extractedArgument);
		}
		if ((type == void.class || type == Void.class)) {
			throw new JParException(EXC_TYPE_NOT_SUPPORTED, type);
		}
		if (type == Boolean.class || type == boolean.class) {
			return new BooleanType(extractedArgument);
		}
		if (type == String.class) {
			return new StringType(extractedArgument);
		}
		if (Collection.class.isAssignableFrom(type)) {
			return new CollectionType(type, extractedArgument);
		}
//		if (type == Set.class) {
//			return new SetType(Set.class, extractedArgument);
//		}
		if (extractedArgument.getOption().sourceType() == Class.class) {
			return new ObjectType(type, extractedArgument);	
		}
		throw new JParException(EXC_TYPE_UNSUPPORTED, type);
	}
	public abstract void getUsageDescription(final StringBuilder descriptionBuilder);
	public abstract boolean isAssignable(final Object value);
	public abstract Object processArgs(final String argumentName, final String argumentValue, final Parameters args);
}
