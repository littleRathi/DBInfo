package de.bs.program.argument.extractor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.bs.program.argument.Argument;
import de.bs.program.argument.ArgumentException;
import de.bs.program.argument.ArgumentValues;
import de.bs.program.argument.ExceptionMessages;

public class ExtractedValues implements ExceptionMessages {
	private ArgumentValues argumentValues;
	private ExtractedArgument argument;
	private boolean useValuesField;
	private String[][] values;
	private List<Set<String>> valuesSet = new LinkedList<Set<String>>();
	
	private ExtractedValues(final String[][] values, final ArgumentValues argumentValues, final ExtractedArgument argument) {
		this.values = values;
		this.argumentValues = argumentValues;
		this.argument = argument;
		
		for (String[] subValues: values) {
			Set<String> subSet = new HashSet<String>();
			for (String value: subValues) {
				subSet.add(value);
			}
			valuesSet.add(subSet);
		}
	}
	
	public boolean isUseValuesField() {
		return useValuesField;
	}
	public ExtractedArgument getArgument() {
		return argument;
	}
	public String getDelimiter() {
		return argumentValues.delimiter();
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
	
	private static String[][] extractValuesFromArgumentValues(final ArgumentValues argumentValues, final ExtractedArgument extractedArgument) {
		if (argumentValues.values() == null || argumentValues.values().length == 0) {
			throw new ArgumentException(EXC_EXTRACTOR_NEED_VALUES, extractedArgument.getArgumentName()); // TODO Exception [REMOVE CONDITION? allow no list]
		}
		if (argumentValues.name() != null && !argumentValues.name().isEmpty()) {
			throw new ArgumentException(EXC_EXTRACTOR_VALUES_NO_NAME, extractedArgument.getArgumentName());
		}
		
		return new String[][]{argumentValues.values()};
	}

//	TODO: handling of the field: allowed should be:  first only array
//	Array, Collection (List, Set, ...)
//		=> get Type and try to transform, if error, then the is the type wrong
//	internal transformation to a internal representation from String[][] Validate <-(ValidateValues|ValidateValue)
	private static String[][] extractValuesFromField(final ArgumentValues argumentValues, final ExtractedArgument extractedArgument, final Field field) {
		preConditionExtractValues(argumentValues, extractedArgument, field.getType(), field.getModifiers(), field.toString());

		String[][] values = null;
		try {
			field.setAccessible(true);
			values = (String[][])field.get(null);
		} catch (Exception e) {
			throw new ArgumentException(EXC_EXTRACTOR_VALUES_GET_VALUES, field.toString(), extractedArgument.getArgumentName());
		}
		
		postConditionExtractValues(values, field.toString());

		return values;
	}
	
	public static String[][] extractValuesFromMethod(final ArgumentValues argumentValues, final Argument argument, final ExtractedArgument extractedArgument, final Method method) {
		preConditionExtractValues(argumentValues, extractedArgument, method.getReturnType(), method.getModifiers(), method.toString());
		
		Class<?>[] parameters = method.getParameterTypes();
		if (parameters != null && parameters.length > 0) {
			throw new ArgumentException(EXC_EXTRACTOR_VALUES_METHOD_NO_PARAMETERS, method.toString(), parameters.length);
		}

		String[][] values = null;
		method.setAccessible(true);
		try {
			values = (String[][]) method.invoke(null);
		} catch (Exception e) {
			throw new ArgumentException(EXC_EXTRACTOR_VALUES_GET_VALUES, method.toString(), extractedArgument.getArgumentName());
		}

		postConditionExtractValues(values, method.toString());
		
		return values;
	}
	
	private static void postConditionExtractValues(final String[][] values, final String element) {
		if (values == null) {
			throw new ArgumentException(EXC_EXTRACTOR_VALUES_IS_NULL, element);
		}
		if (values.length == 0) {
			throw new ArgumentException(EXC_EXTRACTOR_VALUES_MISSING_GROUPS, element);
		}
		if (values[0].length == 0) {
			throw new ArgumentException(EXC_EXTRACTOR_VALUES_MISSING_CONTENT, element);
		}
	}
	
	private static void preConditionExtractValues(final ArgumentValues argumentValues, final ExtractedArgument extractedArgument, final Class<?> valueType, final int modifiers, final String element) {
		if (argumentValues.values() == null || argumentValues.values().length == 0) {
			throw new ArgumentException(EXC_EXTRACTOR_NO_VALUES_ALLOWED, extractedArgument.getArgumentName());
		}
		if (argumentValues.name() == null || argumentValues.name().isEmpty()) {
			throw new ArgumentException(EXC_EXTRACTOR_VALUES_NEED_NAME, element);
		}
		if (!Modifier.isStatic(modifiers)) {
			throw new ArgumentException(EXC_EXTRACTOR_VALUES_ELEMENT_NOT_STATIC, element);
		}
		if (!valueType.isArray()) {
			throw new ArgumentException(EXC_EXTRACTOR_VALUES_WRONG_TYPE, element);
		}
	}
	
	public static ExtractedValues getAnnotationOnField(final ArgumentValues argumentValues, final Argument argument, final ExtractedArgument extractedArgument, final Field field) {
		String[][] values = null;
		if (argument != null) {
			values = extractValuesFromArgumentValues(argumentValues, extractedArgument);
		} else {
			values = extractValuesFromField(argumentValues, extractedArgument, field);
		}
		return createExtractedValues(values, argumentValues, extractedArgument);
	}
	
	public static ExtractedValues getAnnotationOnMethod(final ArgumentValues argumentValues, final Argument argument, final ExtractedArgument extractedArgument, final Method method) {
		String[][] values = null;
		if (argument != null) {
			values = extractValuesFromArgumentValues(argumentValues, extractedArgument);
		} else {
			values = extractValuesFromMethod(argumentValues, argument, extractedArgument, method);
		}
		return createExtractedValues(values, argumentValues, extractedArgument);
	}
	
	private static ExtractedValues createExtractedValues(final String[][] values, final ArgumentValues argumentValues, final ExtractedArgument extractedArgument) {
		ExtractedValues extractedValues = new ExtractedValues(values, argumentValues, extractedArgument);
		extractedArgument.getType().setExtractedValues(extractedValues);
		return extractedValues;
	}
}
