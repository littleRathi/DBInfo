package de.bs.program.argument.process;

import java.util.HashMap;
import java.util.Map;

import de.bs.program.argument.ArgumentException;
import de.bs.program.argument.ExceptionMessages;
import de.bs.program.argument.extractor.ArgumentExtractor;
import de.bs.program.argument.extractor.ExtractedArgument;

public class ArgumentProcess implements ExceptionMessages {
	
	private ArgumentExtractor argExtractor;
	private Arguments arguments;
	
	private Map<String, ExtractedArgument> required = new HashMap<String, ExtractedArgument>();

	public boolean processArgs(final Object program, final String[] args) {
		argExtractor = new ArgumentExtractor(program.getClass());
		required = argExtractor.getRequiredExtractedArguments();
		arguments = new Arguments(args);
		return processArgsToOptions(program);
	}
	
	private boolean processArgsToOptions(final Object program) {
		boolean run = mapArgsToOption(program);
		checkForRequired();
		return run;
	}
	
	private boolean mapArgsToOption(final Object program) {
		while (arguments.next()) {
			String argPart = arguments.get();
			String option = getOption(argPart);
			
			ExtractedArgument op = argExtractor.getExtractedArgumentForForArg(option);
			if (op != null) {
				op.prozessArg(program, option, argPart, arguments);
				required.remove(option);
			}
		}
		return true;
	}
	
	private String getOption(final String argPart) {
		String[] split = argPart.split(":");
		return split[0] + ":";
	}
	
	private void checkForRequired() {
		if (required.size() > 0) {
			StringBuilder sb = null;
			for (ExtractedArgument argument: required.values()) {
				if (sb == null) {
					sb = new StringBuilder(argument.getArgumentName());
				} else {
					sb.append(", ").append(argument.getArgumentName());
				}
			}
			
			throw new ArgumentException(EXC_PROCESS_MISSING_REQUIRED_ARGUMENTS, sb.toString());
		}
	}
}
