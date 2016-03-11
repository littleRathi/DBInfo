package de.bs.cli.jpar.extractor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.bs.cli.jpar.Option;
import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.Arguments;
import de.bs.cli.jpar.ExceptionMessages;

public class ExtractedValues implements ExceptionMessages {
	private Arguments arguments;
	private ExtractedOption option;
	private String[][] values;
	private List<Set<String>> valuesSet = new LinkedList<Set<String>>();
	
	private ExtractedValues(final String[][] values, final Arguments arguments, final ExtractedOption option) {
		this.values = values;
		this.arguments = arguments;
		this.option = option;
		
		for (String[] subValues: values) {
			Set<String> subSet = new HashSet<String>();
			for (String value: subValues) {
				subSet.add(value);
			}
			valuesSet.add(subSet);
		}
	}
	
	public ExtractedOption getOption() {
		return option;
	}
	public String getDelimiter() {
		return arguments.delimiter();
	}
	public String[][] getValues() {
		return values;
	}
	
	public boolean validValue(final String value) {
		boolean result = false;
		for (Set<String> valueSet: valuesSet) {
			if (valueSet.contains(value)) {
				result = true;
				break;
			}
		}
		return result;
	}
	public boolean validValues(final String[] values) {
		boolean valid = false;
		
		for (Set<String> valueSet: valuesSet) {
			if (validInSubValues(values, valueSet)) {
				valid = true;
				break;
			}
		}
		
		return valid;
	}
	private boolean validInSubValues(final String[] values, final Set<String> valueSet) {
		boolean valid = true;
		
		for (String value: values) {
			if (!valueSet.contains(value)) {
				valid = false;
				break;
			}
		}
		
		return valid;
	}
	
	private static String[][] extractValuesFromArgumentValues(final Arguments arguments, final ExtractedOption option) {
		if (arguments.values() == null || arguments.values().length == 0) {
			throw new JParException(EXC_EXTRACTOR_NEED_VALUES, option.getOptionName()); // TODO Exception [REMOVE CONDITION? allow no list]
		}
		if (arguments.name() != null && !arguments.name().isEmpty()) {
			throw new JParException(EXC_EXTRACTOR_VALUES_NO_NAME, option.getOptionName());
		}
		
		return new String[][]{arguments.values()};
	}

//	TODO: handling of the field: allowed should be:  first only array
//	Array, Collection (List, Set, ...)
//		=> get Type and try to transform, if error, then the is the type wrong
//	internal transformation to a internal representation from String[][] Validate <-(ValidateValues|ValidateValue)
	private static String[][] extractValuesFromField(final Arguments arguments, final ExtractedOption option, final Field field) {
		preConditionExtractValues(arguments, option, field.getType(), field.getModifiers(), field.toString());

		String[][] values = null;
		try {
			field.setAccessible(true);
			values = (String[][])field.get(null);
		} catch (Exception e) {
			throw new JParException(EXC_EXTRACTOR_VALUES_GET_VALUES, field.toString(), option.getOptionName());
		}
		
		postConditionExtractValues(values, field.toString());

		return values;
	}
	
	private static String[][] extractValuesFromMethod(final Arguments arguments, final ExtractedOption option, final Method method) {
		preConditionExtractValues(arguments, option, method.getReturnType(), method.getModifiers(), method.toString());
		
		Class<?>[] parameters = method.getParameterTypes();
		if (parameters != null && parameters.length > 0) {
			throw new JParException(EXC_EXTRACTOR_VALUES_METHOD_NO_PARAMETERS, method.toString(), parameters.length);
		}

		String[][] values = null;
		method.setAccessible(true);
		try {
			values = (String[][]) method.invoke(null);
		} catch (Exception e) {
			throw new JParException(EXC_EXTRACTOR_VALUES_GET_VALUES, method.toString(), option.getOptionName());
		}

		postConditionExtractValues(values, method.toString());
		
		return values;
	}
	
	private static void postConditionExtractValues(final String[][] values, final String element) {
		if (values == null) {
			throw new JParException(EXC_EXTRACTOR_VALUES_IS_NULL, element);
		}
		if (values.length == 0) {
			throw new JParException(EXC_EXTRACTOR_VALUES_MISSING_GROUPS, element);
		}
		if (values[0].length == 0) {
			throw new JParException(EXC_EXTRACTOR_VALUES_MISSING_CONTENT, element);
		}
	}
	
	private static void preConditionExtractValues(final Arguments arguments, final ExtractedOption option, final Class<?> valueType, final int modifiers, final String element) {
		if (arguments.values() != null && arguments.values().length > 0) {
			throw new JParException(EXC_EXTRACTOR_NO_VALUES_ALLOWED, element, option.getOptionName());
		}
		if (arguments.name() == null || arguments.name().isEmpty()) {
			throw new JParException(EXC_EXTRACTOR_VALUES_NEED_NAME, element);
		}
		if (!Modifier.isStatic(modifiers)) {
			throw new JParException(EXC_EXTRACTOR_VALUES_ELEMENT_NOT_STATIC, element);
		}
		if (!valueType.isArray()) {
			throw new JParException(EXC_EXTRACTOR_VALUES_WRONG_TYPE, element);
		}
	}
	
	public static ExtractedValues getAnnotationOnField(final Arguments argumentValues, final Option argument, final ExtractedOption extractedArgument, final Field field) {
		String[][] values = null;
		if (argument != null) {
			values = extractValuesFromArgumentValues(argumentValues, extractedArgument);
		} else {
			values = extractValuesFromField(argumentValues, extractedArgument, field);
		}
		return createExtractedValues(values, argumentValues, extractedArgument);
	}
	
	public static ExtractedValues getAnnotationOnMethod(final Arguments argumentValues, final Option argument, final ExtractedOption extractedArgument, final Method method) {
		String[][] values = null;
		if (argument != null) {
			values = extractValuesFromArgumentValues(argumentValues, extractedArgument);
		} else {
			values = extractValuesFromMethod(argumentValues, extractedArgument, method);
		}
		return createExtractedValues(values, argumentValues, extractedArgument);
	}
	
	private static ExtractedValues createExtractedValues(final String[][] values, final Arguments arguments, final ExtractedOption option) {
		ExtractedValues extractedValues = new ExtractedValues(values, arguments, option);
		option.getType().setValues(extractedValues);
		return extractedValues;
	}
}
