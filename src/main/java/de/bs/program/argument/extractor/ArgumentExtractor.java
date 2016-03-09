package de.bs.program.argument.extractor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.bs.program.argument.Argument;
import de.bs.program.argument.ArgumentException;
import de.bs.program.argument.ArgumentProgram;
import de.bs.program.argument.ArgumentValues;
import de.bs.program.argument.ExceptionMessages;

public class ArgumentExtractor implements ExceptionMessages {
	private ExtractedProgram program;
	
	private List<ExtractedArgument> arguments = new LinkedList<ExtractedArgument>();
	private Map<String, ExtractedArgument> argToExtractedArgument = new HashMap<String, ExtractedArgument>();
	private Map<String, ExtractedArgument> requiredExtractedArgument = new HashMap<String, ExtractedArgument>();
	
	public ExtractedProgram getProgram() {
		return program;
	}
	
	public List<ExtractedArgument> getExtractedArguments() {
		return new ArrayList<ExtractedArgument>(arguments);
	}
	
	public ExtractedArgument getExtractedArgumentForForArg(final String arg) {
		return argToExtractedArgument.get(arg);
	}
	
	public Map<String, ExtractedArgument> getRequiredExtractedArguments() {
		return new HashMap<String, ExtractedArgument>(requiredExtractedArgument);
	}
	
	public ArgumentExtractor(final Class<?> programClass) {
		extractDataFromProgram(programClass);
		setDefaults();
		extractDataFromFields(programClass);
		extractDataFromMethods(programClass);
		extractListValuesFromFields(programClass);
		extractListValuesFromMethods(programClass);
	}
	
	private void setDefaults() {
		addArgumentProcessor(new HelpArgument());
	}
	
	private void extractListValuesFromFields(final Class<?> programClass) {
		Field[] allFields = programClass.getDeclaredFields();
		
		for (Field field: allFields) {
			ArgumentValues argumentValues = field.getAnnotation(ArgumentValues.class);
			Argument argument = field.getAnnotation(Argument.class); // for verification 
			
			if (argumentValues != null) {
				ExtractedArgument extractedArgument = getExtractedArgumentFor(argumentValues, argument, "field", field.toString());
				ExtractedValues.getAnnotationOnField(argumentValues, argument, extractedArgument, field);
			}
		}
	}
	
	private void extractListValuesFromMethods(final Class<?> programClass) {
		Method[] allMethods = programClass.getDeclaredMethods();
		
		for (Method method: allMethods) {
			ArgumentValues argumentValues = method.getAnnotation(ArgumentValues.class);
			Argument argument = method.getAnnotation(Argument.class); // for verification

			if (argumentValues != null) {
				ExtractedArgument extractedArgument = getExtractedArgumentFor(argumentValues, argument, "method", method.toString());
				ExtractedValues.getAnnotationOnMethod(argumentValues, argument, extractedArgument, method);
			}
		}
	}
	
	private ExtractedArgument getExtractedArgumentFor(final ArgumentValues values, final Argument argument, final String elementName, final String classDefinition) {
		if (argument == null && (values.name() == null || values.name().isEmpty())) {
			 // TODO Exception DONE [!] double check? also in ExtractedValues.getAnnotationOnMethod()
			throw new ArgumentException(EXC_EXTRACTOR_ARGUMENT_VALUES_MISSING_NAME, elementName, classDefinition);
		}
		if (argument != null && values.name() != null && !values.name().isEmpty()) {
			 // TODO Exception DONE [!] double check? also in ExtractedValues.getAnnotationOnMethod()
			throw new ArgumentException(EXC_EXTRACTOR_ARGUMENT_VALUES_NAME_NOT_ALLOWED, elementName, classDefinition);
		}
		
		String argName = (argument == null ? values.name() : argument.name());
		ExtractedArgument extractedArgument = argToExtractedArgument.get("-" + argName);
		
		if (extractedArgument == null) {
			throw new ArgumentException(EXC_EXTRACTOR_ARGUMENT_VALUES_MISSING_ARGUMENT, argName);
		}
		
		return extractedArgument;
	}
	
	
	private void extractDataFromProgram(final Class<?> programClass) {
		ArgumentProgram[] progAnnotations = programClass.getAnnotationsByType(ArgumentProgram.class);
		
		if (progAnnotations.length == 1) {
			ArgumentProgram progAnnotation = progAnnotations[0];
			
			program = new ExtractedProgram(programClass, progAnnotation);
		} else if (progAnnotations.length > 1){
			throw new ArgumentException(EXC_EXTRACTOR_SEVERAL_ARGUMENT_PROGRAM, programClass);
		} else {
			throw new ArgumentException(EXC_EXTRACTOR_NO_ARGUMENT_PROGRAM, programClass);
		}
	}
	
	private void extractDataFromFields(final Class<?> programClass) {
		Field[] allFields = programClass.getDeclaredFields();
		
		for (Field field: allFields) {
			Argument argument = field.getAnnotation(Argument.class);
			if (argument != null) {
				field.setAccessible(true);
				
				ExtractedArgument ea = new ExtractedArgumentField(field, argument);
				addArgumentProcessor(ea);
			}
		}
	}
	
	private void extractDataFromMethods(final Class<?> programClass) {
		Method[] allMethods = programClass.getDeclaredMethods();
		
		for (Method method: allMethods) {
			Argument argument = method.getAnnotation(Argument.class);
			if (argument != null) {
				method.setAccessible(true);
				
				ExtractedArgument ea = new ExtractedArgumentMethod(method, argument);
				addArgumentProcessor(ea);
			}
		}
	}
	
	private void addArgumentProcessor(final ExtractedArgument ea) {
		if (!argToExtractedArgument.containsKey(ea.getArgumentName())) {
			arguments.add(ea);
			argToExtractedArgument.put(ea.getArgumentName(), ea);
			
			if (ea.isRequired()) {
				requiredExtractedArgument.put(ea.getArgumentName(), ea);
			}
		} else {
			throw new ArgumentException(EXC_EXTRACTOR_DOUBLE_ARGUMENT, ea.getArgumentName());
		}
		
	}
}
