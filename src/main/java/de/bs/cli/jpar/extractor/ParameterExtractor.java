package de.bs.cli.jpar.extractor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.bs.cli.jpar.Option;
import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.CliProgram;
import de.bs.cli.jpar.Arguments;
import de.bs.cli.jpar.ExceptionMessages;

public class ParameterExtractor implements ExceptionMessages {
	private ExtractedProgram program;
	
	private List<ExtractedOption> options = new LinkedList<ExtractedOption>();
	private Map<String, ExtractedOption> argToExtractedOptions = new HashMap<String, ExtractedOption>();
	private Map<String, ExtractedOption> requiredExtractedOptions = new HashMap<String, ExtractedOption>();
	
	public ExtractedProgram getProgram() {
		return program;
	}
	
	public List<ExtractedOption> getExtractedOptions() {
		return new ArrayList<ExtractedOption>(options);
	}
	
	public ExtractedOption getExtractedOptionForForOptionName(final String name) {
		return argToExtractedOptions.get(name);
	}
	
	public Map<String, ExtractedOption> getRequiredExtractedOptions() {
		return new HashMap<String, ExtractedOption>(requiredExtractedOptions);
	}
	
	public ParameterExtractor(final Class<?> programClass) {
		extractDataFromProgram(programClass);
		setDefaults();
		extractDataFromFields(programClass);
		extractDataFromMethods(programClass);
		extractValuesFromFields(programClass);
		extractValuesFromMethods(programClass);
	}
	
	private void setDefaults() {
		addExtractedParameter(new HelpParameter());
	}
	
	private void extractValuesFromFields(final Class<?> programClass) {
		Field[] allFields = programClass.getDeclaredFields();
		
		for (Field field: allFields) {
			Arguments argumentValues = field.getAnnotation(Arguments.class);
			Option option = field.getAnnotation(Option.class); // for verification 
			
			if (argumentValues != null) {
				ExtractedOption extractedArgument = getExtractedArgumentFor(argumentValues, option, "field", field.toString());
				ExtractedValues.getAnnotationOnField(argumentValues, option, extractedArgument, field);
			}
		}
	}
	
	private void extractValuesFromMethods(final Class<?> programClass) {
		Method[] allMethods = programClass.getDeclaredMethods();
		
		for (Method method: allMethods) {
			Arguments argumentValues = method.getAnnotation(Arguments.class);
			Option option = method.getAnnotation(Option.class); // for verification

			if (argumentValues != null) {
				ExtractedOption extractedArgument = getExtractedArgumentFor(argumentValues, option, "method", method.toString());
				ExtractedValues.getAnnotationOnMethod(argumentValues, option, extractedArgument, method);
			}
		}
	}
	
	private ExtractedOption getExtractedArgumentFor(final Arguments values, final Option option, final String elementName, final String classDefinition) {
		if (option == null && (values.name() == null || values.name().isEmpty())) {
			 // TODO Exception DONE [!] double check? also in ExtractedValues.getAnnotationOnMethod()
			throw new JParException(EXC_EXTRACTOR_ARGUMENT_VALUES_MISSING_NAME, elementName, classDefinition);
		}
		if (option != null && values.name() != null && !values.name().isEmpty()) {
			 // TODO Exception DONE [!] double check? also in ExtractedValues.getAnnotationOnMethod()
			throw new JParException(EXC_EXTRACTOR_ARGUMENT_VALUES_NAME_NOT_ALLOWED, elementName, classDefinition);
		}
		
		String argName = (option == null ? values.name() : option.name());
		ExtractedOption extractedParameter = argToExtractedOptions.get("-" + argName);
		
		if (extractedParameter == null) {
			throw new JParException(EXC_EXTRACTOR_ARGUMENT_VALUES_MISSING_ARGUMENT, argName);
		}
		
		return extractedParameter;
	}
	
	
	private void extractDataFromProgram(final Class<?> programClass) {
		CliProgram[] progAnnotations = programClass.getAnnotationsByType(CliProgram.class);
		
		if (progAnnotations.length == 1) {
			CliProgram progAnnotation = progAnnotations[0];
			
			program = new ExtractedProgram(programClass, progAnnotation);
		} else if (progAnnotations.length > 1){
			throw new JParException(EXC_EXTRACTOR_SEVERAL_ARGUMENT_PROGRAM, programClass);
		} else {
			throw new JParException(EXC_EXTRACTOR_NO_ARGUMENT_PROGRAM, programClass);
		}
	}
	
	private void extractDataFromFields(final Class<?> programClass) {
		Field[] allFields = programClass.getDeclaredFields();
		
		for (Field field: allFields) {
			Option option = field.getAnnotation(Option.class);
			if (option != null) {
				field.setAccessible(true);
				
				ExtractedOption ea = new ExtractedOptionField(field, option);
				addExtractedParameter(ea);
			}
		}
	}
	
	private void extractDataFromMethods(final Class<?> programClass) {
		Method[] allMethods = programClass.getDeclaredMethods();
		
		for (Method method: allMethods) {
			Option option = method.getAnnotation(Option.class);
			if (option != null) {
				method.setAccessible(true);
				
				ExtractedOption ea = new ExtractedOptionMethod(method, option);
				addExtractedParameter(ea);
			}
		}
	}
	
	private void addExtractedParameter(final ExtractedOption optin) {
		if (!argToExtractedOptions.containsKey(optin.getOptionName())) {
			options.add(optin);
			argToExtractedOptions.put(optin.getOptionName(), optin);
			
			if (optin.isRequired()) {
				requiredExtractedOptions.put(optin.getOptionName(), optin);
			}
		} else {
			throw new JParException(EXC_EXTRACTOR_DOUBLE_ARGUMENT, optin.getOptionName());
		}
		
	}
}
