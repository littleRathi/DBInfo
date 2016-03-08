package de.bs.program.argument.extractor.type;

import java.util.HashSet;
import java.util.Set;

import de.bs.program.argument.ArgumentException;
import de.bs.program.argument.extractor.ExtractedArgument;
import de.bs.program.argument.process.Arguments;

// TODO: delete, replaced with CollectionType
@Deprecated
public class SetType extends Type {
	public SetType(final Class<?> targetType, final ExtractedArgument extractedArgument) {
		super(targetType, extractedArgument);
	}
	
	@Override
	public void getUsageDescription(final StringBuilder descriptionBuilder) {
		ExtractedArgument ap = getExtractedArgument();
		Class<?> listType = ap.getSourceType();
		if (ap.getDelimiter() == null || ap.getDelimiter().isEmpty()) {
			throw new ArgumentException(EXC_TYPE_MISSING_DELEMITER, ap.getArgumentName(), ap.getDelimiter());
		}
//		Needed, because, the generic type of Set gets erased (so this information is missing)
		if (listType == null || listType == Void.class || listType == void.class) {
			throw new ArgumentException(EXC_TYPE_MISSING_SOURCE_TYPE, ap.getArgumentName(), ap.getDelimiter());
		}
		if (ap.getValues() != null && ap.getValues().length > 0) {
			listType = String.class;
		}
		
		descriptionBuilder.append("<").append(listType.getSimpleName()).append(">[").append(ap.getDelimiter()).append("<").append(listType.getSimpleName()).append(">]");
		
		createWithSpecific(ap, descriptionBuilder, true);
	}
	
	@Override
	public boolean isAssignable(final Object value) {
		return false;
	}
	
	@Override
	public Object processArgs(String option, String argPart, Arguments args) {
		String data = argPart.substring(option.length());
		if (getExtractedValues() == null) {
			throw new ArgumentException(EXC_TYPE_MISSING_VALUES, option);
		}
		String[] argValues = data.split(getExtractedValues().getDelimiter());
		
		if (getExtractedValues() != null && !getExtractedValues().validValues(argValues)) {
			throw new ArgumentException(EXC_TYPE_INVALID_VALUE, argValues, getExtractedArgument().getArgumentName());
		}

		Set<String> newSet = new HashSet<String>();
		for (String value: argValues) {
			newSet.add(value);
		}
		
		return newSet;
	}
	
	private static void createWithSpecific(final ExtractedArgument ap, final StringBuilder result, final boolean multiple) {
		String[][] values = ap.getValues();
		if (values != null) {
			result.append(". Following values can be used: ");
			for (int i = 0; i < values.length; i++) {
				String[] subValues = values[i];
				
				if (subValues.length > 1) {
					if (multiple) {
						result.append("one or more of ");
					} else {
						result.append("one of ");
					}
					result.append(subValues[0]);
					for (int j = 0; j < subValues.length; j++) {
						result.append(", ").append(subValues[j]);
					}
					result.append(" ");
				} else {
					result.append("single option " + subValues[0]);
				}
			}
		}
	}
}