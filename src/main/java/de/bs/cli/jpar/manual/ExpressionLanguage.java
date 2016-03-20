package de.bs.cli.jpar.manual;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.bs.cli.jpar.config.Consts;
import de.bs.cli.jpar.extractor.ExtractedOption;
import de.bs.cli.jpar.extractor.ExtractedProgram;
import de.bs.cli.jpar.extractor.ParameterExtractor;

// VERY simple implementation! Very Simple Implementation of EL Like Language =>  VSIELLL
// change to LaTeX style, for this could be the better choice
public class ExpressionLanguage {
	private static final String PATTERN_SIMPLE_EXPRESSION_LANGUATE = "\\$\\{([a-zA-Z\\*][a-zA-Z0-9\\.\\*:]*)\\}";

	private static Pattern pattern = Pattern.compile(PATTERN_SIMPLE_EXPRESSION_LANGUATE);

	private ParameterExtractor argExtractor;
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
	
	public ExpressionLanguage(final ParameterExtractor argExtractor) {
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
		
		for (ExtractedOption parameter: argExtractor.getExtractedOptions()) {
			extractForElDefaults(parameter);
		}
	}
	
	private void extractReferencesFrom(final ExtractedProgram program) {
		elDefaults.put(Consts.PROG_SHORTCUT, program.getProgramName());
		elDefaults.put("PROG.class.full", program.getType().getName());
		elDefaults.put("PROG.class.single", program.getType().getSimpleName());
	}
	
	private void extractForElDefaults(final ExtractedOption option) {
		extractForElDefaultsWithPrefix(option.getElName(), option);
		
		String ref = option.getTargetName();
		extractForElDefaultsWithPrefix(ref, option);
	}
	
	private void extractForElDefaultsWithPrefix(final String prefix, final ExtractedOption option) {
		elDefaults.put(prefix, option.getOptionName());
		elDefaults.put(prefix + ".simple", option.getName());
		elDefaults.put(prefix + ".class.full", option.getTargetType().getName());
		elDefaults.put(prefix + ".class.simple", option.getTargetType().getSimpleName());
		if (option.isRequired()) {
			elDefaults.put(prefix + ".required", option.getOptionName());
		}
	}
	
	private static String createELRegex(final String reference) {
		return "\\$\\{" + reference.replaceAll("\\*", "\\\\*") + "\\}";
	}
}
