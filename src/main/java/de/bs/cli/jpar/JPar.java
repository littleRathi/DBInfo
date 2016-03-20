package de.bs.cli.jpar;

import de.bs.cli.jpar.config.Defaults;
import de.bs.cli.jpar.config.ExceptionMessages;
import de.bs.cli.jpar.manual.ParameterManual;
import de.bs.cli.jpar.process.ParameterProcess;

public class JPar implements ExceptionMessages {
	public static void process(final Object program, final String[] args) {
		ParameterProcess ap = new ParameterProcess();
		ap.processArgs(program, args);
	}
	
	public static String manual() {
		StackTraceElement[] stackClasses = Thread.currentThread().getStackTrace();
		
		StackTraceElement stackClassEle = null;
		Class<?> stackClass = null;
		for (int i = 0; i < stackClasses.length; i++) {
			stackClassEle = stackClasses[i];
			try {
				stackClass = Class.forName(stackClassEle.getClassName());
				CliProgram[] progAnnotations = stackClass.getAnnotationsByType(CliProgram.class);

				if (stackClass != null && progAnnotations.length == 1) {
					return manual(stackClass);
				}
			} catch (ClassNotFoundException e) {
			}
		}
		throw new JParException(EXCEPTION_MISSING_CALLABLE_PROGRAM);
	}
	
	public static String manual(final Class<?> programClass) {
		ParameterManual co = new ParameterManual();
		return co.createHelpDescription(programClass);
	}
	
	public static void setDefaultListDelimiter(final String newDefaultListDelimiter) {
		Defaults.setListDelimiter(newDefaultListDelimiter);
	}
	
	public static void setDefaultOptionDelimiter(final String newDefaultOptionDelimiter) {
		Defaults.setOptionDelimiter(newDefaultOptionDelimiter);
	}
	
	public static void setDefaultOptionPrefix(final String newDefaultOptionPrefix) {
		Defaults.setOptionPrefix(newDefaultOptionPrefix);
	}
}
