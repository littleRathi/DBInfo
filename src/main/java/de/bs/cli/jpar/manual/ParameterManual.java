package de.bs.cli.jpar.manual;

import de.bs.cli.jpar.extractor.ParameterExtractor;
import de.bs.cli.jpar.extractor.ExtractedOption;
import de.bs.cli.jpar.extractor.ExtractedProgram;

public class ParameterManual {
	
	private ParameterExtractor argExtractor;
	private ExpressionLanguage el;
	
	public String createHelpDescription(final Class<?> programClass) {
		argExtractor = new ParameterExtractor(programClass);
		el = new ExpressionLanguage(argExtractor);
		return buildManual(programClass);
	}
	
	private String buildManual(final Class<?> programClass) {
		StringBuilder sbInfo = new StringBuilder();
		
		buildManualForProgramPart(sbInfo);
		buildManualForOptionsPart(sbInfo, programClass);
		buildManualForSignature(sbInfo);
		
		return sbInfo.toString();
	}
	
	private void buildManualForSignature(final StringBuilder sbInfo) {
		ExtractedProgram program = argExtractor.getProgram();
		
		String text = program.getSignature();
		
		if (text != null && !text.isEmpty()) {
			text = el.replaceReferencesIn(text);
			withBlockWidthIntoStringBuilder(text, 80, sbInfo, "");
		}
	}
	
	private void buildManualForProgramPart(final StringBuilder sbInfo) {
		ExtractedProgram program = argExtractor.getProgram();
		
		sbInfo.append("- ").append(program.getProgramName()).append("\n\n");
		
		String text = program.getDescription();
		text = el.replaceReferencesIn(text);
		withBlockWidthIntoStringBuilder(text, 80, sbInfo, "");
		
		sbInfo.append("\n")
			.append("Options:").append("\n");
	}
	
	private void buildManualForOptionsPart(final StringBuilder sbInfo, final Class<?> programClass) {
		for (ExtractedOption option: argExtractor.getExtractedOptions()) {
			StringBuilder sbOption = new StringBuilder( option.getOptionName()).append(": ");
			
			if (option.isRequired()) {
				sbOption.append("(required option) ");
			}
			
			sbOption.append(option.getManuelDescription()).append(" Usage: ");
			option.getType().getManualDescription(sbOption);
			
			String optionText = el.replaceReferencesIn(sbOption.toString());
			
			withBlockWidthIntoStringBuilder(optionText, 80, sbInfo, "     ");
			sbInfo.append("\n");
		}
	}
	
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
