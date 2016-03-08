package de.bs.program.argument;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

import de.bs.program.argument.help.ArgumentHelp;
import de.bs.program.argument.process.ArgumentProcess;

public class Arguments {
	public static final String PROG = "PROG";
	
	private static final String EXCEPTION_MISSING_CALLABLE_PROGRAM = "No Class found in the stacktrace that has the @" + ArgumentProgram.class + " annotation.";
	
	// TODO Naming -> extractArguments
	public static void processArguments(final Object program, final String[] args) {
		ArgumentProcess ap = new ArgumentProcess();
		ap.processArgs(program, args);
	}
	
	// TODO Naming -> helpArguments
	public static String getHelpDescription() {
		StackTraceElement[] stackClasses = Thread.currentThread().getStackTrace();
		
		StackTraceElement stackClassEle = null;
		Class<?> stackClass = null;
		for (int i = 0; i < stackClasses.length; i++) {
			stackClassEle = stackClasses[i];
			try {
				stackClass = Class.forName(stackClassEle.getClassName());
				ArgumentProgram[] progAnnotations = stackClass.getAnnotationsByType(ArgumentProgram.class);

				if (stackClass != null && progAnnotations.length == 1) {
					return getHelpDescription(stackClass);
				}
			} catch (ClassNotFoundException e) {
			}
		}
		throw new ArgumentException(EXCEPTION_MISSING_CALLABLE_PROGRAM);
	}
	
	public static String getHelpDescription(final Class<?> programClass) {
		ArgumentHelp co = new ArgumentHelp();
		return co.createHelpDescription(programClass);
	}
	
	public static String[] createSimpleValueList(final String... values) {
		return values;
	}
	
	public static String[] createSimpleValueList(final Class<?> valueListClass, final String... additionalValues) {
		List<String> values = new LinkedList<String>();
		
		Field[] fields = valueListClass.getFields();
		for (Field field: fields) {
			int modifiers = field.getModifiers();
			if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
				values.add(field.getName());
			}
		}
		
		for (String additional: additionalValues) {
			values.add(additional);
		}
		return values.toArray(new String[values.size()]);
	}
	
	public static String[][] createSingleGroup(final Class<?> valueListClass, final String... additionalValues) {
		return new String[][]{createSimpleValueList(valueListClass, additionalValues)};
	}
	
	public static String[][] createGroups(final String[]... groups) {
		return groups;
	}
}
