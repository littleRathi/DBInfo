package de.bs.program.argument.help;

import de.bs.program.argument.extractor.ArgumentExtractor;
import de.bs.program.argument.extractor.ExtractedArgument;
import de.bs.program.argument.extractor.ExtractedProgram;

public class ArgumentHelp {
	
	private ArgumentExtractor argExtractor;
	private ExpressionLanguage el;
	
	public String createHelpDescription(final Class<?> programClass) {
		argExtractor = new ArgumentExtractor(programClass);
		el = new ExpressionLanguage(argExtractor);
		return buildHelpDescription(programClass);
	}
	
	// Create information >>
	private String buildHelpDescription(final Class<?> programClass) {
		StringBuilder sbInfo = new StringBuilder();
		
		buildHelpDescriptionForProgramPart(sbInfo);
		buildHelpDescriptionForOptionsPart(sbInfo, programClass);
		buildHelpDescriptionForSignature(sbInfo);
		
		return sbInfo.toString();
	}
	
	private void buildHelpDescriptionForSignature(final StringBuilder sbInfo) {
		ExtractedProgram program = argExtractor.getProgram();
		
		String text = program.getSignature();
		
		if (text != null && !text.isEmpty()) {
			text = el.replaceReferencesIn(text);
			withBlockWidthIntoStringBuilder(text, 80, sbInfo, "");
		}
	}
	
	private void buildHelpDescriptionForProgramPart(final StringBuilder sbInfo) {
		ExtractedProgram program = argExtractor.getProgram();
		
		sbInfo.append("- ").append(program.getProgramName()).append("\n\n");
		
		String text = program.getDescription();
		text = el.replaceReferencesIn(text);
		withBlockWidthIntoStringBuilder(text, 80, sbInfo, "");
		
		sbInfo.append("\n")
			.append("Options:").append("\n");
	}
	
	private void buildHelpDescriptionForOptionsPart(final StringBuilder sbInfo, final Class<?> programClass) {
		for (ExtractedArgument argument: argExtractor.getExtractedArguments()) {
			StringBuilder sbOption = new StringBuilder( argument.getArgumentName()).append(" ");
			
			if (argument.isRequired()) {
				sbOption.append("(required option) ");
			}
			
			sbOption.append(argument.getDescription()).append(" Usage: ");
			argument.getType().getUsageDescription(sbOption);
			
			String optionText = el.replaceReferencesIn(sbOption.toString());
			
			withBlockWidthIntoStringBuilder(optionText, 80, sbInfo, "     ");
			sbInfo.append("\n");
		}
	}
	// << Create information
	
	private static void withBlockWidthIntoStringBuilder(final String text, final int blockSize, final StringBuilder sb, final String prefix) {
		boolean withPrefix = prefix != null && !prefix.isEmpty();
		int length = text.length();
		int dynBlockSize = blockSize;
		int to = blockSize;
		
		for (int i = 0; i < length;) {
			if (i + dynBlockSize >= length) {
				to = length;
			} else {
				to = i + dynBlockSize;
				while (text.charAt(to) != ' ' && to > (i + (dynBlockSize / 2))) {
					to--;
				}
				to++; // so that the space is the last char on the line before
			}
			if (withPrefix && i == 0) {
				dynBlockSize = dynBlockSize - prefix.length();
			}
			if (withPrefix && i > 0) {
				sb.append(prefix);
			}
			sb.append(text.substring(i, to)).append("\n");
			i = to;
		}
	}
}
