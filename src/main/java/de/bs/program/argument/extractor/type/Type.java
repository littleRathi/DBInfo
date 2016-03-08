package de.bs.program.argument.extractor.type;

import java.util.Collection;

import de.bs.program.argument.ArgumentException;
import de.bs.program.argument.ExceptionMessages;
import de.bs.program.argument.extractor.ExtractedArgument;
import de.bs.program.argument.extractor.ExtractedValues;
import de.bs.program.argument.extractor.HelpArgument;
import de.bs.program.argument.process.Arguments;

public abstract class Type implements ExceptionMessages {

	private Class<?> targetType;
	private ExtractedArgument extractedArgument;
	private ExtractedValues extractedValues;
	
	public Type(final Class<?> targetType, final ExtractedArgument extractedArgument) {
		this.targetType = targetType;
		this.extractedArgument = extractedArgument;
	}
	
	public void setExtractedArgument(final ExtractedArgument extractedArgument) {
		this.extractedArgument = extractedArgument;
	}
	public ExtractedArgument getExtractedArgument() {
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
	
	public static Type getTypeProcessor(final Class<?> type, final ExtractedArgument extractedArgument) {
		if (type == Void.class && extractedArgument.getClass() == HelpArgument.class) {
			return new VoidType(type, extractedArgument);
		}
		if ((type == void.class || type == Void.class)) {
			throw new ArgumentException(EXC_TYPE_NOT_SUPPORTED, type);
		}
		if (type == Boolean.class || type == boolean.class) {
			return new BooleanType(Boolean.class, extractedArgument);
		}
		if (type == String.class) {
			return new StringType(String.class, extractedArgument);
		}
		if (Collection.class.isAssignableFrom(type)) {
			return new CollectionType(type, extractedArgument);
		}
//		if (type == Set.class) {
//			return new SetType(Set.class, extractedArgument);
//		}
		if (extractedArgument.getArgument().sourceType() == Class.class) {
			return new ObjectType(type, extractedArgument);	
		}
		throw new ArgumentException(EXC_TYPE_UNSUPPORTED, type);
	}
	public abstract void getUsageDescription(final StringBuilder descriptionBuilder);
	public abstract boolean isAssignable(final Object value);
	public abstract Object processArgs(final String option, final String argPart, final Arguments args);
}
