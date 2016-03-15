package de.bs.cli.jpar.extractor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import de.bs.cli.jpar.Option;
import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.process.Parameters;

public class ExtractedOptionMethod extends ExtractedOption {

	private Method method;
	
	public ExtractedOptionMethod(final Method method, final Option option, final ExtractedArguments arguments) {
		super(option, arguments, getType(method));
		this.method = method;
		
		if (Modifier.isStatic(method.getModifiers())) {
			throw new JParException(EXC_EXTRACTOR_FIELD_NOT_STATIC, method.toString());
		}
	}
	
	public static Class<?> getType(final Method method) {
		Class<?>[] paramClass = method.getParameterTypes();
		if (paramClass.length == 1) {
			return paramClass[0];
		} else if (paramClass.length == 0) {
			throw new JParException(EXC_EXTRACTOR_NO_ARGUMENTS, method);
		} else {
			throw new JParException(EXC_EXTRACTOR_TO_MANY_ARGUMENTS, method);
		}
	}
	
	@Override
	public String getTargetName() {
		return method.getName().toUpperCase();
	}

	@Override
	public void processArg(final Object program, final String option, final String argument, final Parameters args) {
		Object value = null;
		try {
			value = getType().processArgs(option, argument, args);
			method.invoke(program, value);
		} catch (Exception e) {
			throw new JParException(e, EXC_EXTRACTOR_COULD_NOT_SET, value, option);
		}
	}
}
