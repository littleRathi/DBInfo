package de.bs.cli.jpar.extractor.type;

import java.util.HashSet;
import java.util.Set;

import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.extractor.ExtractedOption;
import de.bs.cli.jpar.process.Parameters;

// TODO: delete, replaced with CollectionType
@Deprecated
public class SetType extends Type {
	public SetType(final Class<?> targetType, final ExtractedOption extractedArgument) {
		super(targetType, extractedArgument);
	}
	
	@Override
	public void getManualDescription(final StringBuilder descriptionBuilder) {
		ExtractedOption ap = getOption();
		Class<?> listType = ap.getSourceType();
		if (ap.getDelimiter() == null || ap.getDelimiter().isEmpty()) {
			throw new JParException(EXC_TYPE_MISSING_DELEMITER, ap.getOptionName(), ap.getDelimiter());
		}
//		Needed, because, the generic type of Set gets erased (so this information is missing)
		if (listType == null || listType == Void.class || listType == void.class) {
			throw new JParException(EXC_TYPE_MISSING_SOURCE_TYPE, ap.getOptionName(), ap.getDelimiter());
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
	public Object processArgs(String option, String argPart, Parameters args) {
		String data = argPart.substring(option.length());
//		if (getValues() == null) {
//			throw new JParException(EXC_TYPE_MISSING_VALUES, option);
//		}
		String[] argValues = data.split(getValues().getDelimiter());
		
		if (getValues() != null && !getValues().validValues(argValues)) {
			throw new JParException(EXC_TYPE_INVALID_VALUE, argValues, getOption().getOptionName());
		}

		Set<String> newSet = new HashSet<String>();
		for (String value: argValues) {
			newSet.add(value);
		}
		
		return newSet;
	}
	
	private static void createWithSpecific(final ExtractedOption ap, final StringBuilder result, final boolean multiple) {
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
