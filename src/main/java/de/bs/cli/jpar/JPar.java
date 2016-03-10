package de.bs.cli.jpar;

import de.bs.cli.jpar.manual.ParameterManual;
import de.bs.cli.jpar.process.ParameterProcess;

public class JPar {
	public static final String PROG = "PROG";
	
	private static final String EXCEPTION_MISSING_CALLABLE_PROGRAM = "No Class found in the stacktrace that has the @" + CliProgram.class + " annotation.";
	
	// TODO Naming -> extractArguments
	public static void process(final Object program, final String[] args) {
		ParameterProcess ap = new ParameterProcess();
		ap.processArgs(program, args);
	}
	
	// TODO Naming -> helpArguments
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
}
