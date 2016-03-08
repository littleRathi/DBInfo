package de.bs.program.argument.help;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.bs.program.argument.Arguments;
import de.bs.program.argument.extractor.ArgumentExtractor;
import de.bs.program.argument.extractor.ExtractedArgument;
import de.bs.program.argument.extractor.ExtractedProgram;

// VERY simple implementation! Very Simple Implementation of EL Like Language =>  VSIELLL
public class ExpressionLanguage {
	private static final String PATTERN_SIMPLE_EXPRESSION_LANGUATE = "\\$\\{([a-zA-Z\\*][a-zA-Z0-9\\.\\*:]*)\\}";

	private static Pattern pattern = Pattern.compile(PATTERN_SIMPLE_EXPRESSION_LANGUATE);

	private ArgumentExtractor argExtractor;
	private Map<String, String> elDefaults = new HashMap<String, String>();
	
	
	public static void main(String[] args){
		String[] input = new String[]{"${abc}", "${PW.required}", "${URL.required}"};
		
		String findGlobal = "\\$\\{([a-zA-Z\\*][a-zA-Z0-9\\.\\*:]*)\\}";
		Pattern pattern1 = Pattern.compile(findGlobal);
		
		String findReq = "^\\$\\{[a-zA-Z0-9]*.required\\}$";
		Pattern pattern2 = Pattern.compile(findReq);
		
		Matcher m = null;
		for (String in: input) {
			System.out.println("in = [" + in + "]");
			m = pattern1.matcher(in);
			if (m.find()) {
				System.out.println("0: found");
			} else {
				System.out.println("0: not found");
			}
			m = pattern2.matcher(in);
			if (m.find()) {
				System.out.println("1: found");
			} else {
				System.out.println("1: not found");
			}
		}
	}
	
	public ExpressionLanguage(final ArgumentExtractor argExtractor) {
		this.argExtractor = argExtractor;
		createElDefaults();
	}
	
	public String replaceReferencesIn(final String text) {
		String newString = text;
		
		Matcher matcher = pattern.matcher(newString);
		
		
		String el = null;
		String elStr = null;
		Map<String, String> els = new HashMap<String, String>();
		while (matcher.find()) {
			el = matcher.group();
			elStr = el.substring(2, el.length() -1);
			els.put(createELRegex(elStr), getElReplacement(elStr));
		}
		
		for (Map.Entry<String, String> replacement: els.entrySet()) {
			newString = newString.replaceAll(replacement.getKey(), replacement.getValue());
		}
		
		return newString;
	}
	
	private String getElReplacement(final String el) {
		String replacement = elDefaults.get(el);
		
		if (replacement == null) {
			replacement = findOptionsForElPattern(el);
		}
		return replacement;
	}
	
	private String findOptionsForElPattern(final String strPattern) {
		String elPattern = strPattern.replaceAll("\\*", "[a-zA-Z0-9]*").replace(".", "\\.");
		Pattern pattern = Pattern.compile("^" + elPattern + "$");
		
		StringBuilder sbList = null;
		
		Matcher matcher = null;
		for (Map.Entry<String, String> el: elDefaults.entrySet()) {
			matcher = pattern.matcher(el.getKey());
			
			if (matcher.find()) {
				if (sbList == null) {
					sbList = new StringBuilder(el.getValue());
				} else {
					sbList.append(", ").append(el.getValue());
				}
			}
		}
		
		return (sbList != null ? sbList.toString() : "<nothing found>");
	}
	
	private void createElDefaults() {
		extractReferencesFrom(argExtractor.getProgram());
		
		for (ExtractedArgument argument: argExtractor.getExtractedArguments()) {
			extractForElDefaults(argument);
		}
	}
	
	private void extractReferencesFrom(final ExtractedProgram program) {
		elDefaults.put(Arguments.PROG, program.getProgramName());
		elDefaults.put("PROG.class.full", program.getType().getName());
		elDefaults.put("PROG.class.single", program.getType().getSimpleName());
	}
	
	private void extractForElDefaults(final ExtractedArgument argument) {
		extractForElDefaultsWithPrefix(argument.getElName(), argument);
		
		String ref = argument.getTargetName();
		extractForElDefaultsWithPrefix(ref, argument);
	}
	
	private void extractForElDefaultsWithPrefix(final String prefix, final ExtractedArgument argument) {
		elDefaults.put(prefix, argument.getArgumentName());
		elDefaults.put(prefix + ".simple", argument.getName());
		elDefaults.put(prefix + ".class.full", argument.getTargetType().getName());
		elDefaults.put(prefix + ".class.simple", argument.getTargetType().getSimpleName());
		if (argument.isRequired()) {
			elDefaults.put(prefix + ".required", argument.getArgumentName());
		}
	}
	
	private static String createELRegex(final String reference) {
		return "\\$\\{" + reference.replaceAll("\\*", "\\\\*") + "\\}";
	}
}
