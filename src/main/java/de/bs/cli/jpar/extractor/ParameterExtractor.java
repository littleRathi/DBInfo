package de.bs.cli.jpar.extractor;

import static de.bs.cli.jpar.extractor.ExtractedOption.asOptionName;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.bs.cli.jpar.Option;
import de.bs.cli.jpar.config.Consts;
import de.bs.cli.jpar.config.ExceptionMessages;
import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.CliProgram;
import de.bs.cli.jpar.Arguments;

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
		
		Map<String, ExtractedArguments> extractedArguments = new HashMap<String, ExtractedArguments>();
		extractArgumentsFromFields(programClass, extractedArguments);
		extractArgumentsFromMethods(programClass, extractedArguments);
		extractDataFromFields(programClass, extractedArguments);
		extractDataFromMethods(programClass, extractedArguments);
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
	
	private void setDefaults() {
		addExtractedParameter(new HelpParameter());
	}
	
	private void extractArgumentsFromFields(final Class<?> programClass, final Map<String, ExtractedArguments> extractedArguments) {
		Field[] allFields = programClass.getDeclaredFields();
		
		for (Field field: allFields) {
			Arguments arguments = field.getAnnotation(Arguments.class);
			
			if (arguments != null) {
				Option option = field.getAnnotation(Option.class); // for verification 
				
				String optionName = getOptionName(arguments, option, Consts.FIELD, field.toString());
				ExtractedArguments exArguments = ExtractedArguments.getAnnotationOnField(arguments, option, optionName, field);
				
				addExtractedArguments(exArguments, optionName, extractedArguments);
			}
		}
	}
	
	private void extractArgumentsFromMethods(final Class<?> programClass, final Map<String, ExtractedArguments> extractedArguments) {
		Method[] allMethods = programClass.getDeclaredMethods();
		
		for (Method method: allMethods) {
			Arguments arguments = method.getAnnotation(Arguments.class);

			if (arguments != null) {
				Option option = method.getAnnotation(Option.class); // for verification
				
				String optionName = getOptionName(arguments, option, Consts.METHOD, method.toString());
				ExtractedArguments exArguments = ExtractedArguments.getAnnotationOnMethod(arguments, option, optionName, method);

				addExtractedArguments(exArguments, optionName, extractedArguments);
			}
		}
	}
	
	private void addExtractedArguments(final ExtractedArguments arguments, final String optionName, final Map<String, ExtractedArguments> extractedArguments) {
		if (extractedArguments.containsKey(optionName)) {
			throw new JParException(EXC_EXTRACTOR_ARGUMENTS_DOUBLE, optionName);
		} else {
			extractedArguments.put(optionName, arguments);
		}
	}
	
	private String getOptionName(final Arguments arguments, final Option option, final String elementName, final String classDefinition) {
		if (option != null && arguments.name() != null && !arguments.name().isEmpty()) {
			throw new JParException(EXC_EXTRACTOR_ARGUMENTS_NAME_NOT_ALLOWED, elementName, classDefinition);
		}
		if (option == null && (arguments.name() == null || arguments.name().isEmpty())) {
			throw new JParException(EXC_EXTRACTOR_ARGUMENTS_NAME_MISSING, elementName, classDefinition);
		}
		
		return asOptionName(option != null ? option.name() : arguments.name());
	}
	
	private void extractDataFromFields(final Class<?> programClass, final Map<String, ExtractedArguments> extractedArguments) {
		Field[] allFields = programClass.getDeclaredFields();
		
		for (Field field: allFields) {
			Option option = field.getAnnotation(Option.class);
			if (option != null) {
				field.setAccessible(true);
				
				ExtractedOption ea = new ExtractedOptionField(field, option, extractedArguments.get(asOptionName(option.name())));
				addExtractedParameter(ea);
			}
		}
	}
	
	private void extractDataFromMethods(final Class<?> programClass, final Map<String, ExtractedArguments> extractedArguments) {
		Method[] allMethods = programClass.getDeclaredMethods();
		
		for (Method method: allMethods) {
			Option option = method.getAnnotation(Option.class);
			if (option != null) {
				method.setAccessible(true);
				
				ExtractedOption ea = new ExtractedOptionMethod(method, option, extractedArguments.get(asOptionName(option.name())));
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
			throw new JParException(EXC_EXTRACTOR_DOUBLE_OPTION, optin.getOptionName());
		}
		
	}
}
